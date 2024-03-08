package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.campaign.data.response.OfferInfoForBuyerResponse
import com.tokopedia.campaign.data.response.OfferProductListResponse
import com.tokopedia.campaign.usecase.GetOfferInfoForBuyerUseCase
import com.tokopedia.campaign.usecase.GetOfferProductListUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingInfoForBuyerMapper
import com.tokopedia.shop_widget.buy_more_save_more.data.mapper.GetOfferingProductListMapper
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.BmsmWidgetUiState
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BmsmWidgetTabViewModelTest {

    private lateinit var viewModel: BmsmWidgetTabViewModel

    @RelaxedMockK
    lateinit var getOfferInfoForBuyerUseCase: GetOfferInfoForBuyerUseCase

    @RelaxedMockK
    lateinit var getOfferProductListUseCase: GetOfferProductListUseCase

    @RelaxedMockK
    lateinit var getOfferingInfoForBuyerMapper: GetOfferingInfoForBuyerMapper

    @RelaxedMockK
    lateinit var getOfferingProductListMapper: GetOfferingProductListMapper

    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var getMiniCartDataUseCase: Lazy<GetMiniCartWidgetUseCase>

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var productListObserver: Observer<in List<Product>>

    @RelaxedMockK
    lateinit var addToCartObserver: Observer<in Result<AddToCartDataModel>>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @RelaxedMockK
    lateinit var context: Context

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = BmsmWidgetTabViewModel(
            CoroutineTestDispatchers,
            getOfferInfoForBuyerUseCase,
            getOfferProductListUseCase,
            getOfferingInfoForBuyerMapper,
            getOfferingProductListMapper,
            addToCartUseCase,
            getMiniCartDataUseCase,
            userSession
        )
        with(viewModel) {
            productList.observeForever(productListObserver)
            miniCartAdd.observeForever(addToCartObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            productList.removeObserver(productListObserver)
            miniCartAdd.removeObserver(addToCartObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `when setInitialValue is called, should set uiState data accordingly`() {
        runBlockingTest {
            // Given
            val offerIds = listOf(2178L)
            val shopId = 6555135L
            val defaultOfferingData = OfferingInfoByShopIdUiModel()
            val localCacheModel = LocalCacheModel()
            val expectedUiState = BmsmWidgetUiState(
                offerIds = offerIds,
                shopId = shopId,
                offeringInfo = defaultOfferingData.toOfferingInfoForBuyerUiModel(),
                localCacheModel = localCacheModel
            )

            val emittedValue = arrayListOf<BmsmWidgetUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.setInitialUiState(
                offerIds = offerIds,
                shopId = shopId,
                defaultOfferingData = defaultOfferingData,
                localCacheModel = localCacheModel
            )

            // Then
            val actual = emittedValue.last()
            assertEquals(expectedUiState, actual)

            job.cancel()
        }
    }

    @Test
    fun `when defaultOfferingData is available, should populate productListData accordingly`() {
        runBlockingTest {
            // Given
            val offerIds = listOf(2178L)
            val shopId = 6555135L
            val defaultOfferingData = OfferingInfoByShopIdUiModel(
                products = listOf(
                    com.tokopedia.shop_widget.buy_more_save_more.entity.Product(
                        name = "sampleProduct"
                    )
                )
            )
            val localCacheModel = LocalCacheModel()
            mockGetOfferingProductListGqlCall()
            val expected = defaultOfferingData.toProductListUiModel()

            val emittedValue = arrayListOf<BmsmWidgetUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.setInitialUiState(
                offerIds = offerIds,
                shopId = shopId,
                defaultOfferingData = defaultOfferingData,
                localCacheModel = localCacheModel
            )

            // Then
            val actual = viewModel.productList.getOrAwaitValue()
            assertEquals(expected, actual)

            job.cancel()
        }
    }

    @Test
    fun `when getOfferingInfoForBuyer is called, should get the data and set uiState value accordingly`() {
        runBlockingTest {
            // Given
            val expected = getOfferingInfoForBuyerMapper.map(getDummyOfferingInfoResponse())
            mockGetOfferingInfoForBuyerGqlCall()
            mockGetOfferingProductListGqlCall()

            val emittedValue = arrayListOf<BmsmWidgetUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.getOfferingData()

            // Then
            val actual = emittedValue.last().offeringInfo
            assertEquals(expected, actual)
            job.cancel()
        }
    }

    @Test
    fun `when getOfferingInfoForBuyer is returning error, should handle the error properly`() {
        runBlockingTest {
            // Given
            val expectedErrorMsg = "Server Error"
            mockErrorGetOfferingInfoForBuyerGqlCall()

            // When
            viewModel.getOfferingData()

            // Then
            val actualErrorMsg = viewModel.error.getOrAwaitValue().localizedMessage
            assertEquals(expectedErrorMsg, actualErrorMsg)
        }
    }

    @Test
    fun `when getOfferingProductList is returning error, should handle the error properly`() {
        runBlockingTest {
            // Given
            val expectedErrorMsg = "Server Error"
            mockErrorGetOfferingProductListGqlCall()

            // When
            viewModel.getOfferingData()

            // Then
            val actualErrorMsg = viewModel.error.getOrAwaitValue().localizedMessage
            assertEquals(expectedErrorMsg, actualErrorMsg)
        }
    }

    @Test
    fun `when getMinicartV3 is called, should set uiState value accordingly`() {
        runBlockingTest {
            // Given
            val expected = MiniCartSimplifiedData()
            mockGetMinicartV3GqlCall()

            val emittedValue = arrayListOf<BmsmWidgetUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValue)
            }

            // When
            viewModel.getMinicartV3()

            // Then
            val actual = emittedValue.last().miniCartData
            assertEquals(expected, actual)
            job.cancel()
        }
    }

    @Test
    fun `when addToCart is called, should hit the addToCartGql properly`() {
        runBlockingTest {
            // Given
            val expected = Success(AddToCartDataModel())
            val dummyProduct = Product(
                name = "dummy product",
                minOrder = 2
            )
            mockAddToCartGqlCall()

            // When
            viewModel.addToCart(dummyProduct)

            // Then
            val actual = viewModel.miniCartAdd.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when addToCart is called and product is not having minOrder, should hit the addToCartGql properly`() {
        runBlockingTest {
            // Given
            val expected = Success(AddToCartDataModel())
            val dummyProduct = Product(
                name = "dummy product",
                minOrder = 0
            )
            mockAddToCartGqlCall()

            // When
            viewModel.addToCart(dummyProduct)

            // Then
            val actual = viewModel.miniCartAdd.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when addToCart is return error, should get the error data properly`() {
        runBlockingTest {
            // Given
            val dummyProduct = Product(
                name = "dummy product",
                minOrder = 2
            )
            mockErrorAddToCartGqlCall()

            // When
            viewModel.addToCart(dummyProduct)

            // Then
            val actual = viewModel.miniCartAdd.getOrAwaitValue()
            assert(actual is Fail)
        }
    }

    private fun mockGetOfferingInfoForBuyerGqlCall() {
        val offeringInfoResponse = getDummyOfferingInfoResponse()
        coEvery { getOfferInfoForBuyerUseCase.execute(any()) } returns offeringInfoResponse
    }

    private fun mockGetOfferingProductListGqlCall() {
        val offeringProductListResponse = OfferProductListResponse()
        coEvery { getOfferProductListUseCase.execute(any()) } returns offeringProductListResponse
    }

    private fun mockGetMinicartV3GqlCall() {
        val miniCartResponse = MiniCartData()
        coEvery { getMiniCartDataUseCase.get().invoke(any()) } returns miniCartResponse
    }

    private fun mockAddToCartGqlCall() {
        val addToCartResponse = AddToCartDataModel()
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartResponse
    }

    private fun mockErrorGetOfferingInfoForBuyerGqlCall() {
        val error = Fail(MessageErrorException("Server Error"))
        coEvery { getOfferInfoForBuyerUseCase.execute(any()) } throws error.throwable
    }

    private fun mockErrorGetOfferingProductListGqlCall() {
        val error = MessageErrorException("Server Error")
        coEvery { getOfferProductListUseCase.execute(any()) } throws error
    }

    private fun mockErrorAddToCartGqlCall() {
        val error = MessageErrorException("Server Error")
        coEvery { addToCartUseCase.executeOnBackground() } throws error
    }

    private fun getDummyOfferingInfoResponse(): OfferInfoForBuyerResponse =
        OfferInfoForBuyerResponse(
            offeringInforBuyer = OfferInfoForBuyerResponse.OfferInfoForBuyer(
                responseHeader = OfferInfoForBuyerResponse.ResponseHeader(
                    success = true
                ),
                offerings = listOf(
                    OfferInfoForBuyerResponse.Offering(
                        id = 0,
                        offerName = "BMSM",
                        startDate = "",
                        endDate = ""
                    )
                )
            )
        )
}
