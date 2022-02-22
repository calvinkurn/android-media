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
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
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
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.util.ProductDetailTestUtil
import com.tokopedia.product.util.ProductDetailTestUtil.generateMiniCartMock
import com.tokopedia.product.util.ProductDetailTestUtil.generateNotifyMeMock
import com.tokopedia.product.util.ProductDetailTestUtil.getMockP2Data
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
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
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.invoke
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyString
import rx.Observable

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
        val data = MiniCartSimplifiedData(miniCartItems = listOf(MiniCartItem(productId = "123", quantity = 2)))
        val shopIdSlot = slot<List<String>>()

        `on success get product info login`()

        coEvery {
            miniCartListSimplifiedUseCase.executeOnBackground()
        } returns data

        viewModel.getMiniCart("312")

        verify {
            miniCartListSimplifiedUseCase.setParams(capture(shopIdSlot))
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

        //after delete cart success, assert p2 minicart is deleted
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

        //after delete cart fail, assert p2 minicart still exist
        Assert.assertNotNull(viewModel.p2Data.value?.miniCart?.get("518076293"))
    }

    @Test
    fun `on success update cart tokonow with minicart data`() = runBlockingTest {
        `on success get product info login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem(productId = "518076293", quantity = 10)
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
        //fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem(productId = "518076293", quantity = 10)
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
        //fulfil empty data minicart
        `on success get product info non login`()
        val mockData = UpdateCartV2Data(error = listOf("error gan"), data = Data(message = "sukses update cart"))
        val currentMiniCartMock = MiniCartItem(productId = "518076293", quantity = 10)
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
        //fulfil empty data minicart
        `on success get product info non login`()
        val currentMiniCartMock = MiniCartItem(productId = "518076293", quantity = 10)
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
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, productId = 1234L, cartId = "111", quantity = 4), status = "OK")

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

        //assert minicart update
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
    fun onSuccessLoadRecommendationWithEmptyFilter() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val listOfFilter = listOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
        val pageName = "pdp3"

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOfFilter

        (1..2).forEach { _ ->
            viewModel.loadRecommendation(pageName)
        }

        coVerify(exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertTrue((viewModel.loadTopAdsProduct.value as Success).data.tid == recomWidget.tid)
    }

    @Test
    fun onSuccessLoadRecommendationWithNonEmptyFilter() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val listOfFilter = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip())
        val pageName = "pdp3"
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOfFilter

        viewModel.loadRecommendation(pageName)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        Assert.assertEquals((viewModel.loadTopAdsProduct.value as Success).data.tid, recomWidget.tid)
    }

    @Test
    fun onErrorLoadRecommendation() {
        val pageName = "pdp3"
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking()
        } throws Throwable()

        coEvery {
            getRecommendationFilterChips.executeOnBackground().filterChip
        } returns listOf()

        viewModel.loadRecommendation(pageName)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        Assert.assertTrue(viewModel.loadTopAdsProduct.value is Fail)
    }

    @Test
    fun `success get recommendation with exist list`() {
        val recomWidget = RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        val listOfRecom = arrayListOf(recomWidget)
        val recomDataModel = ProductRecommendationDataModel(filterData = listOf(AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(isActivated = true)
        )))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.getRecommendation(recomDataModel, AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertTrue(viewModel.filterTopAdsProduct.value?.isRecomenDataEmpty == false)
        Assert.assertTrue((viewModel.statusFilterTopAdsProduct.value as Success).data)
    }

    @Test
    fun `success get recommendation with empty list`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns emptyList()

        viewModel.getRecommendation(ProductRecommendationDataModel(), AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertFalse((viewModel.statusFilterTopAdsProduct.value as Success).data)
    }

    @Test
    fun `error get recommendation`() {
        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } throws Throwable()

        viewModel.getRecommendation(ProductRecommendationDataModel(), AnnotationChip(), 1, 1)

        coVerify {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        }

        Assert.assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        Assert.assertTrue(viewModel.statusFilterTopAdsProduct.value is Fail)
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


    //======================================PDP SECTION=============================================//
    //==============================================================================================//
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
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName
                ?: "", productParams.warehouseId ?: "", "", userLocation, "",
                tokoNow)

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
        val tokoNow = TokoNowParam("456","789", "now15")

        `co every p1 success`(dataP1)
        coEvery {
            getPdpLayoutUseCase.requestParams
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
                ?: "", productParams.shopDomain ?: "", productParams.productName
                ?: "", productParams.warehouseId ?: "", "", userLocation, "",
                tokoNow)

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
        } returns GetPdpLayoutUseCase.createParams(productParams.productId
            ?: "", productParams.shopDomain ?: "", productParams.productName
            ?: "", productParams.warehouseId ?: "", "", userLocation, extParam.encodeToUtf8(),
            tokoNow)

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
        Assert.assertTrue(viewModel.p2Data.value?.miniCart?.any {
            it.key == "518076293"
        } ?: false)
        Assert.assertNotNull(viewModel.p2Other.value)
        Assert.assertNotNull(viewModel.p2Login.value)
        Assert.assertTrue(viewModel.topAdsImageView.value is Success)

        val p1Result = (viewModel.productLayout.value as Success).data
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.REPORT } == 1)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 1)
    }

    private fun `co verify p1 success`() {
        //P1
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
        //P1
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

        //P1
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
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.TRADE_IN } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VALUE_PROP } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.VARIANT_OPTIONS } == 0)
        Assert.assertTrue(p1Result.count { it.name() == ProductDetailConstant.SHIPMENT } == 1)
    }

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
    fun `variant clicked not partial`() {
        val partialySelect = false
        val imageVariant = "image"
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value == null)
    }

    @Test
    fun `variant clicked partialy with image not blank`() {
        val partialySelect = true
        val imageVariant = "image"
        viewModel.listOfParentMedia = mutableListOf(Media(uRLOriginal = "gambar 1"))
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second?.first()?.uRLOriginal == imageVariant)
    }

    @Test
    fun `variant clicked partialy with blank image`() {
        val partialySelect = true
        val imageVariant = "gambar gan"
        viewModel.listOfParentMedia = null
        viewModel.onVariantClicked(ProductVariant(), mutableMapOf(), partialySelect, anyInt(), imageVariant)
        Assert.assertTrue(viewModel.onVariantClickedData.value == null)
        Assert.assertTrue(viewModel.updatedImageVariant.value != null)
        Assert.assertTrue(viewModel.updatedImageVariant.value?.second?.isEmpty() == true)
    }

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
     * Add/Remove Wishlist
     */
    @Test
    fun onSuccessAddWishlist() {
        val productId = "123"
        every { (addWishListUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun onErrorAddWishlist() {
        val productId = ""
        val errorMessage = ""
        every {
            (addWishListUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(errorMessage, productId)
        }

        viewModel.addWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
    }

    @Test
    fun onSuccessRemoveWishlist() {
        val productId = "123"
        every { (removeWishlistUseCase.createObservable(any(), any(), any())) }.answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessRemoveWishlist(productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, productId)
        })
    }

    @Test
    fun onErrorRemoveWishlist() {
        val productId = ""
        val errorMessage = ""
        every {
            (removeWishlistUseCase.createObservable(any(), any(), any()))
        }.answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorRemoveWishlist(errorMessage, productId)
        }

        viewModel.removeWishList(productId, null, {
            Assert.assertEquals(it, errorMessage)
        })
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

    //getProductTopadsStatus
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
                ))
        coEvery { getTopadsIsAdsUseCase.executeOnBackground() } returns expectedResponse

        viewModel.getProductTopadsStatus(productId, paramsTest)
        coVerify { getTopadsIsAdsUseCase.executeOnBackground() }

        Assert.assertTrue(expectedResponse.data.status.error_code in 200..300 && expectedResponse.data.productList[0].isCharge)
    }


    /**
     * tokonow recom section
     */
    @Test
    fun `test add to cart non variant then return success cart data`() = runBlockingTest {
        val recomItem = RecommendationItem(productId = 12345, shopId = 123)
        val quantity = 1
        val atcResponseSuccess = AddToCartDataModel(data = DataModel(success = 1, cartId = "12345"), status = "OK")
        coEvery {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.atcRecomNonVariant(recomItem, quantity)
        coVerify {
            addToCartUseCase.createObservable(any()).toBlocking().single()
        }
        Assert.assertTrue(!atcResponseSuccess.isStatusError())
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val miniCart = MiniCartItem(productId = recomItem.productId.toString(), quantity = 10)
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
        val expectedUiModel = PlayWidgetUiModel.Medium(
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
        } returns expectedUiModel

        viewModel.getPlayWidgetData()

        viewModel.playWidgetModel.verifySuccessEquals(Success(expectedUiModel))
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

        val fakeUiModel = PlayWidgetUiModel.Medium(
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
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = true

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeUiModel, fakeChannelId, fakeReminderType
            )
        } returns fakeUiModel

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        viewModel.updatePlayWidgetToggleReminder(
            fakeUiModel,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeUiModel))
        viewModel.playWidgetReminderSwitch.verifySuccessEquals(Success(fakeReminderType))
    }

    @Test
    fun `play widget toggle reminder fail cause by map reminder return false`() {

        val fakeUiModel = PlayWidgetUiModel.Medium(
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
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = false

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeUiModel, fakeChannelId, fakeReminderType
            )
        } returns fakeUiModel

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeUiModel, fakeChannelId, fakeReminderType.switch()
            )
        } returns fakeUiModel

        viewModel.updatePlayWidgetToggleReminder(
            fakeUiModel,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeUiModel))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `play widget toggle reminder fail cause by exception`() {

        val fakeUiModel = PlayWidgetUiModel.Medium(
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
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeUiModel, fakeChannelId, fakeReminderType
            )
        } returns fakeUiModel

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } throws expectedThrowable

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeUiModel, fakeChannelId, fakeReminderType.switch()
            )
        } returns fakeUiModel

        viewModel.updatePlayWidgetToggleReminder(
            fakeUiModel,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeUiModel))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(expectedThrowable))
    }


    //======================================END OF PDP SECTION=======================================//
    //==============================================================================================//

    @Test
    fun flush() {
        viewModel.flush()

        verify {
            getPdpLayoutUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2LoginUseCase.cancelJobs()
        }

        verify {
            getProductInfoP2OtherUseCase.cancelJobs()
        }

        verify {
            toggleFavoriteUseCase.cancelJobs()
        }

        verify {
            trackAffiliateUseCase.cancelJobs()
        }

        verify {
            getTopadsIsAdsUseCase.cancelJobs()
        }

        verify {
            getRecommendationUseCase.unsubscribe()
        }

        verify {
            removeWishlistUseCase.unsubscribe()
        }
        verify {
            deleteCartUseCase.cancelJobs()
        }
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