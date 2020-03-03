package com.tokopedia.shop.home.view.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.*
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVideoViewHolder
import com.tokopedia.shop.home.view.fragment.IFragmentManager
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import java.lang.Exception
import java.lang.NullPointerException

/**
 * Created by rizqiaryansa on 2020-02-27.
 */

class VideoBinder(private var widgetModel: DisplayWidgetUiModel) {

    private var selectedIndex: Int = 0

    companion object {
        private const val UNIQUE_ID_PREFIX = 1456363
    }

    private var isFullScreen = false

    private var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    private var youtubePlayer: YouTubePlayer? = null

    private var youtubeVideoUrl: String? = null
    private var idYoutubeVideo: String? = null

    fun bind(context: Context, viewHolder: ShopHomeVideoViewHolder,
             fragmentManager: IFragmentManager) {

        setValueVideo(widgetModel)

        bindVideo(context, viewHolder, fragmentManager)
        bindTitle(viewHolder)
    }

    private fun setValueVideo(widgetModel: DisplayWidgetUiModel) {
        youtubeVideoUrl = widgetModel.data?.get(selectedIndex)?.videoUrl
        try {
            idYoutubeVideo = Uri.parse(youtubeVideoUrl).lastPathSegment
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    private fun bindVideo(context: Context, videoViewHolder: ShopHomeVideoViewHolder,
                          fragmentManager: IFragmentManager) {
        val view: View? = videoViewHolder.itemView.findViewWithTag(videoViewHolder.itemView.context
                .getString(R.string.video_component_tag))
        view?.id = UNIQUE_ID_PREFIX + videoViewHolder.adapterPosition
        handleClick(context, videoViewHolder, fragmentManager)
    }

    private fun bindTitle(viewHolder: ShopHomeVideoViewHolder) {
        viewHolder.titleVideoYoutube?.text = widgetModel.header?.title
    }

    private fun handleClick(context: Context, videoViewHolder: ShopHomeVideoViewHolder, fragmentManager: IFragmentManager) {
        videoViewHolder.btnYoutubePlayer?.setOnClickListener { view ->
            if (!YouTubeIntents.isYouTubeInstalled(view.context) ||
                    YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(view.context) != YouTubeInitializationResult.SUCCESS) {
                if (YouTubeIntents.canResolvePlayVideoIntent(view.context)) {
                    fragmentManager.getSupportFragment()?.startActivity(
                            YouTubeIntents.createPlayVideoIntent(view.context,
                                    widgetModel.data?.get(selectedIndex)?.videoUrl))
                    return@setOnClickListener
                }
                val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
                fragmentManager.getSupportFragment()?.startActivity(viewIntent)
                return@setOnClickListener
            }
            if (videoViewHolder.videoContainer?.childCount == 0) {
                if (youtubePlayerFragment == null) {
                    youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
                }
                if (youtubePlayer != null) {
                    try {
                        youtubePlayer?.pause()
                        youtubePlayer?.release()
                    } catch (e: Exception) {
                        try {
                            youtubePlayer?.release()
                        } catch (ignore: Exception) {
                        }
                    }
                    youtubePlayer = null
                }

                fragmentManager.getSupportFragmentManager()
                        ?.beginTransaction()
                        ?.remove(youtubePlayerFragment as Fragment)?.commit()

                fragmentManager.getSupportFragmentManager()
                        ?.executePendingTransactions()
                youtubePlayerFragment = null
            }

            if (youtubePlayerFragment == null) {
                youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
            }
            fragmentManager.getSupportFragmentManager()
                    ?.beginTransaction()
                    ?.add(UNIQUE_ID_PREFIX + videoViewHolder.adapterPosition, youtubePlayerFragment as Fragment)
                    ?.commit()

            youtubePlayerFragment?.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, object :
                    YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
                    youtubePlayer = player
                    youtubePlayer?.loadVideo(widgetModel.data?.get(selectedIndex)?.videoUrl)
                    youtubePlayer?.fullscreenControlFlags = 0
                    youtubePlayer?.setOnFullscreenListener {
                        isFullScreen = it
                    }
                }

                override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                    if (YouTubeIntents.canResolvePlayVideoIntent(
                                    fragmentManager.getSupportFragment()?.context)) {
                        fragmentManager.getSupportFragment()
                                ?.startActivity(YouTubeIntents.createPlayVideoIntent(
                                        fragmentManager.getSupportFragment()?.context,
                                        widgetModel.data?.get(selectedIndex)?.videoUrl))
                        return
                    }
                    val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
                    fragmentManager.getSupportFragment()?.startActivity(viewIntent)
                }
            })
        }
    }

    fun unBind(viewHolder: ShopHomeVideoViewHolder, fragmentManager: IFragmentManager) {
        if (youtubePlayerFragment != null) {
            if (youtubePlayer != null) {
                try {
                    youtubePlayer?.pause()
                    youtubePlayer?.release()
                } catch (e: Exception) {
                    if (youtubePlayer != null) {
                        try {
                            youtubePlayer?.release()
                        } catch (ignore: Exception) {
                        }
                    }
                }
                youtubePlayer = null
            }
            fragmentManager.getSupportFragmentManager()
                    ?.beginTransaction()
                    ?.remove(youtubePlayerFragment as Fragment)
                    ?.commit()

            fragmentManager.getSupportFragmentManager()
                    ?.executePendingTransactions()

            youtubePlayerFragment = null
        }
    }
}