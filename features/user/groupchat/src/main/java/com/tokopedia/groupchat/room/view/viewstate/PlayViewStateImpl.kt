package com.tokopedia.groupchat.room.view.viewstate

import android.content.Context
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatVideoFragment
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.util.TextFormatter
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant

/**
 * @author : Steven 13/02/19
 */
class PlayViewStateImpl(var view: View) : PlayViewState {
    private var toolbar: Toolbar = view.findViewById(R.id.toolbar)

    private var channelBanner: ImageView = view.findViewById(R.id.channel_banner)
    private var sponsorLayout = view.findViewById<View>(R.id.sponsor_layout)
    private var sponsorImage = view.findViewById<ImageView>(R.id.sponsor_image)
    private var videoContainer = view.findViewById<View>(R.id.video_horizontal)

    private var youTubePlayer: YouTubePlayer? = null

    var youtubeRunnable: Handler = Handler()


    override fun setToolbarData(title: String?, bannerUrl: String?, totalView: String?, blurredBannerUrl: String?) {

        toolbar.title = title

        loadImageChannelBanner(view.context, bannerUrl, blurredBannerUrl)

        setToolbarParticipantCount(view.context, TextFormatter.format(totalView))

        when {
            title != null -> setVisibilityHeader(View.VISIBLE)
            else -> setVisibilityHeader(View.GONE)
        }
    }


    override fun loadImageChannelBanner(context: Context, bannerUrl: String?, blurredBannerUrl: String?) {
        if (TextUtils.isEmpty(blurredBannerUrl)) {
            ImageHandler.loadImageBlur(context, channelBanner, bannerUrl)
        } else {
            ImageHandler.LoadImage(channelBanner, blurredBannerUrl)
        }
    }

    private fun setToolbarParticipantCount(context: Context, totalParticipant: String) {
        val textParticipant = String.format("%s %s", totalParticipant, context.getString(R.string.view))
        toolbar.subtitle = textParticipant
    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    fun setVisibilityHeader(visible: Int) {
        toolbar.visibility = visible
        channelBanner.visibility = visible
    }

    override fun setSponsorData(adsId: String?, adsImageUrl: String?, adsName: String?) {
        if (adsId == null || adsImageUrl == null) {
            sponsorLayout.visibility = View.GONE
        } else {
            sponsorLayout.visibility = View.VISIBLE
            ImageHandler.loadImage2(sponsorImage, adsImageUrl, R.drawable.loading_page)
            sponsorImage.setOnClickListener {}
        }

        if (sponsorLayout.visibility == View.VISIBLE) {
            //TO DO analytics event view banner
        }
    }

    fun autoPlayVideo(){
        youtubeRunnable.postDelayed({ youTubePlayer?.play() }, PlayFragment.YOUTUBE_DELAY.toLong())
    }


    override fun initVideoFragment(fragmentManager: FragmentManager, viewModel: ChannelInfoViewModel) {
        videoContainer.visibility = View.GONE
        viewModel.videoId?.let {
            if(it.isEmpty()) return
            val videoFragment = fragmentManager.findFragmentById(R.id.video_container) as GroupChatVideoFragment
            videoFragment.run {
                videoContainer.visibility = View.VISIBLE
                sponsorLayout.visibility = View.GONE

                youTubePlayer?.let {
                    it.cueVideo(viewModel.videoId)
                    autoPlayVideo()
                }

                videoFragment.initialize(
                        YoutubePlayerConstant.GOOGLE_API_KEY,
                        object : YouTubePlayer.OnInitializedListener {
                            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                                if (!wasRestored) {
                                    try {
                                        youTubePlayer = player

                                        youTubePlayer?.let {
                                            it.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                                            it.setShowFullscreenButton(false)
                                            it.cueVideo(viewModel.videoId)
                                            autoPlayVideo()

//                                            it.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
//                                                internal var TAG = "youtube"
//
//                                                override fun onPlaying() {
//                                                    Log.i(TAG, "onPlaying: ")
//                                                    if (onPlayTime == 0L) {
//                                                        onPlayTime = System.currentTimeMillis() / 1000L
//                                                    }
//                                                    analytics.eventClickAutoPlayVideo(getChannelInfoViewModel()!!.getChannelId())
//                                                }
//
//                                                override fun onPaused() {
//                                                    Log.i(TAG, "onPaused: ")
//                                                    onPauseTime = System.currentTimeMillis() / 1000L
//                                                }
//
//                                                override fun onStopped() {
//                                                    Log.i(TAG, "onStopped: ")
//                                                }
//
//                                                override fun onBuffering(b: Boolean) {
//                                                    Log.i(TAG, "onBuffering: ")
//                                                }
//
//                                                override fun onSeekTo(i: Int) {
//                                                    Log.i(TAG, "onSeekTo: ")
//                                                }
//                                            })
//
//                                            it.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
//                                                internal var TAG = "youtube"
//
//                                                override fun onLoading() {
//                                                    Log.i(TAG, "onLoading: ")
//                                                }
//
//                                                override fun onLoaded(s: String) {
//                                                    Log.i(TAG, "onLoaded: ")
//                                                }
//
//                                                override fun onAdStarted() {
//                                                    Log.i(TAG, "onAdStarted: ")
//                                                }
//
//                                                override fun onVideoStarted() {
//                                                    Log.i(TAG, "onVideoStarted: ")
//                                                }
//
//                                                override fun onVideoEnded() {
//                                                    Log.i(TAG, "onVideoEnded: ")
//                                                    onEndTime = System.currentTimeMillis() / 1000L
//                                                }
//
//                                                override fun onError(errorReason: YouTubePlayer.ErrorReason) {
//                                                    Log.i(TAG, errorReason.declaringClass() + " onError: " + errorReason.name)
//                                                }
//                                            })
                                        }

                                    } catch (e: Exception) {
                                        onInitializationFailure(provider, YouTubeInitializationResult.SERVICE_MISSING)
                                    }

                                }
                            }

                            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                                Log.e(GroupChatActivity::class.java.simpleName, "Youtube Player View initialization failed")
                            }
                        }
                )

            }
        }
    }
}
