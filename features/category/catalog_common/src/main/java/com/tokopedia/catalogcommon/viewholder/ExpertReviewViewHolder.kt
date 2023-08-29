package com.tokopedia.catalogcommon.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemExpertReviewBinding
import com.tokopedia.catalogcommon.databinding.ViewAccordionExpandBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemBannerHeroBinding
import com.tokopedia.catalogcommon.databinding.WidgetItemDummyBinding
import com.tokopedia.catalogcommon.uimodel.DummyUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.toPx
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
            if (!onceCreateCarousel){
                element.content.forEach {
                    val view = ItemExpertReviewBinding.inflate(
                        LayoutInflater.from(itemView.context),
                        binding?.root,
                        false
                    )

                    if (element.darkMode){
                        view.lnPlay.setBackgroundResource(R.drawable.bg_circle_border_dark)
                        view.ivPlay.setImageResource(R.drawable.ic_play_catalog_white)
                        view.clLayout.setBackgroundResource(com.tokopedia.catalogcommon.R.drawable.bg_rounded_border_dark)
                    }else{
                        view.lnPlay.setBackgroundResource(R.drawable.bg_circle_border_light)
                        view.clLayout.setBackgroundResource(com.tokopedia.catalogcommon.R.drawable.bg_rounded_border_light)
                        view.ivPlay.setImageResource(R.drawable.ic_play_catalog_black)
                    }

                    view.tvReview.text = it.reviewText
                    view.tvReview.setTextColor(it.textReviewColor)
                    view.tvTitle.text = it.title
                    view.tvTitle.setTextColor(it.textTitleColor)
                    view.tvSubTitle.text = it.subTitle
                    view.tvSubTitle.setTextColor(it.textSubTitleColor)
                    view.ivProfile.loadImageRounded(it.imageReviewer)
                    addItem(view.clLayout)
                }
                onceCreateCarousel = true
            }
        }
    }
}
