package com.tokopedia.loginregister.shopcreation.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.common.domain.query.MutationRegisterCheck
import com.tokopedia.loginregister.shopcreation.data.entity.RegisterCheckPojo
import com.tokopedia.loginregister.shopcreation.data.param.RegisterCheckParam
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
