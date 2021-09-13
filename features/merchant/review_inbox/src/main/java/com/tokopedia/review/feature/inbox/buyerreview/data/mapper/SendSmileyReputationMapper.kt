package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.SendSmileyPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileyReputationMapper :
    Func1<Response<TokopediaWsV4Response?>, SendSmileyReputationDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>): SendSmileyReputationDomain {
        return if (response.isSuccessful) {
            if ((!response.body()!!.isNullData
                        && response.body()!!.errorMessageJoined == "")
                || !response.body()!!.isNullData && response.body()!!.errorMessages == null
            ) {
                val data = response.body()!!.convertDataObj(SendSmileyPojo::class.java)
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
            var messageError: String? = ""
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

    private fun mappingToDomain(data: SendSmileyPojo): SendSmileyReputationDomain {
        return SendSmileyReputationDomain(data.isSuccess)
    }
}