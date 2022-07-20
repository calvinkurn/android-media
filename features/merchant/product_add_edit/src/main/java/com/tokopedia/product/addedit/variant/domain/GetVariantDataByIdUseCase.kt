package com.tokopedia.product.addedit.variant.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.variant.data.model.GetVariantDataByIdResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetVariantDataByIdUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<GetVariantDataByIdResponse>(repository) {

    companion object {
        private const val PARAM_VARIANT_ID = "variantId"
        private const val query = """
            query GetVariantDataById(${'$'}variantId: Int!){
              getVariantDataByID(variantID: ${'$'}variantId) {
                data {
                  VariantID
                  Identifier
                  Name
                  Units {
                    VariantUnitID
                    Status
                    UnitName
                    UnitShortName
                    UnitValues {
                      VariantUnitValueID
                      Status
                      Value
                      EquivalentValueID
                      EnglishValue
                      Hex
                      Icon
                    }
                  }
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(query.trimIndent())
        setTypeClass(GetVariantDataByIdResponse::class.java)
    }

    fun setParams(variantId: Int) {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_VARIANT_ID, variantId)
        setRequestParams(requestParams.parameters)
    }
}