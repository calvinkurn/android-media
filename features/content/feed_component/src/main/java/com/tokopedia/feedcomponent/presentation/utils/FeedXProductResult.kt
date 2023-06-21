package com.tokopedia.feedcomponent.presentation.utils

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse

/**
 * Created by shruti.agarwal on 27/03/21.
 */
sealed class FeedXProductResult {
    data class LoadingState(val isLoading: Boolean, val loadingMore: Boolean) : FeedXProductResult()

    object SuccessWithNoData : FeedXProductResult()

    data class Success(val feedXGetActivityProductsResponse: FeedXGetActivityProductsResponse) : FeedXProductResult()

    data class Error(val error: Throwable) : FeedXProductResult()
}
