package com.tokopedia.shop.review.shop.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.shop.review.shop.data.network.ErrorMessageException
import com.tokopedia.shop.review.shop.data.pojo.likedislike.GetLikeDislikePojo
import com.tokopedia.shop.review.shop.data.pojo.likedislike.LikeDislikeList
import com.tokopedia.shop.review.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeListDomain
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * @author by nisie on 9/29/17.
 */
class GetLikeDislikeMapper : Func1<Response<TokopediaWsV4Response?>?, GetLikeDislikeReviewDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>?): GetLikeDislikeReviewDomain {
        if (response != null) {
            return if (response.isSuccessful) {
                if ((!response.body()!!.isNullData
                                && response.body()!!.errorMessageJoined == "")
                        || !response.body()!!.isNullData && response.body()!!.errorMessages ==
                        null) {
                    val data = response.body()!!.convertDataObj(
                            GetLikeDislikePojo::class.java)
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
            return GetLikeDislikeReviewDomain(listOf())
        }
    }

    private fun mappingToDomain(data: GetLikeDislikePojo): GetLikeDislikeReviewDomain {
        return GetLikeDislikeReviewDomain(mappingToList(data.list))
    }

    private fun mappingToList(list: List<LikeDislikeList>): List<LikeDislikeListDomain> {
        val domainList: MutableList<LikeDislikeListDomain> = ArrayList()
        for (pojo in list) {
            domainList.add(LikeDislikeListDomain(
                    pojo.reviewId,
                    pojo.totalLike,
                    pojo.totalDislike,
                    pojo.likeStatus
            ))
        }
        return domainList
    }
}