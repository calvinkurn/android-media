package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import javax.inject.Inject

class CheckoutDataHelper @Inject constructor() {

    fun getOrderProducts(listData: List<CheckoutItem>, cartStringGroup: String): List<CheckoutItem> {
        val products = arrayListOf<CheckoutItem>()
        for (checkoutItem in listData) {
            if (checkoutItem is CheckoutProductModel && checkoutItem.cartStringGroup == cartStringGroup) {
                products.add(checkoutItem)
            }
            if (checkoutItem is CheckoutProductBenefitModel && checkoutItem.cartStringGroup == cartStringGroup) {
                products.add(checkoutItem)
            }
            if (checkoutItem is CheckoutOrderModel && checkoutItem.cartStringGroup == cartStringGroup) {
                break
            }
        }
        return products
    }

    fun getAllProductCategoryIds(listData: List<CheckoutItem>): List<Long> {
        return listData.mapNotNull { checkoutItem ->
            if (checkoutItem is CheckoutProductModel && !checkoutItem.isError) {
                checkoutItem.productCatId
            } else {
                null
            }
        }
    }
}
