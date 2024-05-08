package com.tokopedia.buy_more_get_more.olp.presentation.olp

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartUseCase
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.olp.data.mapper.GetOfferInfoForBuyerMapper
import com.tokopedia.buy_more_get_more.olp.data.mapper.GetOfferProductListMapper
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.ShopData
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.Tier
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpEvent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpUiState
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.SelectedTierData
import com.tokopedia.buy_more_get_more.olp.domain.entity.SharingDataByOfferIdUiModel
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetSharingDataByOfferIDUseCase
import com.tokopedia.buy_more_get_more.olp.presentation.OfferLandingPageViewModel
import com.tokopedia.buy_more_get_more.olp.utils.BmgmUtil
import com.tokopedia.campaign.data.response.OfferInfoForBuyerResponse
import com.tokopedia.campaign.data.response.OfferInfoForBuyerResponse.Offering
import com.tokopedia.campaign.data.response.OfferProductListResponse
import com.tokopedia.campaign.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.campaign.usecase.GetOfferProductListUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class OfferLandingPageViewModelTest {

    private lateinit var viewModel: OfferLandingPageViewModel

    @RelaxedMockK
    lateinit var getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase

    @RelaxedMockK
    lateinit var getOfferProductListUseCase: GetOfferProductListUseCase

    @RelaxedMockK
    lateinit var getSharingDataByOfferIDUseCase: GetSharingDataByOfferIDUseCase

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getOfferingInfoForBuyerMapper: GetOfferInfoForBuyerMapper

    @RelaxedMockK
    lateinit var getOfferProductListMapper: GetOfferProductListMapper

    @RelaxedMockK
    lateinit var offeringInfoObserver: Observer<in Result<OfferInfoForBuyerUiModel>>

    @RelaxedMockK
    lateinit var productListObserver: Observer<in OfferProductListUiModel>

    @RelaxedMockK
    lateinit var sharingDataObserver: Observer<in Result<SharingDataByOfferIdUiModel>>

    @RelaxedMockK
    lateinit var notificationObserver: Observer<in TopNavNotificationModel>

    @RelaxedMockK
    lateinit var cartObserver: Observer<in Result<AddToCartDataModel>>

    @RelaxedMockK
    lateinit var tierGiftObserver: Observer<in Result<SelectedTierData>>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @RelaxedMockK
    lateinit var context: Context

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = OfferLandingPageViewModel(
            CoroutineTestDispatchers,
            getOfferInfoForBuyerUseCase,
            getOfferProductListUseCase,
            getSharingDataByOfferIDUseCase,
            getNotificationUseCase,
            getMiniCartUseCase,
            addToCartUseCase,
            userSession,
            getOfferingInfoForBuyerMapper,
            getOfferProductListMapper
        )
        with(viewModel) {
            offeringInfo.observeForever(offeringInfoObserver)
            productList.observeForever(productListObserver)
            sharingData.observeForever(sharingDataObserver)
            navNotificationLiveData.observeForever(notificationObserver)
            miniCartAdd.observeForever(cartObserver)
            tierGifts.observeForever(tierGiftObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            offeringInfo.removeObserver(offeringInfoObserver)
            productList.removeObserver(productListObserver)
            sharingData.removeObserver(sharingDataObserver)
            navNotificationLiveData.removeObserver(notificationObserver)
            miniCartAdd.removeObserver(cartObserver)
            tierGifts.removeObserver(tierGiftObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `when setInitialUiState is called, should set uiState data accordingly`() {
        runBlockingTest {
            // Given
            val offerIds = listOf(120L)
            val shopId = 6551456L
            val productIds = listOf(28192L)
            val warehouseIds = listOf(20192L)
            val localCacheModel = getUserLocationCache()
            val expectedUiState = OlpUiState(
                offerIds = offerIds,
                shopData = ShopData(
                    shopId = shopId
                ),
                productIds = productIds,
                warehouseIds = warehouseIds,
                localCacheModel = localCacheModel
            )

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                OlpEvent.SetInitialUiState(
                    offerIds = offerIds,
                    shopIds = shopId,
                    productIds = productIds,
                    warehouseIds = warehouseIds,
                    localCacheModel = localCacheModel
                )
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedUiState, actual)

            job.cancel()
        }
    }

    @Test
    fun `when getOfferingInfo is called, should set uiState data accordingly`() {
        runBlockingTest {
            // Given
            val expected =
                Success(getOfferingInfoForBuyerMapper.map(getDummyOfferingInfoResponse()))
            mockGetOfferingInfoForBuyerGqlCall()

            // When
            viewModel.processEvent(OlpEvent.GetOfferingInfo)

            // Then
            val actual = viewModel.offeringInfo.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getOfferingInfo is returning error, should get error message accordingly`() {
        runBlockingTest {
            // Given
            mockErrorGetOfferingInfoForBuyerGqlCall()

            // When
            viewModel.processEvent(OlpEvent.GetOfferingInfo)

            // Then
            val actual = viewModel.offeringInfo.getOrAwaitValue()
            assert(actual is Fail)
        }
    }

    @Test
    fun `when getOfferingProductList is called, should get product list data accordingly`() {
        runBlockingTest {
            // Given
            val expected = getOfferProductListMapper.map(OfferProductListResponse())
            mockGetOfferingProductListGqlCall()

            // When
            viewModel.processEvent(
                OlpEvent.GetOffreringProductList(
                    page = 0,
                    pageSize = 10
                )
            )

            // Then
            val actual = viewModel.productList.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getOfferingProductList is returning error, should get error message accordingly`() {
        runBlockingTest {
            // Given
            val expected = MessageErrorException("Server Error")
            mockErrorGetOfferingProductListGqlCall()

            // When
            viewModel.processEvent(
                OlpEvent.GetOffreringProductList(
                    page = 0,
                    pageSize = 10
                )
            )

            // Then
            val actual = viewModel.error.getOrAwaitValue()
            assertEquals(expected.message, actual.message)
        }
    }

    @Test
    fun `when getNotification is called, should get notification data accordingly`() {
        runBlockingTest {
            // Given
            val expected = TopNavNotificationModel()
            mockGetNotificationGqlCall()

            // When
            viewModel.processEvent(OlpEvent.GetNotification)

            // Then
            val actual = viewModel.navNotificationLiveData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when addToCart is called, should get atc result data accordingly`() {
        runBlockingTest {
            // Given
            val expected = Success(AddToCartDataModel())
            val dummyProduct = OfferProductListUiModel.Product()
            mockAddToCartGqlCall()

            // When
            viewModel.processEvent(OlpEvent.AddToCart(dummyProduct))

            // Then
            val actual = viewModel.miniCartAdd.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when addToCart is returning error, should set atc error result data accordingly`() {
        runBlockingTest {
            // Given
            val error = MessageErrorException("Server Error")
            val expected = Fail(error)
            val dummyProduct = OfferProductListUiModel.Product()
            coEvery { addToCartUseCase.executeOnBackground() } throws error

            // When
            viewModel.processEvent(OlpEvent.AddToCart(dummyProduct))

            // Then
            val actual = viewModel.miniCartAdd.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getSharingData is called, should get sharing data accordingly`() {
        runBlockingTest {
            // Given
            val expected = Success(SharingDataByOfferIdUiModel())
            mockGetSharingDataGqlCall()

            // When
            viewModel.processEvent(OlpEvent.GetSharingData)

            // Then
            val actual = viewModel.sharingData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getSharingData is error, should get error data accordingly`() {
        runBlockingTest {
            // Given
            val error = MessageErrorException("Server Error")
            val expected = Fail(error)
            coEvery { getSharingDataByOfferIDUseCase.execute(any()) } throws error

            // When
            viewModel.processEvent(OlpEvent.GetSharingData)

            // Then
            val actual = viewModel.sharingData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when setSort is called, should set sort data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val sortId = "0"
            val sortName = "default"
            val expected = Pair(sortId, sortName)

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(
                OlpEvent.SetSort(
                    sortId = sortId,
                    sortName = sortName
                )
            )

            // Then
            val uiState = emittedValue.last()
            val actual = Pair(uiState.sortId, uiState.sortName)
            assertEquals(expected, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setWarehouseIds is called, should set warehouseIds data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val warehouseIds = listOf<Long>(0, 1, 2, 3)

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetWarehouseIds(warehouseIds))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.warehouseIds
            assertEquals(warehouseIds, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setShopData is called, should set shopData to uiState accordingly`() {
        runBlockingTest {
            // Given
            val shopData = ShopData(
                shopId = 0,
                shopName = "shop"
            )
            val expected = Pair(shopData.shopId, shopData.shopName)

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetShopData(shopData))

            // Then
            val uiState = emittedValue.last()
            val actual = Pair(uiState.shopData.shopId, uiState.shopData.shopName)
            assertEquals(expected, actual)

            job.cancel()
        }
    }

    @Test
    fun `when handleTapTier is called, should get minicartData and map it to tier gift list accordingly`() {
        runBlockingTest {
            // Given
            val selectedTier = Tier()
            val offerInfo = OfferInfoForBuyerUiModel()
            val tierGifts = listOf(
                TierGifts(
                    gifts = listOf(
                        TierGifts.GiftProduct(
                            productId = 982480,
                            quantity = 100
                        )
                    ),
                    tierId = 0
                )
            )
            val expected = Success(SelectedTierData(selectedTier, offerInfo, tierGifts, listOf()))
            mockGetMiniCartUseCaseGqlCall()

            // When
            viewModel.processEvent(OlpEvent.TapTier(selectedTier, offerInfo))

            // Then
            val actual = viewModel.tierGifts.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when handleTapTier is returning error, should set error data accordingly`() {
        runBlockingTest {
            // Given
            val selectedTier = Tier()
            val offerInfo = OfferInfoForBuyerUiModel()
            mockErrorGetMiniCartUseCaseGqlCall()

            // When
            viewModel.processEvent(OlpEvent.TapTier(selectedTier, offerInfo))

            // Then
            val actual = viewModel.tierGifts.getOrAwaitValue()
            assert(actual is Fail)
        }
    }

    @Test
    fun `when setOfferingJsonData is called, should set setOfferingJsonData to uiState accordingly`() {
        runBlockingTest {
            // Given
            val offeringJsonData = "fejHSfaeuwefb38eg8hvr"

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetOfferingJsonData(offeringJsonData))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.offeringJsonData
            assertEquals(offeringJsonData, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setTncData is called, should set Tnc data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val tnc = listOf(
                "1. first point",
                "2.second point",
                "3.third point"
            )

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetTncData(tnc))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.tnc
            assertEquals(tnc, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setEndDate is called, should set end date data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val endDateInString = "25/09/2023"

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetEndDate(endDateInString))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.endDate
            assertEquals(endDateInString, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setOfferTypeId is called, should set offer type id to uiState accordingly`() {
        runBlockingTest {
            // Given
            val offerTypeId = 0L

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetOfferTypeId(offerTypeId))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.offerTypeId
            assertEquals(offerTypeId, actual)

            job.cancel()
        }
    }

    @Test
    fun `when setSharingData is called, should set sharing data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val sharingData = SharingDataByOfferIdUiModel()

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.processEvent(OlpEvent.SetSharingData(sharingData))

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.sharingData
            assertEquals(sharingData, actual)

            job.cancel()
        }
    }

    @Test
    fun `when addAvailableProductImpression is called, should set impressed product data to uiState accordingly`() {
        runBlockingTest {
            // Given
            val impressedProductData = OfferProductListUiModel.Product()
            val expected = mutableSetOf(impressedProductData)

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.addAvailableProductImpression(impressedProductData)

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.availableProductImpressionList
            assertEquals(expected, actual)

            job.cancel()
        }
    }

    @Test
    fun `when clearAvailableProductImpression is called, should clear impressed product data from uiState accordingly`() {
        runBlockingTest {
            // Given
            val impressedProductData = OfferProductListUiModel.Product()
            val expected = mutableSetOf<OfferProductListUiModel.Product>()

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }
            viewModel.addAvailableProductImpression(impressedProductData)

            // When
            viewModel.clearAvailableProductImpression()

            // Then
            val uiState = emittedValue.last()
            val actual = uiState.availableProductImpressionList
            assertEquals(expected, actual)
            job.cancel()
        }
    }

    @Test
    fun `when getDeeplink is called, should return deeplink based on offering data accordingly`() {
        runBlocking {
            // Given
            val offerIds = listOf(120L)
            val shopId = 6551456L
            val productIds = listOf(28192L)
            val warehouseIds = listOf(20192L)
            val localCacheModel = getUserLocationCache()

            val expected = "tokopedia://buymoresavemore/120" +
                "?warehouse_ids=20192&" +
                "product_ids=28192&" +
                "shop_ids=6551456"

            val emittedValue = arrayListOf<OlpUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            viewModel.processEvent(
                OlpEvent.SetInitialUiState(
                    offerIds = offerIds,
                    shopIds = shopId,
                    productIds = productIds,
                    warehouseIds = warehouseIds,
                    localCacheModel = localCacheModel
                )
            )

            // When
            val actual = viewModel.getDeeplink()

            // Then
            assertEquals(expected, actual)
            job.cancel()
        }
    }

    @Test
    fun `check whether bmsmImagePath value is set when call saveBmsmImageToPhoneStorage`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()
        mockkObject(BmgmUtil)
        every { BmgmUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } answers {
            (lastArg() as MediaBitmapEmptyTarget<Bitmap>).onResourceReady(
                mockBitmap,
                mockTransition
            )
        }

        mockkStatic(ImageProcessingUtil::class)
        coEvery {
            ImageProcessingUtil.writeImageToTkpdPath(
                mockBitmap,
                Bitmap.CompressFormat.PNG
            )
        } returns File("path")
        viewModel.saveBmgmImageToPhoneStorage(context, "")
        assert(viewModel.bmgmImagePath.value.orEmpty().isNotEmpty())
    }

    @Test
    fun `check whether shopImagePath value is null when savedFile is null`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()
        mockkObject(BmgmUtil)
        every { BmgmUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } answers {
            (lastArg() as MediaBitmapEmptyTarget<Bitmap>).onResourceReady(
                mockBitmap,
                mockTransition
            )
        }

        mockkStatic(ImageProcessingUtil::class)
        coEvery {
            ImageProcessingUtil.writeImageToTkpdPath(
                mockBitmap,
                Bitmap.CompressFormat.PNG
            )
        } returns null
        viewModel.saveBmgmImageToPhoneStorage(context, "")
        assert(viewModel.bmgmImagePath.value == null)
    }

    @Test
    fun `given warehouses, when getOfferingInfo is called, should set uiState data accordingly`() {
        runBlocking {
            // Given
            val localCacheModel = LocalCacheModel(
                warehouses = listOf(
                    LocalWarehouseModel(
                        warehouse_id = 12512,
                        service_type = "2h"
                    )
                )
            )
            val expected =
                Success(getOfferingInfoForBuyerMapper.map(getDummyOfferingInfoResponse()))
            mockGetOfferingInfoForBuyerGqlCall()

            // When
            viewModel.processEvent(
                OlpEvent.SetInitialUiState(
                    offerIds = listOf(120L),
                    shopIds = 6551456L,
                    productIds = listOf(28192L),
                    warehouseIds = listOf(20192L),
                    localCacheModel = localCacheModel
                )
            )
            viewModel.processEvent(OlpEvent.GetOfferingInfo)

            // Then
            val actual = viewModel.offeringInfo.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    private fun getDummyUiStateData(): OlpUiState =
        OlpUiState(
            offerIds = listOf(120L),
            shopData = ShopData(
                shopId = 1232323L
            ),
            productIds = listOf(28192L),
            warehouseIds = listOf(20192L),
            localCacheModel = getUserLocationCache()
        )

    private fun mockGetOfferingInfoForBuyerGqlCall() {
        val offeringInfoResponse = getDummyOfferingInfoResponse()
        coEvery { getOfferInfoForBuyerUseCase.execute(any()) } returns offeringInfoResponse
    }

    private fun mockErrorGetOfferingInfoForBuyerGqlCall() {
        val error = Fail(MessageErrorException("Server Error"))
        coEvery { getOfferInfoForBuyerUseCase.execute(any()) } throws error.throwable
    }

    private fun mockGetOfferingProductListGqlCall() {
        val offeringProductListResponse = OfferProductListResponse()
        coEvery { getOfferProductListUseCase.execute(any()) } returns offeringProductListResponse
    }

    private fun mockErrorGetOfferingProductListGqlCall() {
        val error = MessageErrorException("Server Error")
        coEvery { getOfferProductListUseCase.execute(any()) } throws error
    }

    private fun mockGetNotificationGqlCall() {
        val notificationResponse = TopNavNotificationModel()
        coEvery { getNotificationUseCase.executeOnBackground() } returns notificationResponse
    }

    private fun mockAddToCartGqlCall() {
        val successAtcResponse = AddToCartDataModel()
        coEvery { addToCartUseCase.executeOnBackground() } returns successAtcResponse
    }

    private fun mockGetSharingDataGqlCall() {
        val sharingDataResponse = SharingDataByOfferIdUiModel()
        coEvery { getSharingDataByOfferIDUseCase.execute(any()) } returns sharingDataResponse
    }

    private fun mockGetMiniCartUseCaseGqlCall() {
        val miniCartDataResponse = BmgmMiniCartDataUiModel(
            tiers = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    productsBenefit = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "982480",
                            quantity = 100
                        )
                    )
                )
            ),
            isTierAchieved = true
        )
        coEvery { getMiniCartUseCase(any()) } returns miniCartDataResponse
    }

    private fun mockErrorGetMiniCartUseCaseGqlCall() {
        val error = MessageErrorException("Server Error")
        coEvery { getMiniCartUseCase(any()) } throws error
    }

    private fun getDummyOfferingInfoResponse(): OfferInfoForBuyerResponse =
        OfferInfoForBuyerResponse(
            offeringInforBuyer = OfferInfoForBuyerResponse.OfferInfoForBuyer(
                responseHeader = OfferInfoForBuyerResponse.ResponseHeader(
                    success = true
                ),
                offerings = listOf(
                    Offering(
                        id = 0,
                        offerName = "BMSM",
                        startDate = "",
                        endDate = ""
                    )
                )
            )
        )

    private fun getUserLocationCache(): LocalCacheModel {
        return LocalCacheModel(
            address_id = "123",
            city_id = "123",
            district_id = "123",
            lat = "123",
            long = "123",
            postal_code = "123"
        )
    }
}
