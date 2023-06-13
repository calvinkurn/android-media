package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignValidatedProductListResponse
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignValidatedProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.ChooseProductViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChooseProductViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignValidatedProductListUseCase: GetSellerCampaignValidatedProductListUseCase

    @RelaxedMockK
    lateinit var doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ChooseProductViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChooseProductViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignValidatedProductListUseCase,
            doSellerCampaignProductSubmissionUseCase
        )
    }

    @Test
    fun `When searchKeyword is filled, Expect searching status true`() {
        runBlocking {
            with(viewModel) {
                // given, when
                setSearchKeyword("")
                val resultEmpty = isSearching()
                setSearchKeyword("gebyar toped")
                val resultFilled = isSearching()

                // then
                assert(!resultEmpty)
                assert(resultFilled)
            }
        }
    }

    @Test
    fun `When setSelectedItems using empty list, Expect invalid input`() {
        runBlocking {
            with(viewModel) {
                // given, when
                setSelectedItems(listOf())
                val isValid = isSelectionValid.getOrAwaitValue()
                val hasVariant = isSelectionHasVariant.getOrAwaitValue()
                // then
                assert(!isValid)
                assert(!hasVariant)
            }
        }
    }

    @Test
    fun `When setSelectedItems using valid list, Expect valid input`() {
        runBlocking {
            with(viewModel) {
                // given, when
                setSelectedItems(listOf(
                    SelectedProductModel(
                        productId = "123123",
                        parentProductId = "",
                        isProductPreviouslySubmitted = false,
                        hasChild = false
                    ),
                    SelectedProductModel(
                        productId = "123124",
                        parentProductId = "",
                        isProductPreviouslySubmitted = false,
                        hasChild = true
                    ),
                    SelectedProductModel(
                        productId = "123125",
                        parentProductId = "1231231",
                        isProductPreviouslySubmitted = false,
                        hasChild = false
                    )
                ))
                setPreviousSelectedProductCount(10)
                // then
                isSelectionValid.getOrAwaitValue()
                isSelectionHasVariant.getOrAwaitValue()
            }
        }
    }

    @Test
    fun `When getReserveProductList success, Expect valid data invoked`() {
        val campaignId = "123123"
        runBlocking {
            coEvery {
                getSellerCampaignValidatedProductListUseCase.execute(campaignId, any(), any())
            } returns listOf(
                GetSellerCampaignValidatedProductListResponse.Product (
                    productId = "",
                    productName = "",
                    price = 0.0,
                    formattedPrice = "",
                    productUrl = "",
                    sku = "",
                    status = "",
                    pictures = listOf(),
                    disabled = false,
                    disabledReason = "",
                    transactionStatistics = GetSellerCampaignValidatedProductListResponse.TrxStats(0),
                    stock = 0,
                    variantChildsIds = listOf()
                )
            )
            with(viewModel) {
                getReserveProductList(campaignId, 0)
                val result = reserveProductList.getOrAwaitValue()
                assert(result.isNotEmpty())
            }
        }
    }

    @Test
    fun `When getReserveProductList error, Expect error data invoked`() {
        val campaignId = "123123"
        runBlocking {
            coEvery {
                getSellerCampaignValidatedProductListUseCase.execute(campaignId, any(), any())
            } throws MessageErrorException("error message")
            with(viewModel) {
                getReserveProductList(campaignId, 0)
                val error = errors.getOrAwaitValue().message.orEmpty()
                assert(error.isNotEmpty())
            }
        }
    }

    @Test
    fun `When addProduct success, Expect valid data invoked`() {
        val campaignId = "123123"
        runBlocking {
            coEvery {
                doSellerCampaignProductSubmissionUseCase.execute(campaignId, any(), any())
            } returns ProductSubmissionResult(
                isSuccess = true,
                errorMessage = "",
                failedProducts = listOf()
            )
            with(viewModel) {
                addProduct(campaignId)
                val result = isAddProductSuccess.getOrAwaitValue()
                assert(result)
            }
        }
    }

    @Test
    fun `When addProduct success with error message, Expect error data invoked`() {
        val campaignId = "123123"
        runBlocking {
            coEvery {
                doSellerCampaignProductSubmissionUseCase.execute(campaignId, any(), any())
            } returns ProductSubmissionResult(
                isSuccess = false,
                errorMessage = "error",
                failedProducts = listOf()
            )
            with(viewModel) {
                addProduct(campaignId)
                val result = isAddProductSuccess.getOrAwaitValue()
                assertFalse(result)
            }
        }
    }

    @Test
    fun `When addProduct error, Expect error data invoked`() {
        val campaignId = "123123"
        runBlocking {
            coEvery {
                doSellerCampaignProductSubmissionUseCase.execute(campaignId, any(), any())
            } throws MessageErrorException("error message")
            with(viewModel) {
                addProduct(campaignId)
                val error = errors.getOrAwaitValue().message.orEmpty()
                assert(error.isNotEmpty())
            }
        }
    }
}
