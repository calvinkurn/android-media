package com.tokopedia.product.detail.postatc.base

interface PostAtcListener {
    fun impressComponent(componentTrackData: ComponentTrackData)

    fun onClickLihatKeranjang(cartId: String, componentTrackData: ComponentTrackData)

    fun goToAppLink(appLink: String)
    fun goToProduct(productId: String)
    fun refreshPage()

    fun fetchRecommendation(pageName: String, uniqueId: Int)
    fun removeComponent(position: Int)
}
