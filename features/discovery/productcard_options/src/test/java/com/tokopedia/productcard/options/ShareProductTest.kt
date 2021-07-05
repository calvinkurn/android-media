package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import org.junit.Test

internal class ShareProductTest: ProductCardOptionsViewModelTestFixtures() {

    @Test
    fun `Test share product`() {
        val productCardOptionsModelShare = ProductCardOptionsModel(
                hasShareProduct = true,
                productId = "12345",
                productName = "Product name",
                productImageUrl = "https://imageurl",
                productUrl = "https://producturl",
                formattedPrice = "Rp1.000.000",
                shop = ProductCardOptionsModel.Shop(shopId = "12345", shopName = "Shop Name", shopUrl = "https://shopurl")
        )

        createProductCardOptionsViewModel(productCardOptionsModelShare)

        productCardOptionsViewModel.getOption(SHARE_PRODUCT).onClick()

        val shareProductEvent = productCardOptionsViewModel.getShareProductEventLiveData().value
        val shareProductData = shareProductEvent?.getContentIfNotHandled()!!

        shareProductData.productId shouldBe productCardOptionsModelShare.productId
        shareProductData.productName shouldBe productCardOptionsModelShare.productName
        shareProductData.priceText shouldBe productCardOptionsModelShare.formattedPrice
        shareProductData.productUrl shouldBe productCardOptionsModelShare.productUrl
        shareProductData.shopName shouldBe productCardOptionsModelShare.shopName
        shareProductData.shopUrl shouldBe productCardOptionsModelShare.shopUrl
        shareProductData.productImageUrl shouldBe productCardOptionsModelShare.productImageUrl
    }
}