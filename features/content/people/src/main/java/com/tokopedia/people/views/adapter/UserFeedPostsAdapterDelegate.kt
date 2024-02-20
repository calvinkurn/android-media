package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.people.views.viewholder.FeedPostsViewHolder
import com.tokopedia.content.common.R as contentcommonR

/**
 * created by fachrizalmrsln on 22/11/22
 **/
internal class UserFeedPostsAdapterDelegate private constructor() {

    internal class FeedPosts(private val listener: UserFeedPostsBaseAdapter.FeedPostsCallback) :
        TypedAdapterDelegate<UserFeedPostsBaseAdapter.Model.FeedPosts, UserFeedPostsBaseAdapter.Model, FeedPostsViewHolder.Post>
            (contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: UserFeedPostsBaseAdapter.Model.FeedPosts,
            holder: FeedPostsViewHolder.Post,
        ) {
            holder.bindView(item.item)
            listener.onImpressFeedPostData(item = item.item, holder.bindingAdapterPosition)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View,
        ): FeedPostsViewHolder.Post {
            return FeedPostsViewHolder.Post.create(parent, listener)
        }
    }

    internal class Loading : TypedAdapterDelegate<
        UserFeedPostsBaseAdapter.Model.Loading,
        UserFeedPostsBaseAdapter.Model,
        FeedPostsViewHolder.Loading,
        >(contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: UserFeedPostsBaseAdapter.Model.Loading,
            holder: FeedPostsViewHolder.Loading,
        ) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View,
        ): FeedPostsViewHolder.Loading {
            return FeedPostsViewHolder.Loading.create(parent)
        }
    }

    internal class Shimmer : TypedAdapterDelegate<
        UserFeedPostsBaseAdapter.Model.Shimmer,
        UserFeedPostsBaseAdapter.Model,
        FeedPostsViewHolder.Shimmer,
        >(contentcommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: UserFeedPostsBaseAdapter.Model.Shimmer,
            holder: FeedPostsViewHolder.Shimmer,
        ) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View,
        ): FeedPostsViewHolder.Shimmer {
            return FeedPostsViewHolder.Shimmer.create(parent)
        }
    }
}
