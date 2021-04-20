package com.tokopedia.shop.review.shop.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.shop.review.shop.data.network.ErrorMessageException
import com.tokopedia.shop.review.shop.data.pojo.likedislike.LikeDislikePojo
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import retrofit2.Response
import rx.functions.Func1

/**
 * @author by nisie on 9/29/17.
 */
class LikeDislikeMapper : Func1<Response<TokopediaWsV4Response?>?, LikeDislikeDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>?): LikeDislikeDomain {
        if (response != null) {
            return if (response.isSuccessful) {
                if ((!response.body()!!.isNullData
                                && response.body()!!.errorMessageJoined == "")
                        || !response.body()!!.isNullData && response.body()!!.errorMessages ==
                        null) {
                    val data = response.body()!!.convertDataObj(
                            LikeDislikePojo::class.java)
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
        }else{
            return LikeDislikeDomain(0,0,0)
        }
    }

    private fun mappingToDomain(data: LikeDislikePojo): LikeDislikeDomain {
        return LikeDislikeDomain(
                data.totalLike,
                data.totalDislike,
                data.likeStatus
        )
    }
}