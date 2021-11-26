package com.tokopedia.createpost.common.view.plist


interface ShopPageListener {
    fun shopProductImpressed(position: Int, product: ShopPageProduct)
    fun shopProductClicked(position: Int, product: ShopPageProduct)
    fun sortProductCriteriaClicked(criteria: String)
}