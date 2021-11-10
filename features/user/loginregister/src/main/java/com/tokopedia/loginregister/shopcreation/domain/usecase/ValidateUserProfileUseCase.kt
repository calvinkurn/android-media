package com.tokopedia.loginregister.shopcreation.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.domain.param.ValidateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.UserProfileValidatePojo
import com.tokopedia.loginregister.shopcreation.domain.query.MutationUserProfileValidate
import javax.inject.Inject

class ValidateUserProfileUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ValidateUserProfileParam, UserProfileValidatePojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MutationUserProfileValidate.getQuery()
    }

    override suspend fun execute(params: ValidateUserProfileParam): UserProfileValidatePojo {
        return graphqlRepository.request(graphqlQuery(), params.toMap())
    }
}