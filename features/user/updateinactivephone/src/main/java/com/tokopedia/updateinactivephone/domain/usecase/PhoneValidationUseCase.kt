package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import javax.inject.Inject

class PhoneValidationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<PhoneValidationDataModel>
) {
    private lateinit var params: Map<String, Any>

    fun execute(onSuccess: (PhoneValidationDataModel) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.apply {
            setTypeClass(PhoneValidationDataModel::class.java)
            setGraphqlQuery(query)
            setRequestParams(params)
            execute(onSuccess = {
                onSuccess(it)
            }, onError = {
                onError(it)
            })
        }
    }

    fun setParam(phone: String, email: String, index: Int = 0) {
        params = mapOf(
                PARAM_PHONE to phone,
                PARAM_EMAIL to email,
                PARAM_INDEX to index
        )
    }

    fun cancelJob() {
        graphqlUseCase.cancelJobs()
        graphqlUseCase.clearCache()
    }

    companion object {
        private const val PARAM_PHONE = "phone"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_INDEX = "index"

        private val query = """
            query validateInactivePhoneUser(${'$'}phone: String, ${'$'}email: String, ${'$'}index: Int) {
                ValidateInactivePhoneUser(msisdn: ${'$'}phone, email: ${'$'}email, index: ${'$'}index) {
                    isSuccess
                    errorMessage
                    status
                }
            }
        """.trimIndent()
    }
}