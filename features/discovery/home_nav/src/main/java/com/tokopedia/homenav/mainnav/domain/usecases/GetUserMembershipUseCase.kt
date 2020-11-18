package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUserMembershipUseCase @Inject constructor(
        val graphqlUseCase: GraphqlRepository)
    : UseCase<Result<MembershipPojo>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<MembershipPojo> {
        return try {
            val gqlRequest = GraphqlRequest(query, MembershipPojo::class.java, params.parameters)
            val gqlResponse = graphqlUseCase.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD).build())

            val error = gqlResponse.getError(MembershipPojo::class.java)
            if (error == null || error.isEmpty()) {
                Success(gqlResponse.getData(MembershipPojo::class.java))
            } else {
                Fail(MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", ")))
            }
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        private val query = getQuery()
        private fun getQuery(): String {

            return """query getMembership(){
                  tokopoints {
                    status {
                      tier {
                        id
                        name
                        nameDesc
                        eggImageURL
                        eggImageHomepageURL
                      }
                    }
                  }
            } """.trimIndent()
        }
    }
}