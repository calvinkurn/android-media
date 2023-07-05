package com.tokopedia.feedplus.presentation.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedFollowProfileViewHolder
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileAdapterDelegate private constructor() {

    class Profile(
        private val listener: FeedFollowProfileViewHolder.Profile.Listener,
        private val followRecommendationListener: FeedFollowRecommendationListener,
    ) : TypedAdapterDelegate<
        FeedFollowProfileAdapter.Model.Profile,
        FeedFollowProfileAdapter.Model,
        FeedFollowProfileViewHolder.Profile
    >(contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: FeedFollowProfileAdapter.Model.Profile,
            holder: FeedFollowProfileViewHolder.Profile
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedFollowProfileViewHolder.Profile {
            return FeedFollowProfileViewHolder.Profile.create(
                parent,
                listener,
                followRecommendationListener
            )
        }
    }

    class Loading : TypedAdapterDelegate<
        FeedFollowProfileAdapter.Model.Loading,
        FeedFollowProfileAdapter.Model,
        FeedFollowProfileViewHolder.Loading
    >(contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: FeedFollowProfileAdapter.Model.Loading,
            holder: FeedFollowProfileViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedFollowProfileViewHolder.Loading {
            return FeedFollowProfileViewHolder.Loading.create(
                parent
            )
        }
    }
}
