package com.tokopedia.product.addedit.preview.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.product.addedit.preview.data.source.api.param.GetProductV3Param
import com.tokopedia.product.addedit.preview.data.source.api.response.GetProductV3Response
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<Product>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Product {

        val graphqlRequest = GraphqlRequest(getQuery(), GetProductV3Response::class.java, params.parameters)
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        return graphqlResponse.run {
            var product = Product()
            val response = getData<GetProductV3Response>(GetProductV3Response::class.java)
            if (response != null) product = response.product
            product
        }
    }

    companion object {

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"

        fun createRequestParams(param: GetProductV3Param): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_PRODUCT_ID, param.productID)
            requestParams.putObject(PARAM_OPTIONS, param.options)
            return requestParams
        }

        private fun getQuery() = """
            query getProductV3(${'$'}productID: String!, ${'$'}options: OptionV3!) {
                getProductV3(productID: ${'$'}productID, options: ${'$'}options ) {
                    productID
                    productName
                   	status
                    stock
                   	priceCurrency
                    price
                    lastUpdatePrice
                    minOrder
                    maxOrder
                    description
                    weightUnit
                    weight
                    condition
                    mustInsurance
                    isKreasiLokal
                    mustInsurance
                    alias
                    sku
                    gtin
                    url
                    brand{
                      brandID
                      name
                      brandStatus
                      isActive
                    }
                    catalog{
                      catalogID
                      name
                      url
                    }
                    category{
                      id
                      name
                      title
                      isAdult
                      breadcrumbURL
                      detail{
                        id
                        name
                        breadcrumbURL
                        isAdult
                      }
                    }
                    menu{
                      menuID
                      name
                      url
                      alias
                      productCount
                    }
                    pictures{
                      picID
                      description
                      filePath
                      fileName
                      width
                      height
                      urlOriginal
                      urlThumbnail
                      url300
                      status
                    }
                    position{
                      position
                      isSwap
                    }
                    preorder{
                      duration
                      timeUnit
                      isActive
                    }
                    shop {
                      id
                    }
                    wholesale{
                      minQty
                      price
                    }
                    campaign{
                      campaignID
                      campaignType
                      campaignTypeName
                      percentageAmount
                      originalPrice
                      discountedPrice
                      originalStock
                      isActive
                    }
                    video{
                      source
                      url
                    }
                    cashback {
                      percentage
                    }
                    lock{
                      full
                      partial{
                        price
                        status
                        stock
                        wholesale
                        name
                      }
                    }
                    stats{
                      countView
                      countReview
                      countTalk
                      rating
                    }
                    txStats{
                      itemSold
                      txSuccess
                      txReject
                    }
                    variant{
                      products{
                        productID
                        status
                        combination
                        isPrimary
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
                      selections{
                        variantName
                        unitName
                        unitID
                        unitName
                        identifier
                        options{
                          unitValueID
                          value
                          hexCode
                        }
                      }
                    }
                  }
                }
            """.trimIndent()
    }
}