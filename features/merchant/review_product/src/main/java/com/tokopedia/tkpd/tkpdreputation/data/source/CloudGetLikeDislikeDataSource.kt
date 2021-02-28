package com.tokopedia.tkpd.tkpdreputation.data.source

import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.GetLikeDislikePojo
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.LikeDislikeList
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeListDomain
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService
import com.tokopedia.usecase.RequestParams

class CloudGetLikeDislikeDataSource(
        private val reputationService: ReputationService
) {
    suspend fun getLikeDislikeReview(params: RequestParams): GetLikeDislikeReviewDomain {
        val response = reputationService.api.getLikeDislikeReview(params.paramsAllValueInString)
        return if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                if ((!responseBody.isNullData && responseBody.errorMessageJoined.isEmpty()) ||
                        !responseBody.isNullData && responseBody.errorMessageJoined == null) {
                    val data = responseBody.convertDataObj(GetLikeDislikePojo::class.java)
                    mappingToDomain(data)
                } else {
                    if (responseBody.errorMessages != null && responseBody.errorMessages.isNotEmpty()) {
                        throw ErrorMessageException(responseBody.errorMessageJoined)
                    } else throw ErrorMessageException("")
                }
            } ?: throw ErrorMessageException("")
        } else {
            val messageError = response.body()?.errorMessageJoined ?: ""
            if (messageError.isNotEmpty()) {
                throw ErrorMessageException(messageError)
            } else throw RuntimeException(response.code().toString())
        }
    }

    private fun mappingToDomain(data: GetLikeDislikePojo): GetLikeDislikeReviewDomain {
        return GetLikeDislikeReviewDomain(mappingToList(data.list))
    }

    private fun mappingToList(list: List<LikeDislikeList>): List<LikeDislikeListDomain> {
        return list.map { pojo ->
            LikeDislikeListDomain(
                    pojo.reviewId,
                    pojo.totalLike,
                    pojo.totalDislike,
                    pojo.likeStatus
            )
        }
    }
}