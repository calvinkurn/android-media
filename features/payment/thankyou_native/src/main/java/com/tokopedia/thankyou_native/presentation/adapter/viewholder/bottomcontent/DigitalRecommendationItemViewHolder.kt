package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
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
        if (data.thanksPageData.configFlagData?.shouldHideDigitalRecom == true) return

        binding?.digitalRecommendationView?.loadRecommendation(
            data.fragment, data.pgCategoryIds, data.pageType
        )
    }

}
