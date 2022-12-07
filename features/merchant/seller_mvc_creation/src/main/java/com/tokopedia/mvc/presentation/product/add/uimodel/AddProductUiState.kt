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
    val isSelectAllCheckboxActive: Boolean = false,
    val sortOptions: List<ProductSortOptions> = emptyList(),
    val categoryOptions: List<ProductCategoryOption> = emptyList(),
    val shopShowcases: List<ShopShowcase> = emptyList(),
    val selectedSort: ProductSortOptions = ProductSortOptions("DEFAULT", "", "DESC"),
    val defaultWarehouseLocationId: Long = 0,
    val selectedWarehouseLocation: Warehouse = Warehouse(0, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION),
    val selectedShopShowcase: List<ShopShowcase> = emptyList(),
    val selectedCategories: List<ProductCategoryOption> = emptyList(),
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(
        benefitIdr = 0,
        benefitMax = 0,
        benefitPercent = 0,
        benefitType = BenefitType.NOMINAL,
        promoType = PromoType.CASHBACK,
        minPurchase = 0,
        productIds = emptyList(),
        isVoucherProduct = true,
        targetBuyer = VoucherTargetBuyer.ALL_BUYER
    ),
    val error: Throwable? = null
)
