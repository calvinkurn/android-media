package com.tokopedia.product.detail.common

import android.view.View
import androidx.annotation.DimenRes
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 17/05/21
 */

fun UnifyButton?.generateTopchatButtonPdp() {
    if (this == null) return
    val drawAsset = getIconUnifyDrawable(context, IconUnify.CHAT)
    drawAsset?.setBounds(0, 0, 20.toPx(), 20.toPx())
    this.apply {
        setCompoundDrawables(drawAsset, null, null, null)
    }
}

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