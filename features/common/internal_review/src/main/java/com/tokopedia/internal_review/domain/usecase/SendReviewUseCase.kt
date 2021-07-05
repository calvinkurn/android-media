package com.tokopedia.internal_review.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.internal_review.data.remotemodel.ReviewResponseModel
import com.tokopedia.internal_review.view.model.SendReviewParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 28/01/21
 */

class SendReviewUseCase constructor(
        private val gqlRepository: GraphqlRepository
) : UseCase<Boolean>() {

    var params: RequestParams = RequestParams.EMPTY

    inline fun <reified T> GraphqlResponse.getData(): T {
        return this.getData(T::class.java)
    }

    override suspend fun executeOnBackground(): Boolean {
        val gqlRequest = GraphqlRequest(QUERY, ReviewResponseModel::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(ReviewResponseModel::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ReviewResponseModel>()
            if (data.chipSubmitReviewApp.messageError.isNullOrEmpty()) {
                return true
            } else {
                val errorMessages: List<String> = data.chipSubmitReviewApp.messageError
                throw MessageErrorException(errorMessages.joinToString(", ") { it })
            }
        } else throw MessageErrorException(errors.joinToString(", ") { it.message })
    }

    companion object {
        private const val USER_ID = "userID"
        private const val RATING = "rating"
        private const val COMMENT = "comment"
        private const val APP_VERSION = "appVersion"
        private const val DEVICE_MODEL = "deviceModel"
        private const val OS_TYPE = "osType"
        private const val OS_VERSION = "osVersion"

        private val QUERY = """
            mutation submitReviewSellerApp(${'$'}userID: String!, ${'$'}rating: Int!, ${'$'}comment: String, ${'$'}appVersion: String, ${'$'}deviceModel: String, ${'$'}osType: String, ${'$'}osVersion: String) {
              chipSubmitReviewApp(userID: ${'$'}userID, rating: ${'$'}rating, comment: ${'$'}comment, appVersion: ${'$'}appVersion, deviceModel: ${'$'}deviceModel, osType: ${'$'}osType, osVersion: ${'$'}osVersion) {
                messageError
              }
            }
        """.trimIndent()

        fun createParams(param: SendReviewParam): RequestParams {
            return RequestParams.create().apply {
                putString(USER_ID, param.userId)
                putInt(RATING, param.rating)
                putString(COMMENT, param.feedback)
                putString(APP_VERSION, param.appVersion)
                putString(DEVICE_MODEL, param.deviceModel)
                putString(OS_TYPE, param.osType)
                putString(OS_VERSION, param.osVersion)
            }
        }
    }
}