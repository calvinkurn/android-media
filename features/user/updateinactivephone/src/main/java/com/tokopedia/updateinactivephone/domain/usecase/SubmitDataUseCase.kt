package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import javax.inject.Inject

open class SubmitDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, InactivePhoneSubmitDataModel>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): InactivePhoneSubmitDataModel {
        return request(repository, params)
    }

    override fun graphqlQuery(): String {
        return query
    }

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_OLD_PHONE = "oldMsisdn"
        const val PARAM_NEW_PHONE = "newMsisdn"
        const val PARAM_USER_INDEX = "index"
        const val PARAM_ID_CARD_IAMEG = "fileKtp"
        const val PARAM_SELFIE_IMAGE = "fileSelfie"

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