package com.tokopedia.product.estimasiongkir.view.bottomsheet

/**
 * Created by Yehezkiel on 16/02/21
 */
interface ProductDetailShippingListener {
    fun onChooseAddressClicked()
    fun openUspBottomSheet(uspImageUrl: String)
    fun refreshPage(height: Int)

    fun impressScheduledDelivery(prices: List<Pair<String, String>>, date: String)
}
