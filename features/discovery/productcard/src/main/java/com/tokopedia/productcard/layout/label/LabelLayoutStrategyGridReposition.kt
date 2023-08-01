package com.tokopedia.productcard.layout.label

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderLabelOverlay
import com.tokopedia.productcard.utils.renderLabelOverlayStatus
import com.tokopedia.productcard.utils.renderLabelReposition
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

internal class LabelLayoutStrategyGridReposition : LabelLayoutStrategy {
    override fun renderLabelReposition(
        labelRepositionBackground: ImageView?,
        labelReposition: Typography?,
        productCardModel: ProductCardModel,
    ) {
        renderLabelReposition(
            false,
            labelRepositionBackground,
            labelReposition,
            productCardModel.getLabelReposition(),
        )
    }

    override fun renderTextGimmick(view: View, productCardModel: ProductCardModel) {
        val textViewGimmick = view.findViewById<Typography?>(R.id.textViewGimmick)
        if (!productCardModel.isShowLabelOverlay() && productCardModel.isShowLabelGimmick())
            textViewGimmick?.initLabelGroup(productCardModel.getLabelGimmick())
        else
            textViewGimmick?.initLabelGroup(null)
    }

    override fun getGimmickSectionHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        return if (!productCardModel.isShowLabelOverlay() && productCardModel.isShowLabelGimmick()) {
            val labelGimmick = productCardModel.getLabelGimmick()

            if (labelGimmick != null && labelGimmick.title.isNotEmpty())
                context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_gimmick_height)
            else 0
        } else 0
    }

    override fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowBestSeller = !productCardModel.isShowLabelOverlay()
            && productCardModel.isShowLabelBestSeller()
        com.tokopedia.productcard.utils.renderLabelBestSeller(
            isShowBestSeller,
            labelBestSeller,
            productCardModel
        )
    }

    override fun getLabelBestSellerHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = !productCardModel.isShowLabelOverlay()
            && productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_height) +
                context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_margintop)
        else 0
    }

    override fun getGridViewContentMarginTop(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = !productCardModel.isShowLabelOverlay()
            && productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin_top)
        else context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)
    }

    override fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategorySide = productCardModel.isShowLabelCategorySide()

        com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide(
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

        com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom(
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

    override fun setDiscountMargin(label: Label) {
        val margin = 0
        label.setMargin(margin, margin, margin, margin)
    }

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        val labelPriceReposition = view.findViewById<Label?>(R.id.labelPriceReposition)
        view.moveLabelPriceConstraint(productCardModel)

        if (productCardModel.isShowDiscountOrSlashPrice())
            labelPrice?.initLabelGroup(null)
        else
            labelPrice?.initLabelGroup(productCardModel.getLabelPrice())

        labelPriceReposition?.initLabelGroup(null)
    }


    override fun configContentPosition(view: View) {
        val contentLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)
        val shopBadgeMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_margin_top)
        val textViewShopLocationMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_margin_top)
        val imageFulfillmentnMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_fulfillment_badge_margin_top)
        val linearLayoutImageRatingMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_image_rating_star_margin_top)
        val textViewReviewCountMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_review_count_margin_top)
        val imageFreeOngkirPromoMarginTop =
            view.context.resources.getDimensionPixelSize(R.dimen.product_card_free_ongkir_badge_margin_top)


        contentLayout?.applyConstraintSet {
            it.clear(R.id.imageShopBadge, ConstraintSet.TOP)
            it.clear(R.id.textViewShopLocation, ConstraintSet.TOP)
            it.clear(R.id.imageFulfillment, ConstraintSet.TOP)
            it.clear(R.id.linearLayoutImageRating, ConstraintSet.TOP)
            it.clear(R.id.textViewReviewCount, ConstraintSet.TOP)
            it.clear(R.id.imageFreeOngkirPromo, ConstraintSet.TOP)

            it.connect(
                R.id.imageShopBadge,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                shopBadgeMarginTop,
            )
            it.connect(
                R.id.textViewShopLocation,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                textViewShopLocationMarginTop,
            )
            it.connect(
                R.id.imageFulfillment,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                imageFulfillmentnMarginTop,
            )
            it.connect(
                R.id.linearLayoutImageRating,
                ConstraintSet.TOP,
                R.id.textViewFulfillment,
                ConstraintSet.BOTTOM,
                linearLayoutImageRatingMarginTop,
            )
            it.connect(
                R.id.textViewReviewCount,
                ConstraintSet.TOP,
                R.id.textViewFulfillment,
                ConstraintSet.BOTTOM,
                textViewReviewCountMarginTop,
            )
            it.connect(
                R.id.imageFreeOngkirPromo,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                imageFreeOngkirPromoMarginTop,
            )
        }
    }

    private fun View.moveLabelPriceConstraint(productCardModel: ProductCardModel) {
        val targetConstraint =
            if (productCardModel.discountPercentage.isNotEmpty()) R.id.labelDiscount else R.id.textViewSlashedPrice
        val view = findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        view?.applyConstraintSet {
            it.connect(
                R.id.labelPrice,
                ConstraintSet.TOP,
                targetConstraint,
                ConstraintSet.BOTTOM,
                2.toPx()
            )
        }
    }

    override fun renderCampaignLabel(
        labelCampaignBackground: ImageView?,
        labelCampaign: Typography?,
        productCardModel: ProductCardModel
    ) {
        val isShowCampaign = !productCardModel.isShowLabelOverlay() &&
            productCardModel.isShowLabelCampaign()
        renderLabelCampaign(
            isShowCampaign,
            labelCampaignBackground,
            labelCampaign,
            productCardModel,
        )
    }

    override fun renderOverlayLabel(
        labelOverlayBackground: ImageView?,
        labelOverlay: Typography?,
        labelOverlayStatus: Label?,
        productCardModel: ProductCardModel
    ) {
        val overlayLabel = productCardModel.getLabelOverlay()
        val isShowOverlayLabel = productCardModel.isShowLabelOverlay()
        if (isShowOverlayLabel) {
            if (!overlayLabel?.imageUrl.isNullOrEmpty()) {
                renderLabelOverlay(true, labelOverlayBackground, labelOverlay, overlayLabel)
                renderLabelOverlayStatus(false, labelOverlayStatus, overlayLabel)
            } else {
                renderLabelOverlay(false, labelOverlayBackground, labelOverlay, overlayLabel)
                renderLabelOverlayStatus(true, labelOverlayStatus, overlayLabel)
            }
        } else {
            renderLabelOverlay(false, labelOverlayBackground, labelOverlay, overlayLabel)
            renderLabelOverlayStatus(false, labelOverlayStatus, overlayLabel)
        }
    }

    override fun renderProductStatusLabel(
        labelProductStatus: Label?,
        productCardModel: ProductCardModel
    ) {
        val productStatusLabel = productCardModel.getLabelProductStatus()
        val isShowProductStatus = !productCardModel.isShowLabelOverlay()
            && productStatusLabel != null
        if (isShowProductStatus) {
            labelProductStatus?.initLabelGroup(productStatusLabel)
        } else {
            labelProductStatus?.hide()
        }
    }

    override fun renderSpaceCampaignBestSeller(
        space: Space?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCampaign = !productCardModel.isShowLabelOverlay() &&
            productCardModel.isShowLabelCampaign()
        val isShowBestSeller = !productCardModel.isShowLabelOverlay()
            && productCardModel.isShowLabelBestSeller()
        val isShowCampaignOrBestSeller = isShowCampaign || isShowBestSeller
        space?.showWithCondition(isShowCampaignOrBestSeller)
    }
}
