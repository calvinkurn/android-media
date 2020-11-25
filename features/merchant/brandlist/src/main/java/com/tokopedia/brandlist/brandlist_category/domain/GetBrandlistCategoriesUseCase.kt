package com.tokopedia.brandlist.brandlist_category.domain

import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistCategoriesUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<BrandlistCategories>() {
    override suspend fun executeOnBackground(): BrandlistCategories {
        val gqlRequest = GraphqlRequest(QUERY, BrandlistCategories.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<BrandlistCategories.Response>(BrandlistCategories.Response::class.java)
                    .OfficialStoreCategories
        }
    }

    companion object {
        private const val QUERY = "query OfficialStoreCategories{\n" +
                "  OfficialStoreCategories(lang: \"id\") {\n" +
                "    categories {\n" +
                "        id\n" +
                "        imageUrl\n" +
                "        name\n" +
                "        prefixUrl\n" +
                "        url\n" +
                "        fullUrl\n" +
                "        categories\n" +
                "        imageInactiveURL\n" +
                "    }\n" +
                "    total\n" +
                "  }\n" +
                "}"
    }
}