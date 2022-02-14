package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.core.network.ErrorMessageException
import com.tokopedia.review.common.util.ReviewBuyerReviewMapperUtil
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.report.ReportReviewPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewMapper @Inject constructor() : Func1<Response<TokopediaWsV4Response?>?, ReportReviewDomain> {

    override fun call(response: Response<TokopediaWsV4Response?>?): ReportReviewDomain {
        response?.let {
            return if (response.isSuccessful) {
                if (ReviewBuyerReviewMapperUtil.isResponseValid(it)) {
                    val data = response.body()?.convertDataObj(ReportReviewPojo::class.java) ?: ReportReviewPojo()
                    mappingToDomain(data)
                } else {
                    if (ReviewBuyerReviewMapperUtil.isErrorValid(response)) {
                        val messageError = response.body()?.errorMessageJoined ?: ""
                        mappingToDomain(messageError)
                    } else {
                        throw ErrorMessageException("")
                    }
                }
            } else {
                val messageError = response.body()?.errorMessageJoined ?: ""
                if (!TextUtils.isEmpty(messageError)) {
                    throw ErrorMessageException(messageError)
                } else {
                    throw RuntimeException(response.code().toString())
                }
            }
        }
        return ReportReviewDomain("")
    }

    private fun mappingToDomain(data: ReportReviewPojo): ReportReviewDomain {
        return ReportReviewDomain(data.isSuccess)
    }

    private fun mappingToDomain(errorMessage: String): ReportReviewDomain {
        return ReportReviewDomain(errorMessage)
    }
}