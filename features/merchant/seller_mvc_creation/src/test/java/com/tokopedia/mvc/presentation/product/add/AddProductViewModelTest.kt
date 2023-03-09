package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductMetadata
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class AddProductViewModelTest {


    @RelaxedMockK
    lateinit var getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase

    @RelaxedMockK
    lateinit var getShopShowcasesByShopIDUseCase: ShopShowcasesByShopIDUseCase

    @RelaxedMockK
    lateinit var getProductListMetaUseCase: ProductListMetaUseCase

    @RelaxedMockK
    lateinit var getProductsUseCase: ProductListUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var voucherValidationPartialUseCase: VoucherValidationPartialUseCase

    private lateinit var viewModel: AddProductViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AddProductViewModel(
            CoroutineTestDispatchersProvider,
            getShopWarehouseLocationUseCase,
            getShopShowcasesByShopIDUseCase,
            getProductListMetaUseCase,
            getProductsUseCase,
            getInitiateVoucherPageUseCase,
            voucherValidationPartialUseCase
        )
    }

    @Test
    fun `when FetchRequiredData function first called, should set voucher configuration and previously selected products fields correctly`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = VoucherConfiguration().copy(startPeriod = Date(2023, 1, 1, 0,0,0), endPeriod = Date(2023, 2, 1, 0,0,0))
            val previouslySelectedProducts = populateProducts()

            val maxProductSelection = 100
            val warehouseId : Long = 2

            val shopWarehouseParam = GetShopWarehouseLocationUseCase.Param()
            val shopWarehouseResponse = listOf(Warehouse(warehouseId = warehouseId, warehouseName = "Bekasi", WarehouseType.WAREHOUSE))


            coEvery { getShopWarehouseLocationUseCase.execute(shopWarehouseParam) } returns shopWarehouseResponse

            val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(VoucherAction.CREATE, PromoType.FREE_SHIPPING, isVoucherProduct = true)
            val initiateVoucherResponse = VoucherCreationMetadata(
                accessToken = "accessToken",
                isEligible = 1,
                maxProduct = maxProductSelection,
                prefixVoucherCode = "OFC",
                shopId = 1,
                token = "token",
                userId = 1,
                discountActive = true,
                message = ""
            )
            coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse

            val getProductListMetaParam = ProductListMetaUseCase.Param(warehouseId)
            val getProductListMetaResponse = ProductMetadata(sortOptions = listOf(), categoryOptions = listOf())
            coEvery { getProductListMetaUseCase.execute(getProductListMetaParam) } returns getProductListMetaResponse

            val showcases = listOf<ShopShowcase>()
            val getShopShowcasesResponse = showcases
            coEvery { getShopShowcasesByShopIDUseCase.execute() } returns getShopShowcasesResponse

            val getProductsParam = ProductListUseCase.Param(
                categoryIds = listOf(),
                page = 1,
                pageSize = 20,
                searchKeyword = "",
                showcaseIds = listOf(),
                sortDirection = "DESC",
                sortId = "DEFAULT",
                warehouseId = warehouseId
            )
            val getProductsResponse = ProductResult(total = maxProductSelection, listOf())
            coEvery { getProductsUseCase.execute(getProductsParam) } returns getProductsResponse


            val voucherValidationParam = VoucherValidationPartialUseCase.Param(
                benefitIdr = 0,
                benefitMax = 0,
                benefitPercent = 0,
                benefitType = BenefitType.NOMINAL,
                promoType = PromoType.FREE_SHIPPING,
                isVoucherProduct = false,
                minPurchase = 0,
                productIds = emptyList(),
                targetBuyer = VoucherTargetBuyer.ALL_BUYER,
                couponName = "",
                isPublic = true,
                code = "",
                isPeriod = false,
                periodType = 3,
                periodRepeat = 0,
                totalPeriod = 1,
                startDate = "3923-02-01",
                endDate = "3923-03-01",
                startHour = "00:00",
                endHour = "00:00",
                quota = 0
            )

            val voucherValidationResponse = VoucherValidationResult(
                totalAvailableQuota = 5,
                validationDate = emptyList(),
                validationError = VoucherValidationResult.ValidationError(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
                ),
                validationProduct = emptyList()
            )
            coEvery { voucherValidationPartialUseCase.execute(voucherValidationParam) } returns voucherValidationResponse

            val emittedValues = arrayListOf<AddProductUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            //When
            viewModel.processEvent(AddProductEvent.FetchRequiredData(pageMode, voucherConfiguration, previouslySelectedProducts))


            //Then
            val actual = emittedValues.last()

            assertEquals(voucherConfiguration, actual.voucherConfiguration)
            assertEquals(previouslySelectedProducts, actual.previouslySelectedProducts)

            job.cancel()
        }
    }


    private fun populateProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                isVariant = false,
                name = "",
                picture = "",
                preorder = Product.Preorder(durationDays = 0),
                price = Product.Price(min = 50_000, max = 50_000),
                sku = "sku-1",
                status = "",
                stock = 20,
                txStats = Product.TxStats(sold = 10),
                warehouseCount = 1,
                isEligible = true,
                ineligibleReason = "",
                originalVariants = emptyList(),
                selectedVariantsIds = emptySet(),
                isSelected = false,
                enableCheckbox = false,
                isDeletable = false,
            )
        )
    }

}
