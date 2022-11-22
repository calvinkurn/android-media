package com.tokopedia.people.views.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.people.views.uimodel.content.PostUiModel
import com.tokopedia.people.views.viewholder.FeedPostLoadingViewHolder

/**
 * created by fachrizalmrsln on 13/07/22
 **/
class UserFeedPostsBaseAdapter(
    val listener: FeedPostsCallback,
    val onLoadMore: () -> Unit,
) : BaseDiffUtilAdapter<UserFeedPostsBaseAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(UserFeedPostsAdapterDelegate.Loading())
            .addDelegate(UserFeedPostsAdapterDelegate.FeedPosts(listener))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == (itemCount - 1) && holder is FeedPostLoadingViewHolder) onLoadMore()
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return when {
            oldItem is Model.Loading && newItem is Model.Loading -> false
            oldItem is Model.FeedPosts && newItem is Model.FeedPosts -> oldItem.item.id == newItem.item.id
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    interface FeedPostsCallback {
        fun onFeedPostsClick(
            appLink: String,
            itemID: String,
            imageUrl: String,
            position: Int,
            mediaType: String
        )

        fun onImpressFeedPostData(item: PostUiModel, position: Int)
    }

    sealed interface Model {
        object Loading : Model
        data class FeedPosts(val item: PostUiModel) : Model
    }

}
