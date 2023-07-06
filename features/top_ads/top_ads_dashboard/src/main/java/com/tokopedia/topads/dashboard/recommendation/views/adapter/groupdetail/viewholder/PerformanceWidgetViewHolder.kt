package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.common.view.getFragmentManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.TopAdsPerformanceWidgetInfoBottomsheetLayoutBinding
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_PERFORMANCE_WIDGET_BOTTOMSHEET_TAG
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_FREQUENTLY_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_NOT_RATED_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_RARITY_THRESHOLD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PERFORMANCE_WIDGET_BG
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R.color as unifyColor

class PerformanceWidgetViewHolder(itemView: View) :
    AbstractViewHolder<GroupPerformanceWidgetUiModel>(itemView) {

    private val performanceWidgetStatus : Typography = itemView.findViewById(R.id.performanceWidgetStatus)
    private val performanceWidgetDesc : Typography = itemView.findViewById(R.id.performanceWidgetDesc)
    private val performanceWidgetInfoBtn : ImageUnify = itemView.findViewById(R.id.performanceWidgetInfoBtn)
    private val performanceWidgetContainer : ConstraintLayout = itemView.findViewById(R.id.performanceWidgetContainer)
    private val block1 : ImageUnify = itemView.findViewById(R.id.block_1)
    private val block2 : ImageUnify = itemView.findViewById(R.id.block_2)
    private val block3 : ImageUnify = itemView.findViewById(R.id.block_3)

    private val infoBottomSheetBinding by lazy {
        TopAdsPerformanceWidgetInfoBottomsheetLayoutBinding.inflate(LayoutInflater.from(itemView.context))
    }
    
    override fun bind(element: GroupPerformanceWidgetUiModel?) {
        element?.let {

            setWidgetBackground()

            if (it.topSlotImpression == PERFORMANCE_NOT_RATED_THRESHOLD) {
                performanceWidgetStatus.text =
                getString(R.string.topads_insight_not_rated)
                setColorConditions(unifyColor.Unify_NN100, unifyColor.Unify_NN100, unifyColor.Unify_NN100)

                performanceWidgetDesc.text = getString(R.string.topads_insight_performance_not_rated_desc)
            } else {
                val adPerformancePercent = 100 * it.topSlotImpression / it.impression

                performanceWidgetStatus.text = when {
                    adPerformancePercent > PERFORMANCE_FREQUENTLY_THRESHOLD -> {
                        setColorConditions(unifyColor.Unify_GN500, unifyColor.Unify_GN500, unifyColor.Unify_GN500)
                        getString(R.string.topads_insight_performance_appears)
                    }
                    adPerformancePercent > PERFORMANCE_RARITY_THRESHOLD -> {
                        setColorConditions(unifyColor.Unify_YN500, unifyColor.Unify_YN500, unifyColor.Unify_NN100)
                        getString(R.string.topads_insight_top_rarity)
                    }
                    else -> {
                        setColorConditions(unifyColor.Unify_RN600, unifyColor.Unify_NN100, unifyColor.Unify_NN100)
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
            getFragmentManager(itemView.context)?.let {  infoBottomSheetUnify.show( it,INSIGHT_PERFORMANCE_WIDGET_BOTTOMSHEET_TAG) }
        }
    }

    private fun setWidgetBackground() {
        Glide.with(itemView.context)
            .load(PERFORMANCE_WIDGET_BG)
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    performanceWidgetContainer.background = resource
                }
            })
    }

    private fun setColorConditions(@ColorRes color1: Int, @ColorRes color2: Int, @ColorRes color3: Int){
        ImageViewCompat.setImageTintList(
            block1,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, color1))
        )
        ImageViewCompat.setImageTintList(
            block2,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, color2))
        )
        ImageViewCompat.setImageTintList(
            block3,
            ColorStateList.valueOf(ContextCompat.getColor(itemView.context, color3))
        )
    }

    companion object {
        val LAYOUT = R.layout.top_ads_perfomance_widget_item_layout
    }
}
