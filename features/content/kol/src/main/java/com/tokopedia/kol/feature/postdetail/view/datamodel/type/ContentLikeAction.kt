package com.tokopedia.kol.feature.postdetail.view.datamodel.type

import com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase

/**
 * Created by meyta.taliti on 04/08/22.
 */
enum class ContentLikeAction(val value: String) {
    Like(SubmitLikeContentUseCase.ACTION_LIKE),
    UnLike(SubmitLikeContentUseCase.ACTION_UNLIKE);

    companion object {

        fun getLikeAction(isLiked: Boolean): ContentLikeAction {
            return if (isLiked) UnLike else Like
        }
    }
}