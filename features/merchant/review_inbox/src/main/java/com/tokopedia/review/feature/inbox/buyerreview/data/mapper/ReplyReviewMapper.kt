package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.common.util.ReviewBuyerReviewMapperUtil
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReplyReviewPojo
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/28/17.
 */
class ReplyReviewMapper @Inject constructor() :
    Func1<Response<TokopediaWsV4Response?>?, SendReplyReviewDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>?): SendReplyReviewDomain {
        return if (response?.isSuccessful == true) {
            if (ReviewBuyerReviewMapperUtil.isResponseValid(response)) {
                val data = response.body()?.convertDataObj(ReplyReviewPojo::class.java)
                    ?: ReplyReviewPojo()
                mappingToDomain(data)
            } else {
                if (ReviewBuyerReviewMapperUtil.isErrorValid(response)) {
                    throw ErrorMessageException(response.body()?.errorMessageJoined)
                } else {
                    throw ErrorMessageException("")
                }
            }
        } else {
            val messageError = response?.body()?.errorMessageJoined ?: ""
            if (!TextUtils.isEmpty(messageError)) {
                throw ErrorMessageException(messageError)
            } else {
                throw RuntimeException(response?.code().toString())
            }
        }
    }

    private fun mappingToDomain(data: ReplyReviewPojo): SendReplyReviewDomain {
        return SendReplyReviewDomain(data.isSuccess == 1)
    }
}