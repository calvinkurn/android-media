package com.tokopedia.effect

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLES20
import android.util.Log
import android.view.Surface
import android.widget.ImageView
import com.bef.effectsdk.OpenGLUtils
import com.bytedance.labcv.effectsdk.BytedEffectConstants
import com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode
import com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC
import com.bytedance.labcv.effectsdk.RenderManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.effect.model.FaceFilter
import com.tokopedia.effect.util.ImageUtil
import com.tokopedia.effect.util.asset.checker.AssetChecker
import com.tokopedia.effect.util.asset.AssetHelper
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
class EffectManagerImpl @Inject constructor(
    private val context: Context,
    private val assetChecker: AssetChecker,
    private val assetHelper: AssetHelper,
) : EffectManager {

    private val mSavedComposerNodes = mutableSetOf<String>()

    private var mRenderManager: RenderManager? = null

    private val mImageUtil: ImageUtil by lazy { ImageUtil() }

    private val renderApi: Int
        get() {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return if (activityManager.deviceConfigurationInfo.reqGlEsVersion >= GLES_VERSION) 1 else 0
        }


    override fun init(
        surfaceWidth: Int,
        surfaceHeight: Int,
    ) {
        if(mRenderManager != null) return

        try {
            RenderManager.loadLib()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val renderManager = RenderManager()
        val statusCode = renderManager.init(
            context,
            assetHelper.modelDir,
            assetHelper.licenseFilePath,
            context.cacheDir.absolutePath,
            true,
            false,
            renderApi,
        )

        if(statusCode != BytedResultCode.BEF_RESULT_SUC) {
            throw ExceptionInInitializerError(
                "Error while initializing RenderManager, error code: $statusCode, more info https://docs.byteplus.com/effects/docs/error-code-table"
            )
        }

        // turn on multi-input for parallel rendering (detail: https://docs.byteplus.com/effects/docs/c-api#25-set-multi-inputs-for-parallel-rendering-due-to-the-nonlinearity-of-algorithm-detection-and-special-effect-rendering-in-order-to-support-multi-threaded-processing-it-is-necessary-to-avoid-the-same-texture-idbuffer-memory-for-consecutive-frames-during-processing)
        renderManager.set3Buffer(false)
        // enable the gyroscope (detail: https://docs.byteplus.com/effects/docs/ar)
        renderManager.useBuiltinSensor(true)

        mRenderManager = renderManager

        if (GlobalConfig.DEBUG) {
            Log.d(this::class.java.name,"Effect SDK version =" + renderManager.sdkVersion)
        }
    }

    override fun process(
        textureId: Int,
        textureWidth: Int,
        textureHeight: Int,
        width: Int,
        height: Int,
    ): Int {
        GLES20.glClearColor(
            TEST_R0 / 255.0f,
            TEST_G0 / 255.0f,
            TEST_B0 / 255.0f,
            1.0f
        )
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        var destinationTexture = mImageUtil.prepareTexture(textureWidth, textureHeight)
        val transition = ImageUtil.Transition().rotate(Surface.ROTATION_0.toFloat()).flip(false, false).reverse()
        val texture2D = mImageUtil.transferTextureToTexture(
            textureId,
            BytedEffectConstants.TextureFormat.Texture_Oes,
            BytedEffectConstants.TextureFormat.Texure2D,
            width,
            height,
            transition
        )

        val timestamp = System.nanoTime()
        val ret = mRenderManager?.processTexture(texture2D, destinationTexture, width, height, BytedEffectConstants.Rotation.CLOCKWISE_ROTATE_0, timestamp)

        if(ret == false) {
            destinationTexture = texture2D
        }

        return destinationTexture
    }

    override fun drawFrameBase(
        textureWidth: Int,
        textureHeight: Int,
        surfaceWidth: Int,
        surfaceHeight: Int,
        dstTexture: Int,
    ) {
        val drawTransition = ImageUtil.Transition()
            .crop(
                ImageView.ScaleType.CENTER_CROP,
                Surface.ROTATION_0,
                textureWidth,
                textureHeight,
                surfaceWidth,
                surfaceHeight,
            )

        mImageUtil.drawFrameOnScreen(
            dstTexture,
            BytedEffectConstants.TextureFormat.Texure2D,
            surfaceWidth,
            surfaceHeight,
            drawTransition.matrix
        )
    }

    override fun setCameraPosition(isFront: Boolean) {
        mRenderManager?.setCameraPostion(isFront)
    }

    override fun getExternalOESTextureID(): Int {
        return OpenGLUtils.getExternalOESTextureID()
    }

    override fun setPreset(presetId: String, value: Float): Boolean {
        val key = "${assetHelper.presetDir}/$presetId"

        if(!mSavedComposerNodes.contains(presetId)) {
            mRenderManager?.appendComposerNodes(arrayOf(key))
            mSavedComposerNodes.add(key)
        }

        return mRenderManager?.updateComposerNodes(key, PRESET_MAKEUP_KEY, value) == BEF_RESULT_SUC &&
        mRenderManager?.updateComposerNodes(key, PRESET_FILTER_KEY, value) == BEF_RESULT_SUC
    }

    override fun removePreset() {
        /** TODO: remove preset node only */
        mRenderManager?.removeComposerNodes(mSavedComposerNodes.toTypedArray())
    }

    override fun setFaceFilter(faceFilterId: String, value: Float): Boolean {
        if(!mSavedComposerNodes.contains(assetHelper.customFaceDir)) {
            mRenderManager?.appendComposerNodes(arrayOf(assetHelper.customFaceDir))
            mSavedComposerNodes.add(assetHelper.customFaceDir)
        }

        return mRenderManager?.updateComposerNodes(assetHelper.customFaceDir, FaceFilter.getById(faceFilterId).key, value) == BEF_RESULT_SUC
    }

    override fun removeFaceFilter() {
        mRenderManager?.removeComposerNodes(arrayOf(assetHelper.customFaceDir))
        mSavedComposerNodes.remove(assetHelper.customFaceDir)
    }

    companion object {
        private const val GLES_VERSION = 0x30000

        private const val TEST_R0 = 0
        private const val TEST_G0 = 136
        private const val TEST_B0 = 0

        private const val PRESET_MAKEUP_KEY = "Makeup_ALL"
        private const val PRESET_FILTER_KEY = "Filter_ALL"
    }
}
