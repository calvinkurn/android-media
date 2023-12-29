package com.tokopedia.loginregister.shopcreation.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.domain.query.MutationRegisterCheck
import com.tokopedia.loginregister.shopcreation.data.RegisterCheckPojo
import javax.inject.Inject

class RegisterCheckUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RegisterCheckParam, RegisterCheckPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MutationRegisterCheck.getQuery()
    }

    override suspend fun execute(params: RegisterCheckParam): RegisterCheckPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}

data class RegisterCheckParam(
    @SerializedName("id")
    val id: String = ""
) : GqlParam
