package com.tokopedia.shop.review.shop.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.shop.review.shop.data.network.ErrorMessageException
import com.tokopedia.shop.review.shop.data.pojo.DeleteReviewResponsePojo
import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseMapper : Func1<Response<TokopediaWsV4Response?>?, DeleteReviewResponseDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>?): DeleteReviewResponseDomain {
        if (response != null) {
            return if (response.isSuccessful) {
                if ((!response.body()!!.isNullData
                                && response.body()!!.errorMessageJoined == "")
                        || !response.body()!!.isNullData && response.body()!!.errorMessages ==
                        null) {
                    val data = response.body()!!.convertDataObj(
                            DeleteReviewResponsePojo::class.java)
                    mappingToDomain(data)
                } else {
                    if (response.body()!!.errorMessages != null
                            && !response.body()!!.errorMessages.isEmpty()) {
                        throw ErrorMessageException(response.body()!!.errorMessageJoined)
                    } else {
                        throw ErrorMessageException("")
                    }
                }
            } else {
                var messageError: String? = null
                if (response.body() != null) {
                    messageError = response.body()!!.errorMessageJoined
                }
                if (!TextUtils.isEmpty(messageError)) {
                    throw ErrorMessageException(messageError)
                } else {
                    throw RuntimeException(response.code().toString())
                }
            }
        } else {
            return DeleteReviewResponseDomain(0)
        }
    }

    private fun mappingToDomain(data: DeleteReviewResponsePojo): DeleteReviewResponseDomain {
        return DeleteReviewResponseDomain(data.isSuccess)
    }
}