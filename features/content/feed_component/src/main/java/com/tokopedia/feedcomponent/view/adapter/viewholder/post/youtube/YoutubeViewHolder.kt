package com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube

import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.youtubeutils.common.YoutubeInitializer
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant
import kotlinx.android.synthetic.main.item_post_youtube.view.*

/**
 * @author by milhamj on 14/12/18.
 */
class YoutubeViewHolder : BasePostViewHolder<YoutubeViewModel>() {
    override var layoutRes = R.layout.item_post_youtube

    private var youTubeThumbnailLoader: YouTubeThumbnailLoader? = null

    override fun bind(element: YoutubeViewModel) {
        try {
            itemView.youtubeThumbnail.initialize(
                    YoutubePlayerConstant.GOOGLE_API_KEY,
                    YoutubeInitializer.videoThumbnailInitializer(element.youtubeId, object : YoutubeInitializer.OnVideoThumbnailInitialListener {
                        override fun onSuccessInitializeThumbnail(loader: YouTubeThumbnailLoader, youTubeThumbnailView: YouTubeThumbnailView) {
                            itemView.youtubeThumbnail.visible()
                            itemView.youtubeThumbnail.setOnClickListener { onThumbnailClicked(element) }
                            youTubeThumbnailLoader = loader
                            destroyReleaseProcess()
                            itemView.ivPlay.visible()
                            itemView.progressBar.gone()
                        }

                        override fun onErrorInitializeThumbnail(error: String) {
                            destroyReleaseProcess()
                            itemView.ivPlay.visible()
                            itemView.ivPlay.setOnClickListener { onThumbnailClicked(element) }
                            itemView.progressBar.gone()
                        }
                    })
            )
        } catch (e: Exception) {
            itemView.progressBar.visible()
            itemView.youtubeThumbnail.gone()
        }
    }

    private fun destroyReleaseProcess() {
        youTubeThumbnailLoader?.release()
    }

    private fun onThumbnailClicked(element: YoutubeViewModel) {
        RouteManager.route(itemView.context, element.youtubeId)
    }
}