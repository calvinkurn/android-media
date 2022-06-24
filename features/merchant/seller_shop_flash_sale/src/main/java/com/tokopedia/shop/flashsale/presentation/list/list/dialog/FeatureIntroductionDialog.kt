package com.tokopedia.shop.flashsale.presentation.list.list.dialog

import android.content.Context
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class FeatureIntroductionDialog {

    private var onPrimaryActionClick: () -> Unit = {}
    private var onSecondaryActionClick: () -> Unit = {}
    private var onThirdActionClick: () -> Unit = {}

    companion object {
        private const val SHOP_DECORATION_ARTICLE_URL = "https://seller.tokopedia.com/dekorasi-toko"
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
        val btnLater = view.findViewById<UnifyButton>(R.id.btnLater)
        val tpgSecondaryDescription = view.findViewById<Typography>(R.id.tpgSecondaryDescription)

        btnCreateFlashSale?.setOnClickListener {
            onPrimaryActionClick()
            dialog.dismiss()
        }
        btnLater?.setOnClickListener {
            onSecondaryActionClick()
            dialog.dismiss()
        }
        tpgSecondaryDescription?.setOnClickListener {
            onThirdActionClick()
            dialog.dismiss()
        }
    }

    fun setOnPrimaryActionClick(onPrimaryActionClick: () -> Unit) {
        this.onPrimaryActionClick = onPrimaryActionClick
    }

    fun setOnSecondaryActionClick(onSecondaryActionClick: () -> Unit) {
        this.onSecondaryActionClick = onSecondaryActionClick
    }

    fun setOnThirdActionClick(onThirdActionClick: () -> Unit) {
        this.onThirdActionClick = onThirdActionClick
    }
}