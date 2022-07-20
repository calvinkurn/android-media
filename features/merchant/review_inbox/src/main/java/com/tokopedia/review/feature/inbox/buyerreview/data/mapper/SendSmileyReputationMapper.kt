package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.common.util.ReviewBuyerReviewMapperUtil
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.SendSmileyPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileyReputationMapper @Inject constructor() :
    Func1<Response<TokopediaWsV4Response?>?, SendSmileyReputationDomain> {

    override fun call(response: Response<TokopediaWsV4Response?>?): SendSmileyReputationDomain {
        response?.let {
            return if (response.isSuccessful) {
                if (ReviewBuyerReviewMapperUtil.isResponseValid(it)) {
                    val data = response.body()?.convertDataObj(SendSmileyPojo::class.java)
                        ?: SendSmileyPojo()
                    mappingToDomain(data)
                } else {
                    if (ReviewBuyerReviewMapperUtil.isErrorValid(response)) {
                        throw ErrorMessageException(response.body()?.errorMessageJoined)
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
        return SendSmileyReputationDomain()
    }

    private fun mappingToDomain(data: SendSmileyPojo): SendSmileyReputationDomain {
        return SendSmileyReputationDomain(data.isSuccess == 1)
    }
}