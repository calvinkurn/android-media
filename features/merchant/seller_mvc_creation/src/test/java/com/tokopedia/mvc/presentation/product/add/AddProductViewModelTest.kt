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
import io.mockk.coVerify
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

    //region FetchRequiredData
    @Test
    fun `when FetchRequiredData function first called, should set voucher configuration and previously selected products fields correctly`() {
        runBlockingTest {
            //Given
            val pageMode = PageMode.CREATE
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProducts()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()

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


    //getProductsAndProductsMetadata success page mode create

    @Test
    fun `When get voucher metadata in voucher creation mode, action should be VoucherAction CREATE`() = runBlockingTest{
        val pageMode = PageMode.CREATE
        val voucherConfiguration = buildVoucherConfiguration()
        val previouslySelectedProducts = populateProducts()

        mockShopWarehouseGqlCall()
        mockInitiateVoucherPageGqlCall()
        mockGetProductListMetaGqlCall()
        mockGetShopShowcasesGqlCall()
        mockGetProductListGqlCall()
        mockVoucherValidationPartialGqlCall()


        //When
        viewModel.processEvent(AddProductEvent.FetchRequiredData(pageMode, voucherConfiguration, previouslySelectedProducts))


        //Then
        val expectedParam = GetInitiateVoucherPageUseCase.Param(VoucherAction.CREATE, PromoType.FREE_SHIPPING, isVoucherProduct = true)
        coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
    }

    //getProductsAndProductsMetadata success page mode edit
    @Test
    fun `When get voucher metadata in edit voucher mode, action should be VoucherAction UPDATE`() {
        runBlockingTest {
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProducts()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    previouslySelectedProducts
                )
            )


            //Then
            val expectedParam = GetInitiateVoucherPageUseCase.Param(
                VoucherAction.UPDATE,
                PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
        }
    }

    //getProductsAndProductsMetadata success page mode duplicate
    @Test
    fun `When get voucher metadata in duplicate voucher mode, action should be VoucherAction UPDATE`() {
        runBlockingTest {
            val pageMode = PageMode.EDIT
            val voucherConfiguration = buildVoucherConfiguration()
            val previouslySelectedProducts = populateProducts()

            mockShopWarehouseGqlCall()
            mockInitiateVoucherPageGqlCall()
            mockGetProductListMetaGqlCall()
            mockGetShopShowcasesGqlCall()
            mockGetProductListGqlCall()
            mockVoucherValidationPartialGqlCall()


            //When
            viewModel.processEvent(
                AddProductEvent.FetchRequiredData(
                    pageMode,
                    voucherConfiguration,
                    previouslySelectedProducts
                )
            )


            //Then
            val expectedParam = GetInitiateVoucherPageUseCase.Param(
                VoucherAction.UPDATE,
                PromoType.FREE_SHIPPING,
                isVoucherProduct = true
            )
            coVerify { getInitiateVoucherPageUseCase.execute(expectedParam) }
        }
    }


    //getProductsAndProductsMetadata success default warehouse is exist
    //getProductsAndProductsMetadata success default warehouse is null

    //getProductsAndProductsMetadata error, ui effect should emit Error, ui state error should contain throwable

    //getProducts success category ids is not empty
    //getProducts success showcase ids is not empty
    //getProducts success currentPageParentProductsIds is not empty
    //getProducts error ui effect should emit Error, ui state error should contain throwable

    //voucher validation partial success
    //voucher validation partial error

    //endregion


    private fun buildVoucherConfiguration(): VoucherConfiguration {
        return VoucherConfiguration().copy(
            startPeriod = Date(2023, 1, 1, 0, 0, 0),
            endPeriod = Date(2023, 2, 1, 0, 0, 0)
        )
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


    private fun mockShopWarehouseGqlCall(warehouseId: Long = 1) {
        val shopWarehouseParam = GetShopWarehouseLocationUseCase.Param()
        val shopWarehouseResponse = listOf(Warehouse(warehouseId = warehouseId, warehouseName = "Bekasi", WarehouseType.WAREHOUSE))

        coEvery { getShopWarehouseLocationUseCase.execute(shopWarehouseParam) } returns shopWarehouseResponse
    }

    private fun mockInitiateVoucherPageGqlCall() {
        val initiateVoucherPageParam = GetInitiateVoucherPageUseCase.Param(
            VoucherAction.CREATE,
            PromoType.FREE_SHIPPING,
            isVoucherProduct = true
        )
        val initiateVoucherResponse = VoucherCreationMetadata(
            accessToken = "accessToken",
            isEligible = 1,
            maxProduct = 100,
            prefixVoucherCode = "OFC",
            shopId = 1,
            token = "token",
            userId = 1,
            discountActive = true,
            message = ""
        )
        coEvery { getInitiateVoucherPageUseCase.execute(initiateVoucherPageParam) } returns initiateVoucherResponse
    }

    private fun mockGetProductListMetaGqlCall(warehouseId: Long = 2) {
        val getProductListMetaParam = ProductListMetaUseCase.Param(warehouseId)
        val getProductListMetaResponse = ProductMetadata(sortOptions = listOf(), categoryOptions = listOf())
        coEvery { getProductListMetaUseCase.execute(getProductListMetaParam) } returns getProductListMetaResponse
    }

    private fun mockGetShopShowcasesGqlCall(showcases: List<ShopShowcase> = listOf()) {
        coEvery { getShopShowcasesByShopIDUseCase.execute() } returns showcases
    }

    private fun mockGetProductListGqlCall(warehouseId: Long = 2) {
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
        val getProductsResponse = ProductResult(total = 20, listOf())
        coEvery { getProductsUseCase.execute(getProductsParam) } returns getProductsResponse
    }

    private fun mockVoucherValidationPartialGqlCall() {
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
                benefitIdr = "",
                benefitMax = "",
                benefitPercent = "",
                benefitType = "",
                code = "",
                couponName = "",
                couponType = "",
                dateEnd = "",
                dateStart = "",
                hourEnd = "",
                hourStart = "",
                image = "",
                imageSquare = "",
                isPublic = "",
                minPurchase = "",
                minPurchaseType = "",
                minimumTierLevel = "",
                quota = "",
            ),
            validationProduct = emptyList()
        )
        coEvery { voucherValidationPartialUseCase.execute(voucherValidationParam) } returns voucherValidationResponse
    }


}
