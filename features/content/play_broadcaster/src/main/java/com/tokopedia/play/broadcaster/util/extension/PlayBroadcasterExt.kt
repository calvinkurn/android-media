package com.tokopedia.play.broadcaster.util.extension

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

internal val Throwable.isNetworkError: Boolean
    get() = this is ConnectException ||
            this is SocketTimeoutException ||
            this is UnknownHostException