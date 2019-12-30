package ai.advance.liveness.lib;


import ai.advance.common.utils.LogUtil;

/**
 * Register the class of the jni method
 * In order to increase security, the JNI method has obfuscated the name, and the original intention of the method is on the annotation.
 */
class LivenessJNI {
    /**
     * true means so load failed by some reason
     */
    private static boolean libraryEnable;

    static {
        try {
            System.loadLibrary("aailiveness_v1.1.9");
            libraryEnable = true;
        } catch (UnsatisfiedLinkError e) {
            libraryEnable = false;
            LogUtil.sdkLogE("Liveness JNI library load failed:" + e.getMessage());
        }
    }

    static boolean nativeEnable() {
        return libraryEnable;
    }

    static void initSDK() {
        if (nativeEnable()) {
            OoOo0OoO("-", "-", "-", "-");
            oOoOoOo00("-", "-");
        }
    }

    /**
     * sdkInit
     * Used to initialize sdk and pass related information to native(On perm version the method passed in parameters is useless)
     */
    private static native void OoOo0OoO(String accessKey, String secretKey, String sdkVersion, String applicationId);

    /**
     * marketInit
     * Initialize country information and build information(On perm version the method passed in parameters is useless)
     */
    private static native long oOoOoOo00(String market, String buildType);

    static String nativeDetection(long detectorHandle, byte[] imageData, int width,
                                  int height, int detectionType) {
        if (nativeEnable()) {
            return oO0ooO(detectorHandle, imageData, width, height, detectionType);
        }
        return null;
    }

    /**
     * nativeDetection
     * Analyze the image of each frame
     */
    private static native String oO0ooO(long detectorHandle, byte[] imageData, int width,
                                        int height, int detectionType);

    static long nativeInit() {
        if (nativeEnable()) {
            return Oo0Oo(ModelUtils.MODEL_PATH);
        }
        return 0;
    }

    /**
     * nativeInit
     * init model
     *
     * @param modelPath model path
     */
    private static native long Oo0Oo(String modelPath);

    static void nativeRelease(long detectorHandle) {
        if (nativeEnable()) {
            OoO(detectorHandle);
        }
    }

    /**
     * nativeRelease
     * release native memory
     */
    private static native void OoO(long detectorHandle);

    static void nativeAuth(String locale) {
        if (nativeEnable()) {
            O0OO0oOo(locale, "-");
        }
    }

    /**
     * auth nativeAuth
     * auth check
     */
    private static native String O0OO0oOo(String locale, String level);

}
