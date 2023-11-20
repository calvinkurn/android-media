package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import io.mockk.MockKAnnotations
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by @ilhamsuaib on 18/09/23.
 */
class BmgmMiniCartDataMapperTest : BaseMiniCartDataMapper() {

    private val bmgmMiniCartDataMapper by lazy {
        BmgmMiniCartDataMapper()
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given mock response with bmgm should return expected bmgm ui model data`() {
        val mockResponse = getMockMiniCartResponseWithBmgmAndWholesalePrice()
        val expected = BmgmMiniCartDataUiModel(
            offerId = 910,
            offerMessage = listOf(
                "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
            ),
            hasReachMaxDiscount = true,
            priceBeforeBenefit = 250000.0,
            finalPrice = 250000.0,
            tiersApplied = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = 0,
                    tierMessage = "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                    tierDiscountStr = "",
                    priceBeforeBenefit = 250000.0,
                    priceAfterBenefit = 250000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "2150835655",
                            warehouseId = "345977",
                            productName = "Jarum_Suntik",
                            productImage = "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg",
                            cartId = "2166690162",
                            finalPrice = 35000.0,
                            quantity = 2
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "2150835655",
                            warehouseId = "345977",
                            productName = "Jarum_Suntik",
                            productImage = "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg",
                            cartId = "2166690162",
                            finalPrice = 35000.0,
                            quantity = 2
                        )
                    )
                )
            )
        )

        val actual = bmgmMiniCartDataMapper.mapToUiModel(mockResponse.miniCart)

        val expectedStr = GsonSingleton.instance.toJson(expected)
        val actualStr = GsonSingleton.instance.toJson(actual)
        Assert.assertEquals(expectedStr, actualStr)
    }

    @Test
    fun `given mock response with no bmgm should return empty bmgm ui model data`() {
        val mockResponse = getMockMiniCartResponseWithNoBmgmWithOfferId()
        val expected = BmgmMiniCartDataUiModel()

        val actual = bmgmMiniCartDataMapper.mapToUiModel(mockResponse.miniCart)

        val expectedStr = GsonSingleton.instance.toJson(expected)
        val actualStr = GsonSingleton.instance.toJson(actual)
        Assert.assertEquals(expectedStr, actualStr)
    }

    @Test
    fun `given mock response with bmgm but 0 offer ID should return empty bmgm ui model data`() {
        val mockResponse = getMockMiniCartResponseWithBmgmAndZeroOfferId()
        val expected = BmgmMiniCartDataUiModel()

        val actual = bmgmMiniCartDataMapper.mapToUiModel(mockResponse.miniCart)

        val expectedStr = GsonSingleton.instance.toJson(expected)
        val actualStr = GsonSingleton.instance.toJson(actual)
        Assert.assertEquals(expectedStr, actualStr)
    }

    @Test
    fun `given mock response with bmgm and no wholesale price should return bmgm ui model data with regular product price`() {
        val mockResponse = getMockMiniCartResponseWithBmgmButNotEligibleWholesalePrice()
        val expected = BmgmMiniCartDataUiModel(
            offerId = 910,
            offerMessage = listOf(
                "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
            ),
            hasReachMaxDiscount = false,
            priceBeforeBenefit = 250000.0,
            finalPrice = 250000.0,
            tiersApplied = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = 0,
                    tierMessage = "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                    tierDiscountStr = "",
                    priceBeforeBenefit = 250000.0,
                    priceAfterBenefit = 250000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "2150835655",
                            warehouseId = "345977",
                            productName = "Jarum_Suntik",
                            productImage = "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg",
                            cartId = "2166690162",
                            finalPrice = 50000.0,
                            quantity = 1
                        )
                    )
                )
            )
        )

        val actual = bmgmMiniCartDataMapper.mapToUiModel(mockResponse.miniCart)

        val expectedStr = GsonSingleton.instance.toJson(expected)
        val actualStr = GsonSingleton.instance.toJson(actual)
        Assert.assertEquals(expectedStr, actualStr)
    }

    @Test
    fun `given mock response with bmgm and offer status = 2 should return bmgm ui model data with hasReachMaxDiscount = true`() {
        val mockResponse = getMockMiniCartResponseWithBmgmAndWholesalePrice()
        val expected = BmgmMiniCartDataUiModel(
            offerId = 910,
            offerMessage = listOf(
                "Beli 10, <b>diskon 20%</b> di [Test] Square BMSM!",
                "Tambah 4 lagi diskon 20% di [Test] Square BMSM"
            ),
            hasReachMaxDiscount = true,
            priceBeforeBenefit = 250000.0,
            finalPrice = 250000.0,
            tiersApplied = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = 0,
                    tierMessage = "Tambah 4 lagi diskon 20% di [Test] Square BMSM",
                    tierDiscountStr = "",
                    priceBeforeBenefit = 250000.0,
                    priceAfterBenefit = 250000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "2150835655",
                            warehouseId = "345977",
                            productName = "Jarum_Suntik",
                            productImage = "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg",
                            cartId = "2166690162",
                            finalPrice = 35000.0,
                            quantity = 2
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "2150835655",
                            warehouseId = "345977",
                            productName = "Jarum_Suntik",
                            productImage = "https://images.tokopedia.net/img/cache/100-square/hDjmkQ/2022/11/16/5b32ada4-9b17-48b7-a557-97b4de8fe398.jpg",
                            cartId = "2166690162",
                            finalPrice = 35000.0,
                            quantity = 2
                        )
                    )
                )
            )
        )

        val actual = bmgmMiniCartDataMapper.mapToUiModel(mockResponse.miniCart)

        val expectedStr = GsonSingleton.instance.toJson(expected)
        val actualStr = GsonSingleton.instance.toJson(actual)
        Assert.assertEquals(expectedStr, actualStr)
    }
}