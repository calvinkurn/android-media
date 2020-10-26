package com.tokopedia.homenav.mainnav.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
        val graphqlUseCase: GraphqlRepository)
    : UseCase<UserPojo>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): UserPojo {
        val gqlRequest = GraphqlRequest(query, UserPojo::class.java, params.parameters)
        val gqlResponse = graphqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(UserPojo::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(UserPojo::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private val query = getQuery()
        private fun getQuery(): String {

            return """query getProfile(){
                  profile {
                    full_name
                    profilePicture
                  }
            } """.trimIndent()
        }
    }
}