package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.AffiliateUserDetailOnBoardingBottomSheetResponse
import javax.inject.Inject

@GqlQuery("AffiliateUserDetailQuery", AffiliateUserDetailOnBoardingBottomSheetUseCase.query)
class AffiliateUserDetailOnBoardingBottomSheetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<Unit, AffiliateUserDetailOnBoardingBottomSheetResponse>(dispatchers.io) {

    override suspend fun execute(params: Unit): AffiliateUserDetailOnBoardingBottomSheetResponse {
        return repository.request(AffiliateUserDetailQuery(), params)
    }

    override fun graphqlQuery(): String = query

    companion object {
        const val query = """
            query affiliateUserDetail(){
                affiliateUserDetail(){
                IsRegistered
                }
            }"""
    }
}
