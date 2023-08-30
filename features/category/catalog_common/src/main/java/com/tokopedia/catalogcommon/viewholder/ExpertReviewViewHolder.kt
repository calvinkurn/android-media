package com.tokopedia.catalogcommon.viewholder

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemExpertReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase.Companion.KEY_YOUTUBE_VIDEO_ID

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
                        playVideoYoutube(itemExpert.videoLink)
                    }
                    addItem(view.clLayout)
                }
            }


            onceCreateCarousel = true
        }
    }

    fun playVideoYoutube(videoLink: String) {
        val uri = Uri.parse(videoLink)

        val youTubeVideoId = uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID) ?: ""
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(itemView.context)
            == YouTubeInitializationResult.SUCCESS
        ) {
            RouteManager.route(itemView.context, ApplinkConst.YOUTUBE_PLAYER, youTubeVideoId)
        } else {
            itemView.context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(videoLink)
                )
            )
        }
    }
}
