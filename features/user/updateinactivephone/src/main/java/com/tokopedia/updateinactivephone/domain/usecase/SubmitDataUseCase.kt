package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import javax.inject.Inject

class SubmitDataUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<InactivePhoneSubmitDataModel>
) {

    private lateinit var params: Map<String, Any>

    fun execute(onSuccess: (InactivePhoneSubmitDataModel) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.apply {
            setTypeClass(InactivePhoneSubmitDataModel::class.java)
            setGraphqlQuery(query)
            setRequestParams(params)
            execute(onSuccess = {
                onSuccess(it)
            }, onError = {
                onError(it)
            })
        }
    }

    fun setParam(email: String, oldPhone: String, newPhone: String, userIndex: Int, idCardObj: String, selfieObj: String) {
        params = mapOf(
                PARAM_EMAIL to email,
                PARAM_OLD_PHONE to oldPhone,
                PARAM_NEW_PHONE to newPhone,
                PARAM_USER_INDEX to userIndex,
                PARAM_ID_CARD_IAMEG to idCardObj,
                PARAM_SELFIE_IMAGE to selfieObj
        )
    }

    fun cancelJob() {
        graphqlUseCase.cancelJobs()
        graphqlUseCase.clearCache()
    }

    companion object {
        private const val PARAM_EMAIL = "email"
        private const val PARAM_OLD_PHONE = "oldMsisdn"
        private const val PARAM_NEW_PHONE = "newMsisdn"
        private const val PARAM_USER_INDEX = "index"
        private const val PARAM_ID_CARD_IAMEG = "fileKtp"
        private const val PARAM_SELFIE_IMAGE = "fileSelfie"

        private val query = """
            mutation submitInactivePhoneUser (${'$'}email: String!, ${'$'}oldMsisdn: String!, ${'$'}newMsisdn: String!, ${'$'}index: Int!, ${'$'}fileKtp: String!, ${'$'}fileSelfie: String!) {
              submitInactivePhoneUser(email: ${'$'}email, oldMsisdn: ${'$'}oldMsisdn, newMsisdn: ${'$'}newMsisdn, index: ${'$'}index, fileKtp: ${'$'}fileKtp, fileSelfie: ${'$'}fileSelfie){
                isSuccess
                errorMessage
              }
            }
        """.trimIndent()
    }
}