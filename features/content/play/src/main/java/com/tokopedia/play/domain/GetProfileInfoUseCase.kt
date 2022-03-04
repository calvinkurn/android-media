package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ProfileHeader
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 02/03/22
 */
@GqlQuery(GetProfileInfoUseCase.QUERY_NAME, GetProfileInfoUseCase.QUERY)
class GetProfileInfoUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProfileHeader.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(GetProfileInfoUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProfileHeader.Response::class.java)
    }

    fun createParam(userId: String): Map<String, Any> = mapOf(USER_ID to userId)

    companion object {
        const val USER_ID = "username"
        const val QUERY_NAME = "GetProfileInfoUseCaseQuery"
        const val QUERY = """
            query getProfileHeader(${'$'}username: String!){
                feedXProfileHeader(username: ${'$'}username){
                    profile{
                        encryptedUserID
                        username
                        nickname
                    }
                }
            }
        """
    }
}