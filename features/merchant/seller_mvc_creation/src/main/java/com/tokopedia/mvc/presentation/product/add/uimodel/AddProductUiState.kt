package com.tokopedia.mvc.presentation.product.add.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.util.constant.NumberConstant

data class AddProductUiState(
    val isLoading: Boolean = true,
    val page: Int = NumberConstant.FIRST_PAGE,
    val totalProducts: Int = 0,
    val searchKeyword: String = "",
    val products: List<Product> = emptyList(),
    val voucherCreationMetadata: VoucherCreationMetadata? = null,
    val warehouses: List<Warehouse> = emptyList(),
    val selectedProductsIds: List<Long> = emptyList(),
    val isSelectAllActive: Boolean = false,
    val sortOptions: List<ProductSortOptions> = emptyList(),
    val categoryOptions: List<ProductCategoryOption> = emptyList(),
    val shopShowcases: List<ShopShowcase> = emptyList(),
    val selectedSort: ProductSortOptions = ProductSortOptions("DEFAULT", "", "DESC"),
    val selectedWarehouseLocation: Warehouse = Warehouse(0, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION),
    val selectedShopShowcase: ShopShowcase = ShopShowcase(0, "", "", 1),
    val selectedCategories: List<ProductCategoryOption> = emptyList(),
    val error: Throwable? = null
)
