package com.tokopedia.tkpd.flashsale.presentation.avp

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import javax.inject.Inject

class ManageProductVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private lateinit var productData: ReservedProduct.Product

    fun setupInitiateProductData(product: ReservedProduct.Product){
        productData = product
    }

    fun setItemToggleValue(itemPosition: Int, value: Boolean) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.isToggleOn = value
    }

    fun setDiscountAmount(itemPosition: Int, value: Long) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.price.price = value.toString()
    }

    fun getFinalProductData(): ReservedProduct.Product {
        return productData
    }
}