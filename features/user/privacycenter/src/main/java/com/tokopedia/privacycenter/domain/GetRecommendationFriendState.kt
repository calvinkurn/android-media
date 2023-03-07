package com.tokopedia.privacycenter.domain

sealed class GetRecommendationFriendState(
    val isAllowed: Boolean? = null,
    val throwable: Throwable? = null
) {
    class Loading : GetRecommendationFriendState()
    class Success(isAllowed: Boolean) : GetRecommendationFriendState(isAllowed = isAllowed)
    class Failed(throwable: Throwable) : GetRecommendationFriendState(throwable = throwable)
}
