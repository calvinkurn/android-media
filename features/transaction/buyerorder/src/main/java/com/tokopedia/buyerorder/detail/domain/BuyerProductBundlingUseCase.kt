package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.productbundling.BuyerProductBundlingParam
import com.tokopedia.buyerorder.detail.data.productbundling.BuyerProductBundlingResponse
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerProductBundlingUiModel
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.ProductBundleItem
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class BuyerProductBundlingUseCase @Inject constructor(private val useCase: GraphqlUseCase<BuyerProductBundlingResponse>) {

    companion object {
        private const val PARAM_INPUT = "input"
        private val QUERY = """
            query GetCancellationProductBundling(${'$'}input: BomDetailV2Request!) {
              mp_bom_detail(input: ${'$'}input) {
                bundle_detail {
                  bundle {
                    bundle_name
                    order_detail {
                      product_id
                      product_name
                      product_price
                      thumbnail
                    }
                  }
                }
        """.trimIndent()
    }

    init {
        useCase.setTypeClass(BuyerProductBundlingResponse::class.java)
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(orderId: String): List<BuyerProductBundlingUiModel> {
        useCase.setRequestParams(createRequestParam(orderId))
        // TODO: Use actual data
//        return useCase.executeOnBackground().bundleDetail.bundleList
        delay(2000)
        return getDummyProductBundling()
    }

    private fun createRequestParam(orderId: String): Map<String, Any> {
        val params = BuyerProductBundlingParam(orderId = orderId)
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    private fun getDummyProductBundling(): List<BuyerProductBundlingUiModel> {
        return listOf(
                BuyerProductBundlingUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productId = 100,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                ),
                                ProductBundleItem(
                                        productId = 100,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                )
                        )
                ),
                BuyerProductBundlingUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productId = 120,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                ),
                                ProductBundleItem(
                                        productId = 121,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                )
                        )
                ),
                BuyerProductBundlingUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productId = 150,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                ),
                                ProductBundleItem(
                                        productId = 151,
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        productPrice = 100000.00
                                )
                        )
                )
        )
    }

}