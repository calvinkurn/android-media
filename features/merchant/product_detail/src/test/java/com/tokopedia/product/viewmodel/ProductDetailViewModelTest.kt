package com.tokopedia.product.viewmodel

import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.common_sdk_affiliate_toko.model.AdditionalParam
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirProduct
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.Stock
import com.tokopedia.product.detail.common.data.model.product.VariantBasic
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomBottomSheetState
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.ui.OneTimeMethodEvent
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.util.ProductDetailTestUtil.generateMiniCartMock
import com.tokopedia.product.util.ProductDetailTestUtil.generateNotifyMeMock
import com.tokopedia.product.util.ProductDetailTestUtil.getMockP2Data
import com.tokopedia.product.util.ProductDetailTestUtil.getMockPdpLayout
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.recommendation_widget_common.affiliate.RecommendationNowAffiliateData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingData
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.model.TopadsStatus
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import rx.Observable

@ExperimentalCoroutinesApi
open class ProductDetailViewModelTest : BasePdpViewModelTest() {

    //region variable
    @Test
    fun `get mini cart and return value`() {
        `on success get product info login`()

        val selectedMiniCart = viewModel.getMiniCartItem()

        assertTrue(selectedMiniCart != null)
        assertEquals(selectedMiniCart?.productId ?: "", "518076293")
    }

    @Test
    fun `get mini cart and return null`() {
        `on success get product info non login`()

        val selectedMiniCart = viewModel.getMiniCartItem()

        assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `get mini cart return null when p1 or p2 null`() {
        // #1 : p2 is null
        every {
            spykViewModel.p2Data.value
        } returns null
        assertNull(spykViewModel.getMiniCartItem())

        // #1 : p2.miniCart is null
        every {
            spykViewModel.p2Data.value?.miniCart
        } returns null
        assertNull(spykViewModel.getMiniCartItem())

        // #1 : p2.miniCart is null
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(miniCart = mutableMapOf("1" to MiniCartItem.MiniCartItemProduct()))
        spykViewModel.getProductInfoP1 = null
        assertNull(spykViewModel.getMiniCartItem())
    }

    @Test
    fun `on success get user location cache`() {
        viewModel.getProductP1(ProductParams(), userLocationLocal = LocalCacheModel("123"))

        val data = viewModel.getUserLocationCache()
        assertTrue(data.address_id == "123")
    }

    @Test
    fun `on success clear cache`() {
        viewModel.clearCacheP2Data()

        verify {
            getP2DataAndMiniCartUseCase.clearCacheP2Data()
        }
    }

    @Test
    fun `success update video tracker data`() {
        viewModel.updateVideoTrackerData(10L, 120L)
        assertTrue(viewModel.videoTrackerData?.first == 10L)
        assertTrue(viewModel.videoTrackerData?.second == 120L)
    }

    @Test
    fun `update variable p1 with null`() {
        viewModel.updateDynamicProductInfoData(null)
        Assert.assertNull(viewModel.getProductInfoP1)
    }

    @Test
    fun `on success update talk action`() {
        viewModel.updateLastAction(ProductDetailTalkGoToWriteDiscussion)
        assertTrue(viewModel.talkLastAction is ProductDetailTalkGoToWriteDiscussion)
    }

    @Test
    fun `has shop authority`() {
        val mockAllowManage = ShopInfo(isAllowManage = 1)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns true

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        assertTrue(hasShopAuthority)
    }

    @Test
    fun `has not shop authority shopowner`() {
        val mockAllowManage = ShopInfo(isAllowManage = 1)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns false

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        assertTrue(hasShopAuthority)
    }

    @Test
    fun `has not shop authority allow manage`() {
        val mockAllowManage = ShopInfo(isAllowManage = 0)

        every {
            spykViewModel.getShopInfo()
        } returns mockAllowManage

        every {
            spykViewModel.isShopOwner()
        } returns true

        val hasShopAuthority = spykViewModel.hasShopAuthority()

        assertTrue(hasShopAuthority)
    }

    @Test
    fun `is shop owner true`() {
        val shopId = "123"
        val getDynamicProductInfo = ProductInfoP1(BasicInfo(shopID = shopId))
        viewModel.getProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns shopId

        every {
            viewModel.isUserSessionActive
        } returns true

        val isShopOwner = viewModel.isShopOwner()

        assertTrue(isShopOwner)
        viewModel.getProductInfoP1 = null
    }

    @Test
    fun `is shop owner false`() {
        val anotherShopId = "312"
        val getDynamicProductInfo = ProductInfoP1(BasicInfo(shopID = "123"))
        viewModel.getProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns anotherShopId

        every {
            viewModel.isUserSessionActive
        } returns false

        val isShopOwner = viewModel.isShopOwner()

        assertFalse(isShopOwner)
        viewModel.getProductInfoP1 = null
    }
    //endregion

    //region getShopInfo
    @Test
    fun `get shop info from P2 when data null`() {
        every {
            spykViewModel.p2Data.value
        } returns null
        val shopInfo = spykViewModel.getShopInfo()

        Assert.assertNotNull(shopInfo)
    }

    @Test
    fun `get shop info from P2 when data not null`() {
        every {
            spykViewModel.p2Data.value?.shopInfo
        } returns ShopInfo()

        val shopInfo = spykViewModel.getShopInfo()

        Assert.assertNotNull(shopInfo)
    }
    //endregion

    //region getMiniCart
    @Test
    fun `success get minicart`() {
        val data = MiniCartSimplifiedData(
            miniCartItems = mapOf(
                MiniCartItemKey("123") to MiniCartItem.MiniCartItemProduct(
                    productId = "123",
                    quantity = 2
                )
            )
        )
        val shopIdSlot = slot<List<String>>()

        `on success get product info login`()

        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns data

        viewModel.getMiniCart("312")

        verify {
            miniCartListSimplifiedUseCase.setParams(capture(shopIdSlot), any())
        }

        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }

        assertEquals(viewModel.miniCartData.value, true)
        assertEquals(shopIdSlot.captured.firstOrNull() ?: "", "312")

        val p2MiniCart = viewModel.p2Data.value?.miniCart
        Assert.assertNotNull(p2MiniCart)
        assertTrue(p2MiniCart?.isNotEmpty() == true)
        assertTrue(p2MiniCart?.get("123") != null)
        assertEquals(p2MiniCart?.get("123")?.productId ?: "", "123")
        assertEquals(p2MiniCart?.get("123")?.quantity ?: "", 2)
    }
    //endregion

