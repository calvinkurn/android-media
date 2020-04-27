package com.tokopedia.brandlist.brandlist_category.domain

import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistCategoriesUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_CATEGORIES) val query: String
): UseCase<BrandlistCategories>() {
    override suspend fun executeOnBackground(): BrandlistCategories {
        val gqlRequest = GraphqlRequest(query, BrandlistCategories.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<BrandlistCategories.Response>(BrandlistCategories.Response::class.java)
                    .OfficialStoreCategories
        }
    }

    companion object { }
}