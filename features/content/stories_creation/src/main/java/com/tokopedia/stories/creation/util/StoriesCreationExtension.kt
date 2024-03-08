package com.tokopedia.stories.creation.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play_common.R as play_commonR
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created By : Jonathan Darwin on November 08, 2023
 */

fun List<String>.firstNotEmptyOrNull(): String? {
    return if (isEmpty()) null
    else if (this[0].isEmpty()) null
    else this[0]
}

internal fun View.showErrorToaster(
    err: Throwable,
    customErrMessage: String? = null,
    duration: Int = Toaster.LENGTH_SHORT,
    actionLabel: String = "",
    actionListener: View.OnClickListener = View.OnClickListener {  },
    bottomMargin: Int = 0,
) {
    val errMessage = if (customErrMessage == null) {
        ErrorHandler.getErrorMessage(
            context, err, ErrorHandler.Builder()
                .className(this::class.java.simpleName)
                .build()
        )
    } else {
        val (_, errCode) = ErrorHandler.getErrorMessagePair(
            context,
            err,
            ErrorHandler.Builder()
                .className(this::class.java.simpleName)
                .build()
        )
        context?.getString(
            play_commonR.string.play_custom_error_handler_msg,
            customErrMessage.removeSuffix("."),
            errCode
        ).orEmpty()
    }

    showToaster(
        errMessage,
        Toaster.TYPE_ERROR,
        duration,
        actionLabel,
        actionListener,
        bottomMargin
    )
}

internal fun View.showToaster(
    message: String,
    type: Int = Toaster.TYPE_NORMAL,
    duration: Int = Toaster.LENGTH_LONG,
    actionLabel: String = "",
    actionListener: View.OnClickListener = View.OnClickListener { },
    bottomMargin: Int? = null
) : Snackbar {
    if (actionLabel.isNotEmpty()) Toaster.toasterCustomCtaWidth = resources.getDimensionPixelSize(unifyprinciplesR.dimen.layout_lvl8)
    if (bottomMargin != null) Toaster.toasterCustomBottomHeight = bottomMargin

    return Toaster.build(
        this,
        text = message,
        duration = duration,
        type = type,
        actionText = actionLabel,
        clickListener = actionListener).apply {
        show()
    }
}
