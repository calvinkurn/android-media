package com.tokopedia.people.views.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.people.databinding.ItemUserReviewMediaImageBinding
import com.tokopedia.people.databinding.ItemUserReviewMediaVideoBinding
import com.tokopedia.people.views.adapter.UserReviewMediaAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaViewHolder private constructor() {

    class Image(
        private val binding: ItemUserReviewMediaImageBinding,
        private val listener: UserReviewMediaAdapter.Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(feedbackId: String, productId: String, attachment: UserReviewUiModel.Attachment.Image) {
            binding.imgMedia.setImageUrl(attachment.thumbnailUrl)

            binding.root.setOnClickListener {
                listener.onMediaClick(feedbackId, productId,  attachment)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: UserReviewMediaAdapter.Listener,
            ) = Image(
                ItemUserReviewMediaImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    class Video(
        private val lifecycleOwner: LifecycleOwner,
        private val binding: ItemUserReviewMediaVideoBinding,
        private val listener: UserReviewMediaAdapter.Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val player by lazyThreadSafetyNone {
            SimpleExoPlayer.Builder(itemView.context).build()
        }

        init {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_DESTROY -> {
                            player.release()
                        }
                        else -> {}
                    }
                }
            })
        }

        fun bind(feedbackId: String, productId: String, attachment: UserReviewUiModel.Attachment.Video) {

            binding.playerView.player = player
            val mediaSource = HlsMediaSource.Factory(
                DefaultDataSourceFactory(
                    itemView.context,
                    Util.getUserAgent(itemView.context, "Tokopedia Android")
                )
            ).createMediaSource(Uri.parse(attachment.mediaUrl))
            player.prepare(mediaSource)

            binding.root.setOnClickListener {
                listener.onMediaClick(feedbackId, productId, attachment)
            }
        }

        companion object {
            fun create(
                lifecycleOwner: LifecycleOwner,
                parent: ViewGroup,
                listener: UserReviewMediaAdapter.Listener,
            ) = Video(
                lifecycleOwner,
                ItemUserReviewMediaVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }
}
