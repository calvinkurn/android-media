package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileBinding
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileShimmerBinding
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileViewHolder private constructor() {

    class Profile(
        private val binding: ItemFeedFollowProfileBinding,
        private val lifecycleOwner: LifecycleOwner,
        private val listener: Listener,
        private val followRecommendationListener: FeedFollowRecommendationListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val player by lazyThreadSafetyNone {
            SimpleExoPlayer.Builder(itemView.context).build()
        }

        init {
            player.addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            binding.playerView.hide()
                        }
                        Player.STATE_READY -> {
                            binding.playerView.show()
                        }
                        Player.STATE_BUFFERING -> {
                            // don't do anything when buffering, just follow the previous state
                        }
                        else -> binding.playerView.hide()
                    }
                }
            })

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

            binding.playerView.player = player
        }

        fun bind(model: FeedFollowProfileAdapter.Model.Profile) {
            val mediaSource = HlsMediaSource.Factory(
                DefaultDataSourceFactory(
                    itemView.context,
                    Util.getUserAgent(itemView.context, "Tokopedia Android")
                )
            ).createMediaSource(Uri.parse(model.data.videoUrl))
            player.prepare(mediaSource)

            if (model.isSelected) {
                player.playWhenReady = true
            } else {
                player.stop()
            }

            binding.imgProfile.setImageUrl(model.data.imageUrl)
            binding.imgThumbnail.setImageUrl(model.data.thumbnailUrl)
            binding.tvProfileName.text = model.data.name
            binding.btnFollow.apply {
                if (model.data.isFollow) {
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = itemView.context.getString(R.string.feed_following_label)
                } else {
                    buttonVariant = UnifyButton.Variant.FILLED
                    text = itemView.context.getString(R.string.feed_follow_label)
                }
            }

            setupListener(model)
        }

        private fun setupListener(model: FeedFollowProfileAdapter.Model.Profile) {
            binding.root.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }

            if (model.isSelected) {
                binding.imgProfile.setOnClickListener { onClickProfile(model.data) }
                binding.tvProfileName.setOnClickListener { onClickProfile(model.data) }
                binding.btnFollow.setOnClickListener {
                    followRecommendationListener.onClickFollow(model.data)
                }
                binding.icClose.setOnClickListener {
                    followRecommendationListener.onCloseProfileRecommendation(model.data)
                }
            } else {
                binding.imgProfile.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }
                binding.tvProfileName.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }
                binding.btnFollow.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }
                binding.icClose.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }
            }
        }

        private fun onClickProfile(profile: FeedFollowRecommendationModel.Profile) {
            followRecommendationListener.onClickProfileRecommendation(profile)
        }

        interface Listener {
            fun onScrollProfile(position: Int)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                lifecycleOwner: LifecycleOwner,
                listener: Listener,
                followRecommendationListener: FeedFollowRecommendationListener
            ) = Profile(
                ItemFeedFollowProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                lifecycleOwner,
                listener,
                followRecommendationListener
            )
        }
    }

    class Loading(
        binding: ItemFeedFollowProfileShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(
                parent: ViewGroup,
            ) = Loading(
                ItemFeedFollowProfileShimmerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
