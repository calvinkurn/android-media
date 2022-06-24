package com.tokopedia.productcard.fashion

import android.content.Context
import android.widget.ImageView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.unifyprinciples.Typography

internal class FashionStrategyNoCampaign: FashionStrategyControl() {
    override fun getLabelCampaignHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun renderLabelCampaign(
        labelCampaignBackground: ImageView?,
        textViewLabelCampaign: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCampaign = false

        renderLabelCampaign(
            isShowCampaign,
            labelCampaignBackground,
            textViewLabelCampaign,
            productCardModel
        )
    }
}