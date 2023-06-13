package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.people.R
import com.tokopedia.people.views.viewholder.FeedPostLoadingViewHolder
import com.tokopedia.people.views.viewholder.FeedPostsViewHolder

/**
 * created by fachrizalmrsln on 22/11/22
 **/
internal class UserFeedPostsAdapterDelegate private constructor() {

    internal class FeedPosts(private val listener: UserFeedPostsBaseAdapter.FeedPostsCallback) :
        TypedAdapterDelegate<UserFeedPostsBaseAdapter.Model.FeedPosts, UserFeedPostsBaseAdapter.Model, FeedPostsViewHolder>
            (R.layout.up_item_user_feed) {

        override fun onBindViewHolder(
            item: UserFeedPostsBaseAdapter.Model.FeedPosts,
            holder: FeedPostsViewHolder,
        ) {
            holder.bindView(item.item)
            listener.onImpressFeedPostData(item = item.item, holder.bindingAdapterPosition)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View,
        ): FeedPostsViewHolder {
            return FeedPostsViewHolder.create(parent, listener)
        }
    }

    internal class Loading : TypedAdapterDelegate<
        UserFeedPostsBaseAdapter.Model.Loading,
        UserFeedPostsBaseAdapter.Model,
        FeedPostLoadingViewHolder,
        >(R.layout.up_item_loading) {

        override fun onBindViewHolder(
            item: UserFeedPostsBaseAdapter.Model.Loading,
            holder: FeedPostLoadingViewHolder,
        ) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View,
        ): FeedPostLoadingViewHolder {
            return FeedPostLoadingViewHolder.create(parent)
        }
    }

}
