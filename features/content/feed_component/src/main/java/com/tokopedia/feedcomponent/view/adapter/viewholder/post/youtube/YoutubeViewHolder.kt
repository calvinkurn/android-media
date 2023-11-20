package com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube

import android.widget.ImageView
import android.widget.ProgressBar
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.youtubeutils.common.YoutubeInitializer
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant

/**
 * @author by milhamj on 14/12/18.
 */
class YoutubeViewHolder(private val listener: YoutubePostListener) : BasePostViewHolder<YoutubeModel>() {
    override var layoutRes = R.layout.item_post_youtube

    private var youTubeThumbnailLoader: YouTubeThumbnailLoader? = null

    private val youtubeThumbnail: YouTubeThumbnailView = itemView.findViewById(R.id.youtubeThumbnail)
    private val ivPlay: ImageView = itemView.findViewById(R.id.ivPlay)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    override fun bind(element: YoutubeModel) {
        try {
            youtubeThumbnail.initialize(
                    YoutubePlayerConstant.GOOGLE_API_KEY,
                    YoutubeInitializer.videoThumbnailInitializer(element.youtubeId, object : YoutubeInitializer.OnVideoThumbnailInitialListener {
                        override fun onSuccessInitializeThumbnail(loader: YouTubeThumbnailLoader, youTubeThumbnailView: YouTubeThumbnailView) {
                            youtubeThumbnail.show()
                            youtubeThumbnail.setOnClickListener { onThumbnailClicked(element) }
                            youTubeThumbnailLoader = loader

                            destroyReleaseProcess()
                            ivPlay.show()
                            progressBar.hide()
                        }

                        override fun onErrorInitializeThumbnail(error: String) {
                            destroyReleaseProcess()
                            ivPlay.show()
                            ivPlay.setOnClickListener { onThumbnailClicked(element) }
                            progressBar.hide()
                        }
                    })
            )
        } catch (e: Exception) {
            progressBar.show()
            youtubeThumbnail.hide()
        }
    }

    private fun destroyReleaseProcess() {
        youTubeThumbnailLoader?.release()
    }

    private fun onThumbnailClicked(element: YoutubeModel) {
        listener.onYoutubeThumbnailClick(element.positionInFeed, pagerPosition, element.youtubeId)
    }

    interface YoutubePostListener {
        fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String)
    }
}
