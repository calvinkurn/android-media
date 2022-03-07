package com.tokopedia.loginregister.shopcreation.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.domain.param.UpdateUserProfileParam
import com.tokopedia.loginregister.shopcreation.domain.pojo.UserProfileUpdatePojo
import com.tokopedia.loginregister.shopcreation.domain.query.MutationUserProfileUpdate
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdateUserProfileParam, UserProfileUpdatePojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return MutationUserProfileUpdate.getQuery()
    }

    override suspend fun execute(params: UpdateUserProfileParam): UserProfileUpdatePojo {
        return graphqlRepository.request(graphqlQuery(), params.toMap())
    }
}