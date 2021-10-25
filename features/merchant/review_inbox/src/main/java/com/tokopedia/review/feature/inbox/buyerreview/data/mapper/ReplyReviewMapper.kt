package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReplyReviewPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/28/17.
 */
class ReplyReviewMapper @Inject constructor() : Func1<Response<TokopediaWsV4Response?>?, SendReplyReviewDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>?): SendReplyReviewDomain {
        return if (response?.isSuccessful == true) {
            if ((!response.body()!!.isNullData
                        && response.body()!!.errorMessageJoined == "")
                || !response.body()!!.isNullData && response.body()!!.errorMessages == null
            ) {
                val data = response.body()!!.convertDataObj(ReplyReviewPojo::class.java)
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
            if (response?.body() != null) {
                messageError = response.body()!!.errorMessageJoined
            }
            if (!TextUtils.isEmpty(messageError)) {
                throw ErrorMessageException(
                    messageError
                )
            } else {
                throw RuntimeException(response?.code().toString())
            }
        }
    }

    private fun mappingToDomain(data: ReplyReviewPojo): SendReplyReviewDomain {
        return SendReplyReviewDomain(data.isSuccess == 1)
    }
}