    //region getCartTypeByProductId
    @Test
    fun `get cart type by product id when data not null`() {
        spykViewModel.getProductInfoP1 =
            ProductInfoP1(basic = BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(cartRedirection = mapOf("123" to CartTypeData()))

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNotNull(cartRedirection)
    }

    @Test
    fun `get cart type by product id when p1 data null`() {
        spykViewModel.getProductInfoP1 = null
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(cartRedirection = mapOf("321" to CartTypeData()))

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNull(cartRedirection)
    }

    @Test
    fun `get cart type by product id when p1 & p2 data null`() {
        spykViewModel.getProductInfoP1 = null
        every {
            spykViewModel.p2Data.value
        } returns null

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNull(cartRedirection)
    }

    @Test
    fun `on success update variable p1`() {
        viewModel.updateDynamicProductInfoData(ProductInfoP1())

        Assert.assertNotNull(viewModel.getProductInfoP1)
    }
    // endregion

    // region partial action button
    @Test
    fun `hide floating button is false when p2data error`() = with(spykViewModel) {
        every { p2Data.value?.cartRedirection } returns null
        assertFalse(shouldHideFloatingButton())

        every { p2Data.value } returns null
        assertFalse(shouldHideFloatingButton())
    }

    @Test
    fun `hide floating button is false when p1 error`() = with(spykViewModel) {
        getProductInfoP1 = null
        every { isShopOwner() } returns false
        every { p2Data.value } returns ProductInfoP2UiData()
        assertFalse(shouldHideFloatingButton())
    }

    @Test
    fun `hide floating button is false when get pid in cartRedirection is error`() =
        with(spykViewModel) {
            getProductInfoP1 = ProductInfoP1(basic = BasicInfo(productID = "123"))
            every { isShopOwner() } returns false
            every { p2Data.value } returns ProductInfoP2UiData()

            assertFalse(shouldHideFloatingButton())
        }

    @Test
    fun `hide floating button is false when shouldHideFloatingButtonInPdp return false`() =
        with(spykViewModel) {
            val pid = "123"
            val oneOverrideButton = listOf(AvailableButton())
            getProductInfoP1 = ProductInfoP1(basic = BasicInfo(productID = pid))

            every { isShopOwner() } returns false
            // owner false, hide false, override empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(pid to CartTypeData())
            )
            assertFalse(shouldHideFloatingButton())

            // owner false, hide false, override !empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(
                    pid to CartTypeData(overrideButtons = oneOverrideButton)
                )
            )
            assertFalse(shouldHideFloatingButton())

            // owner false, hide true, override !empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(
                    pid to CartTypeData(
                        hideFloatingButton = true,
                        overrideButtons = oneOverrideButton
                    )
                )
            )
            assertFalse(shouldHideFloatingButton())

            every { isShopOwner() } returns true
            // owner true, hide true, override empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(pid to CartTypeData(hideFloatingButton = true))
            )
            assertFalse(shouldHideFloatingButton())

            // owner true, hide true, override !empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(
                    pid to CartTypeData(
                        hideFloatingButton = true,
                        overrideButtons = oneOverrideButton
                    )
                )
            )
            assertFalse(shouldHideFloatingButton())

            // owner true, hide false, override empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(pid to CartTypeData())
            )
            assertFalse(shouldHideFloatingButton())

            // owner true, hide false, override !empty
            every { p2Data.value } returns ProductInfoP2UiData(
                cartRedirection = mapOf(pid to CartTypeData(overrideButtons = oneOverrideButton))
            )
            assertFalse(shouldHideFloatingButton())
        }

    @Test
    fun `hide floating button is true`() = with(spykViewModel) {
        val pid = "123"
        getProductInfoP1 = ProductInfoP1(basic = BasicInfo(productID = pid))
        every { isShopOwner() } returns false
        every { p2Data.value } returns ProductInfoP2UiData(
            cartRedirection = mapOf(pid to CartTypeData(hideFloatingButton = true))
        )
        assertTrue(shouldHideFloatingButton())
    }
    //endregion

    //region getMultiOriginByProductId
    @Test
    fun `get multi origin but p1 & p2 data not null`() {
        val expected = WarehouseInfo(id = "1")
        spykViewModel.getProductInfoP1 = ProductInfoP1(basic = BasicInfo(productID = "123"))
        every { spykViewModel.p2Data.value } returns ProductInfoP2UiData(
            nearestWarehouseInfo = mapOf("123" to expected)
        )
        val data = spykViewModel.getMultiOriginByProductId()
        assertEquals(expected.id, data.id)
    }

    @Test
    fun `get multi origin but p1 data is null`() {
        spykViewModel.getProductInfoP1 = null
        val data = viewModel.getMultiOriginByProductId()
        assertTrue(data.id.isBlank())
    }

    @Test
    fun `get multi origin but p2 data not null`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1()
        every { spykViewModel.p2Data.value } returns null

        val data = spykViewModel.getMultiOriginByProductId()
        assertTrue(data.id.isBlank())
    }

    @Test
    fun `get multi origin but warehouse id unavailable`() {
        val expected = WarehouseInfo(id = "1")
        spykViewModel.getProductInfoP1 = ProductInfoP1()
        every { spykViewModel.p2Data.value } returns ProductInfoP2UiData(
            nearestWarehouseInfo = mapOf("123" to expected)
        )
        val data = spykViewModel.getMultiOriginByProductId()
        assertTrue(data.id.isBlank())
    }
    //endregion

    //region getP2RatesEstimateByProductId
    @Test
    fun `get rates with success result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf(
                        "123"
                    )
                )
            )
        )

        val data = spykViewModel.getP2RatesEstimateDataByProductId()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get rates with null result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf(
                        "321"
                    )
                )
            )
        )

        val data = spykViewModel.getP2RatesEstimateDataByProductId()
        Assert.assertNull(data)
    }

    @Test
    fun `getP2RatesEstimateByProductId resolved jacoco`() {
        spykViewModel.getProductInfoP1 = null
        Assert.assertNull(spykViewModel.getP2RatesEstimateDataByProductId())

        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every { spykViewModel.p2Data.value } returns null
        Assert.assertNull(spykViewModel.getP2RatesEstimateDataByProductId())
    }
    //endregion

    //region getP2ShipmentPlusByProductId
    @Test
    fun `get shipment plus by product id should return non-null when exist`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf("123"),
                    shipmentPlus = mockk(relaxed = true)
                )
            )
        )

        val data = spykViewModel.getP2ShipmentPlusByProductId()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get shipment plus by product id should return null when not exist`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf("321"),
                    shipmentPlus = mockk(relaxed = true)
                )
            )
        )

        val data = spykViewModel.getP2ShipmentPlusByProductId()
        Assert.assertNull(data)
    }
    //endregion

    //region getP2RatesBottomSheetData
    @Test
    fun `get bottom sheet rates error with success result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf(
                        "123"
                    )
                )
            )
        )

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get bottom sheet rates error with null result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(
            ratesEstimate = listOfNotNull(
                P2RatesEstimate(
                    listfProductId = listOf(
                        "321"
                    )
                )
            )
        )

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNull(data)
    }
    //endregion

    //region getBebasOngkirDataByProductId
    @Test
    fun `get bebas ongkir data with success result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(
            boProduct = listOf(BebasOngkirProduct(boType, productId = "123")),
            boImages = listOf(BebasOngkirImage(boType, imageUrl))
        )

        val data = spykViewModel.getBebasOngkirDataByProductId()
        assertTrue(data.imageURL == "gambar boe gan")
        assertTrue(data.boType == 1)
    }

    @Test
    fun `get bebas ongkir data with null result`() {
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(
            boProduct = listOf(BebasOngkirProduct(boType, productId = "312")),
            boImages = listOf(BebasOngkirImage(boType, imageUrl))
        )

        val data = spykViewModel.getBebasOngkirDataByProductId()
        assertTrue(data.imageURL == "")
        assertTrue(data.boType == 0)
    }

    @Test
    fun `getBebasOngkirDataByProductId resolved jacoco`() {
        // p1 is null
        spykViewModel.getProductInfoP1 = null
        var data = spykViewModel.getBebasOngkirDataByProductId()
        assertTrue(data.imageURL.isEmpty())
        assertTrue(data.boType == 0)

        // basic info is null
        spykViewModel.getProductInfoP1 = ProductInfoP1(BasicInfo(productID = "123"))
        every { spykViewModel.p2Data.value } returns null
        data = spykViewModel.getBebasOngkirDataByProductId()
        assertTrue(data.imageURL.isEmpty())
        assertTrue(data.boType == 0)
    }
    //endregion

    //region atc
    @Test
    fun `on success delete cart tokonow non var`() = runTest {
        `on success get product info login`()
        val mockData = RemoveFromCartData(
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(
                message = listOf("sukses delete cart")
            )
        )
        val cartId = slot<List<String>>()

        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.deleteProductInCart("518076293")

        coVerify { deleteCartUseCase.executeOnBackground() }
        verify { deleteCartUseCase.setParams(capture(cartId)) }

        val cartIdDeleted = cartId.captured.firstOrNull()
        Assert.assertNotNull(cartIdDeleted)
        assertEquals(cartIdDeleted, "111")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        assertTrue(viewModel.deleteCartLiveData.value is Success)
        Assert.assertNotNull(
            (viewModel.deleteCartLiveData.value as Success).data,
            "sukses delete cart"
        )

        // after delete cart success, assert p2 minicart is deleted
        Assert.assertNull(viewModel.p2Data.value?.miniCart?.get("518076293"))
    }

    @Test
    fun `on error delete cart tokonow non var`() = runTest {
        `on success get product info login`()
        val cartId = slot<List<String>>()

        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable(message = "gagal delete cart")

        viewModel.deleteProductInCart("518076293")

        coVerify { deleteCartUseCase.executeOnBackground() }
        verify { deleteCartUseCase.setParams(capture(cartId)) }

        val cartIdDeleted = cartId.captured.firstOrNull()
        Assert.assertNotNull(cartIdDeleted)
        assertEquals(cartIdDeleted, "111")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        assertTrue(viewModel.deleteCartLiveData.value is Fail)
        Assert.assertNotNull(
            (viewModel.deleteCartLiveData.value as Fail).throwable.message,
            "sukses delete cart"
        )

        // after delete cart fail, assert p2 minicart still exist
        Assert.assertNotNull(viewModel.p2Data.value?.miniCart?.get("518076293"))
    }

    @Test
    fun `on success update cart tokonow with minicart data`() = runTest {
        `on success get product info login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock =
            MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = (viewModel.updateCartLiveData.getOrAwaitValue() as Success).data
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        assertEquals(result, "sukses update cart")
        assertTrue(viewModel.p2Data.value != null)
        assertTrue(selectedMiniCart != null)
        assertEquals(selectedMiniCart?.quantity, updatedQuantity)
    }

    @Test
    fun `on success update cart tokonow with empty minicart data`() = runTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock =
            MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = (viewModel.updateCartLiveData.getOrAwaitValue() as Success).data
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        assertEquals(result, "sukses update cart")
        assertTrue(viewModel.p2Data.value != null)
        assertTrue(selectedMiniCart != null)
        assertEquals(selectedMiniCart?.quantity, updatedQuantity)
    }

    @Test
    fun `on fail update cart tokonow`() = runTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(
            error = listOf("error gan"),
            data = Data(message = "sukses update cart")
        )
        val currentMiniCartMock =
            MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = viewModel.updateCartLiveData.getOrAwaitValue()
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        assertTrue(result is Fail)
        assertTrue(viewModel.p2Data.value != null)
        assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `on fail update cart throwable tokonow`() = runTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val currentMiniCartMock =
            MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = viewModel.updateCartLiveData.getOrAwaitValue()
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        assertTrue(result is Fail)
        assertTrue(viewModel.p2Data.value != null)
        assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `on success normal atc tokonow`() = runTest {
        `on success get product info login`()
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                productId = "1234",
                cartId = "111",
                quantity = 4
            ),
            status = "OK"
        )
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        }

        assertTrue(result.await() is Success)

        // assert minicart update
        val p2MiniCart = viewModel.p2Data.value?.miniCart
        Assert.assertNotNull(p2MiniCart)
        assertEquals(p2MiniCart?.size ?: 0, 2)
        Assert.assertNotNull(p2MiniCart?.get("1234"))
        assertEquals(p2MiniCart?.get("1234")?.quantity ?: 0, 4)
        assertEquals(p2MiniCart?.get("1234")?.cartId ?: "", "111")
    }

    @Test
    fun `on success normal atc`() = runTest {
        val addToCartRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")
        val result = async { viewModel.addToCartResultState.first() }
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.await() is Success)
    }

    @Test
    fun `on error normal atc`() = runTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.await() is Fail)
    }

    @Test
    fun `on error normal atc cause result null`() = runTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns null

        viewModel.addToCart(addToCartOcsRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.await() is Fail)
    }

    @Test
    fun `on success ocs atc`() = runTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.await() is Success)
    }

    @Test
    fun `on error ocs atc`() = runTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.await() is Fail)
    }

    @Test
    fun `on success occ atc`() = runTest {
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(
            carts = listOf(
                AddToCartOccMultiCartParam(
                    "123",
                    "123",
                    "1"
                )
            )
        )
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOccRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        assertTrue(result.await() is Success)
    }

    @Test
    fun `on error occ atc`() = runTest {
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(
            carts = listOf(
                AddToCartOccMultiCartParam(
                    "123",
                    "123",
                    "1"
                )
            )
        )
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal ya")
        )
        val result = async { viewModel.addToCartResultState.first() }

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseError

        viewModel.addToCart(addToCartOccRequestParams)
        viewModel.onFinishAnimation()

        coVerify {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        assertTrue(result.await() is Fail)
    }

    @Test
    fun `test recom add to cart when click atc button without quantity editor`() {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                cartId = "12345",
                message = arrayListOf("Barang berhasil ditambahkan ke keranjang belanja")
            ),
            status = "OK"
        )
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity, null)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        assertEquals(viewModel.atcRecomTracker.value, Success(recomItem))
        assertEquals(viewModel.atcRecom.value, Success(atcResponseSuccess.data.message.first()))
    }

    @Test
    fun `test recom add to cart when click atc button without quantity editor with zero minimum order`() {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 0
        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                cartId = "12345",
                message = arrayListOf("Barang berhasil ditambahkan ke keranjang belanja")
            ),
            status = "OK"
        )
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity, null)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        assertEquals(viewModel.atcRecomTracker.value, Success(recomItem))
        assertEquals(viewModel.atcRecom.value, Success(atcResponseSuccess.data.message.first()))
    }
    //endregion

    //region ticker p2
    @Test
    fun `test ticker oos when get ticker data by product id`() {
        `on success get product info login`()
        val productWithTicker = viewModel.p2Data.value?.getTickerByProductId("518076293")

        assertEquals(
            productWithTicker?.first()?.message,
            "Untuk sementara barang ini tidak dijual. Kamu bisa wishlist barang ini atau Cari Barang Serupa."
        )
        assertEquals(productWithTicker?.first()?.title, "barang tidak tersedia")
        assertEquals(
            productWithTicker?.first()?.actionLink,
            "https://www.tokopedia.com/rekomendasi/2086995432?ref=recom_oos"
        )
        assertEquals(productWithTicker?.first()?.action, "applink")
    }

    @Test
    fun `test ticker general multiple when get ticker data by product id`() {
        `on success get product info login`()
        val productWithTicker = viewModel.p2Data.value?.getTickerByProductId("518076286")

        assertEquals(productWithTicker?.size, 2)

        assertEquals(productWithTicker?.first()?.message, "ticker 1 message")
        assertEquals(productWithTicker?.get(1)?.message, "ticker 2 message")
    }

    @Test
    fun `test ticker not showing when get ticker data by product id`() {
        `on success get product info login`()
        val productWithTicker = viewModel.p2Data.value?.getTickerByProductId("518076287")

        Assert.assertNull(productWithTicker)
    }
    //endregion

    // ======================================PDP SECTION=============================================//
    // ==============================================================================================//
    // region GetProductInfoP1
    @Test
    fun `test correct product id parameter pdplayout`() {
        val dataP1 = getMockPdpLayout()
        val productId = "123"
        val productParams = ProductParams(productId, "", "", "", "", "")
        val userLocation = UserLocationRequest("123")
        val tokoNow = TokoNowParam("456", "789", "now15")
        `co every p1 success`(dataP1, getMockP2Data())

        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(
            productParams.productId
                ?: "",
            productParams.shopDomain ?: "",
            productParams.productName
                ?: "",
            productParams.warehouseId ?: "",
            "",
            userLocation,
            "",
            tokoNow,
            true
        )

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())

        verify { viewModel.onResetAlreadyRecomHit() }

        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(
                PARAM_PRODUCT_ID,
                ""
            ) == productId
        )
        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "").isEmpty()
        )
        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "").isEmpty()
        )
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest) != null)

        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.shopId == "456")
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.warehouseId == "789")
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.serviceType == "now15")
    }

    @Test
    fun `test correct shop domain and shop key parameter pdplayout`() {
        val dataP1 = getMockPdpLayout()
        val shopDomain = "shopYehez"
        val productKey = "productYehez"
        val productParams = ProductParams("", shopDomain, productKey, "", "", "")
        val userLocation = UserLocationRequest("123")
        val tokoNow = TokoNowParam("456", "789", "now15")

        `co every p1 success`(dataP1, getMockP2Data())
        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(
            productParams.productId
                ?: "",
            productParams.shopDomain ?: "",
            productParams.productName
                ?: "",
            productParams.warehouseId ?: "",
            "",
            userLocation,
            "",
            tokoNow,
            true
        )

        viewModel.getProductP1(productParams, true, " ", userLocationLocal = getUserLocationCache())

        verify { viewModel.onResetAlreadyRecomHit() }

        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "").isEmpty()
        )
        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(
                PARAM_PRODUCT_KEY,
                ""
            ) == productKey
        )
        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(
                PARAM_SHOP_DOMAIN,
                ""
            ) == shopDomain
        )
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest)?.districtID == "123")

        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.shopId == "456")
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.warehouseId == "789")
        assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.serviceType == "now15")
    }

    @Test
    fun `test extParam key parameter pdplayout`() {
        val dataP1 = getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")
        val userLocation = UserLocationRequest("")
        val extParam = anyString()
        val tokoNow = TokoNowParam("123")

        `co every p1 success`(dataP1, getMockP2Data())
        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(
            productParams.productId
                ?: "",
            productParams.shopDomain ?: "",
            productParams.productName
                ?: "",
            productParams.warehouseId ?: "",
            "",
            userLocation,
            extParam.encodeToUtf8(),
            tokoNow,
            true
        )

        viewModel.getProductP1(
            productParams,
            userLocationLocal = getUserLocationCache(),
            extParam = extParam
        )

        assertTrue(
            getPdpLayoutUseCase.requestParams.getString(
                PARAM_EXT_PARAM,
                ""
            ) == extParam.encodeToUtf8()
        )
    }

    @Test
    fun `on success get product info login`() {
        val dataP1 = getMockPdpLayout()
        val productParams = ProductParams("518076293", "", "", "", "", "")

        every {
            viewModel.userId
        } returns "123"

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns true

        every {
            getProductInfoP2LoginUseCase.setErrorLogListener(captureLambda())
        }.answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Throwable(""))
        }

        val miniCart =
            generateMiniCartMock(productId = dataP1.layoutData.basic.productID).toMutableMap()
        `co every p1 success`(dataP1, getMockP2Data(), miniCart)

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())

        verify { viewModel.onResetAlreadyRecomHit() }

        `co verify p1 success`()

        assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        assertTrue(
            viewModel.p2Data.value?.miniCart?.any {
                it.key == "518076293"
            } ?: false
        )
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNotNull(viewModel.p2Login.value)
        assertTrue(viewModel.topAdsImageView.value is Success)

        val p1Result = (viewModel.productLayout.value as Success).data
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO })
        // update: palugada unused component wholesales_info
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO })
        assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 1)
        assertTrue(p1Result.count { it.name() == ProductDetailConstant.REPORT } == 1)
        assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 1)
        assertTrue(p1Result.count { it.name() == ProductDetailConstant.AR_BUTTON } == 1)
        assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRICE } == 1)
    }

    private fun `co verify p1 success`() {
        // P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        coVerify {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun `co every p1 success`(
        dataP1: ProductDetailDataModel,
        dataP2: ProductInfoP2UiData,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>? = null
    ) {
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns flowOf(Result.success(dataP1))

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns ProductInfoP2Login()

        coEvery {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                captureLambda()
            )
        }.coAnswers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Throwable(""))
            dataP2.miniCart = miniCart ?: mutableMapOf()
            dataP2.upcomingCampaigns = generateNotifyMeMock()
            dataP2
        }

        coEvery {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        } returns ProductInfoP2Other()

        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns arrayListOf(TopAdsImageUiModel())
    }

    @Test
    fun `on error get product info login`() {
        val productParams = ProductParams("", "", "", "", "", "")

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns flowOf(Result.failure(Throwable()))

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())
        // P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        assertTrue(viewModel.productLayout.value is Fail)
        Assert.assertNull(viewModel.p2Data.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNull(viewModel.p2Other.value)

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getP2DataAndMiniCartUseCase.executeOnBackground()
        }

        coVerify(inverse = true) {
            getProductInfoP2OtherUseCase.executeOnBackground()
        }
    }

    @Test
    fun `on error get product p1`() {
        val productParams = ProductParams()

        mockkObject(GetPdpLayoutUseCase)
        every {
            GetPdpLayoutUseCase.createParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Throwable()

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())

        assertTrue(viewModel.productLayout.value is Fail)
    }

    @Test
    fun `on success get product info non login`() {
        val dataP1 = getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            userSessionInterface.isLoggedIn
        } returns false

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1, getMockP2Data())

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())

        // P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNull(viewModel.p2Login.value)
        Assert.assertNotNull(viewModel.shouldHideFloatingButton())
    }

    @Test
    fun `on success remove unused component`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpThatShouldRemoveUnusedComponent()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            viewModel.userId
        } returns "123"

        every {
            viewModel.isShopOwner()
        } returns true

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1, getMockP2Data())

        viewModel.getProductP1(
            productParams,
            refreshPage = true,
            userLocationLocal = getUserLocationCache()
        )
        verify {
            viewModel.onResetAlreadyRecomHit()
        }

        val p1Result = (viewModel.productLayout.value as Success).data
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.TRADE_IN })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.VALUE_PROP })
        // remove unused palugada
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_FULLFILMENT })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.ORDER_PRIORITY })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRICE })

        assertTrue(p1Result.any { it.name() == ProductDetailConstant.MEDIA })
        assertTrue(p1Result.any { it.name() == ProductDetailConstant.TICKER_INFO })
        assertTrue(p1Result.any { it.name() == ProductDetailConstant.PRODUCT_CONTENT })
        assertTrue(p1Result.any { it.name() == ProductDetailConstant.PRODUCT_PROTECTION })
        assertTrue(p1Result.any { it.name() == ProductDetailConstant.SHIPMENT })
    }

    @Test
    fun `on success remove unused component when sellerapp`() {
        val dataP1 = getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            GlobalConfig.isSellerApp()
        } returns true

        every {
            viewModel.userId
        } returns "123"

        every {
            viewModel.isShopOwner()
        } returns true

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1, getMockP2Data())

        viewModel.getProductP1(
            productParams,
            refreshPage = true,
            userLocationLocal = getUserLocationCache()
        )
        verify { viewModel.onResetAlreadyRecomHit() }

        val p1Result = (viewModel.productLayout.value as Success).data
        assertTrue(p1Result.none { it.type() == ProductDetailConstant.PRODUCT_LIST })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.PLAY_CAROUSEL })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.REPORT })
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.AR_BUTTON })
    }

    @Test
    fun `remove button_ar when customerapp and os under lollipop`() {
        val dataP1 = getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            GlobalConfig.isSellerApp()
        } returns false

        /**
         * make sure button ar removed because of os under 22
         */
        setOS(20)

        every {
            viewModel.userId
        } returns "123"

        every {
            viewModel.isShopOwner()
        } returns true

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1, getMockP2Data())

        viewModel.getProductP1(
            productParams,
            refreshPage = true,
            userLocationLocal = getUserLocationCache()
        )
        verify { viewModel.onResetAlreadyRecomHit() }

        val p1Result = (viewModel.productLayout.value as Success).data
        assertTrue(p1Result.none { it.name() == ProductDetailConstant.AR_BUTTON })
    }
    // endregion

    // region variant
    @Test
    fun `process initial variant tokonow`() {
        val variantData = ProductDetailTestUtil.getMockVariant()
        val result = ProductDetailVariantLogic.determineVariant(
            mapOfSelectedOptionIds = mutableMapOf(),
            productVariant = variantData
        )

        assertTrue(result != null)
    }

    @Test
    fun `determine variant return value`() {
        val productVariant = ProductVariant()
        val mapOfSelectedOptionIds = mutableMapOf<String, String>()

        val expectedVariantCategory = VariantCategory()

        mockkObject(ProductDetailVariantLogic)

        every {
            ProductDetailVariantLogic.determineVariant(mapOfSelectedOptionIds, productVariant)
        } returns expectedVariantCategory

        val result = ProductDetailVariantLogic.determineVariant(
            mapOfSelectedOptionIds = mapOfSelectedOptionIds,
            productVariant = productVariant
        )
        assertTrue(result == expectedVariantCategory)
    }

    @Test
    fun `determine variant return null`() {
        val productVariant = ProductVariant()
        val mapOfSelectedOptionIds = mutableMapOf<String, String>()

        mockkObject(ProductDetailVariantLogic)

        every {
            ProductDetailVariantLogic.determineVariant(mapOfSelectedOptionIds, productVariant)
        } returns null

        val result = ProductDetailVariantLogic.determineVariant(
            mapOfSelectedOptionIds = mapOfSelectedOptionIds,
            productVariant = productVariant
        )
        assertTrue(result == null)
    }

    @Test
    fun `determine variant is throw`() {
        val productVariant = mockk<ProductVariant>()
        val mapOfSelectedOptionIds = mutableMapOf<String, String>()

        mockkObject(ProductDetailVariantLogic)

        every {
            productVariant.isSelectedChildHasFlashSale(anyString())
        } throws Throwable()

        val result = ProductDetailVariantLogic.determineVariant(
            mapOfSelectedOptionIds = mapOfSelectedOptionIds,
            productVariant = productVariant
        )
        assertTrue(result == null)
    }

    @Test
    fun `child is available when get child of variant selected with single variant only`() {
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                "28323838" to "94748049",
                "28323839" to "94748052"
            )
        )
        `child options id is available when get child of variant selected`(singleVariant)
    }

    @Test
    fun `child is null when get child of variant selected with single and options variant is null`() {
        `on success get pdp layout mini variants options`()
        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = null
        )
        assertTrue(childVariant == null)
    }

    @Test
    fun `child is null when get child of variant selected with single and options variant is empty`() {
        `on success get pdp layout mini variants options`()
        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = ProductSingleVariantDataModel()
        )
        assertTrue(childVariant == null)
    }

    private fun `child options id is available when get child of variant selected`(
        singleVariant: ProductSingleVariantDataModel?
    ) {
        // given pdp layout data
        `on success get pdp layout mini variants options`()

        val p1Result = (viewModel.productLayout.value as Success).data
        // then, get child of variant selected
        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = singleVariant
        )

        // assert the expectation
        assertTrue(
            p1Result.any {
                it.type() == ProductDetailConstant.PRODUCT_VARIANT_INFO &&
                    it.name() == ProductDetailConstant.MINI_VARIANT_OPTIONS
            }
        )
        assertTrue(childVariant?.optionIds?.firstOrNull() != null)
    }

    @Test
    fun `child is null when get child of variant selected with variant data is null and single variant only`() {
        val singleVariant = ProductSingleVariantDataModel()
        `on success get pdp layout mini variants options`()

        viewModel.variantData = null

        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = singleVariant
        )

        assertTrue(viewModel.variantData == null)
        assertTrue(childVariant == null)
    }

    private fun `on success get pdp layout mini variants options`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayoutMiniVariant()
        val productParams = ProductParams(productId = "1589853923")

        every {
            viewModel.userId
        } returns "123"

        every {
            userSessionInterface.isLoggedIn
        } returns true

        every {
            viewModel.isUserSessionActive
        } returns true

        every {
            getProductInfoP2LoginUseCase.setErrorLogListener(captureLambda())
        }.answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Throwable(""))
        }

        val miniCart = generateMiniCartMock(dataP1.layoutData.basic.productID).toMutableMap()
        `co every p1 success`(dataP1, getMockP2Data(), miniCart)

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())
        verify { viewModel.onResetAlreadyRecomHit() }
        `co verify p1 success`()
    }

    //endregion variant

    // region Hit Affiliate, most of it do no op call
    @Test
    fun onSuccessHitAffiliateTracker() {
        every {
            trackAffiliateUseCase.execute(captureLambda(), any())
        } answers {
        }

        viewModel.hitAffiliateTracker(anyString(), anyString())

        verify { viewModel.hitAffiliateTracker(anyString(), anyString()) }
    }

    @Test
    fun onErrorHitAffiliateTracker() {
        every {
            trackAffiliateUseCase.execute(any(), captureLambda())
        } answers {
        }

        viewModel.hitAffiliateTracker(anyString(), anyString())

        verify { viewModel.hitAffiliateTracker(anyString(), anyString()) }
    }
    // endregion

    //region affiliate cookie
    @Test
    fun `integration test affiliate cookie sdk`() {
        val productId = "321"
        val shopId = "123"
        val categoryId = "123"
        val isVariant = true
        val stock = 10

        val mockProductInfoP1 = ProductInfoP1(
            basic = BasicInfo(
                shopID = shopId,
                productID = productId,
                category = Category(
                    detail = listOf(
                        Category.Detail(
                            id = categoryId
                        ),
                        Category.Detail(
                            id = "312"
                        )
                    )
                )
            ),
            data = ComponentData(
                variant = VariantBasic(
                    isVariant = isVariant
                ),
                stock = Stock(
                    value = stock
                )
            )
        )

        val affiliateUUID = "123"
        val affiliateChannel = "affiliate channel"
        val uuid = "1111"
        val affiliateSubIds = mapOf("1" to "subId1", "subid2" to "subId2", "asal" to "asal")
        val affiliateSource = "PDP"

        val slot = slot<AffiliatePageDetail>()
        val subIdsSlot = slot<List<AdditionalParam>>()

        viewModel.hitAffiliateCookie(
            productInfo = mockProductInfoP1,
            affiliateUuid = affiliateUUID,
            uuid = uuid,
            affiliateChannel = affiliateChannel,
            affiliateSubIds = affiliateSubIds,
            affiliateSource = affiliateSource
        )

        coVerify {
            affiliateCookieHelper.initCookie(
                affiliateUUID = affiliateUUID,
                affiliateChannel = affiliateChannel,
                affiliatePageDetail = capture(slot),
                uuid = uuid,
                additionalParam = emptyList(),
                subIds = capture(subIdsSlot),
                source = affiliateSource
            )
        }

        with(slot.captured) {
            assertEquals(productId, this.pageId)
            assertTrue(source is AffiliateSdkPageSource.PDP)
        }

        with(subIdsSlot.captured) {
            assertEquals(size, 2)

            assertEquals(get(0).key, "1")
            assertEquals(get(0).value, "subId1")

            assertEquals(get(1).key, "2")
            assertEquals(get(1).value, "subId2")
        }
    }

    @Test
    fun `when init affiliate cookie throw, no op`() {
        val productId = "321"
        val shopId = "123"
        val categoryId = "123"
        val isVariant = true
        val stock = 10

        val mockProductInfoP1 = ProductInfoP1(
            basic = BasicInfo(
                shopID = shopId,
                productID = productId,
                category = Category(
                    detail = listOf(
                        Category.Detail(
                            id = categoryId
                        ),
                        Category.Detail(
                            id = "312"
                        )
                    )
                )
            ),
            data = ComponentData(
                variant = VariantBasic(
                    isVariant = isVariant
                ),
                stock = Stock(
                    value = stock
                )
            )
        )

        val affiliateUUID = "123"
        val affiliateChannel = "affiliate channel"
        val uuid = "1111"

        coEvery {
            affiliateCookieHelper.initCookie(
                affiliateUUID,
                affiliateChannel,
                any(),
                uuid
            )
        } throws Throwable("gagal bro")

        viewModel.hitAffiliateCookie(
            productInfo = mockProductInfoP1,
            affiliateUuid = affiliateUUID,
            uuid = uuid,
            affiliateChannel = affiliateChannel,
            affiliateSubIds = null,
            affiliateSource = null
        )

        coVerify {
            affiliateCookieHelper.initCookie(
                affiliateUUID = affiliateUUID,
                affiliateChannel = affiliateChannel,
                affiliatePageDetail = any(),
                uuid = uuid,
                subIds = emptyList(),
                source = ""
            )
        }
        // no op, expect to be handled by Affiliate SDK
    }
    //endregion

    // region UpdateCartCounter
    @Test
    fun onSuccessUpdateCartCounter() {
        val data = 10
        every {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        } returns Observable.just(data)

        viewModel.updateCartCounerUseCase {
            assertEquals(it, data)
        }

        verify {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        }
    }
    // endregion UpdateCartCounter

    // region ToggleFavorite
    @Test
    fun onSuccessToggleFavoriteShop() {
        val shopId = "1234"
        val data = FollowShop()
        data.isSuccess = true

        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop
        } returns data

        viewModel.toggleFavorite(shopId)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
        }

        assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, false)
    }

    @Test
    fun onSuccessToggleFavoriteShopNpl() {
        val shopId = "1234"
        val isNpl = true
        val data = FollowShop()
        data.isSuccess = true

        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop
        } returns data

        viewModel.toggleFavorite(shopId, isNpl)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
        }

        assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, isNpl)
    }

    @Test
    fun onErrorToggleFavoriteShop() {
        val shopId = "1234"
        coEvery {
            toggleFavoriteUseCase.executeOnBackground(any()).followShop?.isSuccess
        } throws Throwable()

        viewModel.toggleFavorite(shopId)

        coVerify {
            toggleFavoriteUseCase.executeOnBackground(any())
        }

        assertTrue(viewModel.toggleFavoriteResult.value is Fail)
    }
    // endregion ToggleFavorite

    // region Discussion Most Helpful
    @Test
    fun `on success getDiscussionMostHelpful`() = runTest {
        val expectedResponse = DiscussionMostHelpfulResponseWrapper()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } returns expectedResponse

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        assertEquals(
            expectedResponse,
            (viewModel.discussionMostHelpful.value as Success).data
        )
    }

    @Test
    fun `on error getDiscussionMostHelpful`() = runTest {
        val expectedError = Throwable()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } throws expectedError

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        assertTrue(viewModel.discussionMostHelpful.value is Fail)
    }
    // endregion Discussion

    // region getProductTopadsStatus
    @Test
    fun `when get topads status then verify error response`() = runTest {
        val productId = "12345"
        val paramsTest = "txsc=asdf"

        val isSuccess = slot<Boolean>()
        val errorMessage = slot<String>()

        coEvery {
            getTopadsIsAdsUseCase.executeOnBackground()
        } throws Throwable("error")

        coEvery {
            remoteConfigInstance.getLong(any(), any())
        } returns 5000

        viewModel.getProductTopadsStatus(productId, paramsTest)
        coVerify { getTopadsIsAdsUseCase.executeOnBackground() }

        assertTrue(viewModel.topAdsRecomChargeData.value is Fail)
        assertEquals(isSuccess.captured, false)
        assertEquals(errorMessage.captured, "error")

        assertEquals(
            (viewModel.topAdsRecomChargeData.value as Fail).throwable.message,
            "error"
        )
    }

    @Test
    fun `when get topads status then verify success response and enable to charge`() = runTest {
        val productId = "12345"
        val paramsTest = "txsc=asdf"
        val expectedResponse = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(TopAdsGetDynamicSlottingDataProduct(isCharge = true)),
                status = TopadsStatus(
                    error_code = 200,
                    message = "OK"
                )
            )
        )
        val isSuccess = slot<Boolean>()
        val errorCode = slot<Int>()
        val isTopAds = slot<Boolean>()

        coEvery {
            remoteConfigInstance.getLong(any(), any())
        } returns 5000

        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns expectedResponse

        viewModel.getProductTopadsStatus(productId, paramsTest)
        coVerify { getTopadsIsAdsUseCase.executeOnBackground() }

        assertTrue(expectedResponse.data.status.error_code in 200..300 && expectedResponse.data.productList[0].isCharge)
        assertEquals(isSuccess.captured, true)
        assertEquals(errorCode.captured, 200)
        assertEquals(isTopAds.captured, true)
    }
    // endregion

    // region tokonow recom section
    @Test
    fun `test add to cart non variant then return success cart data`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(
                success = 1,
                cartId = "12345",
                message = arrayListOf("halo")
            ),
            status = "OK"
        )
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity, RecommendationNowAffiliateData())
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        assertTrue(!atcResponseSuccess.isStatusError())
        assertTrue(viewModel.atcRecomTracker.value is Success)
        assertEquals(Success(recomItem), viewModel.atcRecomTracker.value)
    }

    @Test
    fun `test add to cart non variant then return failed with message`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf("gagal maning euy")
        )
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.atcRecomNonVariant(recomItem, quantity, RecommendationNowAffiliateData())
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    @Test
    fun `test add to cart non variant then return failed with throwable`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } throws Throwable()

        viewModel.atcRecomNonVariant(recomItem, quantity, RecommendationNowAffiliateData())
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    @Test
    fun `test update cart non variant then return success cart data`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        val quantity = 11
        val response = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(
            recomItem,
            quantity,
            miniCart,
            RecommendationNowAffiliateData()
        )
        coVerify {
            updateCartUseCase.executeOnBackground()
        }
        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Success)
    }

    @Test
    fun `test update cart non variant then return failed with message`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        val quantity = 11
        val response = UpdateCartV2Data(error = listOf("error nih gan"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(
            recomItem,
            quantity,
            miniCart,
            RecommendationNowAffiliateData()
        )

        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    @Test
    fun `test update cart non variant then return error throwable`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        val quantity = 11
        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateRecomCartNonVariant(
            recomItem,
            quantity,
            miniCart,
            RecommendationNowAffiliateData()
        )
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return success cart data`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        val response = RemoveFromCartData(
            status = "OK",
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(
                message = listOf("sukses delete cart"),
                success = 1
            )
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }
        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Success)
    }

    @Test
    fun `test delete cart non variant then return failed with message`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        val response = RemoveFromCartData(
            status = "ERROR",
            data = com.tokopedia.cartcommon.data.response.deletecart.Data(success = 0)
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return error throwable`() = runTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(
            productId = recomItem.productId.toString(),
            quantity = 10
        )
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        assertTrue(viewModel.atcRecom.value is Fail)
    }

    // endregion
    @Test
    fun `verify add to wishlistv2 returns success`() {
        `on success get product info login`()

        val productId = "518076286"

        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(any(), any()) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        assertEquals(viewModel.p2Data.value?.getWishlistStatusByProductId(productId), true)
    }

    @Test
    fun `verify isWithList in getDynamicProductInfoP1 is true after add wishlist`() {
        viewModel.getProductInfoP1 = ProductInfoP1()
        `verify add to wishlistv2 returns success`()
        assertTrue(viewModel.getProductInfoP1?.data?.isWishlist.orFalse())
    }

    @Test
    fun `verify wishlist value from p1 replace with previous value`() {
        viewModel.getProductInfoP1 = null
        `on success get product info non login`()
        assertFalse(viewModel.getProductInfoP1?.data?.isWishlist.orTrue())

        viewModel.getProductInfoP1 = ProductInfoP1()
        `on success get product info non login`()
        assertFalse(viewModel.getProductInfoP1?.data?.isWishlist.orTrue())

        viewModel.getProductInfoP1 = ProductInfoP1(
            data = ComponentData(isWishlist = true)
        )
        `on success get product info non login`()
        assertTrue(viewModel.getProductInfoP1?.data?.isWishlist.orFalse())

        viewModel.getProductInfoP1 = ProductInfoP1(
            data = ComponentData(isWishlist = false)
        )
        `on success get product info non login`()
        assertFalse(viewModel.getProductInfoP1?.data?.isWishlist.orTrue())
    }

    @Test
    fun `verify add to wishlistv2 returns fail`() {
        `on success get product info login`()

        val productId = "518076286"

        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify {
            addToWishlistV2UseCase.setParams(
                any(),
                any()
            )
        }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        assertEquals(viewModel.p2Data.value?.getWishlistStatusByProductId(productId), false)
    }

    @Test
    fun `verify remove wishlistV2 returns success`() {
        `on success get product info login`()

        val productId = "518076293"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(
            resultWishlistRemoveV2
        )

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(any(), any()) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }

        assertEquals(viewModel.p2Data.value?.getWishlistStatusByProductId(productId), false)
    }

    @Test
    fun `verify remove wishlistV2 returns fail`() {
        `on success get product info login`()

        val productId = "518076293"

        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(any(), any()) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }

        assertEquals(viewModel.p2Data.value?.getWishlistStatusByProductId(productId), true)
    }

    //region product ar
    @Test
    fun `verify product ar data`() {
        `on success get product info login`()

        val p2Ar = viewModel.p2Data.value?.arInfo
        Assert.assertNotNull(p2Ar)
        p2Ar?.let {
            assertEquals(it.isProductIdContainsAr("518076293"), true)
            assertEquals(it.isProductIdContainsAr("518076286"), false)
            assertEquals(it.isProductIdContainsAr("948021897"), false)
            assertEquals(it.isProductIdContainsAr("518076287"), false)
        }
    }
    //endregion

    // ======================================END OF PDP SECTION=======================================//
    // ==============================================================================================//

    // region Review Section
    @Test
    fun `on success get product info login should contain valid review data`() {
        val expectedVideoCount = 1
        val expectedImageCount = 4
        val expectedBuyerMediaCount = 64
        val expectedSocialProofText = "Foto & Video Pembeli"

        `on success get product info login`()

        val actualVideoCount =
            viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
                it is ReviewMediaVideoThumbnailUiModel
            }
        val actualImageCount =
            viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
                it is ReviewMediaImageThumbnailUiModel
            }
        val showingSeeMoreThumbnail =
            viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
                it is ReviewMediaImageThumbnailUiModel && it.isShowingSeeMore()
            }
        val seeMoreThumbnailPosition =
            viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.indexOfFirst {
                it is ReviewMediaImageThumbnailUiModel && it.isShowingSeeMore()
            }
        val showingSeeMoreThumbnailOnLastThumbnailOnly =
            showingSeeMoreThumbnail == 1 && seeMoreThumbnailPosition == 4
        val actualBuyerMediaCount = viewModel.p2Data.value?.imageReview?.buyerMediaCount
        val actualSocialProofText = viewModel.p2Data.value?.imageReview?.staticSocialProofText

        assertEquals("Invalid video count.", expectedVideoCount, actualVideoCount)
        assertEquals("Invalid image count.", expectedImageCount, actualImageCount)
        assertTrue(
            "Should show see more thumbnail on last position but was not show",
            showingSeeMoreThumbnailOnLastThumbnailOnly
        )
        assertEquals(
            "Invalid buyer media count.",
            expectedBuyerMediaCount,
            actualBuyerMediaCount
        )
        assertEquals(
            "Invalid social proof text",
            expectedSocialProofText,
            actualSocialProofText
        )
    }
    // endregion Review Section

    @Test
    fun `atc recom non variant quantity changed with quantity changed from 0 to 1`() {
        val recommItem = RecommendationItem()
        val quantity = 1

        every {
            userSessionInterface.isLoggedIn
        } returns true

        viewModel.onAtcRecomNonVariantQuantityChanged(
            recommItem,
            quantity,
            RecommendationNowAffiliateData()
        )

        coVerify { addToCartUseCase.createObservable(any()) }
    }

    @Test
    fun `atc recom non variant quantity changed with quantity changed from 1 to 2`() {
        val recommItem = RecommendationItem(quantity = 1)
        val quantity = 2
        val miniCartItemProduct = MiniCartItem.MiniCartItemProduct()
        val mapMiniCartItem = mutableMapOf(recommItem.productId.toString() to miniCartItemProduct)

        every {
            spykViewModel.p2Data.value?.miniCart
        } returns mapMiniCartItem

        every {
            userSessionInterface.isLoggedIn
        } returns true

        spykViewModel.onAtcRecomNonVariantQuantityChanged(
            recommItem,
            quantity,
            RecommendationNowAffiliateData()
        )

        coVerify { updateCartUseCase.executeOnBackground() }
    }

    @Test
    fun `atc recom non variant quantity changed delete item`() {
        val recommItem = RecommendationItem(quantity = 1)
        val quantity = 0
        val miniCartItemProduct = MiniCartItem.MiniCartItemProduct()
        val mapMiniCartItem = mutableMapOf(recommItem.productId.toString() to miniCartItemProduct)

        every {
            spykViewModel.p2Data.value?.miniCart
        } returns mapMiniCartItem

        every {
            userSessionInterface.isLoggedIn
        } returns true

        spykViewModel.onAtcRecomNonVariantQuantityChanged(
            recommItem,
            quantity,
            RecommendationNowAffiliateData()
        )

        coVerify { deleteCartUseCase.executeOnBackground() }
    }

    @Test
    fun `atc recom non variant quantity changed not logged in`() {
        val recommItem = RecommendationItem()
        val quantity = 1

        every {
            userSessionInterface.isLoggedIn
        } returns false

        viewModel.onAtcRecomNonVariantQuantityChanged(
            recommItem,
            quantity,
            RecommendationNowAffiliateData()
        )

        assertEquals(recommItem, viewModel.atcRecomTokonowNonLogin.value)
    }

    @Test
    fun `change one time method default value`() = runTest {
        val oneTimeMethodState = { viewModel.oneTimeMethodState.value }
        backgroundScope.launch {
            viewModel.oneTimeMethodState.collect()
        }

        viewModel.changeOneTimeMethod(event = OneTimeMethodEvent.Empty)

        assertTrue(oneTimeMethodState().event is OneTimeMethodEvent.Empty)
        assertEquals(oneTimeMethodState().impressRestriction, false)
    }

    @Test
    fun `change one time method assign all value`() = runTest {
        val oneTimeMethodState = { viewModel.oneTimeMethodState.value }
        backgroundScope.launch {
            viewModel.oneTimeMethodState.collect()
        }

        // second assignment, because the first one is default value which OneTimeMethodEvent.Empty
        assertEquals(oneTimeMethodState().impressRestriction, false)

        val reData = RestrictionData(productId = "123")
        // region impress restriction
        viewModel.changeOneTimeMethod(event = OneTimeMethodEvent.ImpressRestriction(reData))
        assertTrue(oneTimeMethodState().event is OneTimeMethodEvent.ImpressRestriction)
        assertTrue((oneTimeMethodState().event as OneTimeMethodEvent.ImpressRestriction).reData.productId == "123")
        assertEquals(oneTimeMethodState().impressRestriction, true)

        // re-assign and make sure we dont want to update the data, since we need to run every event exactly once
        viewModel.changeOneTimeMethod(event = OneTimeMethodEvent.ImpressRestriction(reData))
        // endregion

        // region validate general edu bs
        viewModel.changeOneTimeMethod(event = OneTimeMethodEvent.ImpressGeneralEduBs("applink"))
        assertTrue(oneTimeMethodState().event is OneTimeMethodEvent.ImpressGeneralEduBs)
        val edu = oneTimeMethodState().event as OneTimeMethodEvent.ImpressGeneralEduBs
        assertTrue(edu.appLink == "applink")

        // re-assign
        viewModel.changeOneTimeMethod(event = OneTimeMethodEvent.ImpressGeneralEduBs("applink"))
        // endregion
    }

    @Test
    fun `product media recom bs state should showing when successfully fetch non-empty recom data`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(
            RecommendationWidget(recommendationItemList = listOf(mockk(relaxed = true)))
        )

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.ShowingData)
    }

    @Test
    fun `product media recom bs state should dismissed when successfully fetch empty recom data`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(RecommendationWidget())

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.Dismissed)
    }

    @Test
    fun `product media recom bs state should error when unsuccessfully fetch recom data`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Exception("Dummy")

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.ShowingError)
    }

    @Test
    fun `product media recom bs state should loading when fetching recom data`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } answers {
            assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.Loading)
            throw Exception("Dummy")
        }

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.ShowingError)
    }

    @Test
    fun `should not fetch recom data when pageName is not changed`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(
            RecommendationWidget(recommendationItemList = listOf(mockk(relaxed = true)))
        )

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)
        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
    }

    @Test
    fun `should re-fetch recom data when pageName is changed`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(
            RecommendationWidget(recommendationItemList = listOf(mockk(relaxed = true)))
        )

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)
        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy 1", "123", false)

        coVerify(exactly = 2) { getRecommendationUseCase.getData(any()) }
    }

    @Test
    fun `should re-fetch recom data when previous recom data is empty`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(RecommendationWidget())

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)
        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", false)

        coVerify(exactly = 2) { getRecommendationUseCase.getData(any()) }
    }

    @Test
    fun `should re-fetch recom data when previous recom data is empty as tokonow`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns listOf(RecommendationWidget())

        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", true)
        viewModel.showProductMediaRecomBottomSheet("Dummy", "Dummy", "123", true)

        coVerify(exactly = 2) { getRecommendationUseCase.getData(any()) }
    }

    @Test
    fun `product media recom bs state should dismissed when dismiss product media recom bs`() {
        `product media recom bs state should showing when successfully fetch non-empty recom data`()

        viewModel.dismissProductMediaRecomBottomSheet()

        assertTrue(viewModel.productMediaRecomBottomSheetState.value is ProductMediaRecomBottomSheetState.Dismissed)
    }

    private fun getUserLocationCache(): LocalCacheModel {
        return LocalCacheModel("123", "123", "123", "123")
    }

    // region bottom-sheet edu
    @Test
    fun `show bottom-sheet edu normally`() {
        // given
        val mockP1 = getMockPdpLayout()
        val mockP2 = getMockP2Data()
        `co every p1 success`(
            dataP1 = mockP1,
            dataP2 = mockP2.copy(
                bottomSheetEdu = mockP2.bottomSheetEdu.copy(isShow = true)
            )
        )

        // when
        viewModel.getProductP1(ProductParams(), userLocationLocal = getUserLocationCache())

        // then
        val showEdu = viewModel.showBottomSheetEdu.getOrAwaitValue()
        assertTrue(showEdu != null)
        assertTrue(showEdu?.isShow == true)
        assertTrue(!showEdu?.appLink.isNullOrBlank())
    }

    @Test
    fun `bottom-sheet edu not appear when isShow is false`() {
        // given
        val mockP1 = getMockPdpLayout()
        val mockP2 = getMockP2Data()
        `co every p1 success`(
            dataP1 = mockP1,
            dataP2 = mockP2.copy(
                bottomSheetEdu = mockP2.bottomSheetEdu.copy(isShow = false)
            )
        )

        // when
        viewModel.getProductP1(ProductParams(), userLocationLocal = getUserLocationCache())

        // then
        val showEdu = viewModel.showBottomSheetEdu.getOrAwaitValue()
        assertTrue(showEdu == null)
    }

    @Test
    fun `bottom-sheet edu not appear when isShow is true but applink is empty`() {
        // given
        val mockP1 = getMockPdpLayout()
        val mockP2 = getMockP2Data()
        `co every p1 success`(
            dataP1 = mockP1,
            dataP2 = mockP2.copy(
                bottomSheetEdu = mockP2.bottomSheetEdu.copy(isShow = true, appLink = "")
            )
        )

        // when
        viewModel.getProductP1(ProductParams(), userLocationLocal = getUserLocationCache())

        // then
        val showEdu = viewModel.showBottomSheetEdu.getOrAwaitValue()
        assertTrue(showEdu == null)
    }
    // endregion

    // region sub-viewmodel mediator
    @Test
    fun `success get product detail data via mediator`() {
        val p2Expected = ProductInfoP2UiData()
        val p1Expected = ProductInfoP1()
        val variantExpected = ProductVariant()

        every { spykViewModel.p2Data.value } returns p2Expected
        spykViewModel.getProductInfoP1 = p1Expected
        spykViewModel.variantData = variantExpected

        Assert.assertNotNull(spykViewModel.getP2())
        Assert.assertNotNull(spykViewModel.getP1())
        Assert.assertNotNull(spykViewModel.getVariant())

        assertTrue(spykViewModel.getP2() == p2Expected)
        assertTrue(spykViewModel.getP1() == p1Expected)
        assertTrue(spykViewModel.getVariant() == variantExpected)
    }
    // endregion

    @Test
    fun `success check affiliate eligibility`() {
        val mockData = GenerateAffiliateLinkEligibility()
        val mockParam = AffiliateInput()

        coEvery {
            affiliateEligibilityCheckUseCase.executeOnBackground()
        } returns mockData

        viewModel.checkAffiliateEligibility(mockParam)
        coVerify {
            affiliateEligibilityCheckUseCase.executeOnBackground()
        }
        assertTrue(viewModel.resultAffiliate.value is Success)
    }

    @Test
    fun `error check affiliate eligibility`() {
        val mockError = Exception()
        val mockParam = AffiliateInput()

        coEvery {
            affiliateEligibilityCheckUseCase.executeOnBackground()
        } throws mockError

        viewModel.checkAffiliateEligibility(mockParam)
        coVerify {
            affiliateEligibilityCheckUseCase.executeOnBackground()
        }
        assertTrue(viewModel.resultAffiliate.value is Fail)
    }

    @Test
    fun `setAPlusContentCollapseState should update aPlusContentMediaCollapse value`() {
        val initialValue = viewModel.isAPlusContentExpanded()
        val expectedValue = !initialValue
        viewModel.setAPlusContentExpandedState(expectedValue)
        assertEquals(expectedValue, viewModel.isAPlusContentExpanded())
    }

    @Test
    fun `initial aPlusContentExpanded value should be false`() {
        assertFalse(viewModel.isAPlusContentExpanded())
    }

    /** PDP Cacheable **/
    @Test
    fun `pdp get layout throwable within use-case flow`() {
        val productParams = ProductParams()

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } answers { flowOf(Result.failure(Throwable())) }

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())

        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        assertTrue(viewModel.productLayout.value is Fail)
    }

    @Test
    fun `p2 get from basic data when cache is true`() = runTest {
        val layoutExpected = getMockPdpLayout().run {
            copy(
                layoutData = layoutData.copy(
                    cacheState = CacheState(remoteCacheableActive = true, isFromCache = true)
                )
            )
        }
        val p2Expected = getMockP2Data()

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns flowOf(Result.success(layoutExpected))

        coEvery {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                captureLambda()
            )
        } returns p2Expected

        viewModel.getProductP1(ProductParams(), userLocationLocal = getUserLocationCache())

        assertTrue(viewModel.topAdsImageView.value == null)

        coVerify(inverse = true) { topAdsImageViewUseCase.getImageData(any()) }
        coVerify(inverse = true) { getTopadsIsAdsUseCase.executeOnBackground() }
    }

    @Test
    fun `p2 get cloud when cache is false`() = runTest {
        val layoutExpected = getMockPdpLayout().run {
            copy(
                layoutData = layoutData.copy(
                    cacheState = CacheState(remoteCacheableActive = true, isFromCache = false)
                )
            )
        }
        val p2Expected = getMockP2Data()
        val expectedResponse = TopadsIsAdsQuery(
            TopAdsGetDynamicSlottingData(
                productList = listOf(TopAdsGetDynamicSlottingDataProduct(isCharge = true)),
                status = TopadsStatus(error_code = 200, message = "OK")
            )
        )

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns flowOf(Result.success(layoutExpected))

        coEvery {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                captureLambda()
            )
        } returns p2Expected
        coEvery { remoteConfigInstance.getLong(any(), any()) } returns 5000
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns expectedResponse

        viewModel.getProductP1(
            ProductParams(),
            userLocationLocal = getUserLocationCache(),
            urlQuery = "txsc=asdf"
        )

        coVerify { topAdsImageViewUseCase.getImageData(any()) }
        coVerify { getTopadsIsAdsUseCase.executeOnBackground() }
    }

    @Test
    fun `p2 error`() = runTest {
        val layoutExpected = getMockPdpLayout().run {
            copy(
                layoutData = layoutData.copy(
                    cacheState = CacheState(remoteCacheableActive = true, isFromCache = true)
                )
            )
        }

        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns flowOf(Result.success(layoutExpected))

        coEvery {
            getP2DataAndMiniCartUseCase.executeOnBackground(
                any(),
                any(),
                any(),
                any(),
                any(),
                captureLambda()
            )
        } throws Throwable()

        viewModel.getProductP1(ProductParams(), userLocationLocal = getUserLocationCache())

        assertTrue(viewModel.productLayout.value is Fail)
    }

    @Test
    fun `prefetch data success load`() {
        val refreshPage = false
        val image = "qwerty"
        val name = "name"
        val price = 10000.0
        val slashPrice = ""
        val discount = 0
        val freeShippingLogo = "asdf"
        val rating = "5.0"
        val integrity = "10 Terjual"
        val prefetchData = ProductDetailPrefetch.Data(
            image = image,
            name = name,
            price = price,
            slashPrice = slashPrice,
            discount = discount,
            freeShippingLogo = freeShippingLogo,
            rating = rating,
            integrity = integrity
        )

        viewModel.getProductP1(
            productParams = ProductParams(),
            userLocationLocal = getUserLocationCache(),
            refreshPage = refreshPage,
            prefetchData = prefetchData
        )

        assertTrue(viewModel.productLayout.value is Success)

        val data = (viewModel.productLayout.value as Success).data
        assertTrue(data.size == 3)

        assertTrue(data[0] is ProductMediaDataModel)
        val media = data[0] as ProductMediaDataModel
        assertTrue(media.name == "product_media")
        assertTrue(media.type == "product_media")

        assertTrue(data[1] is ProductContentDataModel)
        val content = data[1] as ProductContentDataModel
        assertTrue(content.name == "product_content")
        assertTrue(content.type == "product_content")

        assertTrue(data[2] is ProductMiniSocialProofDataModel)
        val social = data[2] as ProductMiniSocialProofDataModel
        assertTrue(social.name == "social_proof_mini")
        assertTrue(social.type == "social_proof_mini")
    }

    @Test
    fun `prefetch data should not be load when refresh page`() {
        val refreshPage = true

        val prefetchData = ProductDetailPrefetch.Data(
            image = "",
            name = "",
            price = 0.0,
            slashPrice = "",
            discount = 0,
            freeShippingLogo = "",
            rating = "",
            integrity = ""
        )

        viewModel.getProductP1(
            productParams = ProductParams(),
            userLocationLocal = getUserLocationCache(),
            refreshPage = refreshPage,
            prefetchData = prefetchData
        )

        assertTrue(viewModel.productLayout.value == null)
    }

    //region atc animation
    @Test
    fun `success atc and animation not finish`() = runTest {
        val addToCartRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")
        val result = mutableListOf<com.tokopedia.usecase.coroutines.Result<AddToCartDataModel>>()
        backgroundScope.launch { viewModel.addToCartResultState.toList(result) }

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        assertTrue(result.isEmpty())
    }
    //endregion

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_DOMAIN = "shopDomain"
        const val PARAM_PRODUCT_KEY = "productKey"
        const val PARAM_USER_LOCATION = "userLocation"
        const val PARAM_EXT_PARAM = "extParam"
        const val PARAM_TOKONOW = "tokonow"
    }
}
