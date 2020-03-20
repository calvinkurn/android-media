package com.tokopedia.product.addedit.common.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.domain.model.params.edit.ProductEditPriceParam
import com.tokopedia.product.addedit.common.domain.model.responses.ProductAddEditV3Response
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ProductAddUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) :
        UseCase<ProductAddEditV3Response>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductAddEditV3Response {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = params.getObject(PARAM_INPUT)
        val gqlRequest = GraphqlRequest(getQuery(), ProductAddEditV3Response::class.java)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(ProductAddEditV3Response::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ProductAddEditV3Response::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val PARAM_INPUT = "input"
        @JvmStatic
        fun createRequestParams(param: ProductEditPriceParam): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_INPUT, param)
            return requestParams
        }

        fun getQuery() = """
                    mutation { ProductAddV3(input:{
                      productName: "Baju polos yang terbaik di tokpedo",
                      price: 20000,
                      priceCurrency : "IDR",
                      stock : 999999,
                      status : "LIMITED",
                      description : "Ini adalah baju polo terbaik ....",
                      minOrder : 1,
                      weightUnit : "GR",
                      weight : 100,
                      condition : "NEW",
                      mustInsurance: false,
                      sku : "SKU",
                      catalog : {
                        catalogID : "1"
                      },
                      category : {
                        id : "1"
                      },
                      menu : {
                        menuID : "0",
                        name : ""
                      },
                      picture : {
                        data : 
                        [
                          {
                            picID : "111",
                            description : "baju polos terbaik",
                            filePath : "folder/product-1",
                            fileName : "bajupolos.jpg", 
                            width : 1000,
                            height : 1500
                          }]
                      },
                      preorder : {
                        duration : 1,
                        timeUnit : "1",
                        isActive : true
                      }
                      
                    }) {
                      header{
                        messages
                        reason
                        errorCode
                      }
                      isSuccess
                    }}
                    """.trimIndent()

    }

}