package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.Warehouse

data class AddProductUiState(
    val isLoading: Boolean = true,
    val products : List<Product> = emptyList(),
    val voucherCreationMetadata: VoucherCreationMetadata? = null,
    val warehouses : List<Warehouse> = emptyList(),
    val selectedProductsIds : List<Long> = emptyList(),
    val isSelectAllActive: Boolean = false,
    val sortOptions: List<ProductSortOptions> = emptyList(),
    val categoryOptions: List<ProductCategoryOption> = emptyList(),
    val shopShowcases : List<ShopShowcase> = emptyList(),
    val error : Throwable? = null
)
