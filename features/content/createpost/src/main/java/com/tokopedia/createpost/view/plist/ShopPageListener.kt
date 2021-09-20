package com.tokopedia.createpost.view.plist


interface ShopPageListener {
    fun shopProductImpressed(position: Int, product: ShopPageProduct)
    fun shopProductClicked(position: Int, product: ShopPageProduct)
    fun sortProductCriteriaClicked(criteria: String)
}