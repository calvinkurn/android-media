package com.tokopedia.home_recom.util

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.home_recom.R
import com.tokopedia.network.utils.ErrorHandler

/**
 * Created by Lukas on 2019-07-25
 */
object RecomSnackBar {
    fun showError(view: View, throwable: Throwable?){
        val snackBar = Snackbar.make(
                view,
                ErrorHandler.getErrorMessage(view.context, throwable),
                Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val padding = view.resources.getDimensionPixelSize(R.dimen.dp_16)
        snackBarView.setPadding(padding, 0, padding, 0)
        snackBarView.setBackgroundColor(Color.TRANSPARENT)
        val rootSnackBarView = snackBarView as FrameLayout
        rootSnackBarView.getChildAt(0).setBackgroundResource(R.drawable.bg_toaster_error)
        snackBar.show()
    }

    fun showSuccess(view: View, message: String){
        val snackBar = Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val padding = view.resources.getDimensionPixelSize(R.dimen.dp_16)
        snackBarView.setPadding(padding, 0, padding, 0)
        snackBarView.setBackgroundColor(Color.TRANSPARENT)
        val rootSnackBarView = snackBarView as FrameLayout
        rootSnackBarView.getChildAt(0).setBackgroundResource(R.drawable.bg_toaster_normal)
        snackBar.show()
    }

    fun showSuccessWithAction(view: View, message: String, actionMessage: String, action: (() -> Unit)){
        val snackBar = Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_LONG)
        snackBar.setAction(actionMessage){ action.invoke() }
        val snackBarView = snackBar.view
        val padding = view.resources.getDimensionPixelSize(R.dimen.dp_16)
        snackBarView.setPadding(padding, 0, padding, 0)
        snackBarView.setBackgroundColor(Color.TRANSPARENT)
        val rootSnackBarView = snackBarView as FrameLayout
        rootSnackBarView.getChildAt(0).setBackgroundResource(R.drawable.bg_toaster_normal)
        snackBar.show()
    }
}