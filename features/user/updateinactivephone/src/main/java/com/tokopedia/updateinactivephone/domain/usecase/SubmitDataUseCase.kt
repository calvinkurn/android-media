package com.tokopedia.updateinactivephone.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import javax.inject.Inject

open class SubmitDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SubmitDataModel, InactivePhoneSubmitDataModel>(dispatcher.io) {

    override suspend fun execute(params: SubmitDataModel): InactivePhoneSubmitDataModel {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String {
        return """
            mutation submitInactivePhoneUser (${'$'}email: String!, ${'$'}oldMsisdn: String!, ${'$'}newMsisdn: String!, ${'$'}index: Int!, ${'$'}fileKtp: String!, ${'$'}fileSelfie: String!, ${'$'}validateToken: String!) {
              submitInactivePhoneUser(email: ${'$'}email, oldMsisdn: ${'$'}oldMsisdn, newMsisdn: ${'$'}newMsisdn, index: ${'$'}index, fileKtp: ${'$'}fileKtp, fileSelfie: ${'$'}fileSelfie, validateToken: ${'$'}validateToken){
                isSuccess
                errorMessage
              }
            }
        """.trimIndent()
    }
}

data class SubmitDataModel(
    @SerializedName("email")
    var email: String = "",
    @SerializedName("oldMsisdn")
    var oldPhone: String = "",
    @SerializedName("newMsisdn")
    var newPhone: String = "",
    @SerializedName("index")
    var userIndex: Int = 0,
    @SerializedName("fileKtp")
    var idCardImage: String = "",
    @SerializedName("fileSelfie")
    var selfieImage: String = "",
    @SerializedName("validateToken")
    var validateToken: String = ""
): GqlParam