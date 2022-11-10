package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.Warehouse

sealed class AddProductEffect {
    data class LoadNextPageSuccess(
        val currentPageItems: List<Product>,
        val allItems: List<Product>
    ) : AddProductEffect()

    data class ShowSortBottomSheet(
        val sortOptions: List<ProductSortOptions>,
        val selectedSort: ProductSortOptions
    ) : AddProductEffect()

    data class ShowProductCategoryBottomSheet(
        val categories: List<ProductCategoryOption>,
        val selectedCategory: ProductCategoryOption
    ) : AddProductEffect()

    data class ShowShowcasesBottomSheet(
        val showcases: List<ShopShowcase>,
        val selectedShowcases: ShopShowcase
    ) : AddProductEffect()

    data class ShowWarehouseLocationBottomSheet(
        val warehouses: List<Warehouse>,
        val selectedWarehouse: Warehouse
    ) : AddProductEffect()
}
