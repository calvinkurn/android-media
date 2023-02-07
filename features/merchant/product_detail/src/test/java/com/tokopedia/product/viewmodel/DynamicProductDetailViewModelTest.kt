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
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirProduct
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.Stock
import com.tokopedia.product.detail.common.data.model.product.VariantBasic
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.util.ProductDetailTestUtil.generateMiniCartMock
import com.tokopedia.product.util.ProductDetailTestUtil.generateNotifyMeMock
import com.tokopedia.product.util.ProductDetailTestUtil.getMockP2Data
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.FollowShop
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingData
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.topads.sdk.domain.model.TopadsStatus
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyString
import rx.Observable
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
open class DynamicProductDetailViewModelTest : BasePdpViewModelTest() {

    //region variable
    @Test
    fun `get mini cart and return value`() {
        `on success get product info login`()

        val selectedMiniCart = viewModel.getMiniCartItem()

        Assert.assertTrue(selectedMiniCart != null)
        Assert.assertEquals(selectedMiniCart?.productId ?: "", "518076293")
    }

    @Test
    fun `get mini cart and return null`() {
        `on success get product info non login`()

        val selectedMiniCart = viewModel.getMiniCartItem()

        Assert.assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `on success get user location cache`() {
        viewModel.getProductP1(ProductParams(), userLocationLocal = LocalCacheModel("123"))

        val data = viewModel.getUserLocationCache()
        Assert.assertTrue(data.address_id == "123")
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
        Assert.assertTrue(viewModel.videoTrackerData?.first == 10L)
        Assert.assertTrue(viewModel.videoTrackerData?.second == 120L)
    }

    @Test
    fun `update variable p1 with null`() {
        viewModel.updateDynamicProductInfoData(null)
        Assert.assertNull(viewModel.getDynamicProductInfoP1)
    }

    @Test
    fun `on success update talk action`() {
        viewModel.updateLastAction(DynamicProductDetailTalkGoToWriteDiscussion)
        Assert.assertTrue(viewModel.talkLastAction is DynamicProductDetailTalkGoToWriteDiscussion)
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

        Assert.assertTrue(hasShopAuthority)
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

        Assert.assertTrue(hasShopAuthority)
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

        Assert.assertTrue(hasShopAuthority)
    }

    @Test
    fun `is shop owner true`() {
        val shopId = "123"
        val getDynamicProductInfo = DynamicProductInfoP1(BasicInfo(shopID = shopId))
        viewModel.getDynamicProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns shopId

        every {
            viewModel.isUserSessionActive
        } returns true

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertTrue(isShopOwner)
        viewModel.getDynamicProductInfoP1 = null
    }

    @Test
    fun `is shop owner false`() {
        val anotherShopId = "312"
        val getDynamicProductInfo = DynamicProductInfoP1(BasicInfo(shopID = "123"))
        viewModel.getDynamicProductInfoP1 = getDynamicProductInfo

        every {
            userSessionInterface.shopId
        } returns anotherShopId

        every {
            viewModel.isUserSessionActive
        } returns false

        val isShopOwner = viewModel.isShopOwner()

        Assert.assertFalse(isShopOwner)
        viewModel.getDynamicProductInfoP1 = null
    }
    //endregion

    //region getShopInfo
    @Test
    fun `get shop info from P2 when data null`() {
        every {
            spykViewModel.p2Data.value?.shopInfo
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
        val data = MiniCartSimplifiedData(miniCartItems = mapOf(MiniCartItemKey("123") to MiniCartItem.MiniCartItemProduct(productId = "123", quantity = 2)))
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

        Assert.assertEquals(viewModel.miniCartData.value, true)
        Assert.assertEquals(shopIdSlot.captured.firstOrNull() ?: "", "312")

        val p2MiniCart = viewModel.p2Data.value?.miniCart
        Assert.assertNotNull(p2MiniCart)
        Assert.assertTrue(p2MiniCart?.isNotEmpty() == true)
        Assert.assertTrue(p2MiniCart?.get("123") != null)
        Assert.assertEquals(p2MiniCart?.get("123")?.productId ?: "", "123")
        Assert.assertEquals(p2MiniCart?.get("123")?.quantity ?: "", 2)
    }
    //endregion

    //region getCartTypeByProductId
    @Test
    fun `get cart type by product id when data not null`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(basic = BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value?.cartRedirection
        } returns mapOf("123" to CartTypeData())

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNotNull(cartRedirection)
    }

    @Test
    fun `get cart type by product id when data null`() {
        spykViewModel.getDynamicProductInfoP1 = null
        every {
            spykViewModel.p2Data.value?.cartRedirection
        } returns mapOf("321" to CartTypeData())

        val cartRedirection = spykViewModel.getCartTypeByProductId()

        Assert.assertNull(cartRedirection)
    }

    @Test
    fun `on success update variable p1`() {
        viewModel.updateDynamicProductInfoData(DynamicProductInfoP1())

        Assert.assertNotNull(viewModel.getDynamicProductInfoP1)
    }
    //endregion

    //region getMultiOriginByProductId
    @Test
    fun `get multi origin but p1 data is null`() {
        spykViewModel.getDynamicProductInfoP1 = null
        val data = viewModel.getMultiOriginByProductId()
        Assert.assertEquals(data.id, "")
    }

    @Test
    fun `get multi origin but p1 data not null`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1()
        val data = spykViewModel.getMultiOriginByProductId()
        Assert.assertNotNull(data.id)
    }
    //endregion

