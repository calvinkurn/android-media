package com.tokopedia.feedplus.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.presentation.adapter.delegate.FeedFollowProfileAdapterDelegate
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedFollowProfileViewHolder
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileAdapter(
    profileListener: FeedFollowProfileViewHolder.Profile.Listener,
) : BaseDiffUtilAdapter<FeedFollowProfileAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(FeedFollowProfileAdapterDelegate.Profile(profileListener))
            .addDelegate(FeedFollowProfileAdapterDelegate.Loading())
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return if (oldItem is Model.Profile && newItem is Model.Profile) {
            oldItem.data.id == newItem.data.id
        } else if (oldItem is Model.Loading && newItem is Model.Loading) {
            false
        } else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        data class Profile(val data: FeedFollowRecommendationModel.Profile) : Model
        object Loading : Model
    }
}
