package com.tokopedia.review.common.util

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import retrofit2.Response

object ReviewBuyerReviewMapperUtil {

    fun isResponseValid(response: Response<TokopediaWsV4Response?>): Boolean {
        return (response.body()?.isNullData == false && response.body()?.errorMessageJoined == "") || response.body()?.isNullData == false && response.body()?.errorMessages == null
    }

    fun isErrorValid(response: Response<TokopediaWsV4Response?>): Boolean {
        return response.body()?.errorMessages != null && response.body()?.errorMessages?.isNotEmpty() == true
    }
}