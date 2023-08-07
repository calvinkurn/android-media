package com.tokopedia.people.views.adapter

import androidx.lifecycle.LifecycleOwner
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.viewholder.UserReviewMediaViewHolder

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaAdapter(
    lifecycleOwner: LifecycleOwner,
    listener: Listener,
) : BaseDiffUtilAdapter<UserReviewMediaAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(UserReviewMediaAdapterDelegate.Image(listener))
            .addDelegate(UserReviewMediaAdapterDelegate.Video(lifecycleOwner, listener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Image && newItem is Model.Image -> oldItem.attachment.attachmentID == newItem.attachment.attachmentID
            oldItem is Model.Video && newItem is Model.Video -> oldItem.attachment.attachmentID == newItem.attachment.attachmentID
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    interface Listener {
        fun onMediaClick(
            feedbackId: String,
            productId: String,
            attachment: UserReviewUiModel.Attachment
        )
    }

    sealed interface Model {
        data class Image(
            val feedbackId: String,
            val productId: String,
            val attachment: UserReviewUiModel.Attachment.Image
        ) : Model

        data class Video(
            val feedbackId: String,
            val productId: String,
            val attachment: UserReviewUiModel.Attachment.Video
        ) : Model
    }
}
