package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.ItemUserReviewMediaBinding
import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaViewHolder private constructor() {

    class Media(
        private val binding: ItemUserReviewMediaBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(feedbackID: String, attachment: UserReviewUiModel.Attachment) {
            binding.imgMedia.setImageUrl(attachment.mediaUrl)

            binding.icPlay.showWithCondition(attachment.isVideo)
            binding.imgBgPlay.showWithCondition(attachment.isVideo)

            binding.root.setOnClickListener {
                listener.onMediaClick(feedbackID, attachment)
            }
        }

        interface Listener {
            fun onMediaClick(feedbackID: String, attachment: UserReviewUiModel.Attachment)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = Media(
                ItemUserReviewMediaBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }
}