    //region getP2RatesEstimateByProductId
    @Test
    fun `get rates with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("123"))))

        val data = spykViewModel.getP2RatesEstimateByProductId()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get rates with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("321"))))

        val data = spykViewModel.getP2RatesEstimateByProductId()
        Assert.assertNull(data)
    }
    //endregion

    //region getP2RatesBottomSheetData
    @Test
    fun `get bottom sheet rates error with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("123"))))

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNotNull(data)
    }

    @Test
    fun `get bottom sheet rates error with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        every {
            spykViewModel.p2Data.value
        } returns ProductInfoP2UiData(ratesEstimate = listOfNotNull(P2RatesEstimate(listfProductId = listOf("321"))))

        val data = spykViewModel.getP2RatesBottomSheetData()
        Assert.assertNull(data)
    }
    //endregion

    //region getBebasOngkirDataByProductId
    @Test
    fun `get bebas ongkir data with success result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(boProduct = listOf(BebasOngkirProduct(boType, productId = "123")), boImages = listOf(BebasOngkirImage(boType, imageUrl)))

        val data = spykViewModel.getBebasOngkirDataByProductId()
        Assert.assertTrue(data.imageURL == "gambar boe gan")
        Assert.assertTrue(data.boType == 1)
    }

    @Test
    fun `get bebas ongkir data with null result`() {
        spykViewModel.getDynamicProductInfoP1 = DynamicProductInfoP1(BasicInfo(productID = "123"))
        val imageUrl = "gambar boe gan"
        val boType = 1
        every {
            spykViewModel.p2Data.value?.bebasOngkir
        } returns BebasOngkir(boProduct = listOf(BebasOngkirProduct(boType, productId = "312")), boImages = listOf(BebasOngkirImage(boType, imageUrl)))

        val data = spykViewModel.getBebasOngkirDataByProductId()
        Assert.assertTrue(data.imageURL == "")
        Assert.assertTrue(data.boType == 0)
    }
    //endregion

    //region atc
    @Test
    fun `on success delete cart tokonow non var`() = runBlockingTest {
        `on success get product info login`()
        val mockData = RemoveFromCartData(data = com.tokopedia.cartcommon.data.response.deletecart.Data(message = listOf("sukses delete cart")))
        val cartId = slot<List<String>>()

        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.deleteProductInCart("518076293")

        coVerify { deleteCartUseCase.executeOnBackground() }
        verify { deleteCartUseCase.setParams(capture(cartId)) }

        val cartIdDeleted = cartId.captured.firstOrNull()
        Assert.assertNotNull(cartIdDeleted)
        Assert.assertEquals(cartIdDeleted, "111")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        Assert.assertTrue(viewModel.deleteCartLiveData.value is Success)
        Assert.assertNotNull((viewModel.deleteCartLiveData.value as Success).data, "sukses delete cart")

        // after delete cart success, assert p2 minicart is deleted
        Assert.assertNull(viewModel.p2Data.value?.miniCart?.get("518076293"))
    }

    @Test
    fun `on error delete cart tokonow non var`() = runBlockingTest {
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
        Assert.assertEquals(cartIdDeleted, "111")

        Assert.assertNotNull(viewModel.deleteCartLiveData.value)
        Assert.assertTrue(viewModel.deleteCartLiveData.value is Fail)
        Assert.assertNotNull((viewModel.deleteCartLiveData.value as Fail).throwable.message, "sukses delete cart")

        // after delete cart fail, assert p2 minicart still exist
        Assert.assertNotNull(viewModel.p2Data.value?.miniCart?.get("518076293"))
    }

    @Test
    fun `on success update cart tokonow with minicart data`() = runBlockingTest {
        `on success get product info login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = (viewModel.updateCartLiveData.getOrAwaitValue() as Success).data
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        Assert.assertEquals(result, "sukses update cart")
        Assert.assertTrue(viewModel.p2Data.value != null)
        Assert.assertTrue(selectedMiniCart != null)
        Assert.assertEquals(selectedMiniCart?.quantity, updatedQuantity)
    }

    @Test
    fun `on success update cart tokonow with empty minicart data`() = runBlockingTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = (viewModel.updateCartLiveData.getOrAwaitValue() as Success).data
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        Assert.assertEquals(result, "sukses update cart")
        Assert.assertTrue(viewModel.p2Data.value != null)
        Assert.assertTrue(selectedMiniCart != null)
        Assert.assertEquals(selectedMiniCart?.quantity, updatedQuantity)
    }

    @Test
    fun `on fail update cart tokonow`() = runBlockingTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(error = listOf("error gan"), data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns mockData

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = viewModel.updateCartLiveData.getOrAwaitValue()
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        Assert.assertTrue(result is Fail)
        Assert.assertTrue(viewModel.p2Data.value != null)
        Assert.assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `on fail update cart throwable tokonow`() = runBlockingTest {
        // fulfil empty data minicart
        `on success get product info non login`()
        val currentMiniCartMock = MiniCartItem.MiniCartItemProduct(productId = "518076293", quantity = 10)
        val updatedQuantity = 5

        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateQuantity(updatedQuantity, currentMiniCartMock)

        val result = viewModel.updateCartLiveData.getOrAwaitValue()
        val selectedMiniCart = viewModel.p2Data.value?.miniCart?.get("518076293")

        Assert.assertTrue(result is Fail)
        Assert.assertTrue(viewModel.p2Data.value != null)
        Assert.assertTrue(selectedMiniCart == null)
    }

    @Test
    fun `on success normal atc tokonow`() = runBlockingTest {
        `on success get product info login`()
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = "1234", cartId = "111", quantity = 4), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)

        // assert minicart update
        val p2MiniCart = viewModel.p2Data?.value?.miniCart
        Assert.assertNotNull(p2MiniCart)
        Assert.assertEquals(p2MiniCart?.size ?: 0, 2)
        Assert.assertNotNull(p2MiniCart?.get("1234"))
        Assert.assertEquals(p2MiniCart?.get("1234")?.quantity ?: 0, 4)
        Assert.assertEquals(p2MiniCart?.get("1234")?.cartId ?: "", "111")
    }

    @Test
    fun `on success normal atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error normal atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }

    @Test
    fun `on error normal atc cause result null`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartRequestParams()

        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns null

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }

    @Test
    fun `on success ocs atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error ocs atc`() = runBlockingTest {
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.addToCart(addToCartOcsRequestParams)

        coVerify {
            addToCartOcsUseCase.createObservable(any()).toBlocking().single()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }

    @Test
    fun `on success occ atc`() = runBlockingTest {
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(carts = listOf(AddToCartOccMultiCartParam("123", "123", "1")))
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1), status = "OK")

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseSuccess

        viewModel.addToCart(addToCartOccRequestParams)

        coVerify {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Success)
    }

    @Test
    fun `on error occ atc`() = runBlockingTest {
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(carts = listOf(AddToCartOccMultiCartParam("123", "123", "1")))
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal ya"))

        coEvery {
            addToCartOccUseCase.setParams(any()).executeOnBackground().mapToAddToCartDataModel()
        } returns atcResponseError

        viewModel.addToCart(addToCartOccRequestParams)

        coVerify {
            addToCartOccUseCase.setParams(any()).executeOnBackground()
        }

        coVerify(inverse = true) {
            addToCartUseCase.createObservable(any()).toBlocking()
        }

        coVerify(inverse = true) {
            addToCartOcsUseCase.createObservable(any()).toBlocking()
        }

        Assert.assertTrue(viewModel.addToCartLiveData.value is Fail)
    }
    //endregion

    //region topads
    /**
     * RecommendationWidget
     */
    @Test
    fun `assert request params tokonow`() {
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns RecommendationWidget()

        viewModel.loadRecommendation(
            "pdp_10",
            "123",
            true,
            mutableMapOf("123" to MiniCartItem.MiniCartItemProduct())
        )

        val requestParamsSlot = slot<RequestParams>()
        coVerify {
            getProductRecommendationUseCase.executeOnBackground(capture(requestParamsSlot))
        }

        val requestParams = requestParamsSlot.captured
        Assert.assertEquals(requestParams.getString("productID", ""), "123")
        Assert.assertEquals(requestParams.getString("pageName", ""), "pdp_10")
        Assert.assertEquals(requestParams.getBoolean("tokonow", false), true)
        val miniCart = requestParams.getObject("minicart") as MutableMap<String, MiniCartItem.MiniCartItemProduct>?
        Assert.assertEquals(miniCart!!.isNotEmpty(), true)
    }

    @Test
    fun `assert request params non tokonow`() {
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns RecommendationWidget()

        viewModel.loadRecommendation(
            "pdp_10",
            "123",
            false,
            null
        )

        val requestParamsSlot = slot<RequestParams>()
        coVerify {
            getProductRecommendationUseCase.executeOnBackground(capture(requestParamsSlot))
        }

        val requestParams = requestParamsSlot.captured
        Assert.assertEquals(requestParams.getString("productID", ""), "123")
        Assert.assertEquals(requestParams.getString("pageName", ""), "pdp_10")
        Assert.assertEquals(requestParams.getBoolean("tokonow", false), false)
        val miniCart = requestParams.getObject("minicart") as MutableMap<MiniCartItemKey, MiniCartItem>?
        Assert.assertNull(miniCart)
    }

    @Test
    fun `success load recommendation without filter`() {
        val mockRecomm = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )
        val pageName = "pdp3"

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns mockRecomm

        viewModel.loadRecommendation(
            pageName,
            "123",
            false,
            mutableMapOf()
        )

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }

        Assert.assertTrue((viewModel.loadTopAdsProduct.value as Success).data.tid == "1")
    }

    @Test
    fun `success load recommendation with filter`() {
        val mockFilter = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip())
        val mockRecomm = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem()),
            recommendationFilterChips = mockFilter
        )
        val pageName = "pdp3"

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns mockRecomm

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf())

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }

        Assert.assertEquals((viewModel.loadTopAdsProduct.value as Success).data.tid, "1")
        Assert.assertEquals(
            (viewModel.loadTopAdsProduct.value as Success)
                .data
                .recommendationFilterChips
                .isNotEmpty(),
            true
        )
    }

    @Test
    fun `error load recommendation`() {
        val pageName = "pdp3"
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf())

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
        Assert.assertTrue(viewModel.loadTopAdsProduct.value is Fail)
        Assert.assertTrue((viewModel.loadTopAdsProduct.value as Fail).throwable.message == pageName)
    }

    @Test
    fun `success load recommendation chip clicked`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        val mockSelectedChip = AnnotationChip(
            RecommendationFilterChipsEntity.RecommendationFilterChip(
                name = "katun chip",
                isActivated = true,
                value = "queryparambro"
            )
        )

        val initialAnnotationChip = listOf(
            AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(
                    name = "katun chip",
                    isActivated = false
                )
            ),
            AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(
                    name = "kulit chip",
                    isActivated = false
                )
            )
        )

        val recomDataModel = ProductRecommendationDataModel(
            filterData = initialAnnotationChip,
            recomWidgetData = RecommendationWidget(
                pageName = "pdp_11"
            )
        )

        viewModel.recommendationChipClicked(recomDataModel, mockSelectedChip, "123")

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        // assert request params
        val reqParams = slotRequestParams.captured
        Assert.assertEquals(reqParams.pageNumber, 1)
        Assert.assertEquals(reqParams.pageName, "pdp_11")
        Assert.assertEquals(reqParams.queryParam, "queryparambro")
        Assert.assertEquals(reqParams.productIds, listOf("123"))

        val filterData = viewModel.filterTopAdsProduct.value
        Assert.assertNotNull(filterData)

        Assert.assertEquals(filterData!!.isRecomenDataEmpty, false)
        Assert.assertEquals(filterData.filterData!!.isNotEmpty(), true)

        val selectedChip = filterData.filterData!!.first {
            it.recommendationFilterChip.isActivated
        }.recommendationFilterChip.name

        val otherChipUnselected = filterData.filterData!!.any {
            it.recommendationFilterChip.name == "kulit chip" && !it.recommendationFilterChip.isActivated
        }

        Assert.assertEquals(selectedChip, "katun chip")
        Assert.assertEquals(otherChipUnselected, true)
        Assert.assertNull(viewModel.statusFilterTopAdsProduct.value)
    }

    @Test
    fun `success load recommendation chip clicked return empty list`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        viewModel.recommendationChipClicked(ProductRecommendationDataModel(), AnnotationChip(), "")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertNull(viewModel.statusFilterTopAdsProduct.value)
    }

    @Test
    fun `error load recommendation chip clicked`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Throwable()

        viewModel.recommendationChipClicked(ProductRecommendationDataModel(), AnnotationChip(), "")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertTrue(viewModel.statusFilterTopAdsProduct.value is Fail)
    }

    @Test
    fun `load recommendation already hitted`() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns recomWidget

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())
        Thread.sleep(500)
        // hit again with same page name
        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())

        // make sure it will only called once
        coVerify(exactly = 1) {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
    }

    @Test
    fun `load recommendation sellerapp`() {
        every {
            GlobalConfig.isSellerApp()
        } returns true

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())

        coVerify(inverse = true) {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
    }

    @Test
    fun `success load view to view recommendation`() {
        val recomWidget = RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        Assert.assertTrue(viewModel.loadViewToView.value is Success)
    }

    @Test
    fun `fail load view to view recommendation when recommendation widget is empty`() {
        val response = listOf<RecommendationWidget>()

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        Assert.assertTrue(viewModel.loadViewToView.value is Fail)
    }

    @Test
    fun `fail load view to view recommendation on exception`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Exception()

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        Assert.assertTrue(viewModel.loadViewToView.value is Fail)
    }

    @Test
    fun `load view to view recommendation already hitted`() {
        val recomWidget = listOf(RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem())))

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns recomWidget

        viewModel.loadViewToView("view_to_view", "", false)
        Thread.sleep(500)
        //hit again with same page name
        viewModel.loadViewToView("view_to_view", "", false)

        //make sure it will only called once
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }

    //endregion

    //region ticker p2
    @Test
    fun `test ticker oos when get ticker data by product id`() {
        `on success get product info login`()
        val productWithTicker = viewModel.p2Data.value?.getTickerByProductId("518076293")

        Assert.assertEquals(productWithTicker?.first()?.message, "Untuk sementara barang ini tidak dijual. Kamu bisa wishlist barang ini atau Cari Barang Serupa.")
        Assert.assertEquals(productWithTicker?.first()?.title, "barang tidak tersedia")
        Assert.assertEquals(productWithTicker?.first()?.actionLink, "https://www.tokopedia.com/rekomendasi/2086995432?ref=recom_oos")
        Assert.assertEquals(productWithTicker?.first()?.action, "applink")
    }

    @Test
    fun `test ticker general multiple when get ticker data by product id`() {
        `on success get product info login`()
        val productWithTicker = viewModel.p2Data.value?.getTickerByProductId("518076286")

        Assert.assertEquals(productWithTicker?.size, 2)

        Assert.assertEquals(productWithTicker?.first()?.message, "ticker 1 message")
        Assert.assertEquals(productWithTicker?.get(1)?.message, "ticker 2 message")
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
    /**
     * GetProductInfoP1
     */
    @Test
    fun `test correct product id parameter pdplayout`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productId = "123"
        val productParams = ProductParams(productId, "", "", "", "", "")
        val userLocation = UserLocationRequest("123")
        val tokoNow = TokoNowParam("456", "789", "now15")
        `co every p1 success`(dataP1)

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
            tokoNow
        )

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "") == productId)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "").isEmpty())
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest) != null)

        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.shopId == "456")
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.warehouseId == "789")
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.serviceType == "now15")
    }

    @Test
    fun `test correct shop domain and shop key parameter pdplayout`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val shopDomain = "shopYehez"
        val productKey = "productYehez"
        val productParams = ProductParams("", shopDomain, productKey, "", "", "")
        val userLocation = UserLocationRequest("123")
        val tokoNow = TokoNowParam("456", "789", "now15")

        `co every p1 success`(dataP1)
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
            tokoNow
        )

        viewModel.getProductP1(productParams, true, " ", userLocationLocal = getUserLocationCache())

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_ID, "").isEmpty())
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_PRODUCT_KEY, "") == productKey)
        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_SHOP_DOMAIN, "") == shopDomain)
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_USER_LOCATION) as? UserLocationRequest)?.districtID == "123")

        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.shopId == "456")
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.warehouseId == "789")
        Assert.assertTrue((getPdpLayoutUseCase.requestParams.getObject(PARAM_TOKONOW) as? TokoNowParam)?.serviceType == "now15")
    }

    @Test
    fun `test extParam key parameter pdplayout`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")
        val userLocation = UserLocationRequest("")
        val extParam = anyString()
        val tokoNow = TokoNowParam("123")

        `co every p1 success`(dataP1)
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
            tokoNow
        )

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache(), extParam = extParam)

        Assert.assertTrue(getPdpLayoutUseCase.requestParams.getString(PARAM_EXT_PARAM, "") == extParam.encodeToUtf8())
    }

    @Test
    fun `on success get product info login`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
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

        `co every p1 success`(dataP1, true)

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())

        `co verify p1 success`()

        Assert.assertTrue(viewModel.productLayout.value is Success)
        Assert.assertNotNull(viewModel.p2Data.value)
        Assert.assertTrue(
            viewModel.p2Data.value?.miniCart?.any {
                it.key == "518076293"
            } ?: false
        )
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNotNull(viewModel.p2Login.value)
        Assert.assertTrue(viewModel.topAdsImageView.value is Success)

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO })
        // update: palugada unused component wholesales_info
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO })
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.REPORT } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.AR_BUTTON } == 1)
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
            getP2DataAndMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any())
        }

        coVerify {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun `co every p1 success`(dataP1: ProductDetailDataModel, hitMiniCart: Boolean = false) {
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } returns dataP1

        coEvery {
            getProductInfoP2LoginUseCase.executeOnBackground()
        } returns ProductInfoP2Login()

        coEvery {
            getP2DataAndMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), captureLambda())
        }.coAnswers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(Throwable(""))
            val p2Mock = getMockP2Data()
            p2Mock.miniCart = if (!hitMiniCart) mutableMapOf() else generateMiniCartMock(dataP1.layoutData.basic.productID).toMutableMap()
            p2Mock.upcomingCampaigns = generateNotifyMeMock()
            p2Mock
        }

        coEvery {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        } returns ProductInfoP2Other()

        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns arrayListOf(TopAdsImageViewModel())
    }

    @Test
    fun `on error get product info login`() {
        val productParams = ProductParams("", "", "", "", "", "")
        coEvery {
            getPdpLayoutUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())
        // P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.productLayout.value is Fail)
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
    fun `on success get product info non login`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
        val productParams = ProductParams("", "", "", "", "", "")

        every {
            userSessionInterface.isLoggedIn
        } returns false

        every {
            viewModel.isUserSessionActive
        } returns false

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, userLocationLocal = getUserLocationCache())

        // P1
        coVerify {
            getPdpLayoutUseCase.executeOnBackground()
        }

        coVerify {
            getProductInfoP2OtherUseCase.executeOnBackground(any(), any())
        }

        coVerify {
            getP2DataAndMiniCartUseCase.executeOnBackground(any(), any(), any(), any(), any(), any())
        }

        coVerify(inverse = true) {
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.productLayout.value is Success)
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

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, refreshPage = true, userLocationLocal = getUserLocationCache())

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.TRADE_IN })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.VALUE_PROP })
        // remove unused palugada
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PRODUCT_FULLFILMENT })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.ORDER_PRIORITY })

        Assert.assertTrue(p1Result.any { it.name() == ProductDetailConstant.MEDIA })
        Assert.assertTrue(p1Result.any { it.name() == ProductDetailConstant.TICKER_INFO })
        Assert.assertTrue(p1Result.any { it.name() == ProductDetailConstant.PRODUCT_CONTENT })
        Assert.assertTrue(p1Result.any { it.name() == ProductDetailConstant.PRODUCT_PROTECTION })
        Assert.assertTrue(p1Result.any { it.name() == ProductDetailConstant.SHIPMENT })
    }

    @Test
    fun `on success remove unused component when sellerapp`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
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

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, refreshPage = true, userLocationLocal = getUserLocationCache())

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.none { it.type() == ProductDetailConstant.PRODUCT_LIST })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.PLAY_CAROUSEL })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.REPORT })
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.AR_BUTTON })
    }

    @Test
    fun `remove button_ar when customerapp and os under lollipop`() {
        val dataP1 = ProductDetailTestUtil.getMockPdpLayout()
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

        `co every p1 success`(dataP1)

        viewModel.getProductP1(productParams, refreshPage = true, userLocationLocal = getUserLocationCache())
        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.none { it.name() == ProductDetailConstant.AR_BUTTON })
    }

    // region variant
    /**
     *  Variant Section
     */
    @Test
    fun `process initial variant`() {
        viewModel.processVariant(ProductVariant(), mutableMapOf(), false)

        Assert.assertTrue(viewModel.initialVariantData.value != null)
    }

    @Test
    fun `process initial variant tokonow`() {
        val variantData = ProductDetailTestUtil.getMockVariant()
        viewModel.processVariant(variantData, mutableMapOf(), true)

        Assert.assertTrue(viewModel.initialVariantData.value == null)
        Assert.assertTrue(viewModel.singleVariantData.value != null)
    }

    @Test
    fun `process initial variant with empty child tokonow`() {
        viewModel.processVariant(ProductVariant(), mutableMapOf(), true)

        Assert.assertTrue(viewModel.initialVariantData.value == null)
        Assert.assertTrue(viewModel.singleVariantData.value == null)
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

        viewModel.processVariant(productVariant, mapOfSelectedOptionIds, true)
        Assert.assertTrue(viewModel.initialVariantData.value == null)
        Assert.assertTrue(viewModel.singleVariantData.value == expectedVariantCategory)
    }

    @Test
    fun `determine variant return null`() {
        val productVariant = ProductVariant()
        val mapOfSelectedOptionIds = mutableMapOf<String, String>()

        mockkObject(ProductDetailVariantLogic)

        every {
            ProductDetailVariantLogic.determineVariant(mapOfSelectedOptionIds, productVariant)
        } returns null

        viewModel.processVariant(productVariant, mapOfSelectedOptionIds, true)
        Assert.assertTrue(viewModel.initialVariantData.value == null)
        Assert.assertTrue(viewModel.singleVariantData.value == null)
    }

    @Test
    fun `variant clicked not partial`() {
        val partialySelect = false
        val imageVariant = "image"
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value == null)
    }

    @Test
    fun `variant clicked partialy will post clicked variant option id`() {
        val partialySelect = true
        val variantOptionId = "1234567890"
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), variantOptionId)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second == variantOptionId)
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
        Assert.assertTrue(childVariant == null)
    }

    @Test
    fun `child is null when get child of variant selected with single and options variant is empty`() {
        `on success get pdp layout mini variants options`()
        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = ProductSingleVariantDataModel()
        )
        Assert.assertTrue(childVariant == null)
    }

    private fun `child options id is available when get child of variant selected`(
        singleVariant: ProductSingleVariantDataModel?,
    ) {
        // given pdp layout data
        `on success get pdp layout mini variants options`()

        val p1Result = (viewModel.productLayout.value as Success).data
        // then, get child of variant selected
        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = singleVariant
        )

        // assert the expectation
        Assert.assertTrue(
            p1Result.any {
                it.type() == ProductDetailConstant.PRODUCT_VARIANT_INFO &&
                    it.name() == ProductDetailConstant.MINI_VARIANT_OPTIONS
            }
        )
        Assert.assertTrue(childVariant?.optionIds?.firstOrNull() != null)
    }

    @Test
    fun `child is null when get child of variant selected with variant data is null and single variant only`() {
        val singleVariant = ProductSingleVariantDataModel()
        `on success get pdp layout mini variants options`()

        viewModel.variantData = null

        val childVariant = viewModel.getChildOfVariantSelected(
            singleVariant = singleVariant
        )

        Assert.assertTrue(viewModel.variantData == null)
        Assert.assertTrue(childVariant == null)
    }

    @Test
    fun `select variant thumbnail is successful when first open pdp`() {
        val expectLvl1Category = "28323838"
        val expectLvl1Selected = "94748050"
        val expectLvl2Category = "28323839"
        val expectLvl2Selected = "94748052"
        val singleVariant = ProductSingleVariantDataModel()

        `on success get pdp layout mini variants options`()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectLvl1Selected,
            categoryKey = expectLvl1Category
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[expectLvl1Category]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[expectLvl2Category]
        Assert.assertTrue(variantLvl1Selected == expectLvl1Selected)
        Assert.assertTrue(variantLvl2Selected == expectLvl2Selected)
    }

    @Test
    fun `change select variant thumbnail is successful when previously selected`() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                expectCategory to "94748049",
                "28323839" to "94748052"
            )
        )

        `on success get pdp layout mini variants options`()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLevelOneSelected = result?.mapOfSelectedVariant.orEmpty()[expectCategory]
        Assert.assertTrue(variantLevelOneSelected == expectSelected)
    }

    @Test
    fun `thumbnail variant selected is success when variant have lvl 1 only`() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                expectCategory to "94748049",
                "28323839" to "94748052"
            )
        )

        `on success get pdp layout mini variants options`()
        // give variant one only to variant data
        val variantLvl1 = viewModel.variantData?.variants?.firstOrNull() ?: Variant()
        viewModel.variantData = viewModel.variantData?.copy(
            variants = listOf(variantLvl1)
        )
        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLevelOneSelected = result?.mapOfSelectedVariant.orEmpty()[expectCategory]
        Assert.assertTrue(variantLevelOneSelected == expectSelected)
    }

    @Test
    fun `variant lvl 2 is empty on thumbnail variant selected when variant lvl 2 options is empty`() {
        val variantLvl1 = "94748050"
        val categoryLvl1 = "28323838"
        val categoryLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryLvl1 to "94748049"
            )
        )

        `on success get pdp layout mini variants options`()

        // give variant two with variant options is empty
        val variants = viewModel.variantData?.variants.orEmpty().toMutableList()
            .map {
                if (it.pv == categoryLvl2) {
                    it.copy(options = emptyList())
                } else {
                    it
                }
            }
        viewModel.variantData = viewModel.variantData?.copy(
            variants = variants
        )
        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = variantLvl1,
            categoryKey = categoryLvl1
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryLvl2]
        Assert.assertTrue(variantLvl2Selected?.isEmpty() == true)
    }

    @Test
    fun `don't select variant thumbnail when select variant with variant id is empty`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )

        `on success get pdp layout mini variants options`()

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl1]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl2]
        Assert.assertTrue(variantLvl1Selected.orEmpty().isEmpty())
        Assert.assertTrue(variantLvl2Selected.orEmpty().isNotEmpty())
    }

    @Test
    fun `thumbnail variant not changes when variants is empty`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )

        `on success get pdp layout mini variants options`()

        // give empty variant data to be expected
        viewModel.variantData = viewModel.variantData?.copy(variants = listOf(Variant(pv = null)))

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
    }

    @Test
    fun `thumbnail variant change is success when variant lv2 have pv is null`() {
        val selectVariant1 = "94748050"
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049"
            )
        )

        `on success get pdp layout mini variants options`()

        // give variant two with pv is null
        val variants = viewModel.variantData?.variants.orEmpty().toMutableList()
            .map {
                if (it.pv == categoryKeyLvl2) {
                    it.copy(pv = null)
                } else {
                    it
                }
            }
        viewModel.variantData = viewModel.variantData?.copy(
            variants = variants
        )

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = selectVariant1,
            categoryKey = categoryKeyLvl1
        )

        val result = viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        val variantLvl1Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl1]
        val variantLvl2Selected = result?.mapOfSelectedVariant.orEmpty()[categoryKeyLvl2]
        val variantLvl2Actual = result?.mapOfSelectedVariant.orEmpty()[""]
        Assert.assertTrue(variantLvl1Selected == selectVariant1)
        Assert.assertTrue(variantLvl2Selected == null)
        Assert.assertFalse(variantLvl2Actual.isNullOrEmpty())
    }

    @Test
    fun `no impact when select variant thumbnail with variant data is null`() {
        val categoryKeyLvl1 = "28323838"
        val categoryKeyLvl2 = "28323839"
        val singleVariant = ProductSingleVariantDataModel(
            mapOfSelectedVariant = mutableMapOf(
                categoryKeyLvl1 to "94748049",
                categoryKeyLvl2 to "94748052"
            )
        )

        `on success get pdp layout mini variants options`()

        // give empty variant data to be expected
        viewModel.variantData = null

        viewModel.onThumbnailVariantSelected(
            uiData = singleVariant,
            variantId = "",
            categoryKey = ""
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
    }

    @Test
    fun `no impact when select variant thumbnail with ui-data is null `() {
        val expectSelected = "94748050"
        val expectCategory = "28323838"
        `on success get pdp layout mini variants options`()

        viewModel.onThumbnailVariantSelected(
            uiData = null,
            variantId = expectSelected,
            categoryKey = expectCategory
        )

        try {
            viewModel.onThumbnailVariantSelectedData.getOrAwaitValue()
        } catch (e: Throwable) {
            Assert.assertTrue(e is TimeoutException)
        }
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

        `co every p1 success`(dataP1, true)

        viewModel.getProductP1(productParams, true, "", userLocationLocal = getUserLocationCache())

        `co verify p1 success`()
    }

    //endregion variant
    /**
     * Hit Affiliate, most of it do no op call
     */
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

    //region affiliate cookie
    @Test
    fun `integration test affiliate cookie sdk`() {
        val productId = "321"
        val shopId = "123"
        val categoryId = "123"
        val isVariant = true
        val stock = 10

        val mockProductInfoP1 = DynamicProductInfoP1(
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

        val slot = slot<AffiliatePageDetail>()

        viewModel.hitAffiliateCookie(
            productInfo = mockProductInfoP1,
            affiliateUuid = affiliateUUID,
            uuid = uuid,
            affiliateChannel = affiliateChannel
        )

        coVerify {
            affiliateCookieHelper.initCookie(
                affiliateUUID,
                affiliateChannel,
                capture(slot),
                uuid
            )
        }

        with(slot.captured) {
            Assert.assertEquals(productId, this.pageId)
            Assert.assertTrue(source is AffiliateSdkPageSource.PDP)
        }
    }

    @Test
    fun `when init affiliate cookie throw, no op`() {
        val productId = "321"
        val shopId = "123"
        val categoryId = "123"
        val isVariant = true
        val stock = 10

        val mockProductInfoP1 = DynamicProductInfoP1(
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
            affiliateChannel = affiliateChannel
        )

        coVerify {
            affiliateCookieHelper.initCookie(
                affiliateUUID,
                affiliateChannel,
                any(),
                uuid
            )
        }
        // no op, expect to be handled by Affiliate SDK
    }
    //endregion

    /**
     * UpdateCartCounter
     */
    @Test
    fun onSuccessUpdateCartCounter() {
        val data = 10
        every {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        } returns Observable.just(data)

        viewModel.updateCartCounerUseCase {
            Assert.assertEquals(it, data)
        }

        verify {
            updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
        }
    }

    /**
     * ToggleFavorite
     */
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

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, false)
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

        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.first, true)
        Assert.assertEquals((viewModel.toggleFavoriteResult.value as Success).data.second, isNpl)
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

        Assert.assertTrue(viewModel.toggleFavoriteResult.value is Fail)
    }

    /**
     * Discussion Most Helpful
     */
    @Test
    fun `on success getDiscussionMostHelpful`() = runBlockingTest {
        val expectedResponse = DiscussionMostHelpfulResponseWrapper()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } returns expectedResponse

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        Assert.assertEquals(expectedResponse, (viewModel.discussionMostHelpful.value as Success).data)
    }

    @Test
    fun `on error getDiscussionMostHelpful`() = runBlockingTest {
        val expectedError = Throwable()

        coEvery {
            discussionMostHelpfulUseCase.executeOnBackground()
        } throws expectedError

        viewModel.getDiscussionMostHelpful("", "")
        coVerify { discussionMostHelpfulUseCase.executeOnBackground() }

        Assert.assertTrue(viewModel.discussionMostHelpful.value is Fail)
    }

    // getProductTopadsStatus
    @Test
    fun `when get topads status then verify error response`() = runBlockingTest {
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
        verify(exactly = 1) {
            ProductDetailServerLogger.logBreadCrumbTopAdsIsAds(
                isSuccess = capture(isSuccess),
                errorMessage = capture(errorMessage)
            )
        }

        Assert.assertTrue(viewModel.topAdsRecomChargeData.value is Fail)
        Assert.assertEquals(isSuccess.captured, false)
        Assert.assertEquals(errorMessage.captured, "error")

        Assert.assertEquals(
            (viewModel.topAdsRecomChargeData.value as Fail).throwable.message,
            "error"
        )
    }

    @Test
    fun `when get topads status then verify success response and enable to charge`() = runBlockingTest {
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
        coVerify(exactly = 1) {
            ProductDetailServerLogger.logBreadCrumbTopAdsIsAds(
                isSuccess = capture(isSuccess),
                errorCode = capture(errorCode),
                isTopAds = capture(isTopAds)
            )
        }

        Assert.assertTrue(expectedResponse.data.status.error_code in 200..300 && expectedResponse.data.productList[0].isCharge)
        Assert.assertEquals(isSuccess.captured, true)
        Assert.assertEquals(errorCode.captured, 200)
        Assert.assertEquals(isTopAds.captured, true)
    }

    /**
     * tokonow recom section
     */
    @Test
    fun `test add to cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, cartId = "12345", message = arrayListOf("halo")), status = "OK")
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        Assert.assertTrue(!atcResponseSuccess.isStatusError())
        Assert.assertTrue(viewModel.atcRecomTokonowSendTracker.value is Success)
        Assert.assertEquals(Success(recomItem), viewModel.atcRecomTokonowSendTracker.value)
    }

    @Test
    fun `test add to cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseError = AddToCartDataModel(data = DataModel(success = 0), status = "", errorMessage = arrayListOf("gagal maning euy"))
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test add to cart non variant then return failed with throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } throws Throwable()

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test update cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        val response = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }
        coVerify {
            miniCartListSimplifiedUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Success)
    }

    @Test
    fun `test update cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        val response = UpdateCartV2Data(error = listOf("error nih gan"))
        coEvery {
            updateCartUseCase.executeOnBackground()
        } returns response

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test update cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val quantity = 11
        coEvery {
            updateCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.updateRecomCartNonVariant(recomItem, quantity, miniCart)
        coVerify {
            updateCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val response = RemoveFromCartData(status = "OK", data = com.tokopedia.cartcommon.data.response.deletecart.Data(message = listOf("sukses delete cart"), success = 1))
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

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Success)
    }

    @Test
    fun `test delete cart non variant then return failed with message`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        val response = RemoveFromCartData(status = "ERROR", data = com.tokopedia.cartcommon.data.response.deletecart.Data(success = 0))
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } returns response

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `test delete cart non variant then return error throwable`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = recomItem.productId.toString(), quantity = 10)
        coEvery {
            deleteCartUseCase.executeOnBackground()
        } throws Throwable()

        viewModel.deleteRecomItemFromCart(recomItem, miniCart)
        coVerify {
            deleteCartUseCase.executeOnBackground()
        }

        Assert.assertTrue(viewModel.atcRecomTokonow.value is Fail)
    }

    @Test
    fun `get play widget data success`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())

        val expectedResponse = PlayWidget()
        val expectedState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )

        coEvery {
            playWidgetTools.getWidgetFromNetwork(
                widgetType = widgetType
            )
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(
                expectedResponse
            )
        } returns expectedState

        viewModel.getPlayWidgetData()

        viewModel.playWidgetModel.verifySuccessEquals(Success(expectedState))
    }

    @Test
    fun `get play widget data error cause by get widget from network`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())

        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.getWidgetFromNetwork(widgetType)
        } throws expectedThrowable

        viewModel.getPlayWidgetData()

        viewModel.playWidgetModel.verifyErrorEquals(Fail(expectedThrowable))
    }

    @Test
    fun `get play widget data error cause by map widget to model`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())

        val expectedResponse = PlayWidget()
        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.getWidgetFromNetwork(
                widgetType = widgetType
            )
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(
                expectedResponse
            )
        } throws expectedThrowable

        viewModel.getPlayWidgetData()

        viewModel.playWidgetModel.verifyErrorEquals(Fail(expectedThrowable))
    }

    @Test
    fun `play widget toggle reminder success`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = true

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifySuccessEquals(Success(fakeReminderType))
    }

    @Test
    fun `play widget toggle reminder fail cause by map reminder return false`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = false

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType.switch()
            )
        } returns fakeState

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `play widget toggle reminder fail cause by exception`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } throws expectedThrowable

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType.switch()
            )
        } returns fakeState

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(expectedThrowable))
    }

    @Test
    fun `verify add to wishlistv2 returns success`() {
        val productId = "123"
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify add to wishlistv2 returns fail`() {
        val productId = "123"
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishListV2(productId, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns success`() {
        val productId = "123"
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`() {
        val productId = "123"
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishListV2(productId, mockListener)

        verify { deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify vertical recommendation when return empty list, will be fail`() {
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        viewModel.getVerticalRecommendationData(
            pageName = pageName,
            productId = productId
        )

        Assert.assertTrue(viewModel.verticalRecommendation.value is Fail)
    }

    @Test
    fun `verify vertical recommendation throw error, will be fail`() {
        val pageNumber = 1
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Throwable()

        viewModel.getVerticalRecommendationData(pageName, pageNumber, productId)

        Assert.assertTrue(viewModel.verticalRecommendation.value is Fail)
    }

    @Test
    fun `verify success get vertical recommendation data`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )

        val pageNumber = 1
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        viewModel.getVerticalRecommendationData(pageName, pageNumber, productId)

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        val captured = slotRequestParams.captured
        Assert.assertEquals(pageName, captured.pageName)
        Assert.assertEquals(pageNumber, captured.pageNumber)
        Assert.assertEquals(listOf(productId), captured.productIds)

        Assert.assertTrue(viewModel.verticalRecommendation.value is Success)
    }

    @Test
    fun `verify success get vertical recommendation data with null productId and pageNumber`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )

        val pageName = "pdp_8_vertical"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        viewModel.getVerticalRecommendationData(
            pageName = pageName,
            productId = null,
            page = null
        )

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        val captured = slotRequestParams.captured
        Assert.assertEquals(pageName, captured.pageName)
        Assert.assertEquals(1, captured.pageNumber)
        Assert.assertEquals(listOf(""), captured.productIds)

        Assert.assertTrue(viewModel.verticalRecommendation.value is Success)
    }

    //region product ar
    @Test
    fun `verify product ar data`() {
        `on success get product info login`()

        val p2Ar = viewModel.p2Data.value?.arInfo
        Assert.assertNotNull(p2Ar)
        p2Ar?.let {
            Assert.assertEquals(it.isProductIdContainsAr("518076293"), true)
            Assert.assertEquals(it.isProductIdContainsAr("518076286"), false)
            Assert.assertEquals(it.isProductIdContainsAr("948021897"), false)
            Assert.assertEquals(it.isProductIdContainsAr("518076287"), false)
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

        val actualVideoCount = viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
            it is ReviewMediaVideoThumbnailUiModel
        }
        val actualImageCount = viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
            it is ReviewMediaImageThumbnailUiModel
        }
        val showingSeeMoreThumbnail = viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.count {
            it is ReviewMediaImageThumbnailUiModel && it.isShowingSeeMore()
        }
        val seeMoreThumbnailPosition = viewModel.p2Data.value?.imageReview?.reviewMediaThumbnails?.mediaThumbnails?.indexOfFirst {
            it is ReviewMediaImageThumbnailUiModel && it.isShowingSeeMore()
        }
        val showingSeeMoreThumbnailOnLastThumbnailOnly = showingSeeMoreThumbnail == 1 && seeMoreThumbnailPosition == 4
        val actualBuyerMediaCount = viewModel.p2Data.value?.imageReview?.buyerMediaCount
        val actualSocialProofText = viewModel.p2Data.value?.imageReview?.staticSocialProofText

        Assert.assertEquals("Invalid video count.", expectedVideoCount, actualVideoCount)
        Assert.assertEquals("Invalid image count.", expectedImageCount, actualImageCount)
        Assert.assertTrue("Should show see more thumbnail on last position but was not show", showingSeeMoreThumbnailOnLastThumbnailOnly)
        Assert.assertEquals("Invalid buyer media count.", expectedBuyerMediaCount, actualBuyerMediaCount)
        Assert.assertEquals("Invalid social proof text", expectedSocialProofText, actualSocialProofText)
    }
    // endregion Review Section

    @Test
    fun `atc recom non variant quantity changed with quantity changed from 0 to 1`() {
        val recommItem = RecommendationItem()
        val quantity = 1

        every {
            userSessionInterface.isLoggedIn
        } returns true

        viewModel.onAtcRecomNonVariantQuantityChanged(recommItem, quantity)

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

        spykViewModel.onAtcRecomNonVariantQuantityChanged(recommItem, quantity)

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

        spykViewModel.onAtcRecomNonVariantQuantityChanged(recommItem, quantity)

        coVerify { deleteCartUseCase.executeOnBackground() }
    }

    @Test
    fun `atc recom non variant quantity changed not logged in`() {
        val recommItem = RecommendationItem()
        val quantity = 1

        every {
            userSessionInterface.isLoggedIn
        } returns false

        viewModel.onAtcRecomNonVariantQuantityChanged(recommItem, quantity)

        Assert.assertEquals(recommItem, viewModel.atcRecomTokonowNonLogin.value)
    }

    private fun getUserLocationCache(): LocalCacheModel {
        return LocalCacheModel("123", "123", "123", "123")
    }

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_SHOP_DOMAIN = "shopDomain"
        const val PARAM_PRODUCT_KEY = "productKey"
        const val PARAM_USER_LOCATION = "userLocation"
        const val PARAM_EXT_PARAM = "extParam"
        const val PARAM_TOKONOW = "tokonow"
    }
}
