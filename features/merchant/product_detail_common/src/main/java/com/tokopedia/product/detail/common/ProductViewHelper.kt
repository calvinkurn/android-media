package com.tokopedia.product.detail.common

import android.view.View
import androidx.annotation.DimenRes
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by Yehezkiel on 17/05/21
 */
fun View?.showToasterError(message: String,
                           @DimenRes heightOffset: Int = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8,
                           ctaMaxWidth: Int? = null,
                           ctaText: String = "",
                           ctaListener: (() -> Unit?)? = null) {
    this?.let {
        val toasterOffset = resources.getDimensionPixelOffset(heightOffset)
        ctaMaxWidth?.let {
            Toaster.toasterCustomCtaWidth = ctaMaxWidth
        }

        Toaster.toasterCustomBottomHeight = toasterOffset
        if (ctaText.isNotEmpty()) {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, clickListener = {
                ctaListener?.invoke()
            }).show()
        } else {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, clickListener = {
                ctaListener?.invoke()
            }).show()
        }
    }
}

fun View?.showToasterSuccess(message: String,
                             @DimenRes heightOffset: Int = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8,
                             ctaMaxWidth: Int? = null,
                             ctaText: String = "",
                             ctaListener: (() -> Unit?)? = null) {
    this?.let {
        val toasterOffset = resources.getDimensionPixelOffset(heightOffset)
        ctaMaxWidth?.let {
            Toaster.toasterCustomCtaWidth = ctaMaxWidth
        }

        Toaster.toasterCustomBottomHeight = toasterOffset
        if (ctaText.isNotEmpty()) {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ctaText, clickListener = View.OnClickListener {
                ctaListener?.invoke()
            }).show()
        } else {
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, clickListener = View.OnClickListener {
                ctaListener?.invoke()
            }).show()
        }
    }
}