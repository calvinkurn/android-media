package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.youtubeview

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant


class YoutubeViewViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), YouTubePlayer.OnInitializedListener {

    private var youTubePlayerSupportFragment: YouTubePlayerSupportFragment =
            fragment.activity?.supportFragmentManager?.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragment
    private lateinit var youTubeViewViewModel: YouTubeViewViewModel
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var videoId: String


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        youTubeViewViewModel = discoveryBaseViewModel as YouTubeViewViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        youTubeViewViewModel.getVideoId().observe(fragment.viewLifecycleOwner, Observer {
            videoId = it.videoId ?: ""
            youTubePlayerSupportFragment.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, this)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickVideo(it.videoId
                    ?: "", it.name ?: "", "")
        })

    }


    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {

        if (!wasRestored) {
            if (player != null) {
                youTubePlayer = player
            }
            if (::videoId.isInitialized) {
                youTubePlayer.cueVideo(videoId)
            }
            youTubePlayer.setShowFullscreenButton(false)
        }

    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, player: YouTubeInitializationResult?) {

    }


}