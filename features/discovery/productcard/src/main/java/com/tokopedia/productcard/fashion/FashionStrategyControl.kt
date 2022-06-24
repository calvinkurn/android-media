package com.tokopedia.productcard.fashion

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.addLabelVariantColor
import com.tokopedia.productcard.addLabelVariantCustom
import com.tokopedia.productcard.addLabelVariantSize
import com.tokopedia.productcard.moveLabelPriceConstraint
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.setupImageRatio
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView
import com.tokopedia.productcard.utils.renderOverlayImageRoundedLabel
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx

internal open class FashionStrategyControl: FashionStrategy {
    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
    ) {
        setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            SQUARE_IMAGE_RATIO,
        )
    }

    override fun getImageHeight(imageWidth: Int): Int = imageWidth

    override fun renderOverlayImageRoundedLabel(
        overlayImageRoundedLabelBackground: ImageView?,
        overlayImageRoundedLabelTextView: Typography?,
        productCardModel: ProductCardModel,
    ) {
        renderOverlayImageRoundedLabel(
            false,
            overlayImageRoundedLabelBackground,
            overlayImageRoundedLabelTextView,
            productCardModel,
        )
    }

    override fun renderTextGimmick(view: View, productCardModel: ProductCardModel) {
        val textViewGimmick = view.findViewById<Typography?>(R.id.textViewGimmick)
        if (productCardModel.isShowLabelGimmick())
            textViewGimmick?.initLabelGroup(productCardModel.getLabelGimmick())
        else
            textViewGimmick?.initLabelGroup(null)
    }

    override fun getGimmickSectionHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        return if (productCardModel.isShowLabelGimmick()) {
            val labelGimmick = productCardModel.getLabelGimmick()

            if (labelGimmick != null && labelGimmick.title.isNotEmpty())
                context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_gimmick_height)
            else 0
        }
        else 0
    }

    override fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        renderLabelBestSeller(
            isShowBestSeller,
            labelBestSeller,
            productCardModel
        )
    }

    override fun getLabelBestSellerHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_height) +
                context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_margintop)
        else 0
    }

    override fun getGridViewContentMarginTop(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin_top)
        else context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)
    }

    override fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategorySide = productCardModel.isShowLabelCategorySide()

        renderLabelBestSellerCategorySide(
            isShowCategorySide,
            textCategorySide,
            productCardModel
        )
    }

    override fun renderLabelBestSellerCategoryBottom(
        textCategoryBottom: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategoryBottom = productCardModel.isShowLabelCategoryBottom()

        renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )
    }

    override fun getTextCategoryBottomHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelCategoryBottom = productCardModel.isShowLabelCategoryBottom()

        return if (hasLabelCategoryBottom)
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_category_bottom_height)
        else 0
    }

    override fun renderTextETA(view: View, productCardModel: ProductCardModel) {
        val textViewETA = view.findViewById<Typography?>(R.id.textViewETA)
        textViewETA?.initLabelGroup(productCardModel.getLabelETA())
    }

    override fun getLabelETAHeight(context: Context, productCardModel: ProductCardModel): Int {
        val labelETA = productCardModel.getLabelETA()

        return if (labelETA != null && labelETA.title.isNotEmpty()) {
            val labelETAMarginTop = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_eta_margin_top)
            val labelETAHeight = context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_eta_height)

            labelETAMarginTop + labelETAHeight
        }
        else 0
    }

    override fun renderLabelCampaign(
        labelCampaignBackground: ImageView?,
        textViewLabelCampaign: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCampaign = productCardModel.isShowLabelCampaign()
        renderLabelCampaign(
            isShowCampaign,
            labelCampaignBackground,
            textViewLabelCampaign,
            productCardModel
        )
    }

    override fun getLabelCampaignHeight(context: Context, productCardModel: ProductCardModel): Int {
        return if (productCardModel.isShowLabelCampaign())
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_campaign_height)
        else 0
    }

    override fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel) {
        val constraintLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        constraintLayout?.applyConstraintSet {
            it.clear(R.id.labelDiscount, ConstraintSet.START)
            it.clear(R.id.labelDiscount, ConstraintSet.TOP)

            it.connect(
                R.id.labelDiscount,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )

            it.connect(
                R.id.labelDiscount,
                ConstraintSet.TOP,
                R.id.textViewPrice,
                ConstraintSet.BOTTOM,
            )
        }
    }

    override fun setDiscountMarginLeft(label: Label) {
        val margin = 0
        label.setMargin(margin, margin, margin, margin)
    }

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        val labelPriceNextToVariant = view.findViewById<Label?>(R.id.labelPriceNextToVariant)
        view.moveLabelPriceConstraint(productCardModel)

        if (productCardModel.isShowDiscountOrSlashPrice())
            labelPrice?.initLabelGroup(null)
        else
            labelPrice?.initLabelGroup(productCardModel.getLabelPrice())

        labelPriceNextToVariant?.initLabelGroup(null)
    }

    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
        colorSampleSize: Int,
    ) {
        val container = view.findViewById<LinearLayout?>(R.id.labelVariantContainer)

        container?.shouldShowWithAction(willShowVariant) { labelVariantContainer ->
            labelVariantContainer.removeAllViews()

            val marginStart = 4.toPx()

            productCardModel.getRenderedLabelGroupVariantList(LABEL_VARIANT_CHAR_LIMIT)
                .forEachIndexed { index, labelVariant ->
                    val hasMarginStart = index > 0

                    when {
                        labelVariant.isColor() -> {
                            labelVariantContainer.addLabelVariantColor(
                                labelVariant,
                                hasMarginStart,
                                colorSampleSize,
                                marginStart,
                            )
                        }
                        labelVariant.isSize() -> {
                            labelVariantContainer.addLabelVariantSize(
                                labelVariant,
                                hasMarginStart,
                                marginStart,
                            )
                        }
                        labelVariant.isCustom() -> {
                            labelVariantContainer.addLabelVariantCustom(labelVariant, marginStart)
                        }
                    }
                }
        }
    }
}