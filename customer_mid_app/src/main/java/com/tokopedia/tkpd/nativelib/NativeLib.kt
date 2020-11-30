package com.tokopedia.tkpd.nativelib

import timber.log.Timber

internal object NativeLib {

    @JvmStatic
    fun init() {
        try {
            System.loadLibrary("native-lib")
        } catch (e: UnsatisfiedLinkError) {
            Timber.e(e)
        }
    }

    @JvmStatic
    fun jniBytes(): ByteArray? {
        try {
            return bytesFromJNI()
        } catch (e: UnsatisfiedLinkError) {
            return null
        }
    }

    private external fun bytesFromJNI(): ByteArray?

}