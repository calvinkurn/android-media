package com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation
import com.tokopedia.shopdiscount.utils.extension.toCalendar
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
import java.util.concurrent.TimeUnit

class ShopDiscountManageVariantViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockErrorMessage = "Error"
    private val mockProductId = "1234"
    private val mockVariantId = "456"
    private val mockVariantIdNotMatched = "789"
    private val mockOriginalPrice = 20000
    private val mockStartDateValueBulkApply = Date(1652415771000)
    private val mockEndDateValueBulkApply = Date(1670880168000)
    private val mockBulkApplyRupiahDiscountAmountBulkApply = 1000
    private val mockBulkApplyPercentageDiscountAmountBulkApply = 10
    private val viewModel by lazy {
        ShopDiscountManageVariantViewModel(
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
        val mockProductData = getMockProductDataWithExistingStartDate()
        viewModel.setProductData(mockProductData)
        val productData = viewModel.getProductData()
        assert(productData.productId == mockProductId)
    }

    private fun getMockProductDataWithDefaultStartDate(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            listProductVariant = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData(
                    slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                        startDate = Date(-62135588884L)
                    ),
                    listProductWarehouse = listOf(
                        ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
                    )
                ),
                ShopDiscountSetupProductUiModel.SetupProductData(
                    slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                        startDate = Date(-62135588884L)
                    ),
                    listProductWarehouse = listOf(
                        ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
                    )
                )
            ),
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice
            )
        )
    }

    private fun getMockProductDataWithExistingStartDate(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            listProductVariant = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData(
                    slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                        startDate = Date()
                    ),
                    listProductWarehouse = listOf(
                        ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
                    )
                ),
                ShopDiscountSetupProductUiModel.SetupProductData(
                    slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(
                        startDate = Date()
                    ),
                    listProductWarehouse = listOf(
                        ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
                    )
                )
            ),
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice
            )
        )
    }

    @Test
    fun `When call updateProductVariantDiscountPeriodData when creating discount with non vps, then getProductData end date should be 1 year from start date`() {
        val mockProductData = getMockProductDataWithDefaultStartDate()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductVariantDiscountPeriodData(
            getMockShopDiscountSellerInfoUiModelWithNoVps()
        )
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.all {
            val daysDiff =
                TimeUnit.MILLISECONDS.toDays(it.slashPriceInfo.endDate.time.orZero() - it.slashPriceInfo.startDate.time.orZero())
            daysDiff >= 365
        })
    }

    @Test
    fun `When call updateProductVariantDiscountPeriodData when creating discount with vps, then getProductData end date should return end date of vps package`() {
        val mockProductData = getMockProductDataWithDefaultStartDate()
        val mockShopDiscountSellerInfoUiModel = getMockShopDiscountSellerInfoUiModelWithVps()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductVariantDiscountPeriodData(mockShopDiscountSellerInfoUiModel)
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.all {
            it.slashPriceInfo.endDate.time == mockShopDiscountSellerInfoUiModel.listSlashPriceBenefitData.firstOrNull()?.expiredAtUnix?.unixToMs()
        })
    }

    @Test
    fun `When call updateProductVariantDiscountPeriodData when creating discount with existing date, then getProductData start and end date should return same as mocked data`() {
        val mockProductData = getMockProductDataWithExistingStartDate()
        val mockShopDiscountSellerInfoUiModel = getMockShopDiscountSellerInfoUiModelWithVps()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductVariantDiscountPeriodData(mockShopDiscountSellerInfoUiModel)
        val productData = viewModel.getProductData()
        productData.listProductVariant.forEachIndexed { index, setupProductData ->
            assert(
                setupProductData.slashPriceInfo.startDate == mockProductData.listProductVariant[index].slashPriceInfo.startDate
            )
            assert(
                setupProductData.slashPriceInfo.endDate == mockProductData.listProductVariant[index].slashPriceInfo.endDate
            )
        }
    }

    @Test
    fun `When call updateProductVariantDiscountPeriodData when creating discount with vps but wrong package id, then getProductData end date time should return 0`() {
        val mockProductData = getMockProductDataWithDefaultStartDate()
        val mockShopDiscountSellerInfoUiModel =
            getMockShopDiscountSellerInfoUiModelWithVpsButWrongPackageId()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductVariantDiscountPeriodData(mockShopDiscountSellerInfoUiModel)
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.all {
            it.slashPriceInfo.endDate.time == 0L
        })
    }

    private fun getMockShopDiscountSellerInfoUiModelWithNoVps(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = false
        )
    }

    private fun getMockShopDiscountSellerInfoUiModelWithVps(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = true,
            listSlashPriceBenefitData = listOf(
                ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                    packageId = "1",
                    expiredAtUnix = 2537080009
                )
            )
        )
    }

    private fun getMockShopDiscountSellerInfoUiModelWithVpsButWrongPackageId(): ShopDiscountSellerInfoUiModel {
        return ShopDiscountSellerInfoUiModel(
            isUseVps = true,
            listSlashPriceBenefitData = listOf(
                ShopDiscountSellerInfoUiModel.SlashPriceBenefitData(
                    packageId = "-1",
                    expiredAtUnix = 2537080009
                )
            )
        )
    }

    @Test
    fun `When call checkShouldEnableSubmitButton with no enabled variants, then isEnableSubmitButton value should be false`() {
        val mockListVariantUiModel = getMockListVariantUiModelWithNoEnabledVariant()
        viewModel.checkShouldEnableSubmitButton(mockListVariantUiModel)
        val isEnableSubmitButton = viewModel.isEnableSubmitButton.value
        assert(isEnableSubmitButton == false)
    }

    @Test
    fun `When call checkShouldEnableSubmitButton with some enabled variants and no error, then isEnableSubmitButton value should be true`() {
        val mockListVariantUiModel = getMockListVariantUiModelWithSomeEnabledVariantAndNoError()
        viewModel.checkShouldEnableSubmitButton(mockListVariantUiModel)
        val isEnableSubmitButton = viewModel.isEnableSubmitButton.value
        assert(isEnableSubmitButton == true)
    }

    @Test
    fun `When call checkShouldEnableSubmitButton with some enabled variants with no error but 0 discount price, then isEnableSubmitButton value should be false`() {
        val mockListVariantUiModel =
            getMockListVariantUiModelWithSomeEnabledVariantAndNoErrorButZeroDiscountPrice()
        viewModel.checkShouldEnableSubmitButton(mockListVariantUiModel)
        val isEnableSubmitButton = viewModel.isEnableSubmitButton.value
        assert(isEnableSubmitButton == false)
    }

    @Test
    fun `When call checkShouldEnableSubmitButton with some enabled variants with error, then isEnableSubmitButton value should be false`() {
        val mockListVariantUiModel = getMockListVariantUiModelWithSomeEnabledVariantAndError()
        viewModel.checkShouldEnableSubmitButton(mockListVariantUiModel)
        val isEnableSubmitButton = viewModel.isEnableSubmitButton.value
        assert(isEnableSubmitButton == false)
    }

    private fun getMockListVariantUiModelWithNoEnabledVariant(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            )
        )
    }

    private fun getMockListVariantUiModelWithSomeEnabledVariantAndNoError(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true,
                valueErrorType = ShopDiscountManageProductDiscountErrorValidation.NONE,
                discountedPrice = 10000
            )
        )
    }

    private fun getMockListVariantUiModelWithSomeEnabledVariantAndNoErrorButZeroDiscountPrice(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true,
                valueErrorType = ShopDiscountManageProductDiscountErrorValidation.NONE,
                discountedPrice = 0
            )
        )
    }

    private fun getMockListVariantUiModelWithSomeEnabledVariantAndError(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true,
                valueErrorType = ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MIN
            )
        )
    }

    @Test
    fun `When call updateProductDataBasedOnVariantUiModel with matched data, then getProductData should return updated data`() {
        val mockProductData = getMockProductDataForUpdate()
        val mockListVariantUiModel = getMockListVariantUiModelForUpdate()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductDataBasedOnVariantUiModel(mockListVariantUiModel)
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.firstOrNull()?.slashPriceInfo?.discountedPrice == mockListVariantUiModel.firstOrNull()?.discountedPrice)
    }

    @Test
    fun `When call updateProductDataBasedOnVariantUiModel with matched data but variant not enabled, then getProductData should remain the same`() {
        val mockProductData = getMockProductDataForUpdate()
        val mockListVariantUiModel = getMockListVariantUiModelForUpdateWithDisabledVariant()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductDataBasedOnVariantUiModel(mockListVariantUiModel)
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.firstOrNull()?.slashPriceInfo?.discountedPrice == mockProductData.listProductVariant.firstOrNull()?.slashPriceInfo?.discountedPrice)
    }

    @Test
    fun `When call updateProductDataBasedOnVariantUiModel with no matched data, then getProductData should remain the same`() {
        val mockProductData = getMockProductDataForUpdate()
        val mockListVariantUiModel = getMockListVariantUiModelForUpdateWithNotMatchedVariantId()
        viewModel.setProductData(mockProductData)
        viewModel.updateProductDataBasedOnVariantUiModel(mockListVariantUiModel)
        val productData = viewModel.getProductData()
        assert(productData.listProductVariant.firstOrNull()?.slashPriceInfo?.discountedPrice == mockProductData.listProductVariant.firstOrNull()?.slashPriceInfo?.discountedPrice)
    }

    private fun getMockProductDataForUpdate(): ShopDiscountSetupProductUiModel.SetupProductData {
        return ShopDiscountSetupProductUiModel.SetupProductData(
            productId = mockProductId,
            listProductVariant = listOf(
                ShopDiscountSetupProductUiModel.SetupProductData(
                    productId = mockVariantId,
                    slashPriceInfo = ShopDiscountSetupProductUiModel.SetupProductData.ProductSlashPriceInfo(),
                    listProductWarehouse = listOf(
                        ShopDiscountSetupProductUiModel.SetupProductData.ProductWarehouse()
                    )
                )
            ),
            mappedResultData = ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
                minOriginalPrice = mockOriginalPrice
            )
        )
    }

    private fun getMockListVariantUiModelForUpdate(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                variantId = mockVariantId,
                isEnabled = true,
                discountedPrice = 10000,
                discountedPercentage = 10
            )
        )
    }

    private fun getMockListVariantUiModelForUpdateWithDisabledVariant(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                variantId = mockVariantId,
                isEnabled = false,
                discountedPrice = 10000,
                discountedPercentage = 10
            )
        )
    }

    private fun getMockListVariantUiModelForUpdateWithNotMatchedVariantId(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                variantId = mockVariantIdNotMatched,
                isEnabled = false,
                discountedPrice = 10000,
                discountedPercentage = 10
            )
        )
    }

    @Test
    fun `When call checkShouldEnableBulkApplyVariant with all enabled variant, then totalEnableBulkApplyVariant should return total of enabled variant`() {
        val mockListVariantUiModel = getMockListVariantUiModelForCheckBulkApply()
        viewModel.checkShouldEnableBulkApplyVariant(mockListVariantUiModel)
        val totalEnabledBulkApplyVariant = viewModel.totalEnableBulkApplyVariant.value
        assert(totalEnabledBulkApplyVariant == mockListVariantUiModel.count { it.isEnabled })
    }

    @Test
    fun `When call checkShouldEnableBulkApplyVariant with no enabled variant, then totalEnableBulkApplyVariant should return total of enabled variant`() {
        val mockListVariantUiModel =
            getMockListVariantUiModelForCheckBulkApplyWithNoEnabledVariant()
        viewModel.checkShouldEnableBulkApplyVariant(mockListVariantUiModel)
        val totalEnabledBulkApplyVariant = viewModel.totalEnableBulkApplyVariant.value
        assert(totalEnabledBulkApplyVariant == 0)
    }

    private fun getMockListVariantUiModelForCheckBulkApply(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            )
        )
    }

    private fun getMockListVariantUiModelForCheckBulkApplyWithNoEnabledVariant(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false
            )
        )
    }

    @Test
    fun `When call applyBulkUpdate with no enabled variant, then bulkApplyListVariantItemUiModel value should remain the same as mocked data`() {
        val mockListVariantUiModel = getMockListVariantUiModelWithNoEnabledVariant()
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValueBulkApply,
            mockEndDateValueBulkApply,
            mockBulkApplyRupiahDiscountAmountBulkApply,
            DiscountType.RUPIAH
        )
        viewModel.applyBulkUpdate(mockListVariantUiModel, mockDiscountSettings)
        val bulkApplyListVariantItemUiModel = viewModel.bulkApplyListVariantItemUiModel.value
        bulkApplyListVariantItemUiModel?.forEachIndexed { index, shopDiscountManageProductVariantItemUiModel ->
            assert(shopDiscountManageProductVariantItemUiModel.discountedPrice == mockListVariantUiModel[index].discountedPrice)
        }
    }

    @Test
    fun `When call applyBulkUpdate with some enabled variant, discount type rupiah and create slash price status, then bulkApplyListVariantItemUiModel value should be updated from discount settings data`() {
        val mockListVariantUiModel =
            getMockListVariantUiModelForApplyBulkUpdateWithCreateSlashPriceStatusId()
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValueBulkApply,
            mockEndDateValueBulkApply,
            mockBulkApplyRupiahDiscountAmountBulkApply,
            DiscountType.RUPIAH
        )
        viewModel.applyBulkUpdate(mockListVariantUiModel, mockDiscountSettings)
        val bulkApplyListVariantItemUiModel = viewModel.bulkApplyListVariantItemUiModel.value
        assert(bulkApplyListVariantItemUiModel?.firstOrNull {
            it.isEnabled
        }?.discountedPrice == mockBulkApplyRupiahDiscountAmountBulkApply)
    }

    @Test
    fun `When call applyBulkUpdate with some enabled variant, discount type percentage and ongoing slash price status, then bulkApplyListVariantItemUiModel value should be updated from discount settings data`() {
        val mockListVariantUiModel =
            getMockListVariantUiModelForApplyBulkUpdateWithOngoingSlashPriceStatusId()
        val mockDiscountSettings = getMockDiscountSettings(
            mockStartDateValueBulkApply,
            mockEndDateValueBulkApply,
            mockBulkApplyPercentageDiscountAmountBulkApply,
            DiscountType.PERCENTAGE
        )
        viewModel.applyBulkUpdate(mockListVariantUiModel, mockDiscountSettings)
        val bulkApplyListVariantItemUiModel = viewModel.bulkApplyListVariantItemUiModel.value
        assert(bulkApplyListVariantItemUiModel?.firstOrNull {
            it.isEnabled
        }?.discountedPercentage == mockBulkApplyPercentageDiscountAmountBulkApply)
    }

    @Test
    fun `When call applyBulkUpdate with some enabled variant, discount type rupiah and create slash price status but null start and end date from discount setting, then bulkApplyListVariantItemUiModel value of start and end date should return this day`() {
        val mockListVariantUiModel =
            getMockListVariantUiModelForApplyBulkUpdateWithCreateSlashPriceStatusId()
        val mockDiscountSettings = getMockDiscountSettings(
            null,
            null,
            mockBulkApplyRupiahDiscountAmountBulkApply,
            DiscountType.RUPIAH
        )
        viewModel.applyBulkUpdate(mockListVariantUiModel, mockDiscountSettings)
        val bulkApplyListVariantItemUiModel = viewModel.bulkApplyListVariantItemUiModel.value
        bulkApplyListVariantItemUiModel?.filter {
            it.isEnabled
        }?.forEach {
            assert(it.discountedPrice == mockBulkApplyRupiahDiscountAmountBulkApply)
            assert(it.startDate.toCalendar().get(Calendar.DAY_OF_WEEK) == Date().toCalendar().get(Calendar.DAY_OF_WEEK))
            assert(it.endDate.toCalendar().get(Calendar.DAY_OF_WEEK) == Date().toCalendar().get(Calendar.DAY_OF_WEEK))
        }
    }

    private fun getMockListVariantUiModelForApplyBulkUpdateWithCreateSlashPriceStatusId(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false,
                variantMinOriginalPrice = 100000,
                slashPriceStatusId = DiscountStatus.DEFAULT.toString()
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true,
                variantMinOriginalPrice = 100000,
                slashPriceStatusId = DiscountStatus.DEFAULT.toString(),
                valueErrorType = ShopDiscountManageProductDiscountErrorValidation.NONE,
                discountedPrice = 10000
            )
        )
    }

    private fun getMockListVariantUiModelForApplyBulkUpdateWithOngoingSlashPriceStatusId(): List<ShopDiscountManageProductVariantItemUiModel> {
        return listOf(
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = false,
                variantMinOriginalPrice = 100000,
                slashPriceStatusId = DiscountStatus.ONGOING.toString()
            ),
            ShopDiscountManageProductVariantItemUiModel(
                isEnabled = true,
                variantMinOriginalPrice = 100000,
                slashPriceStatusId = DiscountStatus.ONGOING.toString(),
                valueErrorType = ShopDiscountManageProductDiscountErrorValidation.NONE,
                discountedPrice = 10000
            )
        )
    }

    private fun getMockDiscountSettings(
        startDate: Date? = null,
        endDate: Date? = null,
        discountAmount: Int,
        discountType: DiscountType
    ): DiscountSettings {
        return DiscountSettings(
            startDate = startDate,
            endDate = endDate,
            discountAmount = discountAmount,
            discountType = discountType,
            maxPurchaseQuantity = 1,
            isUsingCustomPeriod = false
        )
    }

}