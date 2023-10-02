package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.searchcategory.domain.model.FilterModel
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
) {

    private val graphql by lazy { GraphqlUseCase<FilterModel>(graphqlRepository) }

    suspend fun execute(
        params: Map<String, Any?>
    ): DynamicFilterModel {
        graphql.apply {
            setGraphqlQuery(FilterSortProduct())
            setTypeClass(FilterModel::class.java)
            setRequestParams(mapOf(SearchConstant.GQL.KEY_PARAMS to UrlParamUtils.generateUrlParamString(params.toMap())))
        }
        return graphql.executeOnBackground().filterSortProduct
    }
}
