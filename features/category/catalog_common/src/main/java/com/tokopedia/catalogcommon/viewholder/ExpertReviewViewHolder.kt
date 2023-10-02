package com.tokopedia.catalogcommon.viewholder

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemExpertReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase.Companion.KEY_YOUTUBE_VIDEO_ID

class ExpertReviewViewHolder(
    itemView: View,
    private val listener: VideoExpertListener? = null
) :
    AbstractViewHolder<ExpertReviewUiModel>(itemView) {

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
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener{
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    val videoExpertHasSaw = if (prev == element.content.size-1){
                        element.content.subList(Int.ZERO,element.content.size)

                    }else{
                        element.content.subList(Int.ZERO,current+1)
                    }
                    listener?.onVideoExpertImpression(videoExpertHasSaw)
                }
            }
            if (!onceCreateCarousel) {
                element.content.forEach { itemExpert ->
                    val view = ItemExpertReviewBinding.inflate(
                        LayoutInflater.from(itemView.context),
                        binding?.root,
                        false
                    )
                    setupColorIconPlayAndBackgroundColorCard(itemExpert, view)



                    view.tvReview.text = itemExpert.reviewText
                    view.tvReview.setTextColor(itemExpert.textReviewColor)
                    view.tvTitle.text = itemExpert.title
                    view.tvTitle.setTextColor(itemExpert.textTitleColor)
                    view.tvSubTitle.text = itemExpert.subTitle
                    view.tvSubTitle.setTextColor(itemExpert.textSubTitleColor)
                    view.ivProfile.loadImageRounded(itemExpert.imageReviewer, 4f)
                    view.ivPlay.setOnClickListener {
                        playVideoYoutube(itemExpert.videoLink)
                        listener?.onClickVideoExpert()
                    }
                    addItem(view.clLayout)
                }

            }
            activeIndex = Int.ZERO

            listener?.onVideoExpertImpression(element.content.subList(Int.ZERO,Int.ONE))
            onceCreateCarousel = true
        }
    }

    private fun playVideoYoutube(videoLink: String) {
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

    private fun setupColorIconPlayAndBackgroundColorCard(
        element: ExpertReviewUiModel.ItemExpertReviewUiModel,
        view: ItemExpertReviewBinding
    ) {

        view.lnPlay.setBackgroundResource(element.styleIconPlay.background)
        view.ivPlay.setImage(
            newDarkDisable = ContextCompat.getColor(
                itemView.context,
                element.styleIconPlay.iconColor
            ),
            newDarkEnable = ContextCompat.getColor(
                itemView.context,
                element.styleIconPlay.iconColor
            ),
            newLightDisable = ContextCompat.getColor(
                itemView.context,
                element.styleIconPlay.iconColor
            ),
            newLightEnable = ContextCompat.getColor(
                itemView.context,
                element.styleIconPlay.iconColor
            )
        )
        view.clLayout.setBackgroundResource(element.backgroundColor)
    }
}
