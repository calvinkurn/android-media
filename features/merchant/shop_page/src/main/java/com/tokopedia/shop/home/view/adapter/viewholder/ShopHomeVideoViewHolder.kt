package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.Group
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.view.activity.ShopHomePageYoutubePlayerActivity
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import kotlinx.android.synthetic.main.widget_shop_page_video_youtube.view.*

/**
 * Created by rizqiaryansa on 2020-02-26.
 */

class ShopHomeVideoViewHolder(
        val view: View,
        private val previousViewHolder: AbstractViewHolder<*>?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view),
        YouTubeThumbnailView.OnInitializedListener, View.OnClickListener {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_video_youtube
        const val KEY_YOUTUBE_VIDEO_ID = "v"
    }

    private var videoUrl: String = ""
    private var youTubeThumbnailShopPage: YouTubeThumbnailView? = null
    private var loaderImageView: LoaderImageView? = null
    private var youtubVideoModel: ShopHomeDisplayWidgetUiModel? = null

    private var btnYoutubePlayer: AppCompatImageView? = null
    private var ivVideoNotFound: AppCompatImageView? = null
    private var groupVideoError: Group? = null

    init {
        youTubeThumbnailShopPage = view.findViewById(R.id.youtube_home_shop_page)
        btnYoutubePlayer = view.findViewById(R.id.btn_youtube_player)
        btnYoutubePlayer?.hide()
        loaderImageView = view.findViewById(R.id.loaderVideoYoutube)
        ivVideoNotFound = view.findViewById(R.id.ivVideoNotFound)
        groupVideoError = view.findViewById(R.id.groupVideoError)
        youTubeThumbnailShopPage?.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, this)
    }

    override fun bind(model: ShopHomeDisplayWidgetUiModel) {
        this.youtubVideoModel = model
        val videoData = model.data?.first()
        val uri = Uri.parse(videoData?.videoUrl ?: "")
        videoUrl = uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID) ?: ""
        btnYoutubePlayer?.setOnClickListener(this)
        ivVideoNotFound?.setOnClickListener(this)
        videoData?.let {
            youTubeThumbnailShopPage?.addOnImpressionListener(it) {
                listener.onDisplayItemImpression(
                        model,
                        it,
                        adapterPosition,
                        0
                )
            }
        }
        itemView.textViewTitle?.apply {
            if (model.header.title.isEmpty()) {
                hide()
                if (previousViewHolder is ShopHomeSliderSquareViewHolder || previousViewHolder is ShopHomeCarousellProductViewHolder) {
                    (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                        setMargins(leftMargin, 16.toPx(), rightMargin, bottomMargin)
                    }
                }
            } else {
                text = model.header.title
                show()
            }
        }
    }

    override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView?, youTubeThumbnailLoader: YouTubeThumbnailLoader?) {

        youTubeThumbnailLoader?.setVideo(videoUrl)
        youTubeThumbnailLoader?.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
            override fun onThumbnailLoaded(childYouTubeThumbnailView: YouTubeThumbnailView?, p1: String?) {
                childYouTubeThumbnailView?.visible()
                btnYoutubePlayer?.visible()
                groupVideoError?.gone()
                loaderImageView?.gone()
                youTubeThumbnailLoader.release()
            }

            override fun onThumbnailError(childYouTubeThumbnailView: YouTubeThumbnailView?, errorReason: YouTubeThumbnailLoader.ErrorReason?) {
                childYouTubeThumbnailView?.visible()
                groupVideoError?.visible()
                loaderImageView?.gone()
                youTubeThumbnailLoader.release()
            }
        })
    }

    override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView?, youTubeInitializationResult: YouTubeInitializationResult?) {
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
            youtubVideoModel?.data?.let { videoItemList ->
                listener.onDisplayItemClicked(youtubVideoModel, videoItemList.first(), adapterPosition, 0)
            }
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                it.startActivity(ShopHomePageYoutubePlayerActivity.createIntent(it, videoUrl))
            } else {
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(HomeConstant.YOUTUBE_BASE_URL + videoUrl)))
            }
        }
    }
}