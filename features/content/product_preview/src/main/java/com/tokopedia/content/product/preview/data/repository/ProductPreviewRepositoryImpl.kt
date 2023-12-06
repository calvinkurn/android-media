package com.tokopedia.content.product.preview.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewRepositoryImpl @Inject constructor(private val dispatchers: CoroutineDispatchers) :
    ProductPreviewRepository {
    override suspend fun getProductMiniInfo(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getReview(productId: String, page: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun likeReview() {
        TODO("Not yet implemented")
    }

    override suspend fun submitReport(): Boolean {
        TODO("Not yet implemented")
    }
}
