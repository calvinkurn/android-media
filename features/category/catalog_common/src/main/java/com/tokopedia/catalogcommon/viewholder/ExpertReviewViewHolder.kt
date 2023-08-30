package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemExpertReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.utils.view.binding.viewBinding

class ExpertReviewViewHolder(itemView: View) : AbstractViewHolder<ExpertReviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_experts_review
    }

    private val binding by viewBinding<WidgetExpertsReviewBinding>()

    private var onceCreateCarousel = false
    override fun bind(element: ExpertReviewUiModel) {
        binding?.carousel?.apply {
            autoplay = true
            infinite = true
            if (!onceCreateCarousel) {
                element.content.forEach { itemExpert ->
                    val view = ItemExpertReviewBinding.inflate(
                        LayoutInflater.from(itemView.context),
                        binding?.root,
                        false
                    )

                    if (element.darkMode) {
                        view.lnPlay.setBackgroundResource(R.drawable.bg_circle_border_dark)
                        view.ivPlay.setImageResource(R.drawable.ic_play_catalog_white)
                        view.clLayout.setBackgroundResource(com.tokopedia.catalogcommon.R.drawable.bg_rounded_border_dark)
                    } else {
                        view.lnPlay.setBackgroundResource(R.drawable.bg_circle_border_light)
                        view.clLayout.setBackgroundResource(com.tokopedia.catalogcommon.R.drawable.bg_rounded_border_light)
                        view.ivPlay.setImageResource(R.drawable.ic_play_catalog_black)
                    }

                    view.tvReview.text = itemExpert.reviewText
                    view.tvReview.setTextColor(itemExpert.textReviewColor)
                    view.tvTitle.text = itemExpert.title
                    view.tvTitle.setTextColor(itemExpert.textTitleColor)
                    view.tvSubTitle.text = itemExpert.subTitle
                    view.tvSubTitle.setTextColor(itemExpert.textSubTitleColor)
                    view.ivProfile.loadImageRounded(itemExpert.imageReviewer, 4f)
                    view.ivPlay.setOnClickListener {
                        RouteManager.route(itemView.context, itemExpert.videoLink)
                    }
                    addItem(view.clLayout)
                }
            }


            onceCreateCarousel = true
        }
    }
}
