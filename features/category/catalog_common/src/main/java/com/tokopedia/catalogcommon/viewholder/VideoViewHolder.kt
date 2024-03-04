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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemVideoBinding
import com.tokopedia.catalogcommon.databinding.WidgetVideoBinding
import com.tokopedia.catalogcommon.listener.VideoListener
import com.tokopedia.catalogcommon.uimodel.VideoUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class VideoViewHolder(
    itemView: View,
    private val listener: VideoListener? = null
) :
    AbstractViewHolder<VideoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_video

        const val KEY_YOUTUBE_VIDEO_ID = "v"
    }

    private val binding by viewBinding<WidgetVideoBinding>()
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
                    view.ivBanner.loadImage(itemExpert.thumbnailUrl)
                    view.tvTitle.text = itemExpert.title
                    view.tvTitle.setTextColor(itemExpert.textTitleColor)
                    view.tvSubTitle.text = itemExpert.author
                    view.tvSubTitle.setTextColor(itemExpert.textSubTitleColor)
                    view.ivPlay.setOnClickListener {
                        playVideoYoutube(itemExpert.videoLink)
                        listener?.onClickVideoExpert()
                    }
                    addItem(view.clLayout)
                }
            }
            activeIndex = Int.ZERO

            listener?.onVideoImpression(element.content, element.widgetName)
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
        view.clLayout.background = MethodChecker.getDrawable(itemView.context, element.backgroundColor)
    }
}
