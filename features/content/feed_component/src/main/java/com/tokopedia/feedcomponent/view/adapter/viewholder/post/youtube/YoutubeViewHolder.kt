package com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube

import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.youtubeutils.common.YoutubeInitializer
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import kotlinx.android.synthetic.main.item_post_youtube.view.*

/**
 * @author by milhamj on 14/12/18.
 */
class YoutubeViewHolder(private val listener: YoutubePostListener) : BasePostViewHolder<YoutubeViewModel>() {
    override var layoutRes = R.layout.item_post_youtube

    private var youTubeThumbnailLoader: YouTubeThumbnailLoader? = null

    override fun bind(element: YoutubeViewModel) {
        try {
            itemView.youtubeThumbnail.initialize(
                    YoutubePlayerConstant.GOOGLE_API_KEY,
                    YoutubeInitializer.videoThumbnailInitializer(element.youtubeId, object : YoutubeInitializer.OnVideoThumbnailInitialListener {
                        override fun onSuccessInitializeThumbnail(loader: YouTubeThumbnailLoader, youTubeThumbnailView: YouTubeThumbnailView) {
                            itemView.youtubeThumbnail.show()
                            itemView.youtubeThumbnail.setOnClickListener { onThumbnailClicked(element) }
                            youTubeThumbnailLoader = loader

                            destroyReleaseProcess()
                            itemView.ivPlay.show()
                            itemView.progressBar.hide()
                        }

                        override fun onErrorInitializeThumbnail(error: String) {
                            destroyReleaseProcess()
                            itemView.ivPlay.show()
                            itemView.ivPlay.setOnClickListener { onThumbnailClicked(element) }
                            itemView.progressBar.hide()
                        }
                    })
            )
        } catch (e: Exception) {
            itemView.progressBar.show()
            itemView.youtubeThumbnail.hide()
        }
    }

    private fun destroyReleaseProcess() {
        youTubeThumbnailLoader?.release()
    }

    private fun onThumbnailClicked(element: YoutubeViewModel) {
        listener.onYoutubeThumbnailClick(element.positionInFeed, pagerPosition, element.youtubeId)
    }

    interface YoutubePostListener {
        fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String)
    }
}