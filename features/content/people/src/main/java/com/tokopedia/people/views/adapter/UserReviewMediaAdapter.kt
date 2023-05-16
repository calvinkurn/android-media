package com.tokopedia.people.views.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.viewholder.UserReviewMediaViewHolder

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaAdapter(
    listener: UserReviewMediaViewHolder.Media.Listener
) : BaseDiffUtilAdapter<UserReviewMediaAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(UserReviewMediaAdapterDelegate.Media(listener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Media && newItem is Model.Media -> oldItem.feedbackID == newItem.feedbackID
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        data class Media(val feedbackID: String, val attachment: UserReviewUiModel.Attachment) : Model
    }
}
