package com.tokopedia.catalogcommon.viewholder

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemExpertReviewBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.listener.VideoExpertListener
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.utils.view.binding.viewBinding

class ExpertReviewViewHolder(
    itemView: View,
    private val listener: VideoExpertListener? = null
) :
    AbstractViewHolder<ExpertReviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_experts_review

        private const val keyYoutubeVideoId = "v"
    }

    private val binding by viewBinding<WidgetExpertsReviewBinding>()

    private var onceCreateCarousel = false
    override fun bind(element: ExpertReviewUiModel) {
        binding?.carousel?.apply {
            autoplay = false
            infinite = true
            listener?.onVideoExpertImpression(element)

            // TODO: Re-Implement when iOS Ready
            /*onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    val videoExpertHasSaw = if (prev == element.content.size - 1) {
                        element.content.subList(Int.ZERO, element.content.size)
                    } else {
                        element.content.subList(Int.ZERO, current + 1)
                    }
                    listener?.onVideoExpertImpression(videoExpertHasSaw)
                }
            }*/
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
            onceCreateCarousel = true
        }
    }

    private fun playVideoYoutube(videoLink: String) {
        val uri = Uri.parse(videoLink)

        val youTubeVideoId = uri.getQueryParameter(keyYoutubeVideoId) ?: ""
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
        view.lnPlay.changeColorBackground(R.drawable.bg_circle_border_light,
            MethodChecker.getColor(itemView.context,element.styleIconPlay.background))
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
        view.clLayout.changeColorBackground(R.drawable.bg_rounded_border_dynamic_color,
            Color.parseColor("#${element.backgroundColor}"))
    }

    private fun View.changeColorBackground(resource:Int, color:Int){
        val drawable = ContextCompat.getDrawable(itemView.context, resource)
        if (drawable is GradientDrawable) {
            drawable.setColor(color)
            this.background = drawable
        }
    }
}
