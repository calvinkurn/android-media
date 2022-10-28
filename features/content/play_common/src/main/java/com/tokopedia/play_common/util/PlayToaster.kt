package com.tokopedia.play_common.util

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play_common.R
import com.tokopedia.unifycomponents.Toaster
import java.lang.ref.WeakReference

/**
 * Created by kenny.hadisaputra on 16/02/22
 */
class PlayToaster(
    view: View,
    lifecycleOwner: LifecycleOwner,
) : LifecycleEventObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val weakRefView = WeakReference(view)
    private val context: Context?
        get() = weakRefView.get()?.context
    private val view: View?
        get() = weakRefView.get()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                dismissToaster()
                Toaster.onCTAClick = View.OnClickListener {}
            }
            else -> {}
        }
    }

    fun dismissToaster() {
    }

    fun showErrorInView(
        view: View,
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
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            context?.getString(
                R.string.play_custom_error_handler_msg,
                customErrMessage.removeSuffix("."),
                errCode
            ).orEmpty()
        }

        showToasterInView(
            view,
            errMessage,
            Toaster.TYPE_ERROR,
            duration,
            actionLabel,
            actionListener,
            bottomMargin
        )
    }

    fun showError(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_SHORT,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  },
        bottomMargin: Int = 0,
    ) {
        val targetView = view ?: return
        showErrorInView(targetView, err, customErrMessage, duration, actionLabel, actionListener, bottomMargin)
    }

    fun showToasterInView(
        view: View,
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_SHORT,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { },
        bottomMargin: Int = 0
    ) {
        Toaster.toasterCustomBottomHeight = bottomMargin

        Toaster.build(view,
            text = message,
            duration = duration,
            type = type,
            actionText = actionLabel,
            clickListener = actionListener,
        ).show()
    }

    fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_SHORT,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { },
        bottomMargin: Int = 0
    ) {
        val targetView = view ?: return
        showToasterInView(targetView, message, type, duration, actionLabel, actionListener, bottomMargin)
    }
}