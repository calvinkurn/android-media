package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.searchcategory.domain.model.QuickFilterModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetSortFilterUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<QuickFilterModel>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(FilterSortProduct())
            setTypeClass(QuickFilterModel::class.java)
        }
    }

    suspend fun execute(params: Map<String?, Any?>?): DynamicFilterModel {
        graphql.setRequestParams(
            RequestParams.create().apply {
                putString(KEY_PARAMS, UrlParamUtils.generateUrlParamString(params))
            }.parameters
        )

        return graphql.executeOnBackground().filterSortProduct
    }
}
