package com.tokopedia.home_recom.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster


fun Fragment.showToastSuccess(message: String){
    activity?.findViewById<View>(android.R.id.content)?.let { view ->
        Toaster.build(view, message, Snackbar.LENGTH_LONG).show()
    }
}

fun Fragment.showToastError(throwable: Throwable? = null){
    activity?.findViewById<View>(android.R.id.content)?.let { view ->
        Toaster.build(
                view,
                RecommendationPageErrorHandler.getErrorMessage(view.context, throwable),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR
        ).show()
    }
}

fun Fragment.showToastSuccessWithAction(message: String, actionString: String, action: () -> Unit){
    activity?.findViewById<View>(android.R.id.content)?.let { view ->
        Toaster.build(
                view,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                actionString,
                View.OnClickListener {
                    action.invoke()
                }
        ).show()
    }
}