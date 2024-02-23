package com.tokopedia.loginregister.common.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckPojo
import javax.inject.Inject

class RegisterCheckUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RegisterCheckParam, RegisterCheckPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return """
        mutation register_check ($ID: String!){
            registerCheck(id: $ID) {
                isExist
                isPending
                status
                registerType
                userID
                view
                errors
                uh
                registerOvoEnable
            }
        }
    """.trimIndent()
    }

    override suspend fun execute(params: RegisterCheckParam): RegisterCheckPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    companion object {
        private const val ID = "\$id"
    }
}

data class RegisterCheckParam(
    @SerializedName("id")
    val id: String = ""
) : GqlParam
