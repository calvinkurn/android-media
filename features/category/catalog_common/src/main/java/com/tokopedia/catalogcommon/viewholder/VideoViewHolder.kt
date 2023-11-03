package com.tokopedia.catalogcommon.viewholder

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemVideoBinding
import com.tokopedia.catalogcommon.databinding.WidgetExpertsReviewBinding
import com.tokopedia.catalogcommon.listener.VideoListener
import com.tokopedia.catalogcommon.uimodel.VideoUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase.Companion.KEY_YOUTUBE_VIDEO_ID

class VideoViewHolder(
    itemView: View,
    private val listener: VideoListener? = null
) :
    AbstractViewHolder<VideoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_video
    }

    private val binding by viewBinding<WidgetExpertsReviewBinding>()

    private var onceCreateCarousel = false
    override fun bind(element: VideoUiModel) {
        binding?.carousel?.apply {
            autoplay = false
            infinite = true
            if (!onceCreateCarousel) {
                element.content.forEach { itemExpert ->
                    val view = ItemVideoBinding.inflate(
                        LayoutInflater.from(itemView.context),
                        binding?.root,
                        false
                    )
                    setupColorIconPlayAndBackgroundColorCard(itemExpert, view)
                    view.tvTitle.text = itemExpert.title
                    view.tvTitle.setTextColor(itemExpert.textTitleColor)
                    view.tvSubTitle.text = itemExpert.subTitle
                    view.tvSubTitle.setTextColor(itemExpert.textSubTitleColor)
                    view.ivPlay.setOnClickListener {
                        playVideoYoutube(itemExpert.videoLink)
                        listener?.onClickVideoExpert()
                    }
                    addItem(view.clLayout)
                }

            }
            activeIndex = Int.ZERO

            listener?.onVideoImpression(element.content.subList(Int.ZERO,Int.ONE))
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
        element: VideoUiModel.ItemVideoUiModel,
        view: ItemVideoBinding
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
