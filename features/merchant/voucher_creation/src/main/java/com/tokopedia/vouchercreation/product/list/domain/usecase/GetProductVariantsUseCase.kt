package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.response.GetProductV3Response
import javax.inject.Inject

class GetProductVariantsUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<GetProductV3Response>(repository) {

    companion object {

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"
        private const val PARAM_VARIANT = "variant"

        @JvmStatic
        fun createRequestParams(productId: String): RequestParams {
            val optionsParam = RequestParams().apply {
                putBoolean(PARAM_VARIANT, true)
            }.parameters
            return RequestParams().apply {
                putString(PARAM_PRODUCT_ID, productId)
                putObject(PARAM_OPTIONS, optionsParam)
            }
        }
    }

    private val query = """
        query getProductV3(${'$'}productID:String!, ${'$'}options:OptionV3!) {
          getProductV3(productID:${'$'}productID, options:${'$'}options) {
            variant{
              products{
                productID
                status
                combination
                isPrimary
                isCampaign
                price
                sku
                stock
                pictures {
                  picID
                  description
                  filePath
                  fileName
                  width
                  height
                  isFromIG
                  urlOriginal
                  urlThumbnail
                  url300
                  status
                }
              }
              selections {
                  variantID
                  variantName
                  unitName
                  unitID
                  unitName
                  identifier
                  options {
                    unitValueID
                    value
                    hexCode
                  }
              }
            }
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetProductV3Response::class.java)
    }
}