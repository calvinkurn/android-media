package com.tokopedia.tokomart.searchcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomart.searchcategory.domain.model.FilterModel
import com.tokopedia.usecase.coroutines.UseCase

class GetFilterUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        val params = useCaseRequestParams.parameters

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        GQL_QUERY,
                        FilterModel::class.java,
                        mapOf(KEY_PARAMS to UrlParamUtils.generateUrlParamString(params))
                )
        )

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return graphqlResponse.getData<FilterModel?>(FilterModel::class.java)
                ?.filterSortProduct ?: DynamicFilterModel()
    }

    companion object {
        private const val GQL_QUERY = """
            query FilterSortProduct(${'$'}params: String!) {
                filter_sort_product(params: ${'$'}params) {
                      data {
                        filter {
                          title
                          search {
                            searchable
                            placeholder
                          }
                          template_name
                          options {
                            name
                            key
                            icon
                            Description
                            value
                            inputType
                            totalData
                            valMax
                            valMin
                            isPopular
                            isNew
                            hexColor
                            child {
                              key
                              value
                              name
                              icon
                              inputType
                              totalData
                              isPopular
                              child {
                                key
                                value
                                name
                                icon
                                inputType
                                totalData
                                isPopular
                              }
                            }
                          }
                        }
                        sort {
                          name
                          key
                          value
                          inputType
                          applyFilter
                        }
                     }
               }
            }"""
    }
}