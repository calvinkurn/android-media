package com.tokopedia.product.estimasiongkir.view.bottomsheet

/**
 * Created by Yehezkiel on 16/02/21
 */
interface ProductDetailShippingListener {
    fun onChooseAddressClicked()
    fun openUspBottomSheet(freeOngkirUrl: String, uspImageUrl: String)
    fun refreshPage(height: Int)
}