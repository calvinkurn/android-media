package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.view.uimodel.CartBmGmTickerData
import com.tokopedia.cart.view.uimodel.CartDetailInfo
import com.tokopedia.cart.view.uimodel.CartGroupBmGmHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cart.view.uimodel.CartWishlistHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cart.view.uimodel.GetBmGmGroupProductTickerState
import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class CartBmGmTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN getBmGmGroupProductTicker success THEN should render ticker success`() {
        // GIVEN
        val valueOfferId = 1L
        val cartItemHolderData = CartItemHolderData(
            cartStringOrder = "cart-string-order",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    bmGmData = CartDetailInfo.BmGmData(
                        offerId = 1L
                    )
                )
            )
        )
        val bmGmData = BmGmGetGroupProductTickerResponse(
            BmGmGetGroupProductTickerResponse.GetGroupProductTicker(
                data = BmGmGetGroupProductTickerResponse.GetGroupProductTicker.Data(
                    multipleData = listOf(
                        BmGmGetGroupProductTickerResponse.GetGroupProductTicker.Data.MultipleData()
                    )
                )
            )
        )
        cartViewModel.cartModel.lastOfferId = "1L-cart-string-order"

        coEvery { bmGmGetGroupProductTickerUseCase(any()) } returns bmGmData

        // WHEN
        cartViewModel.getBmGmGroupProductTicker(
            cartItemHolderData = cartItemHolderData,
            params = BmGmGetGroupProductTickerParams()
        )

        // THEN
        assertEquals(GetBmGmGroupProductTickerState.Success(Pair(cartItemHolderData, bmGmData)), cartViewModel.bmGmGroupProductTickerState.value)
    }

    @Test
    fun `WHEN getBmGmGroupProductTicker success with empty data THEN should render ticker error`() {
        // GIVEN
        val valueOfferId = 1L
        val cartItemHolderData = CartItemHolderData(
            cartStringOrder = "cart-string-order",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    bmGmData = CartDetailInfo.BmGmData(
                        offerId = 1L
                    )
                )
            )
        )
        val bmGmData = BmGmGetGroupProductTickerResponse()
        cartViewModel.cartModel.lastOfferId = "1L-cart-string-order"

        coEvery { bmGmGetGroupProductTickerUseCase(any()) } returns bmGmData

        // WHEN
        cartViewModel.getBmGmGroupProductTicker(
            cartItemHolderData = cartItemHolderData,
            params = BmGmGetGroupProductTickerParams()
        )

        // THEN
        assertEquals(true, cartViewModel.bmGmGroupProductTickerState.value is GetBmGmGroupProductTickerState.Failed)
    }

    @Test
    fun `WHEN getBmGmGroupProductTicker failed THEN should render ticker error`() {
        // GIVEN
        val valueOfferId = 1L
        val cartItemHolderData = CartItemHolderData(
            cartStringOrder = "cart-string-order",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    bmGmData = CartDetailInfo.BmGmData(
                        offerId = 1L
                    )
                )
            )
        )
        val exception =
            ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
        cartViewModel.cartModel.lastOfferId = "1L-cart-string-order"

        coEvery { bmGmGetGroupProductTickerUseCase(any()) } throws exception

        // WHEN
        cartViewModel.getBmGmGroupProductTicker(
            cartItemHolderData = cartItemHolderData,
            params = BmGmGetGroupProductTickerParams()
        )

        // THEN
        assertEquals(GetBmGmGroupProductTickerState.Failed(Pair(cartItemHolderData, exception)), cartViewModel.bmGmGroupProductTickerState.value)
    }

    @Test
    fun `WHEN removeProductByCartId THEN should return correct new list after being deleted`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "123",
            isSelected = true,
            cartBmGmTickerData = CartBmGmTickerData(
                isShowBmGmDivider = true,
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    CartDetailInfo.BmGmData(offerId = 1L)
                )
            )
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "124",
            isSelected = false,
            cartBmGmTickerData = CartBmGmTickerData(
                isShowBmGmDivider = true,
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    CartDetailInfo.BmGmData(offerId = 1L)
                )
            )
        )
        val cartItemHolderDataThree = CartItemHolderData(cartId = "125", isSelected = true)
        val cartItemHolderDataFour = CartItemHolderData(cartId = "126", isSelected = false)
        val cartGroupHolderData = CartGroupHolderData(
            productUiModelList = mutableListOf(
                cartItemHolderData,
                cartItemHolderDataTwo,
                cartItemHolderDataThree,
                cartItemHolderDataFour
            ),
            cartGroupBmGmHolderData = CartGroupBmGmHolderData(
                hasBmGmOffer = true
            )
        )

        val disabledReasonHolderData = DisabledReasonHolderData()
        val cartItemHolderSecondData =
            CartItemHolderData(cartId = "234", isSelected = true, isError = true)
        val cartItemHolderSecondDataTwo =
            CartItemHolderData(cartId = "235", isSelected = true, isError = true)
        val cartGroupHolderDataTwo = CartGroupHolderData(
            isError = true,
            productUiModelList = mutableListOf(
                cartItemHolderSecondData,
                cartItemHolderSecondDataTwo
            )
        )

        val cartItemHolderThirdData = CartItemHolderData(cartId = "345")
        val cartGroupHolderDataThree = CartGroupHolderData(
            productUiModelList = mutableListOf(cartItemHolderThirdData)
        )

        val cartItemHolderFourData = CartItemHolderData(cartId = "456")
        val cartGroupHolderDataFour = CartGroupHolderData(
            productUiModelList = mutableListOf(cartItemHolderFourData)
        )

        val selectedAmountHolderData = CartSelectedAmountHolderData()
        val disabledItemHeaderHolderData = DisabledItemHeaderHolderData()
        val disabledAccordionHolderData = DisabledAccordionHolderData()

        cartViewModel.cartDataList.value = arrayListOf(
            selectedAmountHolderData,
            cartGroupHolderData,
            cartItemHolderData,
            cartItemHolderDataTwo,
            cartItemHolderDataThree,
            cartItemHolderDataFour,
            cartGroupHolderDataThree,
            cartItemHolderThirdData,
            cartGroupHolderDataFour,
            cartItemHolderFourData,
            disabledItemHeaderHolderData,
            disabledReasonHolderData,
            cartGroupHolderDataTwo,
            cartItemHolderSecondData,
            cartItemHolderSecondDataTwo,
            disabledAccordionHolderData,
            CartWishlistHolderData(),
            CartRecommendationItemHolderData(recommendationItem = RecommendationItem()),
            CartTopAdsHeadlineData(),
            CartRecentViewHolderData()
        )

        // WHEN
        val newCartDataList = cartViewModel.removeProductByCartId(
            listOf("123", "124", "234", "235", "456"),
            needRefresh = true,
            isFromGlobalCheckbox = false
        )

        // THEN
        assertEquals(10, newCartDataList.size)
    }

    @Test
    fun `WHEN updateBmGmTickerData THEN second product on the same offer list is updated with BMGM data with first product data`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            cartStringOrder = "222",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 1L)
                ),
                isShowTickerBmGm = true
            ),
            productId = "1111"
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "125",
            isShopShown = true,
            cartStringOrder = "222",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 1L)
                ),
                isShowTickerBmGm = false
            ),
            productId = "2222"
        )

        val cartItemHolderDataThree = CartItemHolderData(
            cartId = "126",
            isShopShown = true,
            cartStringOrder = "222",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 1L)
                ),
                isShowTickerBmGm = false
            ),
            productId = "3333"
        )

        cartViewModel.cartDataList.value = arrayListOf(cartItemHolderData, cartItemHolderDataTwo, cartItemHolderDataThree)

        // WHEN
        cartViewModel.updateBmGmTickerData(listOf(cartItemHolderData))

        // THEN
        Assert.assertTrue(cartItemHolderDataTwo.cartBmGmTickerData.isShowTickerBmGm)
    }

    @Test
    fun `WHEN updateBmGmTickerData called but no other products left in same offer id`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            cartStringOrder = "222",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 1L)
                ),
                isShowTickerBmGm = true
            ),
            productId = "1111"
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "125",
            isShopShown = true,
            cartStringOrder = "223",
            productId = "2222"
        )

        cartViewModel.cartDataList.value = arrayListOf(cartItemHolderData, cartItemHolderDataTwo)

        // WHEN
        cartViewModel.updateBmGmTickerData(listOf(cartItemHolderData))

        // THEN
        Assert.assertFalse(cartItemHolderDataTwo.cartBmGmTickerData.isShowTickerBmGm)
    }

    @Test
    fun `WHEN updateBmGmTickerData called but no product is matched`() {
        // GIVEN
        val cartItemHolderData = CartItemHolderData(
            cartId = "124",
            cartStringOrder = "222",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 1L)
                ),
                isShowTickerBmGm = true
            ),
            productId = "1111"
        )
        val cartItemHolderDataTwo = CartItemHolderData(
            cartId = "125",
            isShopShown = true,
            cartStringOrder = "223",
            productId = "2222"
        )

        val cartItemHolderDataWillDeleted = CartItemHolderData(
            cartId = "120",
            cartStringOrder = "221",
            cartBmGmTickerData = CartBmGmTickerData(
                bmGmCartInfoData = CartDetailInfo(
                    cartDetailType = "BMGM",
                    bmGmData = CartDetailInfo.BmGmData(offerId = 10L)
                ),
                isShowTickerBmGm = true
            ),
            productId = "1212"
        )

        cartViewModel.cartDataList.value = arrayListOf(cartItemHolderData, cartItemHolderDataTwo)

        // WHEN
        cartViewModel.updateBmGmTickerData(listOf(cartItemHolderDataWillDeleted))

        // THEN
        Assert.assertTrue(cartItemHolderData.cartBmGmTickerData.isShowTickerBmGm)
    }
}
