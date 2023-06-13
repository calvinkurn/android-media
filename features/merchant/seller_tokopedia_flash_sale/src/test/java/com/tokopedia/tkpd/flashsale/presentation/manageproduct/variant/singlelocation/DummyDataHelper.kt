package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product

object DummyDataHelper {
    fun generateDummyProductData(): Product {
        return Product(childProducts = listOf(generateDummyChildProduct()))
    }

    fun generateDummyChildProduct(): Product.ChildProduct {
        return Product.ChildProduct(
            "",
            false,
            false,
            false,
            "",
            "",
            Product.Price(0, 0, 0),
            Product.ProductCriteria(0, 0, 0, 0, 0),
            0,
            0,
            0,
            "",
            0,
            "",
            generateDummyWarehouses()
        )
    }

    fun generateDummyChildProductToggledOn(isWarehouseEnabled: Boolean = true): Product.ChildProduct {
        return Product.ChildProduct(
            "",
            false,
            false,
            true,
            "",
            "",
            Product.Price(0, 0, 0),
            Product.ProductCriteria(0, 0, 0, 0, 0),
            0,
            0,
            0,
            "",
            0,
            "",
            generateDummyWarehousesToggledOn(isWarehouseEnabled)
        )
    }

    fun generateDummyWarehouses(): List<Product.Warehouse> {
        return listOf(
            Product.Warehouse(
                0,
                "",
                0,
                0,
                Product.Warehouse.DiscountSetup(0, 0, 0),
                false,
                false,
                false,
                ""
            )
        )
    }

    fun generateDummyWarehousesToggledOn(isWarehouseEnabled: Boolean): List<Product.Warehouse> {
        return listOf(
            Product.Warehouse(
                0,
                "",
                0,
                0,
                Product.Warehouse.DiscountSetup(0, 0, 0),
                false,
                isWarehouseEnabled,
                false,
                ""
            )
        )
    }

    fun generateDummyCriteria(): Product.ProductCriteria {
        return Product.ProductCriteria(
            0,
            0,
            0,
            0,
            0,
            0
        )
    }

    fun generateDummyCriteriaResult(): List<CriteriaCheckingResult> {
        return listOf(CriteriaCheckingResult())
    }
}
