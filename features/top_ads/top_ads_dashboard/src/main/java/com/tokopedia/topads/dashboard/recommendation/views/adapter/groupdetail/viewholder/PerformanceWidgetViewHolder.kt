package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.topads.common.view.getFragmentManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.TopAdsPerformanceWidgetInfoBottomsheetLayoutBinding
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_FREQUENTLY_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_RARITY_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_NOT_RATED_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class PerformanceWidgetViewHolder(itemView: View) :
    AbstractViewHolder<GroupPerformanceWidgetUiModel>(itemView) {

    private val performanceWidgetStatus : Typography = itemView.findViewById(R.id.performanceWidgetStatus)
    private val performanceWidgetDesc : Typography = itemView.findViewById(R.id.performanceWidgetDesc)
    private val performanceWidgetInfoBtn : IconUnify = itemView.findViewById(R.id.performanceWidgetInfoBtn)
    private val block1 : ImageUnify = itemView.findViewById(R.id.block_1)
    private val block2 : ImageUnify = itemView.findViewById(R.id.block_2)
    private val block3 : ImageUnify = itemView.findViewById(R.id.block_3)

    private val infoBottomSheetBinding by lazy {
        TopAdsPerformanceWidgetInfoBottomsheetLayoutBinding.inflate(LayoutInflater.from(itemView.context))
    }
    
    override fun bind(element: GroupPerformanceWidgetUiModel?) {
        element?.let {

            if (it.topSlotImpression == PERFORMANCE_NOT_RATED_THRESHOLD) {
                performanceWidgetStatus.text =
                getString(R.string.topads_insight_not_rated)
                setGreyCondition()

                performanceWidgetDesc.text = getString(R.string.topads_insight_performance_not_rated_desc)
            } else {
                val adPerformance = 100 * it.topSlotImpression / it.impression

                performanceWidgetStatus.text = when {
                    adPerformance > PERFORMANCE_FREQUENTLY_THRESHOLD -> {
                        setGreenCondition()
                        getString(R.string.topads_insight_performance_appears)
                    }
                    adPerformance > PERFORMANCE_RARITY_THRESHOLD -> {
                        setYellowCondition()
                        getString(R.string.topads_insight_top_rarity)
                    }
                    else -> {
                        setRedCondition()
                        getString(R.string.topads_insight_lose_competition)
                    }
                }

                performanceWidgetDesc.text = String.format(getString(R.string.topads_insight_performance_count),it.topSlotImpression, it.impression)
            }
        }
        val infoBottomSheetUnify = BottomSheetUnify().apply {
            setChild(infoBottomSheetBinding.root)
            isDragable = false
            isHideable = true
            showKnob = false
            clearContentPadding = true
            showCloseIcon = true
            isFullpage = false
            setTitle(this@PerformanceWidgetViewHolder.getString(R.string.topads_insight_performance_appears))
        }
        performanceWidgetInfoBtn.setOnClickListener {
            getFragmentManager(itemView.context)?.let {  infoBottomSheetUnify.show( it,"something") }
        }
    }

    private fun setGreenCondition() {
        ImageViewCompat.setImageTintList(
            block1,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        )
        ImageViewCompat.setImageTintList(
            block2,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        )
        ImageViewCompat.setImageTintList(
            block3,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        )
    }

    private fun setYellowCondition() {
        ImageViewCompat.setImageTintList(
            block1,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_YN500))
        )
        ImageViewCompat.setImageTintList(
            block2,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_YN500))
        )
        ImageViewCompat.setImageTintList(
            block3,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
    }

    private fun setRedCondition() {
        ImageViewCompat.setImageTintList(
            block1,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_RN600))
        )
        ImageViewCompat.setImageTintList(
            block2,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
        ImageViewCompat.setImageTintList(
            block3,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
    }

    private fun setGreyCondition() {
        ImageViewCompat.setImageTintList(
            block1,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
        ImageViewCompat.setImageTintList(
            block2,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
        ImageViewCompat.setImageTintList(
            block3,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
        )
    }

    companion object {
        val LAYOUT = R.layout.top_ads_perfomance_widget_item_layout
    }
}
