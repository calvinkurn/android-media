package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.databinding.WidgetShopPageCampaignVideoYoutubeBinding
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.view.activity.ShopHomePageYoutubePlayerActivity
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel

class ShopCampaignVideoViewHolder(
    val view: View,
    private val listener: ShopHomeDisplayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.widget_shop_page_campaign_video_youtube
        const val KEY_YOUTUBE_VIDEO_ID = "v"
    }
    private val viewBinding: WidgetShopPageCampaignVideoYoutubeBinding? by viewBinding()
    private var youTubeThumbnailShopPageImageUnify: ShopCarouselBannerImageUnify? = null
    private var loaderImageView: LoaderUnify? = null
    private var btnYoutubePlayer: AppCompatImageView? = null
    private var ivVideoNotFound: AppCompatImageView? = null
    private var groupVideoError: Group? = null
    private var headerView: ShopCampaignTabWidgetHeaderView? = null
    private var youTubeVideoModel: ShopHomeDisplayWidgetUiModel? = null

    init {
        youTubeThumbnailShopPageImageUnify = viewBinding?.youtubeHomeShopPage
        btnYoutubePlayer = viewBinding?.btnYoutubePlayer
        btnYoutubePlayer?.hide()
        loaderImageView = viewBinding?.loaderVideoYoutube
        ivVideoNotFound = viewBinding?.ivVideoNotFound
        groupVideoError = viewBinding?.groupVideoError
        headerView = viewBinding?.headerView
    }

    override fun bind(uiModel: ShopHomeDisplayWidgetUiModel) {
        this.youTubeVideoModel = uiModel
        setHeader(uiModel)
        setVideo(uiModel)
        setWidgetImpressionListener(uiModel)
    }

    private fun setVideo(uiModel: ShopHomeDisplayWidgetUiModel) {
        val cornerRadius = itemView.context?.resources?.getDimension(R.dimen.dp_4).orZero()
        youTubeThumbnailShopPageImageUnify?.cornerRadius = cornerRadius.toInt()
        if (uiModel.data?.firstOrNull()?.youTubeVideoDetail == null) {
            val videoData = uiModel.data?.firstOrNull()
            listener.loadYouTubeData(videoData?.videoUrl.orEmpty(), uiModel.widgetId)
            btnYoutubePlayer?.setOnClickListener(this)
            ivVideoNotFound?.setOnClickListener(this)
            videoData?.let {
                youTubeThumbnailShopPageImageUnify?.addOnImpressionListener(it) {
                    listener.onDisplayItemImpression(
                        uiModel,
                        it,
                        bindingAdapterPosition,
                        0
                    )
                }
            }
        } else {
            val highResVideoThumbnailUrl = uiModel.data.firstOrNull()?.youTubeVideoDetail.getMaxResThumbnailUrl()
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
                    } catch (_: Throwable) {
                    }
                }
            } else {
                groupVideoError?.visible()
                loaderImageView?.gone()
            }
        }
    }

    private fun setHeader(uiModel: ShopHomeDisplayWidgetUiModel) {
        val title = uiModel.header.title
        if (title.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
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
                it.startActivity(ShopHomePageYoutubePlayerActivity.createIntent(it, youTubeVideoId))
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

    private fun setWidgetImpressionListener(model: ShopHomeDisplayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            listener.onDisplayWidgetImpression(model, adapterPosition)
        }
    }
}
