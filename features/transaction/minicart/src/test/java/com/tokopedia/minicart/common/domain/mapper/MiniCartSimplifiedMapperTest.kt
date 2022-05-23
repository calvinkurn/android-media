package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.minicart.common.data.response.minicartlist.AvailableGroup
import com.tokopedia.minicart.common.data.response.minicartlist.AvailableSection
import com.tokopedia.minicart.common.data.response.minicartlist.BundleDetail
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.Data
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import org.junit.Assert.assertEquals
import org.junit.Test

class MiniCartSimplifiedMapperTest {

    private var mapper: MiniCartSimplifiedMapper = MiniCartSimplifiedMapper()

    @Test
    fun `WHEN map mini cart data without parent THEN should return exact same count`() {
        // GIVEN
        val data = MiniCartData(
                data = Data(
                        availableSection = AvailableSection(
                                availableGroup = listOf(
                                        AvailableGroup(
                                                cartDetails = listOf(
                                                        CartDetail(
                                                                products = listOf(
                                                                        Product(
                                                                                productId = "1"
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )

        // WHEN
        val miniCartSimplifiedData = mapper.mapMiniCartSimplifiedData(data)

        // THEN
        assertEquals(1, miniCartSimplifiedData.miniCartItems.size)
        assertEquals(mapOf(
                MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(productId = "1")
        ), miniCartSimplifiedData.miniCartItems)
    }

    @Test
    fun `WHEN map mini cart data with parent THEN should return all product and parent with total quantity`() {
        // GIVEN
        val data = MiniCartData(
                data = Data(
                        availableSection = AvailableSection(
                                availableGroup = listOf(
                                        AvailableGroup(
                                                cartDetails = listOf(
                                                        CartDetail(
                                                                products = listOf(
                                                                        Product(
                                                                                productId = "2",
                                                                                parentId = "1",
                                                                                productQuantity = 1
                                                                        ),
                                                                        Product(
                                                                                productId = "3",
                                                                                parentId = "1",
                                                                                productQuantity = 2
                                                                        ),
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )

        // WHEN
        val miniCartSimplifiedData = mapper.mapMiniCartSimplifiedData(data)

        // THEN
        assertEquals(3, miniCartSimplifiedData.miniCartItems.size)
        assertEquals(mapOf(
                MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", productParentId = "1", quantity = 1),
                MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", productParentId = "1", quantity = 2),
                MiniCartItemKey("1", type = MiniCartItemType.PARENT) to MiniCartItem.MiniCartItemParentProduct(parentId = "1", totalQuantity = 3, products = mapOf(
                        MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", productParentId = "1", quantity = 1),
                        MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", productParentId = "1", quantity = 2)
                )),
        ), miniCartSimplifiedData.miniCartItems)
    }

    @Test
    fun `WHEN map mini cart data with bundle THEN should return bundle`() {
        // GIVEN
        val data = MiniCartData(
                data = Data(
                        availableSection = AvailableSection(
                                availableGroup = listOf(
                                        AvailableGroup(
                                                cartDetails = listOf(
                                                        CartDetail(
                                                                bundleDetail = BundleDetail(
                                                                        bundleId = "123",
                                                                        bundleQty = 1
                                                                ),
                                                                products = listOf(
                                                                        Product(
                                                                                productId = "2",
                                                                                productQuantity = 1
                                                                        ),
                                                                        Product(
                                                                                productId = "3",
                                                                                productQuantity = 1
                                                                        ),
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )

        // WHEN
        val miniCartSimplifiedData = mapper.mapMiniCartSimplifiedData(data)

        // THEN
        assertEquals(1, miniCartSimplifiedData.miniCartItems.size)
        assertEquals(mapOf(
                MiniCartItemKey("123", type = MiniCartItemType.BUNDLE) to MiniCartItem.MiniCartItemBundle(bundleId = "123", bundleQuantity = 1, bundleLabelQuantity = 1, bundleMultiplier = 1, products = mapOf(
                        MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", quantity = 1),
                        MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", quantity = 1)
                )),
        ), miniCartSimplifiedData.miniCartItems)
    }
}