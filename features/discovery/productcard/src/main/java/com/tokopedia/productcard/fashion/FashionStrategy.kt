package com.tokopedia.productcard.fashion

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView

internal interface FashionStrategy {
    fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
    )

    fun getImageHeight(imageWidth: Int): Int

    fun renderLabelReposition(
        labelRepositionBackground: ImageView?,
        labelReposition: Typography?,
        productCardModel: ProductCardModel,
    )

    fun renderTextGimmick(view: View, productCardModel: ProductCardModel)

    fun getGimmickSectionHeight(context: Context, productCardModel: ProductCardModel): Int

    fun renderLabelBestSeller(labelBestSeller: Typography?, productCardModel: ProductCardModel)

    fun getLabelBestSellerHeight(context: Context, productCardModel: ProductCardModel): Int

    fun getGridViewContentMarginTop(context: Context, productCardModel: ProductCardModel): Int

    fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    )

    fun renderLabelBestSellerCategoryBottom(
        textCategoryBottom: Typography?,
        productCardModel: ProductCardModel,
    )

    fun getTextCategoryBottomHeight(context: Context, productCardModel: ProductCardModel): Int

    fun getLabelETAHeight(context: Context, productCardModel: ProductCardModel): Int

    fun renderLabelCampaign(
        labelCampaignBackground: ImageView?,
        textViewLabelCampaign: Typography?,
        productCardModel: ProductCardModel,
    )

    fun getLabelCampaignHeight(context: Context, productCardModel: ProductCardModel): Int

    fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel)

    fun setDiscountMarginLeft(label: Label)

    fun renderLabelPrice(view: View, productCardModel: ProductCardModel)

    fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    )

    fun renderShopBadge(view: View, productCardModel: ProductCardModel)

    fun renderTextShopLocation(view: View, productCardModel: ProductCardModel)

    val sizeCharLimit: Int

    val colorLimit: Int

    fun getLabelVariantSizeCount(productCardModel: ProductCardModel, colorVariantTaken: Int): Int

    fun getLabelVariantColorCount(colorVariant: List<ProductCardModel.LabelGroupVariant>): Int

    fun setupProductNameLineCount(
        textViewProductName: Typography?,
        willShowVariant: Boolean,
        productCardModel: ProductCardModel,
    )
}