package com.tokopedia.people.views.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.viewholder.UserReviewViewHolder

/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewAdapter(
    listener: UserReviewViewHolder.Review.Listener,
    private val onLoading: () -> Unit
) : BaseDiffUtilAdapter<UserReviewAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(UserReviewAdapterDelegate.Review(listener))
            .addDelegate(UserReviewAdapterDelegate.Loading())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if(position == (itemCount - 1)) onLoading()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Loading && newItem is Model.Loading -> false
            oldItem is Model.Review && newItem is Model.Review -> oldItem.data.feedbackID == newItem.data.feedbackID
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        data class Review(val data: UserReviewUiModel.Review) : Model
        object Loading : Model
    }
}
