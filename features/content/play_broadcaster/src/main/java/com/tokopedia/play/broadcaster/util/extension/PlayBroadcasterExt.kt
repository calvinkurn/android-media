package com.tokopedia.play.broadcaster.util.extension

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify

/**
 * Created by jegul on 26/05/20
 */
internal fun Context.getDialog(
        title: String,
        desc: String,
        @DialogUnify.ActionType actionType: Int = DialogUnify.SINGLE_ACTION,
        @DialogUnify.ImageType imageType: Int = DialogUnify.NO_IMAGE,
        primaryCta: String,
        primaryListener: (DialogUnify) -> Unit = {},
        secondaryCta: String = "",
        secondaryListener: (DialogUnify) -> Unit = {},
        cancelable: Boolean = false,
        overlayClose: Boolean = false
): DialogUnify = DialogUnify(this, actionType, imageType).apply {
    setTitle(title)
    setDescription(desc)
    setPrimaryCTAText(primaryCta)
    setSecondaryCTAText(secondaryCta)
    setPrimaryCTAClickListener { primaryListener(this) }
    setSecondaryCTAClickListener { secondaryListener(this) }
    setCancelable(cancelable)
    setOverlayClose(overlayClose)
}

internal fun DialogUnify.setLoading(isLoading: Boolean) {
    dialogPrimaryCTA.isLoading = isLoading
    setCancelable(!isLoading)
    setCanceledOnTouchOutside(!isLoading)
    dialogOverlay.setOnClickListener {
        if (!isLoading) dismiss()
    }
}

internal fun sendCrashlyticsLog(throwable: Throwable) {
    if (GlobalConfig.DEBUG) {
        throwable.printStackTrace()
    }
    try {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    } catch (e: Exception) {
    }
}