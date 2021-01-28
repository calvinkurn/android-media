package com.tokopedia.sellerreview.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.usecase.BaseGqlUseCase
import com.tokopedia.sellerreview.data.remotemodel.ReviewResponseModel
import com.tokopedia.sellerreview.view.model.SendReviewParam
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 28/01/21
 */

class SendReviewUseCase(
        private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<Boolean>() {

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
        private const val USER_ID = "user_id"
        private const val RATING = "rating"
        private const val COMMENT = "comment"
        private const val APP_VERSION = "app_version"
        private const val DEVICE_MODEL = "device_model"
        private const val OS_TYPE = "os_type"
        private const val OS_VERSION = "os_version"

        private val QUERY = """
            mutation submitReviewSellerApp(${'$'}user_id: Int!, ${'$'}rating: Int!, ${'$'}comment: String, ${'$'}app_version: String, ${'$'}device_model: String, ${'$'}os_type: String, ${'$'}os_version: String) {
              chipSubmitReviewApp(user_id: ${'$'}user_id, rating: ${'$'}rating, comment: ${'$'}comment, app_version: ${'$'}app_version, device_model: ${'$'}device_model, os_type: ${'$'}os_type, os_version: ${'$'}os_version) {
                messageError
              }
            }
        """.trimIndent()

        fun createParams(param: SendReviewParam): RequestParams {
            return RequestParams.create().apply {
                putLong(USER_ID, param.userId)
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