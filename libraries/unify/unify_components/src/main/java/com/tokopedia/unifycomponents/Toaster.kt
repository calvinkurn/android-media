package com.tokopedia.unifycomponents

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View

/**
 * Created by meta on 27/02/19.
 */
object Toaster {

    private val RED = "RED"
    private val GREEN = "GREEN"

    fun showGreen(view: View, charSequence: CharSequence, duration: Int) {
        val snackbar = buildView(view, charSequence, duration, GREEN)
        snackbar.show()
    }

    fun showGreenWithAction(view: View, charSequence: CharSequence, duration: Int, actionText: CharSequence, actionClicklistener: View.OnClickListener) {
        val snackbar = buildView(view, charSequence, duration, GREEN)
        snackbar.setAction(actionText, actionClicklistener)
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.Neutral_N0))
        snackbar.show()
    }

    fun showRed(view: View, charSequence: CharSequence, duration: Int) {
        val snackbar = buildView(view, charSequence, duration, RED)
        snackbar.show()
    }

    fun showRedWithAction(view: View, charSequence: CharSequence, duration: Int, actionText: CharSequence, actionClicklistener: View.OnClickListener) {
        val snackbar = buildView(view, charSequence, duration, RED)
        snackbar.setAction(actionText, actionClicklistener)
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.Neutral_N0))
        snackbar.show()
    }

    private fun buildView(view: View, charSequence: CharSequence, duration: Int, type: String): Snackbar {
        val snackbar = Snackbar.make(view, charSequence, duration)
        val snackbarView = snackbar.view
        snackbarView.setPadding(0, 0, 0, 0)
        if (type == RED) {
            snackbarView.background = ContextCompat.getDrawable(view.context, R.drawable.bg_toaster_red)
        } else if (type == GREEN) {
            snackbarView.background = ContextCompat.getDrawable(view.context, R.drawable.bg_toaster_green)
        }
        return snackbar
    }
}
