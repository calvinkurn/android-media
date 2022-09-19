package com.tokopedia.loginregister.shopcreation.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.domain.param.RegisterCheckParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.common.domain.query.MutationRegisterCheck
import javax.inject.Inject

class RegisterCheckUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RegisterCheckParam, RegisterCheckPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MutationRegisterCheck.getQuery()
    }

    override suspend fun execute(params: RegisterCheckParam): RegisterCheckPojo {
        return graphqlRepository.request(graphqlQuery(), params.toMap())
    }
}