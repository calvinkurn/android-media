package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.databinding.WidgetShopPageVideoYoutubeBinding
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import timber.log.Timber
import java.util.*

/**
 * Created by rizqiaryansa on 2020-02-26.
 */

class ShopHomeVideoViewHolder(
    val view: View,
    private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view), View.OnClickListener {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_video_youtube
        const val KEY_YOUTUBE_VIDEO_ID = "v"
    }
    private val viewBinding: WidgetShopPageVideoYoutubeBinding? by viewBinding()
    private var youTubeThumbnailShopPageImageUnify: ShopCarouselBannerImageUnify? = null
    private var loaderImageView: LoaderUnify? = null
    private var youTubeVideoModel: ShopHomeDisplayWidgetUiModel? = null

    private var btnYoutubePlayer: AppCompatImageView? = null
    private var ivVideoNotFound: AppCompatImageView? = null
    private var groupVideoError: Group? = null
    private var textViewTitle: Typography? = viewBinding?.textViewTitle

    init {
        youTubeThumbnailShopPageImageUnify = viewBinding?.youtubeHomeShopPage
        btnYoutubePlayer = viewBinding?.btnYoutubePlayer
        btnYoutubePlayer?.hide()
        loaderImageView = viewBinding?.loaderVideoYoutube
        ivVideoNotFound = viewBinding?.ivVideoNotFound
        groupVideoError = viewBinding?.groupVideoError
    }

    override fun bind(model: ShopHomeDisplayWidgetUiModel) {
        this.youTubeVideoModel = model
        if (model.data?.firstOrNull()?.youTubeVideoDetail == null) {
            val videoData = model.data?.firstOrNull()
            listener.loadYouTubeData(videoData?.videoUrl.orEmpty(), model.widgetId)
            btnYoutubePlayer?.setOnClickListener(this)
            ivVideoNotFound?.setOnClickListener(this)
            videoData?.let {
                youTubeThumbnailShopPageImageUnify?.addOnImpressionListener(it) {
                    listener.onDisplayItemImpression(
                        model,
                        it,
                        adapterPosition,
                        0
                    )
                }
            }
        } else {
            val highResVideoThumbnailUrl = model.data?.firstOrNull()?.youTubeVideoDetail.getMaxResThumbnailUrl()
            if (highResVideoThumbnailUrl.isNotEmpty()) {
                youTubeThumbnailShopPageImageUnify?.onUrlLoaded = { isSuccess ->
                    if (isSuccess) {
                        btnYoutubePlayer?.visible()
                        groupVideoError?.gone()
                        loaderImageView?.gone()
                    } else {
                        groupVideoError?.visible()
                        loaderImageView?.gone()
                    }
                    isSuccess
                }
                youTubeThumbnailShopPageImageUnify?.apply {
                    try {
                        if (context.isValidGlideContext())
                            urlSrc = highResVideoThumbnailUrl
                    } catch (e: Throwable) {
                    }
                }
            } else {
                groupVideoError?.visible()
                loaderImageView?.gone()
            }
        }

        textViewTitle?.apply {
            if (model.header.title.isEmpty()) {
                hide()
            } else {
                text = model.header.title
                show()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_youtube_player -> {
                playYoutube(view)
            }
            R.id.ivVideoNotFound -> {
                playYoutube(view)
            }
        }
    }

    private fun playYoutube(view: View) {
        view.context?.let {
            youTubeVideoModel?.data?.let { videoItemList ->
                videoItemList.firstOrNull()?.run {
                    listener.onDisplayItemClicked(youTubeVideoModel, this, adapterPosition, 0)
                }
            }
            val uri = Uri.parse(youTubeVideoModel?.data?.firstOrNull()?.videoUrl ?: "")
            val youTubeVideoId = uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID) ?: ""
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                == YouTubeInitializationResult.SUCCESS
            ) {
                redirectToYoutubePlayerPage(youTubeVideoId)
            } else {
                it.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(HomeConstant.YOUTUBE_BASE_URL + youTubeVideoId)
                    )
                )
            }
        }
    }

    private fun redirectToYoutubePlayerPage(youTubeVideoId: String) {
        RouteManager.route(itemView.context, ApplinkConst.YOUTUBE_PLAYER, youTubeVideoId)
    }

    private fun YoutubeVideoDetailModel?.getMaxResThumbnailUrl(): String {
        return this?.items?.firstOrNull()?.snippet?.thumbnails?.let { thumbnails ->
            thumbnails.maxres?.let {
                return it.url.orEmpty()
            }
            thumbnails.standard?.let {
                return it.url.orEmpty()
            }
            thumbnails.high?.let {
                return it.url.orEmpty()
            }
            thumbnails.medium?.let {
                return it.url.orEmpty()
            }
            thumbnails.default?.let {
                return it.url.orEmpty()
            }
        } ?: ""
    }
}
