package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.MarketPlaceThankPage
import com.tokopedia.thankyou_native.data.mapper.ThankPageTypeMapper
import com.tokopedia.thankyou_native.databinding.ThankLayoutDigitalRecomBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.DigitalRecommendationWidgetModel
import com.tokopedia.utils.view.binding.viewBinding

class DigitalRecommendationItemViewHolder(
    view: View?
): AbstractViewHolder<DigitalRecommendationWidgetModel>(view) {

    private var binding: ThankLayoutDigitalRecomBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thank_layout_digital_recom
    }

    override fun bind(data: DigitalRecommendationWidgetModel) {
        val pgCategoryIds = mutableListOf<Int>()
        var pageType = DigitalRecommendationPage.DG_THANK_YOU_PAGE

        if (ThankPageTypeMapper.getThankPageType(data.thanksPageData) is MarketPlaceThankPage) {
            data.thanksPageData.shopOrder.forEach { shopOrder ->
                shopOrder.purchaseItemList.forEach { purchaseItem ->
                    val categoryId = purchaseItem.categoryId.toIntOrNull()
                    categoryId?.let {
                        pgCategoryIds.add(it)
                    }
                }
            }

            pageType = DigitalRecommendationPage.PG_THANK_YOU_PAGE
        }

        binding?.digitalRecommendationView?.shouldShowWithAction(
            !(data.thanksPageData.configFlagData?.shouldHideDigitalRecom ?: false)
        ) {
            binding?.digitalRecommendationView?.loadRecommendation(
                data.fragment, pgCategoryIds, pageType
            )
        }
    }

}
