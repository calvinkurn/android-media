package com.tokopedia.editor.util.lib

import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallHelper
import timber.log.Timber

internal fun safeLoadNativeLibrary(context: Context, name: String) {
    try {
        SplitInstallHelper.loadLibrary(context, name)
    } catch (t: Throwable) {
        Timber.d("media-editor: $t")
    }
}

object SafeNativeLoader {

    private val libs = listOf(
        "c++_shared",
        "mobileffmpeg",
        "mobileffmpeg_abidetect",
        "avutil",
        "swscale",
        "swresample",
        "avcodec",
        "avformat",
        "avdevice",
        "avfilter",
    )

    fun load(context: Context) {
        if (isSplitInstallEnabled()) {
            SplitCompat.installActivity(context)

            libs.forEach {
                safeLoadNativeLibrary(context, it)
            }
        }
    }

    /**
     * A hansel-able feature toggle.
     *
     * If the SplitCompat.install(...) won't work properly,
     * we are able to disable by patching it through Hansel.
     *
     * This temporary method, this LOC will be deleted in
     * upcoming two versions after this editor got released.
     */
    private fun isSplitInstallEnabled(): Boolean {
        return true
    }
}
