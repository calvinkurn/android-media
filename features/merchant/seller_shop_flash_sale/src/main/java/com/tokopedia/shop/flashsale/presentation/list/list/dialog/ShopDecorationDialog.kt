package com.tokopedia.shop.flashsale.presentation.list.list.dialog

import com.tokopedia.imageassets.ImageUrl

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography


class ShopDecorationDialog {

    private var onPrimaryActionClick: () -> Unit = {}
    private var onHyperlinkClick: () -> Unit = {}

    companion object {
        private const val MAX_DIALOG_WIDTH_IN_PERCENT = 0.9f
        private const val IMAGE_URL = ImageUrl.ShopDecorationDialog_IMAGE_URL
    }

    @SuppressLint("UnifyComponentUsage")
    fun show(context : Context) {
        val dialog = Dialog(context)

        val drawable = ColorDrawable(Color.TRANSPARENT)
        dialog.window?.setBackgroundDrawable(drawable)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val view = View.inflate(context, R.layout.ssfs_dialog_shop_decoration, null)
        dialog.setContentView(view)
        setupView(view, dialog)

        dialog.show()

        val width = (context.resources.displayMetrics.widthPixels * MAX_DIALOG_WIDTH_IN_PERCENT)
        dialog.window?.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupView(view: View, dialog: Dialog) {
        val imgIllustration = view.findViewById<ImageUnify>(R.id.imgIllustration)
        imgIllustration.loadImage(IMAGE_URL)

        val btnCreateFlashSale = view.findViewById<UnifyButton>(R.id.btnCreateFlashSale)
        btnCreateFlashSale?.setOnClickListener {
            onPrimaryActionClick()
            dialog.dismiss()
        }

        val btnLater = view.findViewById<UnifyButton>(R.id.btnLater)
        btnLater.applyBackground()
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

    private fun UnifyButton.applyBackground() {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
            val backgroundColor = ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_ghost_background_color)
            setColor(backgroundColor)
        }
        background = drawable
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