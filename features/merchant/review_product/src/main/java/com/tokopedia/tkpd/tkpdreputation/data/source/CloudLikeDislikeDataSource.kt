package com.tokopedia.tkpd.tkpdreputation.data.source

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike.LikeDislikePojo
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface

class CloudLikeDislikeDataSource(
        private val reputationService: ReputationService,
        private val userSession: UserSessionInterface
) {

    suspend fun getLikeDislikeReview(params: RequestParams): LikeDislikeDomain {
        return reputationService.api.likeDislikeReview(AuthHelper.generateParamsNetwork(
                userSession.userId,
                userSession.deviceId,
                params.paramsAllValueInString
        )).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    if ((!responseBody.isNullData && responseBody.errorMessageJoined.isEmpty()) || !responseBody.isNullData && responseBody.errorMessages == null) {
                        val data = responseBody.convertDataObj(LikeDislikePojo::class.java)
                        return mappingToDomain(data)
                    } else {
                        if (responseBody.errorMessages != null && !responseBody.errorMessages.isEmpty()) {
                            throw ErrorMessageException(responseBody.errorMessageJoined)
                        } else throw ErrorMessageException("")
                    }
                } ?: throw ErrorMessageException("")
            } else {
                val messageError = response.body()?.let { it.errorMessageJoined } ?: ""
                if (!TextUtils.isEmpty((messageError))) {
                    throw ErrorMessageException(messageError)
                } else throw RuntimeException(response.code().toString())
            }
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