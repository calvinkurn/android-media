package com.tokopedia.productcard.layout.label

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderLabelOverlay
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

internal class LabelLayoutStrategyFashionReposition: LabelLayoutStrategy {
    override fun renderLabelReposition(
        labelRepositionBackground: ImageView?,
        labelReposition: Typography?,
        productCardModel: ProductCardModel,
    ) {
        com.tokopedia.productcard.utils.renderLabelReposition(
            true,
            labelRepositionBackground,
            labelReposition,
            productCardModel.getLabelReposition(),
        )
    }

    override fun renderTextGimmick(view: View, productCardModel: ProductCardModel) {
        val textViewGimmick = view.findViewById<Typography?>(R.id.textViewGimmick)
        textViewGimmick?.initLabelGroup(null)
    }

    override fun getGimmickSectionHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel,
    ) {
        com.tokopedia.productcard.utils.renderLabelBestSeller(
            false,
            labelBestSeller,
            productCardModel
        )
    }

    override fun getLabelBestSellerHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun getGridViewContentMarginTop(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)

    override fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategorySide = false
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
        val isShowCategoryBottom = false

        com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )
    }

    override fun getTextCategoryBottomHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPriceReposition = view.findViewById<Label?>(R.id.labelPriceReposition)

        if (productCardModel.isShowLabelPrice())
            labelPriceReposition?.initLabelGroup(productCardModel.getLabelPrice())
        else
            labelPriceReposition?.initLabelGroup(null)

        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        labelPrice?.initLabelGroup(null)
    }


    override fun configContentPosition(view: View) {
        val contentLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)
        val shopBadgeMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_margin_top)
        val textViewShopLocationMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_margin_top)
        val imageFulfillmentnMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_fulfillment_badge_margin_top)
        val linearLayoutImageRatingMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_image_rating_star_margin_top)
        val textViewReviewCountMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_review_count_margin_top)
        val imageFreeOngkirPromoMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_free_ongkir_badge_margin_top)

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
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                shopBadgeMarginTop,
            )
            it.connect(
                R.id.textViewShopLocation,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                textViewShopLocationMarginTop,
            )
            it.connect(
                R.id.imageFulfillment,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                imageFulfillmentnMarginTop,
            )
            it.connect(
                R.id.linearLayoutImageRating,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                linearLayoutImageRatingMarginTop,
            )
            it.connect(
                R.id.textViewReviewCount,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                textViewReviewCountMarginTop,
            )
            it.connect(
                R.id.imageFreeOngkirPromo,
                ConstraintSet.TOP,
                R.id.textViewFulfillment,
                ConstraintSet.BOTTOM,
                imageFreeOngkirPromoMarginTop,
            )
        }
    }

    override fun renderCampaignLabel(
        labelCampaignBackground: ImageView?,
        labelCampaign: Typography?,
        productCardModel: ProductCardModel
    ) {
        val isShowCampaign = productCardModel.isShowLabelCampaign()
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
        renderLabelOverlay(
            false,
            labelOverlayBackground,
            labelOverlay,
            productCardModel.getLabelOverlay(),
        )
    }

    override fun renderProductStatusLabel(
        labelProductStatus: Label?,
        productCardModel: ProductCardModel
    ) {
        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())
    }

    override fun renderSpaceCampaignBestSeller(space: Space?, productCardModel: ProductCardModel) {
        val isShowCampaign = productCardModel.isShowLabelCampaign()
        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        val isShowCampaignOrBestSeller = isShowCampaign || isShowBestSeller
        space?.showWithCondition(isShowCampaignOrBestSeller)
    }
}
