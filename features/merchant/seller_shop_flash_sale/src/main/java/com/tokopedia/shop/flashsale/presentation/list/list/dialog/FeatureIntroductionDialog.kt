package com.tokopedia.shop.flashsale.presentation.list.list.dialog

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class FeatureIntroductionDialog {

    private var onPrimaryActionClick: () -> Unit = {}
    private var onHyperlinkClick: () -> Unit = {}

    companion object {
        private const val FEATURE_INTRODUCTION_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_flash_sale_toko_introduction.png"
    }

    fun show(context : Context) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        val view = View.inflate(context, R.layout.ssfs_dialog_feature_introduction, null)
        dialog.setUnlockVersion()
        dialog.setChild(view)
        setupView(view, dialog)
        dialog.show()
    }

    private fun setupView(view: View, dialog: DialogUnify) {
        val imgIllustration = view.findViewById<ImageUnify>(R.id.imgIllustration)
        imgIllustration.loadImage(FEATURE_INTRODUCTION_IMAGE_URL)

        val btnCreateFlashSale = view.findViewById<UnifyButton>(R.id.btnCreateFlashSale)
        btnCreateFlashSale?.setOnClickListener {
            onPrimaryActionClick()
            dialog.dismiss()
        }

        val btnLater = view.findViewById<UnifyButton>(R.id.btnLater)
        btnLater?.setOnClickListener {
            dialog.dismiss()
        }

        val tpgSecondaryDescription = view.findViewById<Typography>(R.id.tpgSecondaryDescription)
        tpgSecondaryDescription.applySpannable(onHyperlinkClick = {
            onHyperlinkClick()
            dialog.dismiss()
        })
    }

    fun setOnPrimaryActionClick(onPrimaryActionClick: () -> Unit) {
        this.onPrimaryActionClick = onPrimaryActionClick
    }


    fun setOnHyperlinkClick(onHyperlinkClick: () -> Unit) {
        this.onHyperlinkClick = onHyperlinkClick
    }


    private fun TextView.applySpannable(onHyperlinkClick : () -> Unit) {
        val hyperlinkText = this.context.getString(R.string.sfs_shop_decor)
        val description = String.format(this.context.getString(R.string.sfs_placeholder_fst_introduction_secondary_description), hyperlinkText)

        val spannableString = SpannableString(description)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                onHyperlinkClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = MethodChecker.getColor(this@applySpannable.context,com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                ds.isUnderlineText = false
            }
        }

        val boldSpan = StyleSpan(Typeface.BOLD)

        val start = description.length - hyperlinkText.length
        spannableString.setSpan(boldSpan, start, description.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, start, description.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        this.text = spannableString
        this.movementMethod = LinkMovementMethod.getInstance()
    }
}