package com.tokopedia.people.views.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.viewholder.UserReviewMediaViewHolder

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaAdapter(
    listener: Listener,
) : BaseDiffUtilAdapter<UserReviewMediaAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(UserReviewMediaAdapterDelegate.Image(listener))
            .addDelegate(UserReviewMediaAdapterDelegate.Video(listener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Image && newItem is Model.Image -> oldItem.feedbackID == newItem.feedbackID
            oldItem is Model.Video && newItem is Model.Video -> oldItem.feedbackID == newItem.feedbackID
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    interface Listener {
        fun onMediaClick(feedbackID: String, attachment: UserReviewUiModel.Attachment)
    }

    sealed interface Model {
        data class Image(val feedbackID: String, val attachment: UserReviewUiModel.Attachment.Image) : Model
        data class Video(val feedbackID: String, val attachment: UserReviewUiModel.Attachment.Video) : Model
    }
}
