package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.mapper

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.item.ManageProductVariantItem
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.item.ManageProductVariantItemMultiloc

object ManageVariantMapper {

    fun ReservedProduct.Product.toSingleWarehouseVariantItems(): List<ManageProductVariantItem> {
        return this.childProducts.map {
            ManageProductVariantItem(
                disabledReason = it.disabledReason,
                isDisabled =  it.isDisabled,
                isMultiwarehouse = it.isMultiwarehouse,
                isToggleOn = it.isToggleOn,
                name = it.name,
                picture = it.picture,
                price = it.price,
                productCriteria = it.productCriteria,
                discountSetup = it.discountSetup,
                productId = it.productId,
                sku = it.sku,
                stock = it.stock,
                url = it.url,
                warehouses = it.warehouses
            )
        }
    }

    fun ReservedProduct.Product.ChildProduct.toSingleWarehouseVariantItem(): ManageProductVariantItem {
        return ManageProductVariantItem(
                disabledReason = disabledReason,
                isDisabled =  isDisabled,
                isMultiwarehouse = isMultiwarehouse,
                isToggleOn = isToggleOn,
                name = name,
                picture = picture,
                price = price,
                productCriteria = productCriteria,
                discountSetup = discountSetup,
                productId = productId,
                sku = sku,
                stock = stock,
                url = url,
                warehouses = warehouses
            )
    }

    fun ReservedProduct.Product.toMultiWarehouseVariantItems(): List<ManageProductVariantItemMultiloc> {
        return this.childProducts.map {
            ManageProductVariantItemMultiloc(
                disabledReason = it.disabledReason,
                isDisabled =  it.isDisabled,
                isMultiwarehouse = it.isMultiwarehouse,
                isToggleOn = it.isToggleOn,
                name = it.name,
                picture = it.picture,
                price = it.price,
                productCriteria = it.productCriteria,
                discountSetup = it.discountSetup,
                productId = it.productId,
                sku = it.sku,
                stock = it.stock,
                url = it.url,
                warehouses = it.warehouses
            )
        }
    }

    fun ReservedProduct.Product.ChildProduct.toMultiWarehouseVariantItem(): ManageProductVariantItemMultiloc {
        return ManageProductVariantItemMultiloc(
            disabledReason = disabledReason,
            isDisabled =  isDisabled,
            isMultiwarehouse = isMultiwarehouse,
            isToggleOn = isToggleOn,
            name = name,
            picture = picture,
            price = price,
            productCriteria = productCriteria,
            discountSetup = discountSetup,
            productId = productId,
            sku = sku,
            stock = stock,
            url = url,
            warehouses = warehouses
        )
    }
}