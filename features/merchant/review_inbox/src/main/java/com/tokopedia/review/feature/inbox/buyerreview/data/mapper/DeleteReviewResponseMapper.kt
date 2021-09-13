package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.DeleteReviewResponsePojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 8/31/17.
 */
class DeleteReviewResponseMapper @Inject constructor() :
    Func1<Response<TokopediaWsV4Response?>?, DeleteReviewResponseDomain> {

    override fun call(response: Response<TokopediaWsV4Response?>?): DeleteReviewResponseDomain {
        response?.let {
            return if (response.isSuccessful) {
                if ((!response.body()!!.isNullData
                            && response.body()!!.errorMessageJoined == "")
                    || !response.body()!!.isNullData && response.body()!!.errorMessages ==
                    null
                ) {
                    val data = response.body()!!.convertDataObj(
                        DeleteReviewResponsePojo::class.java
                    )
                    mappingToDomain(data)
                } else {
                    if (response.body()!!.errorMessages != null
                        && !response.body()!!.errorMessages.isEmpty()
                    ) {
                        throw ErrorMessageException(
                            response.body()!!.errorMessageJoined
                        )
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
                    throw ErrorMessageException(
                        messageError
                    )
                } else {
                    throw RuntimeException(response.code().toString())
                }
            }
        }
        return DeleteReviewResponseDomain()
    }

    private fun mappingToDomain(data: DeleteReviewResponsePojo): DeleteReviewResponseDomain {
        return DeleteReviewResponseDomain(data.isSuccess == 1)
    }
}