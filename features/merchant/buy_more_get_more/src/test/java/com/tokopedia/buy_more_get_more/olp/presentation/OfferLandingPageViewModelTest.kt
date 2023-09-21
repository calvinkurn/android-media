package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.ShopData
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpEvent
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.OlpUiState
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.SharingDataByOfferIdUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.enum.Status
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetOfferProductListUseCase
import com.tokopedia.buy_more_get_more.olp.domain.usecase.GetSharingDataByOfferIDUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var offeringInfoObserver: Observer<in OfferInfoForBuyerUiModel>

    @RelaxedMockK
    lateinit var productListObserver: Observer<in OfferProductListUiModel>

    @RelaxedMockK
    lateinit var sharingDataObserver: Observer<in Result<SharingDataByOfferIdUiModel>>

    @RelaxedMockK
    lateinit var notificationObserver: Observer<in TopNavNotificationModel>

    @RelaxedMockK
    lateinit var cartObserver: Observer<in Result<AddToCartDataModel>>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

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
            addToCartUseCase,
            userSession
        )
        with(viewModel) {
            offeringInfo.observeForever(offeringInfoObserver)
            productList.observeForever(productListObserver)
            sharingData.observeForever(sharingDataObserver)
            navNotificationLiveData.observeForever(notificationObserver)
            miniCartAdd.observeForever(cartObserver)
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
            val expected = getDummyOfferingInfoResponse()
            mockGetOfferingInfoForBuyerGqlCall()

            // When
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

    private fun getDummyOfferingInfoResponse(): OfferInfoForBuyerUiModel =
        OfferInfoForBuyerUiModel(
            responseHeader = OfferInfoForBuyerUiModel.ResponseHeader(
                success = true,
                status = Status.SUCCESS
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
