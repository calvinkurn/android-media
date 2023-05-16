package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.people.views.viewholder.UserReviewMediaViewHolder

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaAdapterDelegate private constructor() {

    class Media(
        private val listener: UserReviewMediaViewHolder.Media.Listener,
    ) : TypedAdapterDelegate<UserReviewMediaAdapter.Model.Media, UserReviewMediaAdapter.Model, UserReviewMediaViewHolder.Media>
        (R.layout.view_cc_empty) {
        override fun onBindViewHolder(
            item: UserReviewMediaAdapter.Model.Media,
            holder: UserReviewMediaViewHolder.Media,
        ) {
            holder.bind(item.feedbackID, item.attachment)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): UserReviewMediaViewHolder.Media {
            return UserReviewMediaViewHolder.Media.create(
                parent,
                listener
            )
        }
    }
}

