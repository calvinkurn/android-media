package com.tokopedia.tkpd.tkpdreputation.inbox.data.source

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.tkpd.tkpdreputation.data.pojo.DeleteReviewResponsePojo
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.network.ErrorMessageException
import com.tokopedia.tkpd.tkpdreputation.network.ReputationService
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface

class CloudDeleteReviewResponseDataSource(
        private val reputationService: ReputationService,
        private val userSession: UserSessionInterface
) {
    suspend fun deleteReviewResponse(params: RequestParams): DeleteReviewResponseDomain {
        val response = reputationService.api.deleteReviewResponse(
                AuthHelper.generateParamsNetwork(
                        userSession.userId,
                        userSession.deviceId,
                        params.paramsAllValueInString
                )
        )
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                if ((!responseBody.isNullData && responseBody.errorMessageJoined.isEmpty()) || !responseBody.isNullData && responseBody.errorMessages == null) {
                    val data = responseBody.convertDataObj(DeleteReviewResponsePojo::class.java)
                    return mappingToDomain(data)
                } else {
                    if (responseBody.errorMessages != null && responseBody.errorMessages.isNotEmpty()) {
                        throw ErrorMessageException(responseBody.errorMessageJoined)
                    } else throw ErrorMessageException("")
                }
            } ?: throw ErrorMessageException("")
        } else {
            val messageError = response.body()?.let { it.errorMessageJoined }
            if (!TextUtils.isEmpty(messageError)) {
                throw ErrorMessageException(messageError)
            } else throw RuntimeException(response.code().toString())
        }
    }

    private fun mappingToDomain(data: DeleteReviewResponsePojo): DeleteReviewResponseDomain {
        return DeleteReviewResponseDomain(data.isSuccess)
    }
}
