package com.tokopedia.people.views.viewholder

import com.tokopedia.people.databinding.ItemUserReviewMediaBinding
import com.tokopedia.people.views.uimodel.UserReviewUiModel

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewMediaViewHolder private constructor() {

    class Media(
        private val binding: ItemUserReviewMediaBinding,
        private val listener: Listener,
    ) {

        fun bind(review: UserReviewUiModel.Review) {

        }

        interface Listener {

        }
    }
}
