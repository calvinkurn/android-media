package com.tokopedia.productcard.layout.label

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

interface LabelLayoutStrategy {
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

    fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel)

    fun setDiscountMargin(label: Label)

    fun renderLabelPrice(view: View, productCardModel: ProductCardModel)

    fun configContentPosition(view: View)

    fun renderCampaignLabel(
        labelCampaignBackground: ImageView?,
        labelCampaign: Typography?,
        productCardModel: ProductCardModel,
    )

    fun renderOverlayLabel(
        labelOverlayBackground: ImageView?,
        labelOverlay: Typography?,
        labelOverlayStatus: Label?,
        productCardModel: ProductCardModel,
    )
}
