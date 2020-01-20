package com.tokopedia.reputation.common.data.source.cloud.model

import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.reputation.common.view.AnimatedStarsView

data class ReviewLottieModel(
        var isAnimated: Boolean = false,
        var reviewView: LottieAnimationView
)

data class AnimModel(
        var isAnimated: Boolean = false,
        var reviewView: AnimatedStarsView
)