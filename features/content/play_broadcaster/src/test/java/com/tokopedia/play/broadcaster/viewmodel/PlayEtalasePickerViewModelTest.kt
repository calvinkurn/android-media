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
            getCurrentEtalaseResult()
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
            getCurrentEtalaseResult()
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
            getCurrentEtalaseResult().currentValue
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
            getCurrentEtalaseResult().currentValue
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
}