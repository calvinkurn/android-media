package com.tokopedia.brandlist.brandlist_search.domain

import com.tokopedia.brandlist.brandlist_search.data.model.BrandlistSearchResponse
import com.tokopedia.brandlist.common.GQLQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class SearchBrandUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_BRANDLIST_SEARCH_BRAND) val query: String
): UseCase<BrandlistSearchResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): BrandlistSearchResponse {
        val gqlRequest = GraphqlRequest(query, BrandlistSearchResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        val error = graphqlResponse.getError(BrandlistSearchResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return graphqlResponse.run {
                getData<BrandlistSearchResponse>(BrandlistSearchResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
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

        fun createRequestParam(
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