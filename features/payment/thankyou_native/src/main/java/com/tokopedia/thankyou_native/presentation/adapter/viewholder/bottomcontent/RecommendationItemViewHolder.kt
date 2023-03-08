package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.DigitalThankPage
import com.tokopedia.thankyou_native.data.mapper.MarketPlaceThankPage
import com.tokopedia.thankyou_native.data.mapper.ThankPageTypeMapper
import com.tokopedia.thankyou_native.databinding.ThankBottomContentRecomBinding
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.addContainer
import com.tokopedia.thankyou_native.presentation.adapter.model.RecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment
import com.tokopedia.thankyou_native.presentation.views.listener.RecommendationItemListener
import com.tokopedia.thankyou_native.recommendation.presentation.view.MarketPlaceRecommendation
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationItemViewHolder(
    view: View?,
    val listener: RecommendationItemListener,
): AbstractViewHolder<RecommendationWidgetModel>(view) {

    private var binding: ThankBottomContentRecomBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thank_bottom_content_recom
    }

    override fun bind(data: RecommendationWidgetModel) {
        listener.addRecommendation()
    }
}
