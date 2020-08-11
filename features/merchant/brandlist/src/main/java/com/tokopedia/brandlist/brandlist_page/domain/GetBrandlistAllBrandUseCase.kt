package com.tokopedia.brandlist.brandlist_page.domain

import com.tokopedia.brandlist.brandlist_page.data.model.BrandlistAllBrandResponse
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetBrandlistAllBrandUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<OfficialStoreAllBrands>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): OfficialStoreAllBrands {
        val gqlRequest = GraphqlRequest(QUERY_STRING, BrandlistAllBrandResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            var allBrands = OfficialStoreAllBrands()
            val response = getData<BrandlistAllBrandResponse>(BrandlistAllBrandResponse::class.java)
            if (response != null) allBrands = response.officialStoreAllBrands
            allBrands
        }
    }

    companion object {

        private const val CATEGORY_ID = "categoryId"
        private const val DEVICE = "device"
        private const val OFFSET = "offset"
        private const val QUERY = "query"
        private const val SIZE = "size"
        private const val SORT = "sort"
        private const val FIRST_LETTER = "firstLetter"
        private const val DEVICE_VALUE_ANDROID = 4 // Android
        private const val QUERY_STRING = "query OfficialStoreAllBrands(\$categoryId: Int!, \$device: Int!, \$offset: Int!, \$query: String!, \$size: Int!, \$sort: Int!, \$firstLetter: String!) {\n" +
                "  OfficialStoreAllBrands(categoryId: \$categoryId, device: \$device, offset: \$offset, query: \$query, size: \$size, sort: \$sort, firstLetter: \$firstLetter) {\n" +
                "    brands {\n" +
                "      id\n" +
                "      name\n" +
                "      appsUrl\n" +
                "      defaultUrl\n" +
                "      logoUrl\n" +
                "      isNew\n" +
                "      exclusiveLogoURL\n" +
                "    }\n" +
                "    totalBrands\n" +
                "  }\n" +
                "}"

        fun createParams(
                categoryId: Int,
                offset: Int,
                query: String,
                brandSize: Int,
                sortType: Int,
                firstLetter: String) = RequestParams.create().apply {
            putInt(CATEGORY_ID, categoryId)
            putInt(DEVICE, DEVICE_VALUE_ANDROID)
            putInt(OFFSET, offset)
            putString(QUERY, query)
            putInt(SIZE, brandSize)
            putInt(SORT, sortType)
            putString(FIRST_LETTER, firstLetter)
        }
    }
}