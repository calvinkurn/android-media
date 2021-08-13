package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUserMembershipUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<MembershipPojo>
) : UseCase<Result<MembershipPojo>>() {

    init {
        val query =
                """query getMembership(){
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

        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(MembershipPojo::class.java)
    }
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Result<MembershipPojo> {
        return try{
            graphqlUseCase.setRequestParams(params.parameters)
            val data = graphqlUseCase.executeOnBackground()
            return Success(data)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setStrategyCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }
}