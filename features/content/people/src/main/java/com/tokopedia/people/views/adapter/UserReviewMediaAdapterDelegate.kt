package com.tokopedia.people.views.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.people.views.viewholder.UserReviewMediaViewHolder

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaAdapterDelegate private constructor() {

    class Image(
        private val listener: UserReviewMediaAdapter.Listener,
    ) : TypedAdapterDelegate<UserReviewMediaAdapter.Model.Image, UserReviewMediaAdapter.Model, UserReviewMediaViewHolder.Image>
        (R.layout.view_cc_empty) {
        override fun onBindViewHolder(
            item: UserReviewMediaAdapter.Model.Image,
            holder: UserReviewMediaViewHolder.Image,
        ) {
            holder.bind(item.feedbackId, item.productId, item.attachment)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): UserReviewMediaViewHolder.Image {
            return UserReviewMediaViewHolder.Image.create(
                parent,
                listener
            )
        }
    }

    class Video(
        private val lifecycleOwner: LifecycleOwner,
        private val listener: UserReviewMediaAdapter.Listener,
    ) : TypedAdapterDelegate<UserReviewMediaAdapter.Model.Video, UserReviewMediaAdapter.Model, UserReviewMediaViewHolder.Video>
        (R.layout.view_cc_empty) {
        override fun onBindViewHolder(
            item: UserReviewMediaAdapter.Model.Video,
            holder: UserReviewMediaViewHolder.Video,
        ) {
            holder.bind(item.feedbackId, item.productId, item.attachment)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): UserReviewMediaViewHolder.Video {
            return UserReviewMediaViewHolder.Video.create(
                lifecycleOwner,
                parent,
                listener
            )
        }
    }
}

