package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductInputValidationResult
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.WarehouseUiModelMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.EditProductInfoViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditProductInfoViewModelTest {
    @RelaxedMockK
    lateinit var productErrorStatusHandler: ProductErrorStatusHandler
    @RelaxedMockK
    lateinit var warehouseUiModelMapper: WarehouseUiModelMapper
    @RelaxedMockK
    lateinit var doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditProductInfoViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = EditProductInfoViewModel(
            CoroutineTestDispatchersProvider,
            productErrorStatusHandler,
            warehouseUiModelMapper,
            doSellerCampaignProductSubmissionUseCase
        )
    }

    @Test
    fun `When setProduct using multiloc data, Expect trigger multiloc related livedata`() {
        runBlocking {
            // given
            val productIdTest = "123123"
            val warehouseIdTest = "123123"
            val warehouseIdTest2 = "123124"
            coEvery {
                warehouseUiModelMapper.map(any())
            } returns listOf(
                WarehouseUiModel(id = warehouseIdTest),
                WarehouseUiModel(id = warehouseIdTest2),
            )
            coEvery {
                warehouseUiModelMapper.isShopMultiloc(any())
            } returns true
            with(viewModel) {
                // when
                val productTest = SellerCampaignProductList.Product(productId = productIdTest)
                setProduct(productTest)

                // then
                val product = product.getOrAwaitValue()
                val warehouseList = warehouseList.getOrAwaitValue()
                val isShopMultiloc = isShopMultiloc.getOrAwaitValue()

                assert(product.productId == productIdTest)
                assert(warehouseList.isNotEmpty())
                assert(isShopMultiloc)
                assert(productInputData.productId == productIdTest)
            }
        }
    }

    @Test
    fun `When editProduct is success, Expect trigger success result`() {
        runBlocking {
            val campaignIdTest = "123123"
            mockSuccessfulSubmission(campaignIdTest)
            with(viewModel) {
                // given, when
                setProduct(SellerCampaignProductList.Product(
                    productMapData = SellerCampaignProductList.ProductMapData(campaignId = campaignIdTest)
                ))
                editProduct()

                // then
                assert(editProductResult.getOrAwaitValue().isSuccess)
                assert(!isLoading.getOrAwaitValue())
            }
        }
    }

    @Test
    fun `When editProduct is failed, Expect trigger error result`() {
        runBlocking {
            val campaignIdTest = "123123"
            mockFailedSubmission(campaignIdTest)
            with(viewModel) {
                // given, when
                setProduct(SellerCampaignProductList.Product(
                    productMapData = SellerCampaignProductList.ProductMapData(campaignId = campaignIdTest)
                ))
                editProduct()

                // then
                assert(!errorThrowable.getOrAwaitValue().message.isNullOrBlank())
                assert(!isLoading.getOrAwaitValue())
            }
        }
    }

    @Test
    fun `When removeProducts is success, Expect trigger success result`() {
        runBlocking {
            val campaignIdTest = "345345"
            mockSuccessfulSubmission(campaignIdTest)
            with(viewModel) {
                // given, when
                removeProducts(campaignIdTest.toLongOrZero(), emptyList())

                // then
                assert(removeProductsStatus.getOrAwaitValue() is Success)
            }
        }
    }

    @Test
    fun `When removeProducts is failed, Expect trigger error result`() {
        runBlocking {
            val campaignIdTest = "345345"
            mockFailedSubmission(campaignIdTest)
            with(viewModel) {
                // given, when
                removeProducts(campaignIdTest.toLongOrZero(), emptyList())

                // then
                assert(removeProductsStatus.getOrAwaitValue() is Fail)
            }
        }
    }

    @Test
    fun `When input data using percent, Expect trigger valid result`() {
        runBlocking {
            val campaignIdTest = "123123"
            mockSuccessfulSubmission(campaignIdTest)
            with(viewModel) {
                val productTest = SellerCampaignProductList.Product(productId = "123123", price = 1000)
                setProduct(productTest)
                setWarehouseList(listOf())
                setCampaignPrice("1000")
                setCampaignPricePercent("50")
                setCampaignStock("")
                setCampaignMaxOrder("")
                setUsingPricePercentage(true)
                setInputWarehouseId("")
                setInputOriginalStock(123)
                setDeleteStatus(true)

                var validationResult = ProductInputValidationResult()
                var campaignPriceResult = 0L

                launch {
                    isValid.collectLatest {
                        validationResult = it
                    }
                }
                launch {
                    campaignPrice.collectLatest {
                        campaignPriceResult = it
                    }
                }

                assert(validationResult.errorList.isEmpty())
                assert(campaignPriceResult == 500L)
            }
        }
    }

    @Test
    fun `When input data using currency, Expect trigger valid result`() {
        runBlocking {
            val campaignIdTest = "123123"
            mockSuccessfulSubmission(campaignIdTest)
            with(viewModel) {
                val productTest = SellerCampaignProductList.Product(productId = "123123", price = 1000)
                setProduct(productTest)
                setWarehouseList(listOf())
                setCampaignPrice("500")
                setCampaignPricePercent("50")
                setCampaignStock("")
                setCampaignMaxOrder("")
                setUsingPricePercentage(false)
                setInputWarehouseId("")
                setInputOriginalStock(123)
                setDeleteStatus(true)

                var validationResult = ProductInputValidationResult()
                var campaignPercentResult = 0L

                launch {
                    isValid.collectLatest {
                        validationResult = it
                    }
                }
                launch {
                    campaignPricePercent.collectLatest {
                        campaignPercentResult = it
                    }
                }

                assert(validationResult.errorList.isEmpty())
                assert(campaignPercentResult == 50L)
            }
        }
    }

    private fun mockSuccessfulSubmission(campaignIdTest: String) {
        coEvery {
            doSellerCampaignProductSubmissionUseCase.execute(campaignIdTest, any(), any())
        } returns ProductSubmissionResult(
            isSuccess = true,
            errorMessage = "",
            failedProducts = listOf()
        )
    }

    private fun mockFailedSubmission(campaignIdTest: String) {
        coEvery {
            doSellerCampaignProductSubmissionUseCase.execute(campaignIdTest, any(), any())
        } throws MessageErrorException("error")
    }

}
