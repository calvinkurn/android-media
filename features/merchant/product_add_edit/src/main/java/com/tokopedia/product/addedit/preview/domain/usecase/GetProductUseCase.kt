package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.data.source.api.param.GetProductV3Param
import com.tokopedia.product.addedit.preview.data.source.api.param.OptionV3
import com.tokopedia.product.addedit.preview.data.source.api.response.GetProductV3Response
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<Product>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Product {

        val graphqlRequest = GraphqlRequest(getQuery(), GetProductV3Response::class.java, params.parameters)
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors: List<GraphqlError>? = graphqlResponse.getError(GetProductV3Response::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<GetProductV3Response>(GetProductV3Response::class.java)
            return data.product
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"

        fun createRequestParams(productId: String): RequestParams {
            val options = OptionV3()
            val getProductV3Param = GetProductV3Param(productId, options)
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_PRODUCT_ID, getProductV3Param.productID)
            requestParams.putObject(PARAM_OPTIONS, getProductV3Param.options)
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
                    menus
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
                        variantID
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
                      sizecharts{
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
                    }
                  }
                }
            """.trimIndent()
    }
}