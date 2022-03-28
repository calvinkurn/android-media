package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductListUseCase
import com.tokopedia.vouchercreation.product.list.domain.usecase.ValidateVoucherUseCase
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class ManageProductViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var viewModel: ManageProductViewModel
    private lateinit var spykViewModel: ManageProductViewModel

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var validateVoucherUseCase: ValidateVoucherUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ManageProductViewModel(
            testCoroutineDispatcherProvider,
            getProductListUseCase,
            validateVoucherUseCase
        )
        spykViewModel = spyk(viewModel)
    }

    @Test
    fun `check if isSelectAllMode should return same value as mocked data`() {
        val mockValue = false
        viewModel.isSelectAllMode = mockValue
        assert(viewModel.isSelectAllMode == mockValue)
    }

    @Test
    fun `check if isSingleClick should return same value as mocked data`() {
        val mockValue = true
        viewModel.isSingleClick = mockValue
        assert(viewModel.isSingleClick == mockValue)
    }

    @Test
    fun `check if getProductListResultLiveData should return success value`() {
        val mockPageSize = 10
        val mockShopId = "123"
        val mockProductId1 = "123"
        val mockProductId2 = "1234"
        val mockListProductId = listOf(mockProductId1, mockProductId2)
        val mockProductListResponse = ProductListResponse(
            ProductList(
                Header(),
                listOf(getMockkProductData(mockProductId1), getMockkProductData(mockProductId2))
            )
        )
        coEvery { getProductListUseCase.executeOnBackground() } returns mockProductListResponse
        viewModel.getProductList(
            mockPageSize,
            mockShopId,
            mockListProductId
        )
        val liveDataResult = viewModel.getProductListResult.value
        assert(liveDataResult is Success)
        val liveDataProductListSize = (liveDataResult as Success).data.productList.data.size
        assert(liveDataProductListSize == mockProductListResponse.productList.data.size)
    }

    @Test
    fun `check if getProductListResultLiveData should return error value`() {
        coEvery { getProductListUseCase.executeOnBackground() } throws Exception()
        viewModel.getProductList()
        val liveDataResult = viewModel.getProductListResult.value
        assert(liveDataResult is Fail)
    }

    @Test
    fun `check if validateProductList should return success value`() {
        val mockProductParentId = "123455"
        coEvery {
            validateVoucherUseCase.executeOnBackground()
        } returns getMockVoucherValidationPartialResponse(mockProductParentId)
        viewModel.validateProductList(
            "",
            "",
            1,
            2,
            3,
            2,
            listOf()
        )
        val result = viewModel.validateVoucherResult.value
        assert(result is Success)
        val resultProductParentId =
            (result as Success).data.response.voucherValidationData.validationPartial.first().parentProductId
        assert(resultProductParentId == mockProductParentId)
    }

    @Test
    fun `check if validateProductList should return fail value`() {
        coEvery { validateVoucherUseCase.executeOnBackground() } throws Exception()
        viewModel.validateProductList(
            "",
            "",
            1,
            2,
            3,
            2,
            listOf()
        )
        val liveDataResult = viewModel.validateVoucherResult.value
        assert(liveDataResult is Fail)
    }

    @Test
    fun `check if updateProductUiModelsDisplayMode should return mocked data`() {
        val mockProductId1 = "1234"
        val mockProductId2 = "5678"
        val mockListVariantData = listOf(
            Pair("567", false),
            Pair("568", false)
        )
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockProductId1, mockListVariantData),
            getMockProductUiModelWithVariant(mockProductId2, mockListVariantData)
        )
        val result = viewModel.updateProductUiModelsDisplayMode(
            isViewing = false,
            isEditing = false,
            productList = mockProductList
        )
        assert(result.size == mockProductList.size)
        assert(result.first() == mockProductList.first())
    }

    @Test
    fun `check if filterSelectedProductVariant should return product list if there is selected variant`() {
        val mockProductId1 = "1234"
        val mockProductId2 = "5678"
        val mockListVariantData = listOf(
            Pair("567", true),
            Pair("568", false)
        )
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockProductId1, mockListVariantData),
            getMockProductUiModelWithVariant(mockProductId2, mockListVariantData)
        )
        val result = viewModel.filterSelectedProductVariant(mockProductList)
        assert(result.any { it.variants.isNotEmpty() })
    }

    @Test
    fun `check if filterSelectedProductVariant should return empty product list if there isn't selected variant`() {
        val mockProductId1 = "1234"
        val mockProductId2 = "5678"
        val mockListVariantData = listOf(
            Pair("567", false),
            Pair("568", false)
        )
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockProductId1, mockListVariantData),
            getMockProductUiModelWithVariant(mockProductId2, mockListVariantData)
        )
        val result = viewModel.filterSelectedProductVariant(mockProductList)
        assert(!result.any { it.variants.isNotEmpty() })
    }

    @Test
    fun `check if mapProductDataToProductUiModel should return same product id as mocked data`() {
        val mockProductId = "1234"
        val mockProductList = listOf(
            getMockkProductData(mockProductId)
        )
        val result = viewModel.mapProductDataToProductUiModel(
            isViewing = false,
            isEditing = false,
            mockProductList
        )
        assert(result.first().id == mockProductId)
    }

    @Test
    fun `check if applyValidationResult return value match with validationResults`() {
        val mockProductId = "1234"
        val mockProductVariantId = "12345"
        val mockProductList = listOf(
            getMockProductUiModel(mockProductId)
        )
        val mockValidationResult = listOf(
            getMockVoucherValidationPartialProduct(
                mockProductId,
                listOf(mockProductVariantId)
            )
        )
        val result = viewModel.applyValidationResult(
            mockProductList,
            mockValidationResult
        )
        assert(result.first().variants.first().sku.isNotEmpty())
    }

    @Test
    fun `check if setVariantSelection with match parent product id should return list with unselected variant`() {
        val mockParentProductId = "1234"
        val mockListChildProductId = listOf("56")
        val mockListVariantData = mockListChildProductId.map {
            Pair(it, true)
        }
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockParentProductId, mockListVariantData)
        )
        val result = viewModel.setVariantSelection(
            mockProductList,
            listOf(getMockProductId(mockParentProductId, mockListChildProductId)),
            false
        )
        assert(result.any { productUiModel ->
            productUiModel.variants.all { !it.isSelected }
        })
    }

    @Test
    fun `check if setVariantSelection with non matching parent product id should return list with same variant data as mocked`() {
        val mockParentProductId = "1234"
        val mockSelectedParentProductId = "12345"
        val mockListChildProductId = listOf("56")
        val mockListVariantData = mockListChildProductId.map {
            Pair(it, true)
        }
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockParentProductId, mockListVariantData)
        )
        val result = viewModel.setVariantSelection(
            mockProductList,
            listOf(getMockProductId(mockSelectedParentProductId, mockListChildProductId)),
            false
        )
        assert(result.any { productUiModel ->
            productUiModel.variants.all { it.isSelected }
        })
    }

    @Test
    fun `check if setVariantSelection with non matching variant id should return list with product with empty variant`() {
        val mockParentProductId = "1234"
        val mockListChildProductId = listOf("56")
        val mockListChildSelectedProductId = listOf("57")
        val mockListVariantData = mockListChildProductId.map {
            Pair(it, false)
        }
        val mockProductList = listOf(
            getMockProductUiModelWithVariant(mockParentProductId, mockListVariantData)
        )
        val result = viewModel.setVariantSelection(
            mockProductList,
            listOf(getMockProductId(mockParentProductId, mockListChildSelectedProductId)),
            false
        )
        assert(result.any { productUiModel ->
            productUiModel.variants.isEmpty()
        })
    }

    @Test
    fun `check if getProductUiModels should return the same data as set on setProductUiModels`() {
        val mockProductId = "123"
        val mockProductUiModel = getMockProductUiModel(mockProductId)
        viewModel.setProductUiModels(listOf(mockProductUiModel))
        val productUiModel = viewModel.getProductUiModels()
        assert(productUiModel.firstOrNull()?.id == mockProductId)
    }

    @Test
    fun `check if getIdsFromProductList should return the same data as mock data`() {
        val mockProductId = "123"
        val mockProductUiModel = getMockProductUiModel(mockProductId)
        val listProductId = viewModel.getIdsFromProductList(listOf(mockProductUiModel))
        assert(listProductId.firstOrNull().orEmpty() == mockProductId)
    }

    @Test
    fun `check if getIsViewing should return the same data as set on setIsViewing`() {
        val mockIsViewing = true
        viewModel.setIsViewing(mockIsViewing)
        val isViewingValue = viewModel.getIsViewing()
        assert(isViewingValue == mockIsViewing)
    }

    @Test
    fun `check if getIsEditing should return the same data as set on setIsEditing`() {
        val mockIsEditing = true
        viewModel.setIsEditing(mockIsEditing)
        val isEditingValue = viewModel.getIsEditing()
        assert(isEditingValue == mockIsEditing)
    }

    @Test
    fun `check if getMaxProductLimit should return the same data as set on setMaxProductLimit`() {
        val mockMaxProductLimit = 20
        viewModel.setMaxProductLimit(mockMaxProductLimit)
        val maxProductLimitValue = viewModel.getMaxProductLimit()
        assert(maxProductLimitValue == mockMaxProductLimit)
    }

    @Test
    fun `check if getCouponSettings should return the same data as set on setCouponSettings`() {
        val mockCouponSettings = getMockCouponSettings(CouponType.CASHBACK, DiscountType.NOMINAL)
        viewModel.setCouponSettings(mockCouponSettings)
        val couponSettingsValue = viewModel.getCouponSettings()
        assert(couponSettingsValue == mockCouponSettings)
    }

    @Test
    fun `check if getSelectedProductIds should return the same data as set on setSelectedProductIds`() {
        val mockParentProductId = "123"
        val listMockChildProductId = listOf("567", "789")
        val listMockProductId = arrayListOf(
            getMockProductId(mockParentProductId, listMockChildProductId)
        )
        viewModel.setSelectedProductIds(listMockProductId)
        val selectedProductIdsValue = viewModel.getSelectedProductIds()
        assert(selectedProductIdsValue == listMockProductId)
    }

    @Test
    fun `check if getSelectedParentProductIds should return the same data as set on setSelectedProductIds`() {
        val mockParentProductId = "1234"
        val listMockChildProductId = listOf("567", "789")
        val listMockProductId = arrayListOf(
            getMockProductId(mockParentProductId, listMockChildProductId)
        )
        viewModel.setSelectedProductIds(listMockProductId)
        val selectedProductIdsValue = viewModel.getSelectedParentProductIds()
        assert(selectedProductIdsValue.firstOrNull().orEmpty() == mockParentProductId)
    }

    @Test
    fun `check if selectedProductListLiveData should return the same data as set on setSetSelectedProducts`() {
        val mockProductId = "123"
        val listMockProductId = listOf(
            getMockProductUiModel(mockProductId)
        )
        viewModel.setSetSelectedProducts(listMockProductId)
        val selectedProductListValue = viewModel.selectedProductListLiveData.value
        assert(selectedProductListValue == listMockProductId)
    }

    @Test
    fun `check if getWarehouseLocationId should return the same data as set on setWarehouseLocationId`() {
        val mockWarehouseLocationId = "12345"
        viewModel.setWarehouseLocationId(mockWarehouseLocationId)
        val warehouseLocationIdValue = viewModel.getWarehouseLocationId()
        assert(warehouseLocationIdValue == mockWarehouseLocationId)
    }

    @Test
    fun `check if getBenefitType with CouponType FREE_SHIPPING should return AddProductViewModel BENEFIT_TYPE_IDR`() {
        val benefitType = viewModel.getBenefitType(
            getMockCouponSettings(
                CouponType.FREE_SHIPPING,
                DiscountType.NOMINAL
            )
        )
        assert(benefitType == AddProductViewModel.BENEFIT_TYPE_IDR)
    }

    @Test
    fun `check if getBenefitType with CouponType CASHBACK and DiscountType NOMINAL should return AddProductViewModel BENEFIT_TYPE_IDR`() {
        val benefitType = viewModel.getBenefitType(
            getMockCouponSettings(
                CouponType.CASHBACK,
                DiscountType.NOMINAL
            )
        )
        assert(benefitType == AddProductViewModel.BENEFIT_TYPE_IDR)
    }

    @Test
    fun `check if getBenefitType with CouponType CASHBACK and DiscountType PERCENTAGE should return AddProductViewModel BENEFIT_TYPE_PERCENT`() {
        val benefitType = viewModel.getBenefitType(
            getMockCouponSettings(
                CouponType.CASHBACK,
                DiscountType.PERCENTAGE
            )
        )
        assert(benefitType == AddProductViewModel.BENEFIT_TYPE_PERCENT)
    }

    @Test
    fun `check if getBenefitType with CouponType CASHBACK and DiscountType NONE should return AddProductViewModel BENEFIT_TYPE_IDR`() {
        val benefitType = viewModel.getBenefitType(
            getMockCouponSettings(
                CouponType.CASHBACK,
                DiscountType.NONE
            )
        )
        assert(benefitType == AddProductViewModel.BENEFIT_TYPE_IDR)
    }

    @Test
    fun `check if getBenefitType with CouponType DiscountType doesn't match anything then should return AddProductViewModel BENEFIT_TYPE_IDR`() {
        val benefitType = viewModel.getBenefitType(
            getMockCouponSettings(
                CouponType.NONE,
                DiscountType.NONE
            )
        )
        assert(benefitType == AddProductViewModel.BENEFIT_TYPE_IDR)
    }

    @Test
    fun `check if getCouponType with CouponType NONE then should return AddProductViewModel EMPTY_STRING`() {
        val couponType = viewModel.getCouponType(
            getMockCouponSettings(
                CouponType.NONE,
                DiscountType.NONE
            )
        )
        assert(couponType == AddProductViewModel.EMPTY_STRING)
    }

    @Test
    fun `check if getCouponType with CouponType CASHBACK then should return AddProductViewModel COUPON_TYPE_CASHBACK`() {
        val couponType = viewModel.getCouponType(
            getMockCouponSettings(
                CouponType.CASHBACK,
                DiscountType.NONE
            )
        )
        assert(couponType == AddProductViewModel.COUPON_TYPE_CASHBACK)
    }

    @Test
    fun `check if getCouponType with CouponType FREE_SHIPPING then should return AddProductViewModel COUPON_TYPE_SHIPPING`() {
        val couponType = viewModel.getCouponType(
            getMockCouponSettings(
                CouponType.FREE_SHIPPING,
                DiscountType.NONE
            )
        )
        assert(couponType == AddProductViewModel.COUPON_TYPE_SHIPPING)
    }

    @Test
    fun `check if getBenefitIdr should return same data as mocked data`() {
        val mockDiscountAmount = 30
        val mockCouponSettings = getMockCouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NONE,
            mockDiscountAmount
        )
        val benefitIdr = viewModel.getBenefitIdr(mockCouponSettings)
        assert(benefitIdr == mockDiscountAmount)
    }

    @Test
    fun `check if getBenefitMax should return same data as mocked data`() {
        val mockMaxDiscount = 5
        val mockCouponSettings = getMockCouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NONE,
            maxDiscount = mockMaxDiscount
        )
        val getBenefitMax = viewModel.getBenefitMax(mockCouponSettings)
        assert(getBenefitMax == mockMaxDiscount)
    }

    @Test
    fun `check if getBenefitPercent should return same data as mocked data`() {
        val mockDiscountPercentage = 50
        val mockCouponSettings = getMockCouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NONE,
            discountPercentage = mockDiscountPercentage
        )
        val getBenefitPercent = viewModel.getBenefitPercent(mockCouponSettings)
        assert(getBenefitPercent == mockDiscountPercentage)
    }

    @Test
    fun `check if getMinimumPurchase should return same data as mocked data`() {
        val mockMinimumPurchase = 5
        val mockCouponSettings = getMockCouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NONE,
            minimumPurchase = mockMinimumPurchase
        )
        val getMinimumPurchase = viewModel.getMinimumPurchase(mockCouponSettings)
        assert(getMinimumPurchase == mockMinimumPurchase)
    }

    @Test
    fun `check if resetProductUiModelState should return expected result`() {
        val mockProductId = "123"
        val mockListPairVariantData = listOf(
            Pair("567", true),
            Pair("5678", false)
        )
        val mockListProductUiModel = listOf(
            getMockProductUiModelWithVariant(
                mockProductId, mockListPairVariantData
            )
        )
        val listProductUiModel = viewModel.resetProductUiModelState(mockListProductUiModel)
        assert(listProductUiModel.all { productUiModel ->
            productUiModel.variants.all { !it.isSelected }
        })
    }

    private fun getMockProductId(
        parentProductId: String,
        listChildProductId: List<String>
    ): ProductId {
        return ProductId(
            parentProductId.toLong(),
            listChildProductId.map { it.toLong() }
        )
    }

    private fun getMockCouponSettings(
        couponType: CouponType,
        discountType: DiscountType,
        discountAmount: Int = 10,
        maxDiscount: Int = 10,
        discountPercentage: Int = 10,
        minimumPurchase: Int = 1
    ): CouponSettings {
        return CouponSettings(
            couponType,
            discountType,
            MinimumPurchaseType.NOMINAL,
            discountAmount,
            discountPercentage,
            maxDiscount,
            2,
            minimumPurchase,
            10L
        )
    }

    private fun getMockVoucherValidationPartialProduct(
        mockProductId: String,
        listVariantId: List<String>
    ): VoucherValidationPartialProduct {
        val listVariant = listVariantId.map {
            VariantValidationData(
                productId = it,
                sku = "567"
            )
        }
        return VoucherValidationPartialProduct(
            parentProductId = mockProductId,
            variants = listVariant
        )
    }

    private fun getMockProductUiModel(productId: String): ProductUiModel {
        return ProductUiModel(
            id = productId
        )
    }

    private fun getMockProductUiModelWithVariant(
        productId: String,
        listVariantData: List<Pair<String, Boolean>>,
    ): ProductUiModel {
        return ProductUiModel(
            id = productId,
            variants = listVariantData.map {
                VariantUiModel(
                    variantId = it.first,
                    isSelected = it.second
                )
            }
        )
    }

    private fun getMockVoucherValidationPartialResponse(parentProductId: String): VoucherValidationPartialResponse {
        return VoucherValidationPartialResponse(
            VoucherValidationPartial(
                voucherValidationData = VoucherValidationData(
                    listOf(getMockVoucherValidationPartialProduct(parentProductId, listOf()))
                )
            )
        )
    }

    private fun getMockkProductData(productId: String): ProductData {
        return ProductData(
            productId,
            name = "",
            GoodsPriceRange(0.0, 0.0),
            stock = 1,
            stockReserved = 1,
            hasStockReserved = true,
            hasInbound = true,
            status = "",
            minOrder = 1,
            maxOrder = 2,
            weight = 3,
            weightUnit = "asd",
            condition = "cond",
            isMustInsurance = true,
            isKreasiLokal = true,
            isCOD = true,
            isVariant = true,
            isCampaign = true,
            featured = 1,
            cashback = 2,
            url = "",
            sku = "sku",
            GoodsScore(0.0),
            listOf(),
            listOf(),
            listOf(GoodsPicture("URL")),
            GoodsPosition(1),
            GoodsTxStats(1),
            warehouseCount = 1,
            false,
            listOf(),
            bundleCount = 1,
            suspendLevel = 2
        )
    }
}