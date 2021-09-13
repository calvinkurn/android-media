package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.core.network.ErrorMessageException
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.report.ReportReviewPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewMapper : Func1<Response<TokopediaWsV4Response?>, ReportReviewDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>): ReportReviewDomain {
        return if (response.isSuccessful) {
            if ((!response.body()!!.isNullData
                        && response.body()!!.errorMessageJoined == "")
                || !response.body()!!.isNullData && response.body()!!.errorMessages ==
                null
            ) {
                val data = response.body()!!.convertDataObj(ReportReviewPojo::class.java)
                mappingToDomain(data)
            } else {
                if (response.body()!!.errorMessages != null
                    && !response.body()!!.errorMessages.isEmpty()
                ) {
                    val messageError = response.body()!!.errorMessageJoined
                    mappingToDomain(messageError)
                } else {
                    throw ErrorMessageException("")
                }
            }
        } else {
            var messageError: String? = ""
            if (response.body() != null) {
                messageError = response.body()!!.errorMessageJoined
            }
            if (!TextUtils.isEmpty(messageError)) {
                throw ErrorMessageException(messageError)
            } else {
                throw RuntimeException(response.code().toString())
            }
        }
    }

    private fun mappingToDomain(data: ReportReviewPojo): ReportReviewDomain {
        return ReportReviewDomain(data.isSuccess)
    }

    private fun mappingToDomain(errorMessage: String): ReportReviewDomain {
        return ReportReviewDomain(errorMessage)
    }
}