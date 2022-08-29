package com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.START_DATE_ERROR
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MAX
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_PRICE_MIN
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_R2_ABUSIVE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.ERROR_START_DATE
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation.Companion.NONE
import com.tokopedia.shopdiscount.utils.extension.unixToMs
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class ShopDiscountManageProductViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockErrorMessage = "Error"
    private val mockProductId = "1234"
    private val mockOriginalPrice = 20000
    private val mockDiscountedPrice = 10000
    private val mockDiscountedPriceGreaterThanOriginalPrice = 25000
    private val mockMaxOrder = 10
    private val mockDiscountedPercentage = 50
    private val mockEndDate = Date(1684287429L.unixToMs())

    private val viewModel by lazy {
        ShopDiscountManageProductViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceBenefitUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When success get seller info benefit data, should return success result and matched with mock data`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } returns getGetSlashPriceBenefitMockSuccessResponse()
        viewModel.getSlashPriceBenefit()
        val liveDataValue = viewModel.slashPriceBenefitLiveData.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.isUseVps)
        assert(liveDataSuccessValue.data.responseHeader.success)
    }

    private fun getGetSlashPriceBenefitMockSuccessResponse(): GetSlashPriceBenefitResponse {
        return GetSlashPriceBenefitResponse(
            GetSlashPriceBenefitResponse.GetSlashPriceBenefit(
                isUseVps = true,
                responseHeader = ResponseHeader(
                    success = true
                )
            )
        )
    }

    @Test
    fun `When error get seller info benefit data, should return fail result`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } throws Exception(mockErrorMessage)
        viewModel.getSlashPriceBenefit()
        val liveDataValue = viewModel.slashPriceBenefitLiveData.value
        assert(liveDataValue is Fail)
        val liveDataFailValue = liveDataValue as Fail
        assert(liveDataFailValue.throwable.message == mockErrorMessage)
    }

    @Test
    fun `When call setProductData, then getProductData should return mocked data`() {
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        val productData = viewModel.getProductData()
        assert(productData.productId == mockProductId)
    }

    private fun getMockProductData(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            listProductWarehouse = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
            ),
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice
            )
        )
    }

    @Test
    fun `When call updateProductDiscountPeriodData with given start date is more than 5 minutes and has start date error, then getProductData should return updated start date and end date and errorType should be no error`() {
        val mockProductData = getMockProductDataWithStartDateError()
        viewModel.setProductData(mockProductData)
        val mockStartDateTenMinutesFromNow = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 10)
        }.time
        viewModel.updateProductDiscountPeriodData(mockStartDateTenMinutesFromNow, mockEndDate)
        val liveData = viewModel.updatedDiscountPeriodData
        assert(liveData.value?.slashPriceInfo?.startDate?.time == mockStartDateTenMinutesFromNow.time)
        assert(liveData.value?.slashPriceInfo?.endDate?.time == mockEndDate.time)
        assert(liveData.value?.productStatus?.errorType == ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.NO_ERROR)
    }

    @Test
    fun `When call updateProductDiscountPeriodData with given start date is less than 5 minutes and has start date error, then getProductData should return updated start date and end date and errorType should be error start date`() {
        val mockProductData = getMockProductDataWithStartDateError()
        viewModel.setProductData(mockProductData)
        val mockStartDateTenMinutesFromNow = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 2)
        }.time
        viewModel.updateProductDiscountPeriodData(mockStartDateTenMinutesFromNow, mockEndDate)
        val liveData = viewModel.updatedDiscountPeriodData
        assert(liveData.value?.slashPriceInfo?.startDate?.time == mockStartDateTenMinutesFromNow.time)
        assert(liveData.value?.slashPriceInfo?.endDate?.time == mockEndDate.time)
        assert(liveData.value?.productStatus?.errorType == START_DATE_ERROR)
    }

    @Test
    fun `When call updateProductDiscountPeriodData with given start date is less than 5 minutes and no start date error, then getProductData should return updated start date and end date and errorType should be no error`() {
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        val mockStartDateTenMinutesFromNow = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 2)
        }.time
        viewModel.updateProductDiscountPeriodData(mockStartDateTenMinutesFromNow, mockEndDate)
        val liveData = viewModel.updatedDiscountPeriodData
        assert(liveData.value?.slashPriceInfo?.startDate?.time == mockStartDateTenMinutesFromNow.time)
        assert(liveData.value?.slashPriceInfo?.endDate?.time == mockEndDate.time)
        assert(liveData.value?.productStatus?.errorType == ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.NO_ERROR)
    }

    private fun getMockProductDataWithStartDateError(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            listProductWarehouse = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    avgSoldPrice = 18000
                )
            ),
            productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                errorType = START_DATE_ERROR
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 16000
            )
        )
    }

    @Test
    fun `When call updateDiscountPrice, then updatedDiscountPercentageData should return correct discounted percentage`() {
        val expectedDiscountedPercentage = 50
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        viewModel.updateDiscountPrice(mockDiscountedPrice)
        val liveData = viewModel.updatedDiscountPercentageData
        assert(liveData.value == expectedDiscountedPercentage)
    }

    @Test
    fun `When call updateDiscountPrice with discounted price greater than original price, then updatedDiscountPercentageData should return 0 percent`() {
        val expectedDiscountedPercentage = 0
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        viewModel.updateDiscountPrice(mockDiscountedPriceGreaterThanOriginalPrice)
        val liveData = viewModel.updatedDiscountPercentageData
        assert(liveData.value == expectedDiscountedPercentage)
    }

    @Test
    fun `When call updateDiscountPrice with discounted price is zero, then updatedDiscountPercentageData should return 99 percent`() {
        val expectedDiscountedPercentage = 99
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        viewModel.updateDiscountPrice(0)
        val liveData = viewModel.updatedDiscountPercentageData
        assert(liveData.value == expectedDiscountedPercentage)
    }

    @Test
    fun `When call updateDiscountPercent, then updatedDiscountPriceData should return correct discounted price`() {
        val expectedDiscountedPrice = 10000
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        viewModel.updateDiscountPercent(mockDiscountedPercentage)
        val liveData = viewModel.updatedDiscountPriceData
        assert(liveData.value == expectedDiscountedPrice)
    }

    @Test
    fun `When call updateMaxOrderData, then getProductData should return mocked max order`() {
        val mockProductData = getMockProductData()
        viewModel.setProductData(mockProductData)
        viewModel.updateMaxOrderData(mockMaxOrder)
        val productData = viewModel.getProductData()
        assert(productData.listProductWarehouse.firstOrNull()?.maxOrder.toIntOrZero() == mockMaxOrder)
    }

    @Test
    fun `When call validateInput with correct input, then inputValidation value should be no error`() {
        val mockProductData = getMockProductDataWithCorrectValue()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == NONE)
    }

    @Test
    fun `When call validateInput with discounted price greater than max discounted price, then inputValidation value should be error max price`() {
        val mockProductData = getMockProductDataWithErrorPriceMax()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == ERROR_PRICE_MAX)
    }

    @Test
    fun `When call validateInput with discounted price less than min discounted price, then inputValidation value should be error min price`() {
        val mockProductData = getMockProductDataWithErrorPriceMin()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == ERROR_PRICE_MIN)
    }

    @Test
    fun `When call validateInput with discounted price is more than average sold price, then inputValidation value should be error R2 abusive`() {
        val mockProductData = getMockProductDataWithErrorR2Abusive()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == ERROR_R2_ABUSIVE)
    }

    @Test
    fun `When call validateInput with discounted price is correct and less than average sold price, then inputValidation value should be no error`() {
        val mockProductData = getMockProductDataWithDiscountedPriceGreaterThatAverageSoldPrice()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == NONE)
    }

    @Test
    fun `When call validateInput with start date error, then inputValidation value should error start date`() {
        val mockProductData = getMockProductDataWithStartDateError()
        viewModel.setProductData(mockProductData)
        viewModel.validateInput()
        val liveData = viewModel.inputValidation
        assert(liveData.value == ERROR_START_DATE)
    }

    private fun getMockProductDataWithErrorR2Abusive(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            listProductWarehouse = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    avgSoldPrice = 15000
                )
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 16000
            )
        )
    }

    private fun getMockProductDataWithDiscountedPriceGreaterThatAverageSoldPrice(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            listProductWarehouse = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    avgSoldPrice = 18000
                )
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 16000
            )
        )
    }

    private fun getMockProductDataWithErrorPriceMin(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 1
            )
        )
    }

    private fun getMockProductDataWithErrorPriceMax(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 50000
            )
        )
    }

    private fun getMockProductDataWithCorrectValue(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice,
                maxOriginalPrice = mockOriginalPrice
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 10000
            )
        )
    }

    @Test
    fun `When call getDiscountPeriodDataBasedOnBenefit with vps, then discountPeriodDataBasedOnBenefitLiveData value should return vps package end date`() {
        val mockSlashPriceBenefitData = getMockSlashPriceBenefitDataVps()
        viewModel.getDiscountPeriodDataBasedOnBenefit(mockSlashPriceBenefitData)
        val liveData = viewModel.discountPeriodDataBasedOnBenefitLiveData
        assert(liveData.value?.second?.time.orZero() / 1000L == mockEndDate.time / 1000L)
    }

    @Test
    fun `When call getDiscountPeriodDataBasedOnBenefit with vps but no vps package id, then discountPeriodDataBasedOnBenefitLiveData value should return vps package end date value of zero`() {
        val mockSlashPriceBenefitData = getMockSlashPriceBenefitDataVpsWithNoVpsPackageId()
        viewModel.getDiscountPeriodDataBasedOnBenefit(mockSlashPriceBenefitData)
        val liveData = viewModel.discountPeriodDataBasedOnBenefitLiveData
        assert(liveData.value?.second?.time == 0L)
    }

    @Test
    fun `When call getDiscountPeriodDataBasedOnBenefit with no vps, then discountPeriodDataBasedOnBenefitLiveData value should return end date with range of 1 year of start date`() {
        val mockSlashPriceBenefitData = getMockSlashPriceBenefitDataNonVps()
        viewModel.getDiscountPeriodDataBasedOnBenefit(mockSlashPriceBenefitData)
        val liveData = viewModel.discountPeriodDataBasedOnBenefitLiveData
        val startDateMills = liveData.value?.first?.time.orZero()
        val endDateMills = liveData.value?.second?.time.orZero()
        val diffMills = endDateMills - startDateMills
        val daysDateDiff = (diffMills / (1000 * 60 * 60 * 24)).toInt()
        assert(daysDateDiff >= 364)
    }

    private fun getMockSlashPriceBenefitDataNonVps(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = false
        )
    }

    private fun getMockSlashPriceBenefitDataVpsWithNoVpsPackageId(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = true,
            listSlashPriceBenefitData = listOf(
                ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                    packageId = "-1"
                )
            )
        )
    }

    private fun getMockSlashPriceBenefitDataVps(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = true,
            listSlashPriceBenefitData = listOf(
                ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                    packageId = "1",
                    expiredAtUnix = mockEndDate.time / 1000L
                )
            )
        )
    }

    @Test
    fun `When call getMinDiscountPrice with minDiscountPrice less than 100, then should return 100`() {
        val mockProductData = getMockProductDataWithDiscountedPriceLessThanOneHundred()
        viewModel.setProductData(mockProductData)
        val minDiscountedPrice = viewModel.getMinDiscountPrice()
        assert(minDiscountedPrice == 100)
    }

    private fun getMockProductDataWithDiscountedPriceLessThanOneHundred(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = 50,
                maxOriginalPrice = mockOriginalPrice
            ),
            listProductWarehouse = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse(
                    avgSoldPrice = 18000
                )
            ),
            productStatus = ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
                errorType = START_DATE_ERROR
            ),
            slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                discountedPrice = 16000
            )
        )
    }
}