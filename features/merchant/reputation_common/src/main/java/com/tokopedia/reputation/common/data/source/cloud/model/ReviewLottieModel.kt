package com.tokopedia.reputation.common.data.source.cloud.model

import com.tokopedia.reputation.common.view.AnimatedStarsCreateReviewView
import com.tokopedia.reputation.common.view.AnimatedStarsReviewPendingView
import com.tokopedia.reputation.common.view.AnimatedStarsView

data class AnimModel(
        var isAnimated: Boolean = false,
        var reviewView: AnimatedStarsView
)

data class AnimCreateReviewModel(
        var isAnimated: Boolean = false,
        var reviewView: AnimatedStarsCreateReviewView
)

data class AnimReviewPendingModel(
        var isAnimated: Boolean = false,
        var reviewView: AnimatedStarsReviewPendingView
)
