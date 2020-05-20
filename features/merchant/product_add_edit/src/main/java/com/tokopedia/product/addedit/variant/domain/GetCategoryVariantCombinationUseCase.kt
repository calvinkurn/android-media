package com.tokopedia.product.addedit.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryVariantCombinationUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<GetCategoryVariantCombinationResponse>(repository) {

    companion object {
        private const val PARAM_CATEGORY_ID = "categoryID"
        private val query =
            """
            query GetCategoryVariantCombination(${'$'}categoryID: String!) {
              GetCategoryVariantCombination(categoryID: ${'$'}categoryID) {
                header{
                  reason,
                  messages
                }
                data{
                  departmentID,
                  variantIDCombinations,
                  variantDetails{
                    variantID,
                    identifier,
                    name,
                    status,
                    units{
                      variantUnitID,
                      status,
                      unitName,
                      unitShortName,
                      unitValues{
                        variantUnitValueID,
                        status,
                        value,
                        hex,
                        icon
                      }
                    }
                  }
                }
              }
            }
            """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetCategoryVariantCombinationResponse::class.java)
    }

    fun setParams(categoryId: String) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_CATEGORY_ID, categoryId)
        setRequestParams(requestParams.parameters)
    }
}