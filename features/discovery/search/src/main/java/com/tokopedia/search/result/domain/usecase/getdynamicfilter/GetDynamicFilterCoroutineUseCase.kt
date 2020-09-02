package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.data.response.GqlDynamicFilterResponse
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase

class GetDynamicFilterCoroutineUseCase (
        private val graphqlUseCase: GraphqlUseCase<GqlDynamicFilterResponse>
): UseCase<DynamicFilterModel>() {

    override suspend fun executeOnBackground(): DynamicFilterModel {
        graphqlUseCase.setTypeClass(GqlDynamicFilterResponse::class.java)
        graphqlUseCase.setGraphqlQuery(GQL_QUERY)
        graphqlUseCase.setRequestParams(createRequestParams())

        return graphqlUseCase.executeOnBackground().dynamicFilterModel
    }

    private fun createRequestParams() =
            mapOf(KEY_PARAMS to UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters))

    companion object {
        const val GQL_QUERY = """
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
                        }
                    }
                }
        """
    }
}