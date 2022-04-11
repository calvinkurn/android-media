package com.tokopedia.product.detail.common

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import java.util.Locale

/**
 * Created by Yehezkiel on 17/05/21
 */

fun String.goToWebView(context: Context) {
    RouteManager.route(context, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, this))
}

fun UnifyButton?.generateTopchatButtonPdp() {
    if (this == null) return
    val drawAsset = getIconUnifyDrawable(context, IconUnify.CHAT)
    drawAsset?.setBounds(0, 0, 20.toPx(), 20.toPx())
    this.apply {
        setCompoundDrawables(drawAsset, null, null, null)
    }
}

fun UnifyButton.generateTheme(colorDescription: String) {
    when (colorDescription) {
        ProductDetailCommonConstant.KEY_BUTTON_PRIMARY -> {
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.buttonType = UnifyButton.Type.TRANSACTION
            this.isEnabled = true
        }
        ProductDetailCommonConstant.KEY_BUTTON_DISABLE -> {
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.buttonType = UnifyButton.Type.MAIN
            this.isEnabled = false
        }
        ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN -> {
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.buttonType = UnifyButton.Type.MAIN
            this.isEnabled = true
        }
        ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN -> {
            this.buttonVariant = UnifyButton.Variant.GHOST
            this.buttonType = UnifyButton.Type.MAIN
            this.isEnabled = true
        }
        ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY -> {
            this.buttonVariant = UnifyButton.Variant.GHOST
            this.buttonType = UnifyButton.Type.ALTERNATE
            this.isEnabled = true
        }
        else -> {
            this.buttonVariant = UnifyButton.Variant.GHOST
            this.buttonType = UnifyButton.Type.TRANSACTION
            this.isEnabled = true
        }
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