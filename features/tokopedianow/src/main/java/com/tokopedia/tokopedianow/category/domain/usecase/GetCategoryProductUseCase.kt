package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.category.domain.query.AceSearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryProductUseCase @Inject constructor(
    private val aceSearchParamMapper: AceSearchParamMapper,
    graphqlRepository: GraphqlRepository
) {
    companion object {
        const val SOURCE_VALUE = "category_tokonow_directory"
        const val PAGE_VALUE = 1
        const val ROWS_VALUE = 7
    }

    private val graphql by lazy { GraphqlUseCase<AceSearchProductModel>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(AceSearchProduct)
            setTypeClass(AceSearchProductModel::class.java)
        }
    }

    suspend fun execute(categoryIdL2: String): AceSearchProductModel {
        return graphql.run {
            val queryParams = aceSearchParamMapper.createRequestParams(
                page = PAGE_VALUE,
                rows = ROWS_VALUE,
                srpPageId = categoryIdL2,
                source = SOURCE_VALUE
            )

            val requestParams = RequestParams.create().apply {
                putString(KEY_PARAMS, UrlParamUtils.generateUrlParamString(queryParams))
            }.parameters

            setRequestParams(requestParams)
            executeOnBackground()
        }
    }
}
