package com.tokopedia.effect

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLES20
import android.util.Log
import android.widget.ImageView
import com.bytedance.labcv.effectsdk.BytedEffectConstants
import com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode
import com.bytedance.labcv.effectsdk.RenderManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.effect.util.ImageUtil
import com.tokopedia.effect.util.asset.AssetPathHelper
import com.tokopedia.effect.util.asset.checker.AssetChecker
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
class EffectManagerImpl @Inject constructor(
    private val context: Context,
    private val assetChecker: AssetChecker,
    private val assetPathHelper: AssetPathHelper,
) : EffectManager {

    private var mRenderManager: RenderManager? = null

    private val mImageUtil: ImageUtil by lazy { ImageUtil() }

    private val renderApi: Int
        get() {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return if (activityManager.deviceConfigurationInfo.reqGlEsVersion >= GLES_VERSION) 1 else 0
        }


    override fun init() {
        if(mRenderManager != null) return

        try {
            RenderManager.loadLib()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val renderManager = RenderManager()
        val statusCode = renderManager.init(
            context,
            assetPathHelper.modelDir,
            assetPathHelper.licenseFilePath,
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
        srcTexture: Int,
        cameraRotation: Int,
        width: Int,
        height: Int,
        surfaceWidth: Int,
        surfaceHeight: Int
    ) {
        GLES20.glClearColor(TEST_R0 / 255.0f, TEST_G0 / 255.0f, TEST_B0 / 255.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        var dstTexture = srcTexture

        val transition = ImageUtil.Transition().rotate(cameraRotation.toFloat()).flip(false, true).reverse()

        val texture2D = mImageUtil.transferTextureToTexture(
            dstTexture,
            BytedEffectConstants.TextureFormat.Texture_Oes,
            BytedEffectConstants.TextureFormat.Texure2D,
            width,
            height,
            transition
        )

        dstTexture = mImageUtil.prepareTexture(width, height)

        /** TODO: is it always true? */
        setCameraPosition(true)
        val timestamp = System.currentTimeMillis()

        val ret = mRenderManager?.processTexture(texture2D, dstTexture, width, height, BytedEffectConstants.Rotation.CLOCKWISE_ROTATE_0, timestamp)
        if (GlobalConfig.DEBUG) {
            Log.d(this::class.java.name, "EffectManager: $ret")
        }

        // do the same for both display and encoder surface views
        val drawTransition = ImageUtil.Transition().crop(
            ImageView.ScaleType.CENTER_CROP,
            cameraRotation,
            width, height,
            surfaceWidth, surfaceHeight
        )
        mImageUtil.drawFrameOnScreen(
            dstTexture,
            BytedEffectConstants.TextureFormat.Texure2D,
            surfaceWidth, surfaceHeight,
            drawTransition.matrix
        )
    }

    override fun setCameraPosition(isFront: Boolean) {
        mRenderManager?.setCameraPostion(isFront)
    }

    companion object {
        private const val GLES_VERSION = 0x30000

        private const val TEST_R0 = 0
        private const val TEST_G0 = 136
        private const val TEST_B0 = 0
    }
}
