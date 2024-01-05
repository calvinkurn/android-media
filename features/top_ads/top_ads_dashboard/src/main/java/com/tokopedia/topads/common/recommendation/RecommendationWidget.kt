package com.tokopedia.topads.common.recommendation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.TopAdsRecommendationWidgetLayoutBinding
import com.tokopedia.unifycomponents.CardUnify2

class RecommendationWidget(context: Context, attrs: AttributeSet?) : CardUnify2(context, attrs) {

    var binding: TopAdsRecommendationWidgetLayoutBinding? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = TopAdsRecommendationWidgetLayoutBinding.inflate(inflater, this, true)
    }

    private fun setIcon(icon: Int) {
        binding?.insightWidgetIcon?.loadImage(
            ContextCompat.getDrawable(
                context,
                icon
            )
        )
    }

    fun renderWidget(remainingAdsGroup: Int, totalAdsGroup: Int) {
        if (remainingAdsGroup == totalAdsGroup) {
            binding?.insightWidgetTitle?.text = context?.getString(R.string.top_ads_optimised_widget_title_groups_page)
            binding?.insightWidgetSubtitle?.text = context?.getString(R.string.top_ads_optimised_widget_subtitle_groups_page)
            setIcon(R.drawable.perfomace_widget_optimized_icon)
        } else {
            binding?.insightWidgetTitle?.text = String.format(context?.getString(R.string.top_ads_non_optimised_widget_title_groups_page)
                ?: "", remainingAdsGroup, totalAdsGroup)
            binding?.insightWidgetSubtitle?.text = context?.getString(R.string.top_ads_non_optimised_widget_subtitle_groups_page)
            setIcon(R.drawable.performance_widget_default_icon)
        }
    }

    fun renderWidgetOnDetailPage(count: Int) {
        if (count.isZero()) {
            binding?.insightWidgetTitle?.text = context?.getString(R.string.top_ads_optimised_widget_title_group_detail_page)
            binding?.insightWidgetSubtitle?.text = context?.getString(R.string.top_ads_optimised_widget_subtitle_groups_page)
            setIcon(R.drawable.perfomace_widget_optimized_icon)
        } else {
            binding?.insightWidgetTitle?.text = String.format(context?.getString(R.string.top_ads_non_optimised_widget_title_group_detail_page)
                ?: "", count)
            binding?.insightWidgetSubtitle?.text = context?.getString(R.string.top_ads_non_optimised_widget_subtitle_groups_page)
            setIcon(R.drawable.performance_widget_default_icon)
        }

    }
}
