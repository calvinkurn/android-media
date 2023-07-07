package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileBinding
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileShimmerBinding
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileViewHolder private constructor() {

    class Profile(
        private val binding: ItemFeedFollowProfileBinding,
        private val listener: Listener,
        private val followRecommendationListener: FeedFollowRecommendationListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: FeedFollowProfileAdapter.Model.Profile) {
            /** TODO: setup exoPlayer */

            if (model.isSelected) {
                /** TODO: play video */
            } else {
                /** TODO: pause video */
            }

            binding.imgProfile.setImageUrl(model.data.imageUrl)
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
                listener: Listener,
                followRecommendationListener: FeedFollowRecommendationListener
            ) = Profile(
                ItemFeedFollowProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
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
