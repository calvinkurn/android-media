package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.ProductBundle
import com.tokopedia.buyerorder.detail.data.ProductBundleItem
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import javax.inject.Inject

class BuyerProductBundlingUseCase @Inject constructor(private val useCase: GraphqlUseCase<List<ProductBundle>>) {

    companion object {
        // TODO: Put actual query
        private val QUERY = ""
    }

    suspend fun execute(orderId: String): Result<List<ProductBundle>> {
        // TODO: Execute actual logic
        delay(2000)
        return Success(getDummyProductBundling())
    }

    private fun getDummyProductBundling(): List<ProductBundle> {
        return listOf(
                ProductBundle(
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