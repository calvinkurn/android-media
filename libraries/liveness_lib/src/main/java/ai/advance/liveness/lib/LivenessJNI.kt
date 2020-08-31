package ai.advance.liveness.lib


import ai.advance.common.utils.LogUtil

/**
 * Register the class of the jni method
 * In order to increase security, the JNI method has obfuscated the name, and the original intention of the method is on the annotation.
 */
internal object LivenessJNI {
    /**
     * true means so load failed by some reason
     */
    private var libraryEnable: Boolean = false

    init {
        try {
            System.loadLibrary("aailiveness_v1.1.9")
            libraryEnable = true
        } catch (e: UnsatisfiedLinkError) {
            libraryEnable = false
            LogUtil.sdkLogE("Liveness JNI library load failed:" + e.message)
        }

    }

    fun nativeEnable(): Boolean {
        return libraryEnable
    }

    fun initSDK() {
        if (nativeEnable()) {
            OoOo0OoO("-", "-", "-", "-")
            oOoOoOo00("-", "-")
        }
    }

    /**
     * sdkInit
     * Used to initialize sdk and pass related information to native(On perm version the method passed in parameters is useless)
     */
    private external fun OoOo0OoO(accessKey: String, secretKey: String, sdkVersion: String, applicationId: String)

    /**
     * marketInit
     * Initialize country information and build information(On perm version the method passed in parameters is useless)
     */
    private external fun oOoOoOo00(market: String, buildType: String): Long

    fun nativeDetection(detectorHandle: Long, imageData: ByteArray, width: Int,
                        height: Int, detectionType: Int): String? {
        return if (nativeEnable()) {
            oO0ooO(detectorHandle, imageData, width, height, detectionType)
        } else null
    }

    /**
     * nativeDetection
     * Analyze the image of each frame
     */
    private external fun oO0ooO(detectorHandle: Long, imageData: ByteArray, width: Int,
                                height: Int, detectionType: Int): String

    fun nativeInit(): Long {
        var result: Long = 0
        if (nativeEnable()) {
            try{
                result = Oo0Oo(ModelUtils.MODEL_PATH)
            } catch (e: Exception) {
                return result
            }
        }
        return result
    }

    /**
     * nativeInit
     * init model
     *
     * @param modelPath model path
     */
    private external fun Oo0Oo(modelPath: String): Long

    fun nativeRelease(detectorHandle: Long) {
        if (nativeEnable()) {
            OoO(detectorHandle)
        }
    }

    /**
     * nativeRelease
     * release native memory
     */
    private external fun OoO(detectorHandle: Long)

    fun nativeAuth(locale: String) {
        if (nativeEnable()) {
            O0OO0oOo(locale, "-")
        }
    }

    /**
     * auth nativeAuth
     * auth check
     */
    private external fun O0OO0oOo(locale: String, level: String): String

}
