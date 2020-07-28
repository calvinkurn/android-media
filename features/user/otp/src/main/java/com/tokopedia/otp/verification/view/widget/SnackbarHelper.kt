package com.tokopedia.otp.verification.view.widget

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.otp.R

/**
 * Created by Ade Fulki on 19/05/20.
 */

class SnackbarHelper(
        val context: Context
) {
    @JvmOverloads
    fun makeInfo(
            parentView: View,
            message: String = "",
            duration: Int = Snackbar.LENGTH_SHORT,
            actionMessage: String = "",
            listener: View.OnClickListener? = null,
            callback: Snackbar.Callback? = null): Snackbar {

        val snackbar = Snackbar.make(parentView, message, duration)
                .setTextColor()
                .setInfoBackground()

        listener?.let {
            snackbar.setAction(actionMessage, listener)
        }

        callback?.let {
            snackbar.addCallback(callback)
        }

        ViewCompat.setElevation(snackbar.view, 6f)
        return snackbar
    }

    @JvmOverloads
    fun makeError(
            parentView: View,
            message: String = "",
            duration: Int = Snackbar.LENGTH_SHORT,
            actionMessage: String = "",
            listener: View.OnClickListener? = null,
            callback: Snackbar.Callback? = null): Snackbar {

        val snackbar = Snackbar.make(parentView, message, duration)
                .setTextColor()
                .setErrorBackground()

        listener?.let {
            snackbar.setAction(actionMessage, listener)
        }

        callback?.let {
            snackbar.addCallback(callback)
        }

        ViewCompat.setElevation(snackbar.view, 6f)
        return snackbar
    }

    private fun Snackbar.setTextColor(): Snackbar {
        val textColor = ContextCompat.getColor(context, R.color.font_black_primary_70)
        this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(textColor)
        this.setActionTextColor(textColor)
        return this
    }

    private fun Snackbar.setInfoBackground(): Snackbar {
        this.view.background = ContextCompat.getDrawable(context, R.drawable.bg_snackbar_normal)
        return this
    }

    private fun Snackbar.setErrorBackground(): Snackbar {
        this.view.background = ContextCompat.getDrawable(context, R.drawable.bg_snackbar_error)
        return this
    }
}