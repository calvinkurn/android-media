package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import javax.inject.Inject

open class SubmitDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SubmitDataModel, InactivePhoneSubmitDataModel>(dispatcher.io) {

    override suspend fun execute(params: SubmitDataModel): InactivePhoneSubmitDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return query
    }

    private fun createParams(params: SubmitDataModel): Map<String, Any> = mapOf(
        PARAM_EMAIL to params.email,
        PARAM_OLD_PHONE to params.oldPhone,
        PARAM_NEW_PHONE to params.newPhone,
        PARAM_USER_INDEX to params.userIndex,
        PARAM_ID_CARD_IMAGE to params.idCardImage,
        PARAM_SELFIE_IMAGE to params.selfieImage
    )

    companion object {
        private const val PARAM_EMAIL = "email"
        private const val PARAM_OLD_PHONE = "oldMsisdn"
        private const val PARAM_NEW_PHONE = "newMsisdn"
        private const val PARAM_USER_INDEX = "index"
        private const val PARAM_ID_CARD_IMAGE = "fileKtp"
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

data class SubmitDataModel(
    var email: String = "",
    var oldPhone: String = "",
    var newPhone: String = "",
    var userIndex: Int = 0,
    var idCardImage: String = "",
    var selfieImage: String = ""
)