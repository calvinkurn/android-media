package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

object DataDummyOfVariantMultiLocationType {
    fun createDummyProduct(isWarehouseToggleOn : Boolean = false): ReservedProduct.Product {
        return ReservedProduct.Product(
            childProducts = createDummyChildsProduct(isWarehouseToggleOn),
            isMultiWarehouse = false,
            isParentProduct = true,
            name = "Judul Produk Bisa Sepanjang Dua Baris Kebawah",
            picture = "https://placekitten.com/100/100",
            price = ReservedProduct.Product.Price(
                price = 5000,
                lowerPrice = 4000,
                upperPrice = 6000
            ),
            productCriteria = ReservedProduct.Product.ProductCriteria(
                criteriaId = 0,
                maxCustomStock = 100,
                maxDiscount = 70,
                maxFinalPrice = 6000,
                minCustomStock = 11,
                minDiscount = 10,
                minFinalPrice = 2000
            ),
            productId = 0,
            sku = "SK-0918",
            stock = 30,
            url = "",
            warehouses = listOf()
        )
    }

    fun createDummyChildsProduct(isWarehouseToggleOn : Boolean): List<ReservedProduct.Product.ChildProduct> {
        val childsProduct: MutableList<ReservedProduct.Product.ChildProduct> = mutableListOf()
        for (child in 1 until 6) {
            val childProduct = ReservedProduct.Product.ChildProduct(
                disabledReason = "i don't know",
                isDisabled = false,
                isMultiwarehouse = true,
                isToggleOn = false,
                name = "Data | L",
                picture = "",
                price = ReservedProduct.Product.Price(
                    4000,
                    5000,
                    6000
                ),
                productCriteria = ReservedProduct.Product.ProductCriteria(
                    criteriaId = 0,
                    maxCustomStock = 100,
                    maxDiscount = 70,
                    maxFinalPrice = 6000,
                    minCustomStock = 11,
                    minDiscount = 10,
                    minFinalPrice = 2000
                ),
                0,
                productId = child.toLong(),
                sku = "SKU-$child",
                stock = 80,
                url = "",
                warehouses = createWarehousesDummy(isWarehouseToggleOn)
            )
            childsProduct.add(childProduct)
        }
        return childsProduct
    }

    fun createWarehousesDummy(isWarehouseToggleOn : Boolean): List<ReservedProduct.Product.Warehouse> {
        val listWarehouse = arrayListOf<ReservedProduct.Product.Warehouse>()
        for (child in 1 until 6) {
            val warehouse = ReservedProduct.Product.Warehouse(
                warehouseId = child.toLong(),
                name = "Warehouse - ${child}",
                stock = (10 * child).toLong(),
                price = 5000,
                discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                    discount = 10,
                    price = 5000,
                    stock = (10 * child).toLong()
                ),
                isDilayaniTokopedia = false,
                isToggleOn = isWarehouseToggleOn,
                isDisabled = false,
                disabledReason = ""
            )
            listWarehouse.add(warehouse)
        }
        return listWarehouse
    }

    fun createWarehouseDummy(stock: Long, price: Long): ReservedProduct.Product.Warehouse {
        return ReservedProduct.Product.Warehouse(
            warehouseId = 1,
            name = "Jakarta",
            stock = stock,
            price = price,
            discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                discount = 10,
                price = 5000,
                stock = 500
            ),
            isDilayaniTokopedia = false,
            isToggleOn = true,
            isDisabled = false,
            disabledReason = ""
        )
    }

    fun createDummyOfProductCriteria(): ReservedProduct.Product.ProductCriteria {
        return ReservedProduct.Product.ProductCriteria(
            criteriaId = 1,
            minFinalPrice = 2000,
            maxFinalPrice = 10000,
            minCustomStock = 10,
            maxCustomStock = 100,
            minDiscount = 20,
            maxDiscount = 60
        )
    }

    fun createDummyOfDiscountSetup(): ReservedProduct.Product.Warehouse.DiscountSetup {
        return ReservedProduct.Product.Warehouse.DiscountSetup(
            discount = 25,
            price = 4000,
            stock = 20
        )
    }

    fun createWarehousesDilayaniTokopedia(isToogleOn : Boolean = false) : List<ReservedProduct.Product.Warehouse> {
        val listWarehouse = arrayListOf<ReservedProduct.Product.Warehouse>()
        for (child in 1 until 6) {
            val warehouse = ReservedProduct.Product.Warehouse(
                warehouseId = child.toLong(),
                name = "Warehouse - ${child}",
                stock = (10 * child).toLong(),
                price = 5000,
                discountSetup = ReservedProduct.Product.Warehouse.DiscountSetup(
                    discount = 10,
                    price = 5000,
                    stock = (10 * child).toLong()
                ),
                isDilayaniTokopedia = child %2 == 0,
                isToggleOn = isToogleOn,
                isDisabled = false,
                disabledReason = ""
            )
            listWarehouse.add(warehouse)
        }
        return listWarehouse
    }

    fun createDummyOfListCriteria(nameList : ArrayList<String>) : List<CriteriaCheckingResult> {
        val list = arrayListOf<CriteriaCheckingResult>()
        nameList.forEach { name ->
            list.add(createDummyOfCriteria(name))
        }
        return list
    }

    private fun createDummyOfCriteria(name : String) : CriteriaCheckingResult {
        return CriteriaCheckingResult(name = name, locationResult =  createListOfCriteriaLocationResult())
    }

    fun createDummyOfListCriteriaWithEmptyLocation(nameList : ArrayList<String>) : List<CriteriaCheckingResult> {
        val list = arrayListOf<CriteriaCheckingResult>()
        nameList.forEach { name ->
            list.add(createDummyOfCriteriaWithEmptyLocation(name))
        }
        return list
    }

    private fun createDummyOfCriteriaWithEmptyLocation(name : String) : CriteriaCheckingResult {
        return CriteriaCheckingResult(name = name)
    }

    private fun createListOfCriteriaLocationResult() : List<CriteriaCheckingResult.LocationCheckingResult>{
        val list = arrayListOf<CriteriaCheckingResult.LocationCheckingResult>()
        for(child in 1 until 6){
            list.add(createCriteriaLocationResult("Warehouse - $child", true, child))
        }
        return list
    }

    private fun createCriteriaLocationResult(cityName : String, isDilayaniTokopedia : Boolean, index : Int) : CriteriaCheckingResult.LocationCheckingResult {
        return CriteriaCheckingResult.LocationCheckingResult(
            cityName, isDilayaniTokopedia, createPricerCriteria(true, index), createStockCriteria(true, index)
        )
    }

    private fun createPricerCriteria(isEligible : Boolean, index : Int) : CriteriaCheckingResult.PriceCheckingResult {
        return CriteriaCheckingResult.PriceCheckingResult(isEligible, min = 5L * index, max = 10L * index)
    }

    private fun createStockCriteria(isEligible : Boolean, index : Int) : CriteriaCheckingResult.StockCheckingResult {
        return CriteriaCheckingResult.StockCheckingResult(isEligible, min = 5L * index, max = 10L * index)
    }
}
