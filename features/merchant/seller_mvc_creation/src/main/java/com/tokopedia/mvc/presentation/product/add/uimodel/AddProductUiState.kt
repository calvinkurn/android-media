package com.tokopedia.mvc.presentation.product.add.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.util.constant.NumberConstant

data class AddProductUiState(
    val isLoading: Boolean = true,
    val isFilterActive: Boolean = false,
    val page: Int = NumberConstant.FIRST_PAGE,
    val totalProducts: Int = 0,
    val searchKeyword: String = "",
    val products: List<Product> = emptyList(),
    val maxProductSelection: Int = 0,
    val warehouses: List<Warehouse> = emptyList(),
    val selectedProductsIds: Set<Long> = emptySet(),
    val selectedProductCount: Int = 0,
    val checkboxState: CheckboxState = CheckboxState.UNCHECKED,
    val sortOptions: List<ProductSortOptions> = emptyList(),
    val categoryOptions: List<ProductCategoryOption> = emptyList(),
    val shopShowcases: List<ShopShowcase> = emptyList(),
    val selectedSort: ProductSortOptions = ProductSortOptions("DEFAULT", "", "DESC"),
    val defaultWarehouseLocationId: Long = 0,
    val selectedWarehouseLocation: Warehouse = Warehouse(0, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION),
    val selectedShopShowcase: List<ShopShowcase> = emptyList(),
    val selectedCategories: List<ProductCategoryOption> = emptyList(),
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(
        voucherId = NumberConstant.VOUCHER_ID_NOT_CREATED,
        benefitIdr = 0,
        benefitMax = 0,
        benefitPercent = 0,
        benefitType = BenefitType.NOMINAL,
        promoType = PromoType.CASHBACK,
        isVoucherProduct = true,
        minPurchase = 0,
        productIds = emptyList(),
        targetBuyer = VoucherTargetBuyer.ALL_BUYER
    ),
    val previouslySelectedProducts: List<Product> = emptyList(),
    val error: Throwable? = null
) {
    enum class CheckboxState {
        CHECKED,
        UNCHECKED,
        INDETERMINATE
    }
}
