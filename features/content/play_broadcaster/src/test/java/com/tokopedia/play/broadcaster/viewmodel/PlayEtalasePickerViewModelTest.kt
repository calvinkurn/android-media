package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.robot.andThen
import com.tokopedia.play.broadcaster.robot.andWhen
import com.tokopedia.play.broadcaster.robot.givenPlayEtalasePickerViewModel
import com.tokopedia.play.broadcaster.robot.thenVerify
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 20/05/21
 */
class PlayEtalasePickerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchers

    private val playBroadcastMapper = PlayBroadcastUiMapper(
            TestHtmlTextTransformer()
    )

    private val responseBuilder = PlayBroadcasterResponseBuilder()
    private val modelBuilder = TestDoubleModelBuilder()

    private val etalaseUseCase: GetSelfEtalaseListUseCase = mockk(relaxed = true)
    private val mockEtalaseIdNameList = listOf(
            "1" to "Etalase Umum",
            "2" to "Etalase Diskon",
    )
    private val mockSelfEtalaseResponse = responseBuilder.buildGetSelfEtalaseUseCaseResponse(
            mockEtalaseIdNameList
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when load etalase list success, it should return the correct etalase list`() {
        val mappedResponse = playBroadcastMapper.mapEtalaseList(mockSelfEtalaseResponse)

        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase
        ).andWhen {
            getEtalaseResult()
        }.thenVerify {
            it.assertWhenSuccess { _, data ->
                data.isEqualTo(mappedResponse)
            }
        }
    }

    @Test
    fun `when load etalase list failed, it should return failed`() {
        val error = IllegalArgumentException("Error getting etalase list")
        coEvery { etalaseUseCase.executeOnBackground() } throws error

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase
        ).andWhen {
            getEtalaseResult()
        }.thenVerify {
            it.assertWhenFailed { state, data ->
                data.isEmpty()
                state.error.isEqualToComparingFieldByField(error)
            }
        }
    }

    @Test
    fun `when not yet retrieve product preview, products in all etalase should be empty`() {
        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase
        ).andWhen {
            getEtalaseResult().currentValue
        }.thenVerify {
            it.assertNotEmpty()

            it.forEach { etalase ->
                etalase.productMap.assertEmpty()
            }
        }
    }

    @Test
    fun `when already retrieve product preview for certain etalase, products in that etalase should not be empty`() {
        val mockIdNameProductList = listOf(
                "1" to "Buku Dewa",
                "2" to "Pencil John Wick"
        )
        val firstEtalaseId = mockEtalaseIdNameList.first().first
        val mockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(mockIdNameProductList)
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse
        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns mockResponse

        givenPlayEtalasePickerViewModel(
                dispatcher = dispatcher,
                getSelfEtalaseListUseCase = etalaseUseCase,
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase
        ).andThen {
            loadProductPreview(firstEtalaseId)
        }.andWhen {
            getEtalaseResult().currentValue
        }.thenVerify {
            it.assertNotEmpty()

            it.forEach { etalase ->
                if (etalase.id == firstEtalaseId) {
                    etalase.productMap.assertNotEmpty()
                    etalase.productMap.values.flatten().isEqualToIgnoringFields(
                            playBroadcastMapper.mapProductList(mockResponse, { false }, { Selectable }),
                            ProductContentUiModel::isSelectable, ProductContentUiModel::isSelectedHandler, ProductContentUiModel::transitionName
                    )
                } else etalase.productMap.assertEmpty()
            }
        }
    }

    @Test
    fun `when getting maximum product desc, it should return the correct desc`() {
        val maxDesc = "Maximum product is 99"

        givenPlayEtalasePickerViewModel {
            setMaxProductDesc(maxDesc)
        }.andWhen {
            getMaxProductDesc()
        }.thenVerify {
            it.isEqualTo(maxDesc)
        }
    }

    @Test
    fun `when getting selected product list, it should return the correct selected product`() {
        val mockIdNameProductList = listOf(
                "1" to "Buku Dewa",
                "2" to "Pencil John Wick"
        )
        val firstEtalaseId = mockEtalaseIdNameList.first().first
        val selectedProductId = mockIdNameProductList.last().first.toLong()

        val mockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(mockIdNameProductList)
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse
        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns mockResponse

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase,
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase,
        ).andThen {
            loadProductPreview(firstEtalaseId)
        }.andWhen {
            getSelectedProducts()
        }.thenVerify {
            it.assertEmpty()
        }.andThen {
            selectProduct(selectedProductId)
        }.andWhen {
            getSelectedProducts()
        }.thenVerify {
            it.assertCount(1)
            it.isEqualToIgnoringFields(
                    playBroadcastMapper.mapProductList(mockResponse, { false }, { Selectable }).subList(1, 2),
                    ProductContentUiModel::isSelectable, ProductContentUiModel::isSelectedHandler, ProductContentUiModel::transitionName
            )
        }.andThen {
            deselectProduct(selectedProductId)
        }.andWhen {
            getSelectedProducts()
        }.thenVerify {
            it.assertEmpty()
        }
    }

    @Test
    fun `when selecting invalid product id, then it should not be included in selected products`() {
        val mockIdNameProductList = listOf(
                "1" to "Buku Dewa",
                "2" to "Pencil John Wick"
        )

        val mockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(mockIdNameProductList)
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse
        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns mockResponse

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase,
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase,
        ).andWhen {
            getSelectedProducts()
        }.thenVerify {
            it.assertEmpty()
        }.andThen {
            selectProduct(9999)
        }.andWhen {
            getSelectedProducts()
        }.thenVerify {
            it.assertEmpty()
        }
    }

    @Test
    fun `when load etalase products for next page, it should append the products in corresponding etalase`() {
        val mockIdNameProductList = listOf(
                "1" to "Buku Dewa",
                "2" to "Pencil John Wick"
        )

        val otherMockIdNameProductList = listOf(
                "3" to "Buku Dewa",
                "4" to "Pencil John Wick"
        )

        val mockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(mockIdNameProductList)
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

        coEvery { etalaseUseCase.executeOnBackground() } returns mockSelfEtalaseResponse
        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns mockResponse

        val firstEtalaseId = mockEtalaseIdNameList.first().first

        givenPlayEtalasePickerViewModel(
                getSelfEtalaseListUseCase = etalaseUseCase,
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase,
        ).andThen {
            loadEtalaseProducts(firstEtalaseId, 1)
        }.andWhen {
            getSelectedEtalaseResult()
        }.thenVerify {
            it.assertWhenSuccess { _, data ->
                data.productMap.assertCount(1)
                data.productMap.values.flatten().map(ProductContentUiModel::id)
                        .isEqualTo(
                                mockIdNameProductList.map { (id, _) ->
                                    id.toLong()
                                }
                        )
            }
        }.andThen {
            val otherMockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(otherMockIdNameProductList)
            coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns otherMockResponse
            loadEtalaseProducts(firstEtalaseId, 2)
        }.andWhen {
            getSelectedEtalaseResult()
        }.thenVerify {
            it.assertWhenSuccess { _, data ->
                data.productMap.assertCount(2)
                data.productMap.values.flatten().map(ProductContentUiModel::id).isEqualTo(
                        (mockIdNameProductList + otherMockIdNameProductList).map { (id, _) ->
                            id.toLong()
                        }
                )
            }
        }
    }

    @Test
    fun `when upload product success, it should return success`() {
        val mockProductDataStore = modelBuilder.buildProductDataStore(
                dispatcher = dispatcher
        )

        val mockDataStore = modelBuilder.buildSetupDataStore(
                productDataStore = mockProductDataStore
        )

        givenPlayEtalasePickerViewModel(
                setupDataStore = mockDataStore
        ) {
            mockProductDataStore.setUploadSuccess(true)
        }.andThen {
            uploadProduct()
        }.andWhen {
            getUploadProductResult()
        }.thenVerify {
            it.assertSuccess()
        }
    }

    @Test
    fun `when upload product failed, it should return failed`() {
        val mockProductDataStore = modelBuilder.buildProductDataStore(
                dispatcher = dispatcher
        )

        val mockDataStore = modelBuilder.buildSetupDataStore(
                productDataStore = mockProductDataStore
        )

        givenPlayEtalasePickerViewModel(
                setupDataStore = mockDataStore
        ) {
            mockProductDataStore.setUploadSuccess(false)
        }.andThen {
            uploadProduct()
        }.andWhen {
            getUploadProductResult()
        }.thenVerify {
            it.assertFailed()
        }
    }

    @Test
    fun `when search products success, it should return the search results`() {
        val mockIdNameProductList = listOf(
                "1" to "Buku Dewa",
                "2" to "Pencil John Wick"
        )

        val mockResponse = responseBuilder.buildGetProductsInEtalaseUseCaseResponse(mockIdNameProductList)
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } returns mockResponse

        givenPlayEtalasePickerViewModel(
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase,
        ).andThen {
            searchProducts("abc", 1)
        }.andWhen {
            getSearchedProductResult()
        }.thenVerify {
            it.assertWhenSuccess { _, data ->
                data.isEqualToIgnoringFields(
                        playBroadcastMapper.mapProductList(mockResponse, { false }, { Selectable }),
                        ProductContentUiModel::isSelectable, ProductContentUiModel::isSelectedHandler, ProductContentUiModel::transitionName
                )
            }
        }
    }

    @Test
    fun `when search products failed, it should return error`() {
        val mockProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)
        val error = IllegalArgumentException("Error Getting Products")

        coEvery { mockProductsInEtalaseUseCase.executeOnBackground() } throws error

        givenPlayEtalasePickerViewModel(
                getProductsInEtalaseUseCase = mockProductsInEtalaseUseCase,
        ).andThen {
            searchProducts("abc", 1)
        }.andWhen {
            getSearchedProductResult()
        }.thenVerify {
            it.assertWhenFailed { state, data ->
                data.assertEmpty()
                state.error.isEqualToComparingFieldByField(error)
            }
        }
    }
}