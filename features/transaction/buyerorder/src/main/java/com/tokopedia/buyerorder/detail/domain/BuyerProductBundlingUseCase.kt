package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.ProductBundleItem
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class BuyerProductBundlingUseCase @Inject constructor(private val useCase: GraphqlUseCase<List<BuyerBundlingProductUiModel>>) {

    companion object {
        // TODO: Put actual query
        private val QUERY = ""
    }

    suspend fun execute(orderId: String): Result<List<BuyerBundlingProductUiModel>> {
        // TODO: Execute actual logic
        delay(2000)
        return Success(getDummyProductBundling())
    }

    private fun getDummyProductBundling(): List<BuyerBundlingProductUiModel> {
        return listOf(
                BuyerBundlingProductUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                ),
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                )
                        )
                ),
                BuyerBundlingProductUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                ),
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                )
                        )
                ),
                BuyerBundlingProductUiModel(
                        bundleName = "Tes Bundle",
                        productList = listOf(
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                ),
                                ProductBundleItem(
                                        productThumbnailUrl = "",
                                        productName = "Product coba2",
                                        priceText = "Rp100.000"
                                )
                        )
                )
        )
    }

}