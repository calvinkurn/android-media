package com.tokopedia.minicart.common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.data.response.minicartlist.*
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartItemType
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertEquals(
            mapOf(
                MiniCartItemKey("1") to MiniCartItem.MiniCartItemProduct(productId = "1")
            ),
            miniCartSimplifiedData.miniCartItems
        )
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
        assertEquals(3, miniCartSimplifiedData.miniCartItems.size)
        assertEquals(
            mapOf(
                MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", productParentId = "1", quantity = 1),
                MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", productParentId = "1", quantity = 2),
                MiniCartItemKey("1", type = MiniCartItemType.PARENT) to MiniCartItem.MiniCartItemParentProduct(
                    parentId = "1",
                    totalQuantity = 3,
                    products = mapOf(
                        MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", productParentId = "1", quantity = 1),
                        MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", productParentId = "1", quantity = 2)
                    )
                )
            ),
            miniCartSimplifiedData.miniCartItems
        )
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
                                        bundleGroupId = "234",
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
        assertEquals(
            mapOf(
                MiniCartItemKey("234", type = MiniCartItemType.BUNDLE) to MiniCartItem.MiniCartItemBundleGroup(
                    bundleId = "123",
                    bundleGroupId = "234",
                    bundleQuantity = 1,
                    bundleLabelQuantity = 1,
                    bundleMultiplier = 1,
                    products = mapOf(
                        MiniCartItemKey("2") to MiniCartItem.MiniCartItemProduct(productId = "2", quantity = 1),
                        MiniCartItemKey("3") to MiniCartItem.MiniCartItemProduct(productId = "3", quantity = 1)
                    )
                )
            ),
            miniCartSimplifiedData.miniCartItems
        )
    }

    @Test
    fun `WHEN map mini cart data with shopping summary THEN should return each shop data with correct separator position`() {
        // GIVEN
        val data = MiniCartData(
            data = Data(
                simplifiedShoppingSummary = SimplifiedShoppingSummary(
                    "Title",
                    sections = listOf(
                        Section(
                            title = "Shop 1",
                            description = "Desc 1",
                            details = listOf(
                                SectionDetail(name = "Item 1", value = "Rp 10000"),
                                SectionDetail(name = "Item 2", value = "Rp 25000")
                            )
                        ),
                        Section(
                            title = "Shop 2",
                            description = "Desc 2",
                            details = listOf(
                                SectionDetail(name = "Item 1", value = "Rp 30000"),
                                SectionDetail(name = "Item 2", value = "Rp 15000")
                            )
                        ),
                        Section(
                            title = "",
                            description = "",
                            details = listOf(
                                SectionDetail(name = "Total Harga (12 Barang)", value = "<b>Rp5.000.000</b>")
                            )
                        )
                    )
                )
            )
        )

        val separatorOneIdx = 3
        val separatorTwoIdx = 7

        // WHEN
        val miniCartSimplifiedData = mapper.mapMiniCartSimplifiedData(data)

        // THEN
        assertTrue(miniCartSimplifiedData.shoppingSummaryBottomSheetData.items[separatorOneIdx] is ShoppingSummarySeparatorUiModel)
        assertTrue(miniCartSimplifiedData.shoppingSummaryBottomSheetData.items[separatorTwoIdx] is ShoppingSummarySeparatorUiModel)
    }

    @Test
    fun `WHEN map mini cart data with empty title or empty details and not in last index THEN should not return the data`() {
        // GIVEN
        val data = MiniCartData(
            data = Data(
                simplifiedShoppingSummary = SimplifiedShoppingSummary(
                    "Title",
                    sections = listOf(
                        Section(
                            title = "Shop 1",
                            description = "Desc 1",
                            details = listOf(
                                SectionDetail(name = "Item 1", value = "Rp 10000"),
                                SectionDetail(name = "Item 2", value = "Rp 25000")
                            )
                        ),
                        Section(title = "Barang yang dilayani Tokopedia", description = "", details = emptyList()),
                        Section(
                            title = "",
                            description = "",
                            details = listOf(
                                SectionDetail(name = "Item 1", value = "Rp 10000"),
                                SectionDetail(name = "Item 2", value = "Rp 25000")
                            )
                        ),
                        Section(
                            title = "",
                            description = "",
                            details = listOf(
                                SectionDetail(name = "Total Harga (12 Barang)", value = "<b>Rp5.000.000</b>")
                            )
                        )
                    )
                )
            )
        )
        val separatorIdx = 3

        // WHEN
        val miniCartSimplifiedData = mapper.mapMiniCartSimplifiedData(data)

        // THEN
        assertEquals(
            listOf<Visitable<*>>(
                ShoppingSummaryHeaderUiModel("", "Shop 1", "Desc 1"),
                ShoppingSummaryProductUiModel("Item 1", "Rp 10000"),
                ShoppingSummaryProductUiModel("Item 2", "Rp 25000"),
                miniCartSimplifiedData.shoppingSummaryBottomSheetData.items[separatorIdx],
                ShoppingSummaryTotalTransactionUiModel("Total Harga (12 Barang)", "<b>Rp5.000.000</b>")
            ),
            miniCartSimplifiedData.shoppingSummaryBottomSheetData.items
        )
    }
}
