package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.people.views.viewholder.UserReviewViewHolder
import com.tokopedia.content.common.R as contentCommonR

/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewAdapterDelegate private constructor() {

    class Review(
        private val listener: UserReviewViewHolder.Review.Listener,
    ) : TypedAdapterDelegate<UserReviewAdapter.Model.Review, UserReviewAdapter.Model, UserReviewViewHolder.Review>
    (contentCommonR.layout.view_cc_empty) {
        override fun onBindViewHolder(
            item: UserReviewAdapter.Model.Review,
            holder: UserReviewViewHolder.Review
        ) {
            holder.bind(item.data)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): UserReviewViewHolder.Review {
            return UserReviewViewHolder.Review.create(
                parent,
                listener
            )
        }
    }

    class Loading : TypedAdapterDelegate<UserReviewAdapter.Model.Loading, UserReviewAdapter.Model, UserReviewViewHolder.Loading>
        (contentCommonR.layout.view_cc_empty) {

        override fun onBindViewHolder(
            item: UserReviewAdapter.Model.Loading,
            holder: UserReviewViewHolder.Loading
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): UserReviewViewHolder.Loading {
            return UserReviewViewHolder.Loading.create(parent)
        }
    }
}
