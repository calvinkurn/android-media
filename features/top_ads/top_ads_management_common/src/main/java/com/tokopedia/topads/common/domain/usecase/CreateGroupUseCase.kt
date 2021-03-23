package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val CREATE_GROUPQUERY = """
  mutation topadsCreateGroupAds(${'$'}input: TopadsCreateGroupAdsInput!){
  topadsCreateGroupAds(input:${'$'}input){
    errors {
      code
      detail
      title
    }
    meta{
      messages {
        code
        detail
        title
      }
    }
  }
}
"""

@GqlQuery("QUERY_CREATE", CREATE_GROUPQUERY)
class CreateGroupUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                             val userSession: UserSessionInterface)
    : GraphqlUseCase<ResponseCreateGroup>(graphqlRepository) {

    fun executeQuerySafeMode(onSuccess: (ResponseCreateGroup.TopadsCreateGroupAds) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ResponseCreateGroup::class.java)
        setGraphqlQuery(QUERY_CREATE.GQL_QUERY)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        execute({
            onSuccess(it.topadsCreateGroupAds)

        }, onError)
    }

    fun setParams(param: HashMap<String, Any>) {
        setRequestParams(param)
    }
}
