package com.tokopedia.byteplus.effect

import android.app.ActivityManager
import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.widget.ImageView
import com.bef.effectsdk.OpenGLUtils
import com.bytedance.labcv.effectsdk.BytedEffectConstants
import com.bytedance.labcv.effectsdk.BytedEffectConstants.BytedResultCode.BEF_RESULT_SUC
import com.bytedance.labcv.effectsdk.RenderManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.byteplus.effect.model.FaceFilter
import com.tokopedia.byteplus.effect.model.FaceFilterType
import com.tokopedia.byteplus.effect.model.SavedComposeNode
import com.tokopedia.byteplus.effect.util.ImageUtil
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.AssetHelper
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 24, 2023
 */
class EffectManagerImpl @Inject constructor(
    private val context: Context,
    private val assetChecker: AssetChecker,
    private val assetHelper: AssetHelper,
) : EffectManager {

    private var mSavedComposeNode = SavedComposeNode.Empty

    private var mRenderManager: RenderManager? = null

    private var mImageUtil: ImageUtil? = null

    // Texture
    private var mTextureId = 0
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mTextureWidth = 0
    private var mTextureHeight = 0

    // thread
    private var mRenderThread: Thread? = null
    private var mGLHandler: Handler? = null

    private val renderApi: Int
        get() {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return if (activityManager.deviceConfigurationInfo.reqGlEsVersion >= GLES_VERSION) 1 else 0
        }

    override fun startRenderThread() {
        if (mRenderThread?.isAlive == true) return

        stopRenderThread()

        mRenderThread = Thread(
            object : Runnable {
                override fun run() {
                    Looper.prepare()
                    mGLHandler = Handler()
                    Looper.loop()
                }
            },
            "RenderThread"
        )
        mRenderThread?.start()
    }

    override fun stopRenderThread() {
        mRenderThread?.interrupt()
        mRenderThread = null
    }

    override fun init() {
        if(mRenderManager != null) return

        try {
            RenderManager.loadLib()
        } catch (_: Exception) {

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

        if(statusCode != BEF_RESULT_SUC) {
            throw ExceptionInInitializerError(
                "Error while initializing RenderManager, error code: $statusCode, more info https://docs.byteplus.com/effects/docs/error-code-table"
            )
        }

        // turn on multi-input for parallel rendering (detail: https://docs.byteplus.com/effects/docs/c-api#25-set-multi-inputs-for-parallel-rendering-due-to-the-nonlinearity-of-algorithm-detection-and-special-effect-rendering-in-order-to-support-multi-threaded-processing-it-is-necessary-to-avoid-the-same-texture-idbuffer-memory-for-consecutive-frames-during-processing)
        renderManager.set3Buffer(false)
        // enable the gyroscope (detail: https://docs.byteplus.com/effects/docs/ar)
        renderManager.useBuiltinSensor(true)

        mRenderManager = renderManager
        mImageUtil = ImageUtil()

        if (GlobalConfig.DEBUG) {
            Log.d(this::class.java.name,"Effect SDK version =" + renderManager.sdkVersion)
        }

        bindAppliedEffect()
    }

    override fun setupTexture(
        surfaceWidth: Int,
        surfaceHeight: Int,
    ) {
        // initialize surface texture
        mTextureId = OpenGLUtils.getExternalOESTextureID()
        mSurfaceTexture = SurfaceTexture(mTextureId)

        mTextureWidth = surfaceWidth
        mTextureHeight = surfaceHeight

        mSurfaceTexture?.setDefaultBufferSize(mTextureWidth, mTextureHeight)
    }

    override fun setRenderListener(
        surfaceWidth: Int,
        surfaceHeight: Int,
        listener: EffectManager.Listener
    ) {
        mSurfaceTexture?.setOnFrameAvailableListener {
            mGLHandler?.post {
                try {
                    mSurfaceTexture?.updateTexImage()

                    val destinationTexture = process(
                        textureId = mTextureId,
                        textureWidth = mTextureWidth,
                        textureHeight = mTextureHeight,
                        width = surfaceWidth,
                        height = surfaceHeight,
                    )

                    listener.onRenderFrame(destinationTexture, mTextureWidth, mTextureHeight)
                } catch (_: Exception) {

                }
            }
        }
    }

    private fun process(
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
        var destinationTexture = mImageUtil?.prepareTexture(textureWidth, textureHeight) ?: 0
        val transition = ImageUtil.Transition().rotate(Surface.ROTATION_0.toFloat()).flip(false, false).reverse()
        val texture2D = mImageUtil?.transferTextureToTexture(
            textureId,
            BytedEffectConstants.TextureFormat.Texture_Oes,
            BytedEffectConstants.TextureFormat.Texure2D,
            width,
            height,
            transition
        ) ?: 0

        if (isEffectApplied()) {
            val timestamp = System.nanoTime()
            val ret = mRenderManager?.processTexture(texture2D, destinationTexture, width, height, BytedEffectConstants.Rotation.CLOCKWISE_ROTATE_0, timestamp)
            if(ret == false) {
                destinationTexture = texture2D
            }
        }
        else {
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

        mImageUtil?.drawFrameOnScreen(
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

    override fun setFaceFilter(faceFilterId: String, value: Float): Boolean {
        return mRenderManager?.let { renderManager ->
            val faceFilterType = FaceFilterType.getById(faceFilterId)
            if (faceFilterType.isUnknown) return@let false
            if (!assetChecker.isCustomFaceAvailable()) return@let false

            if (!mSavedComposeNode.faceFilterComposeNodeApplied) {
                val isSuccessAppendNodes = mRenderManager?.appendComposerNodes(arrayOf(assetHelper.customFaceDir)) == BEF_RESULT_SUC

                if (isSuccessAppendNodes) {
                    mSavedComposeNode.update {
                        copy(faceFilterComposeNodeApplied = true)
                    }
                }
                else {
                    return@let false
                }
            }

            val isSuccess = renderManager.updateComposerNodes(
                assetHelper.customFaceDir,
                faceFilterType.key,
                value
            ) == BEF_RESULT_SUC

            if (isSuccess) {
                mSavedComposeNode.update {
                    val isFound = mSavedComposeNode.faceFilters.any { it.type == faceFilterType }
                    if (isFound) {
                        copy(
                            faceFilters = faceFilters.map {
                                if(it.type == faceFilterType) it.copy(value = value)
                                else it
                            }.toMutableList()
                        )
                    }
                    else {
                        copy(
                            faceFilters = faceFilters + FaceFilter(type = faceFilterType, value = value)
                        )
                    }
                }
            }
            isSuccess
        } ?: true
    }

    override fun removeFaceFilter() {
        FaceFilterType.values().forEach { type ->
            mRenderManager?.updateComposerNodes(
                assetHelper.customFaceDir,
                type.key,
                0f
            )
        }
        mRenderManager?.removeComposerNodes(arrayOf(assetHelper.customFaceDir))
        mSavedComposeNode.update { clearFaceFilter() }
    }

    override fun setPreset(presetId: String, value: Float): Boolean {
        return mRenderManager?.let { renderManager ->
            if (presetId == mSavedComposeNode.preset.key && value == mSavedComposeNode.preset.value) return@let true
            if (!assetChecker.isPresetFileAvailable(presetId)) return@let false

            removePreset()

            val presetFilePath = assetHelper.getPresetFilePath(presetId)

            val isSuccess = renderManager.appendComposerNodes(arrayOf(presetFilePath)) == BEF_RESULT_SUC &&
                    renderManager.updateComposerNodes(presetFilePath, PRESET_MAKEUP_KEY, value) == BEF_RESULT_SUC &&
                    renderManager.updateComposerNodes(presetFilePath, PRESET_FILTER_KEY, value) == BEF_RESULT_SUC

            if (isSuccess) {
                mSavedComposeNode.update {
                    copy(preset = preset.copy(key = presetId, value = value))
                }
            }

            isSuccess
        } ?: true
    }

    override fun removePreset() {
        mRenderManager?.removeComposerNodes(arrayOf(mSavedComposeNode.preset.key))
        mSavedComposeNode.update { clearPreset() }
    }

    override fun release() {
        removeFaceFilter()
        removePreset()

        mSurfaceTexture?.release()
        mRenderManager?.release()

        mRenderManager = null
        mImageUtil = null
    }

    override fun getSurfaceTexture(): SurfaceTexture? = mSurfaceTexture

    override fun getHandler(): Handler? = mGLHandler

    private fun bindAppliedEffect() {
        mSavedComposeNode.faceFilters.forEach {
            if (!it.type.isUnknown)
                setFaceFilter(it.type.id, it.value)
        }

        if (!mSavedComposeNode.preset.isUnknown) {
            setPreset(mSavedComposeNode.preset.key, mSavedComposeNode.preset.value)
        }
    }

    private fun isEffectApplied(): Boolean {
        return mSavedComposeNode.faceFilters.isNotEmpty() || !mSavedComposeNode.preset.isUnknown
    }

    private fun SavedComposeNode.update(block: SavedComposeNode.() -> SavedComposeNode) {
        mSavedComposeNode = mSavedComposeNode.block()
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
