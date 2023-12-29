package com.tokopedia.universal_sharing.test

import app.cash.turbine.test
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductWrapperResponse
import com.tokopedia.universal_sharing.domain.mapper.UniversalSharingPostPurchaseMapper
import com.tokopedia.universal_sharing.domain.usecase.UniversalSharingPostPurchaseGetDetailProductUseCase
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import com.tokopedia.universal_sharing.util.Result
import com.tokopedia.universal_sharing.util.stubRepository
import com.tokopedia.universal_sharing.util.stubRepositoryAsThrow
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseAction
import com.tokopedia.universal_sharing.view.UniversalSharingPostPurchaseViewModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class UniversalSharingPostPurchaseTest {

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    private lateinit var repository: GraphqlRepository

    private lateinit var getDetailProductUseCase: UniversalSharingPostPurchaseGetDetailProductUseCase
    private lateinit var mapper: UniversalSharingPostPurchaseMapper
    private lateinit var viewModel: UniversalSharingPostPurchaseViewModel

    private val dummyThrowable = Throwable("Oops!")
    private val dummyOrderId = "dummy order id"
    private val dummyShopName = "dummy shop name"
    private val dummyProductId = "dummy product id"
    private val dummyProductName = "dummy product name"
    private val dummyProduct = UniversalSharingPostPurchaseModel(
        shopList = listOf(
            UniversalSharingPostPurchaseShopModel(
                shopName = dummyShopName,
                productList = listOf(
                    UniversalSharingPostPurchaseProductModel(
                        productName = dummyProductName
                    ),
                    UniversalSharingPostPurchaseProductModel()
                )
            ),
            UniversalSharingPostPurchaseShopModel()
        )
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getDetailProductUseCase = spyk(
            UniversalSharingPostPurchaseGetDetailProductUseCase(
                repository,
                CoroutineTestDispatchersProvider
            )
        )
        mapper = spyk(UniversalSharingPostPurchaseMapper())
        viewModel = UniversalSharingPostPurchaseViewModel(
            getDetailProductUseCase,
            mapper,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `successfully load page, show list of product state`() {
        runTest {
            viewModel.uiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                val initialState = awaitItem()
                assertTrue(initialState is Result.Loading)

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.RefreshData(dummyProduct)
                )

                // Then
                val updatedState = awaitItem()
                assertTrue(updatedState is Result.Success)
                Assert.assertEquals(
                    4, // [shop, product, product, shop]
                    ((updatedState as Result.Success).data as List<Visitable<*>>).size
                )
                Assert.assertEquals(
                    dummyShopName,
                    (
                        (updatedState.data as List<Visitable<*>>).firstOrNull()
                            as UniversalSharingPostPurchaseShopTitleUiModel
                        ).name
                )
                Assert.assertEquals(
                    dummyProductName,
                    (updatedState.data[1] as UniversalSharingPostPurchaseProductUiModel).name
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `load page with empty product, show error state`() {
        runTest {
            // Given
            val dummyProduct = UniversalSharingPostPurchaseModel()

            viewModel.uiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                val initialState = awaitItem()
                assertTrue(initialState is Result.Loading)

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.RefreshData(dummyProduct)
                )
                // Then
                val updatedState = awaitItem()
                assertTrue(updatedState is Result.Error)
                Assert.assertEquals(
                    "Product is empty",
                    (updatedState as Result.Error).throwable.message
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `failed to load page, show error state`() {
        runTest {
            // Given
            every {
                mapper.mapToUiModel(any())
            } throws dummyThrowable

            viewModel.uiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                val initialState = awaitItem()
                assertTrue(initialState is Result.Loading)

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.RefreshData(dummyProduct)
                )
                // Then
                val updatedState = awaitItem()
                assertTrue(updatedState is Result.Error)
                Assert.assertEquals(
                    dummyThrowable.message,
                    (updatedState as Result.Error).throwable.message
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `click share, show loading state`() {
        runTest {
            // Given
            coEvery {
                getDetailProductUseCase.observe()
            } returns MutableStateFlow<Result<UniversalSharingPostPurchaseProductResponse>>(
                Result.Loading
            )

            viewModel.sharingUiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                skipItems(1)

                // Then
                val updatedState = awaitItem()
                assertTrue(updatedState.isLoading)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `successfully click share, show detail product data state`() {
        runTest {
            // Given
            val dummyResponse = UniversalSharingPostPurchaseProductWrapperResponse()
            repository.stubRepository(dummyResponse)

            viewModel.sharingUiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                skipItems(1) // skip initial item

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.ClickShare(
                        orderId = dummyOrderId,
                        shopName = dummyShopName,
                        productId = dummyProductId
                    )
                )
                // Then
                val updatedTempState = awaitItem()
                assertTrue(dummyProductId == updatedTempState.productId)
                assertTrue(dummyOrderId == updatedTempState.orderId)
                assertTrue(null == updatedTempState.productData)
                assertFalse(updatedTempState.isLoading)
                assertTrue(null == updatedTempState.error)

                // Skip loading because previous value is swallowed

                val updatedResultState = awaitItem()
                assertTrue(dummyProductId == updatedResultState.productId)
                assertTrue(dummyOrderId == updatedResultState.orderId)
                assertTrue(dummyResponse.product == updatedResultState.productData)
                assertFalse(updatedResultState.isLoading)
                assertTrue(null == updatedResultState.error)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `failed to click share, show error state`() {
        runTest {
            // Given
            val dummyResponse = UniversalSharingPostPurchaseProductWrapperResponse()
            repository.stubRepositoryAsThrow(dummyThrowable)

            viewModel.sharingUiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                skipItems(1) // skip initial item

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.ClickShare(
                        orderId = dummyOrderId,
                        shopName = dummyShopName,
                        productId = dummyProductId
                    )
                )
                // Then
                val updatedTempState = awaitItem()
                assertTrue(dummyProductId == updatedTempState.productId)
                assertTrue(dummyOrderId == updatedTempState.orderId)
                assertTrue(null == updatedTempState.productData)
                assertFalse(updatedTempState.isLoading)
                assertTrue(null == updatedTempState.error)

                // Skip loading because previous value is swallowed

                val updatedResultState = awaitItem()
                assertTrue(dummyProductId == updatedResultState.productId)
                assertTrue(dummyOrderId == updatedResultState.orderId)
                assertTrue(null == updatedResultState.productData)
                assertFalse(updatedResultState.isLoading)
                assertTrue(dummyThrowable == updatedResultState.error)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `click share and error, show error state`() {
        runTest {
            // Given
            coEvery {
                getDetailProductUseCase.getDetailProduct(any())
            } throws dummyThrowable

            viewModel.sharingUiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                skipItems(1)

                // When
                viewModel.processAction(
                    UniversalSharingPostPurchaseAction.ClickShare(
                        orderId = dummyOrderId,
                        shopName = dummyShopName,
                        productId = dummyProductId
                    )
                )
                // Then
                val updatedState = awaitItem()
                assertTrue(dummyThrowable == updatedState.error)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `error when observe share, show error state`() {
        runTest {
            // Given
            coEvery {
                getDetailProductUseCase.observe()
            } throws dummyThrowable

            viewModel.sharingUiState.test {
                // When
                viewModel.setupViewModelObserver()
                // Then
                skipItems(1)

                // Then
                val updatedState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable,
                    updatedState.error
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
