package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewViewHolder private constructor() {

    class Review(
        private val binding: ItemUserReviewBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserReviewUiModel.Review) {
            binding.apply {

            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Review(
                ItemUserReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }

        interface Listener {

        }
    }

    class Loading(
        binding: UpItemUserPostLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(
                parent: ViewGroup,
            ) = Loading(
                UpItemUserPostLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
