package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileBinding
import com.tokopedia.feedplus.databinding.ItemFeedFollowProfileShimmerBinding
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.feedplus.R

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileViewHolder private constructor() {

    class Profile(
        private val binding: ItemFeedFollowProfileBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: FeedFollowProfileAdapter.Model.Profile) {
            /** TODO: setup exoPlayer */

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

                setOnClickListener {
                    /** TODO: setup click listener */
                }
            }

            binding.root.setOnClickListener { listener.onScrollProfile(absoluteAdapterPosition) }
            binding.imgProfile.setOnClickListener { onClickProfile() }
            binding.tvProfileName.setOnClickListener { onClickProfile() }
            binding.icClose.setOnClickListener {
                /** TODO: handle this */
            }
        }

        private fun onClickProfile() {
            /** TODO: handle this */
        }

        interface Listener {
            fun onScrollProfile(position: Int)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Profile(
                ItemFeedFollowProfileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
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
