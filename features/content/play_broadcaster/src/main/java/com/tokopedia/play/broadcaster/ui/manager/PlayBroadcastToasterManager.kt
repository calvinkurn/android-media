package com.tokopedia.play.broadcaster.ui.manager

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.unifycomponents.Toaster

/**
 * Created By : Jonathan Darwin on October 12, 2022
 */
class PlayBroadcastToasterManager(
    private val fragment: Fragment,
) {

    private val context: Context
        get() = fragment.requireContext()

    private val view: View?
        get() = fragment.view

    private val toasterBottomMargin: Int by lazy(LazyThreadSafetyMode.NONE) {
        context.resources.getDimensionPixelOffset(
            R.dimen.play_bro_toaster_bottom_margin
        )
    }

    private var activeToaster: Snackbar? = null

    fun showErrorToaster(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        val errMessage = if (customErrMessage == null) {
            ErrorHandler.getErrorMessage(
                context,
                err,
                ErrorHandler.Builder()
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
            fragment.getString(
                R.string.play_bro_custom_error_handler_msg,
                customErrMessage,
                errCode
            )
        }
        showToaster(errMessage, Toaster.TYPE_ERROR, duration, actionLabel, actionListener)
    }

    @SuppressLint("ResourceFragmentDetector")
    fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        val toaster = view?.showToaster(
            message = message,
            duration = duration,
            type = type,
            actionLabel = actionLabel,
            actionListener = actionListener,
            bottomMargin = toasterBottomMargin
        )

        activeToaster = toaster
    }

    fun dismissActiveToaster() {
        activeToaster?.dismiss()
        activeToaster = null
    }
}
