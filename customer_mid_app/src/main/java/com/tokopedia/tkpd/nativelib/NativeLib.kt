package com.tokopedia.tkpd.nativelib

internal object NativeLib {

    private var libraryEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            System.loadLibrary("native-lib")
            libraryEnable = true
        } catch (e: UnsatisfiedLinkError) {
            libraryEnable = false
        }
    }

    @JvmStatic
    fun jniBytes(): ByteArray?{
        if (libraryEnable) {
            return bytesFromJNI()
        } else {
            return null
        }
    }

    private external fun bytesFromJNI(): ByteArray?

}