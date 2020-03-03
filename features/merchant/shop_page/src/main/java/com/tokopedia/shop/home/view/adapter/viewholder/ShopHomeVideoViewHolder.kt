package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.view.activity.ShopHomePageYoutubePlayerActivity
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant

/**
 * Created by rizqiaryansa on 2020-02-26.
 */

class ShopHomeVideoViewHolder(view: View) : AbstractViewHolder<DisplayWidgetUiModel>(view),
    YouTubeThumbnailView.OnInitializedListener, View.OnClickListener{

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_video_youtube
    }

    private val selectedIndex: Int = 0
    private var dataVideo: List<DisplayWidgetUiModel.WidgetItem>? = null
    private var youTubeThumbnailShopPage: YouTubeThumbnailView? = null

    var btnYoutubePlayer: AppCompatImageView? = null
    var titleVideoYoutube: Typography? = null
    var videoContainer: FrameLayout? = null

    init {
        youTubeThumbnailShopPage = view.findViewById(R.id.youtube_home_shop_page)
        btnYoutubePlayer = view.findViewById(R.id.btn_youtube_player)
        btnYoutubePlayer?.hide()
        titleVideoYoutube = view.findViewById(R.id.titleVideoYoutube)
        youTubeThumbnailShopPage?.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, this)
    }

    override fun bind(element: DisplayWidgetUiModel?) {
        dataVideo = element?.data
        btnYoutubePlayer?.setOnClickListener(this)
    }

    override fun onInitializationSuccess(youTubeThumbnailView: YouTubeThumbnailView?, youTubeThumbnailLoader: YouTubeThumbnailLoader?) {

        youTubeThumbnailLoader?.setVideo(dataVideo?.get(selectedIndex)?.videoUrl)
        youTubeThumbnailLoader?.setOnThumbnailLoadedListener(object: YouTubeThumbnailLoader.OnThumbnailLoadedListener {
            override fun onThumbnailLoaded(childYouTubeThumbnailView: YouTubeThumbnailView?, p1: String?) {
                childYouTubeThumbnailView?.visible()
                youTubeThumbnailShopPage?.visible()
                btnYoutubePlayer?.visible()
                youTubeThumbnailLoader.release()
            }

            override fun onThumbnailError(childYouTubeThumbnailView: YouTubeThumbnailView?, errorReason: YouTubeThumbnailLoader.ErrorReason?) {
                youTubeThumbnailLoader.release()
            }
        })
    }

    override fun onInitializationFailure(youTubeThumbnailView: YouTubeThumbnailView?, youTubeInitializationResult: YouTubeInitializationResult?) {
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_youtube_player -> {
                view.context?.let {
                    if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                            == YouTubeInitializationResult.SUCCESS) {
                        it.startActivity(ShopHomePageYoutubePlayerActivity.createIntent(it, dataVideo?.get(selectedIndex)?.videoUrl.toString()))
                    } else {
                        it.startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse(HomeConstant.YOUTUBE_BASE_URL + dataVideo?.get(selectedIndex)?.videoUrl)))
                    }
                }
            }
        }
    }
}