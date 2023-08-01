package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.category.domain.query.AceSearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryProductUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<AceSearchProductModel>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(AceSearchProduct)
            setTypeClass(AceSearchProductModel::class.java)
        }
    }

    suspend fun execute(queryParams: Map<String?, Any?>): AceSearchProductModel {
        return graphql.run {
            val requestParams = RequestParams.create().apply {
                putString(KEY_PARAMS, UrlParamUtils.generateUrlParamString(queryParams))
            }.parameters

            setRequestParams(requestParams)
            executeOnBackground()
        }
    }
}
