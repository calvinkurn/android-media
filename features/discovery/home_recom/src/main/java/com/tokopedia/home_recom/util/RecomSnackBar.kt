package com.tokopedia.home_recom.util

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.home_recom.R
import com.tokopedia.network.utils.ErrorHandler

/**
 * Created by Lukas on 2019-07-25
 * A class for handling custom snack bar specially for recommendation widget
 */
object RecomSnackBar {

    /**
     * Void [showError]
     * It will show snack bar with red color
     * @param view the root view
     * @param throwable throwable error cause
     */
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

    /**
     * Void [showSuccess]
     * It will show snack bar with default color
     * @param view the root view
     * @param message the message want show it
     */
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

    /**
     * Void [showSuccessWithAction]
     * It will show snack bar with default color and an extra action
     * @param view the root view
     * @param message the message want show it
     * @param actionMessage the message for action button
     * @param action the click action you want handle
     */
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