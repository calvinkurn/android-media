package com.tokopedia.feedplus.presentation.util.common

import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase

/**
 * Created by shruti.agarwal on 06/03/23.
 */
enum class FeedLikeAction(val value: String) {
    Like(SubmitLikeContentUseCase.ACTION_LIKE),
    UnLike(SubmitLikeContentUseCase.ACTION_UNLIKE);

    companion object {

        fun getLikeAction(isLiked: Boolean): FeedLikeAction {
            return if (isLiked) UnLike else Like
        }
    }
}
