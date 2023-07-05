package com.tokopedia.feedplus.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.presentation.adapter.delegate.FeedFollowProfileAdapterDelegate
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedFollowProfileViewHolder
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileAdapter(
    profileListener: FeedFollowProfileViewHolder.Profile.Listener,
    private val followRecommendationListener: FeedFollowRecommendationListener,
) : BaseDiffUtilAdapter<FeedFollowProfileAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(FeedFollowProfileAdapterDelegate.Profile(profileListener, followRecommendationListener))
            .addDelegate(FeedFollowProfileAdapterDelegate.Loading())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (position == itemCount - 1) {
            followRecommendationListener.onLoadNextProfileRecommendation()
        }
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
