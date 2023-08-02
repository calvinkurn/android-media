package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileBinding
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileShimmerBinding
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.feedplus.R
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.hide
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
            FeedExoPlayer(itemView.context)
        }

        private var mProfile: FeedFollowProfileAdapter.Model.Profile? = null

        init {
            player.getExoPlayer().addListener(object : Player.EventListener {
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

            /** resume & pause video based on viewlifecycle */
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_DESTROY -> {
                            player.setVideoStateListener(null)
                            player.release()
                        }
                        else -> {}
                    }
                }
            })

            binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(view: View) {}

                override fun onViewDetachedFromWindow(view: View) {
                    player.pause()
                }
            })

            binding.playerView.player = player.getExoPlayer()
        }

        fun bind(model: FeedFollowProfileAdapter.Model.Profile) {

            mProfile = model

            /** resume & pause video when user swipe the recommendation (left-right) / content (up-down) */
            if (model.isSelected) {
                if (!player.getExoPlayer().isPlaying) {
                    player.start(model.data.videoUrl, isMute = false)
                    followRecommendationListener.onImpressProfile(model.data)
                }
            } else {
                player.stop()
                binding.playerView.hide()
            }

            binding.imgProfile.setImageUrl(model.data.imageUrl)
            binding.imgThumbnail.setImageUrl(model.data.thumbnailUrl)
            binding.tvProfileName.text = model.data.name
            binding.btnFollow.apply {
                if (model.data.isFollowed) {
                    buttonVariant = UnifyButton.Variant.GHOST
                    text = itemView.context.getString(R.string.feed_following_label)
                } else {
                    buttonVariant = UnifyButton.Variant.FILLED
                    text = itemView.context.getString(contentCommonR.string.feed_component_follow)
                }
            }

            setupListener(model)
        }

        private fun setupListener(model: FeedFollowProfileAdapter.Model.Profile) {
            binding.root.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }

            binding.imgProfile.setOnClickListener { onClickProfile(model.data) }
            binding.tvProfileName.setOnClickListener { onClickProfile(model.data) }
            binding.btnFollow.setOnClickListener {
                followRecommendationListener.onClickFollow(model.data)
            }
            binding.icClose.setOnClickListener {
                followRecommendationListener.onCloseProfileRecommendation(model.data)
            }

            binding.imgProfile.isClickable = model.isSelected
            binding.tvProfileName.isClickable = model.isSelected
            binding.btnFollow.isClickable = model.isSelected
            binding.icClose.isClickable = model.isSelected
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
