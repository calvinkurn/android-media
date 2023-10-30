package com.tokopedia.content.product.picker.seller.analytic

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
interface ContentPinnedProductAnalytic {

    fun onClickPinProductLiveRoom(productId: String)

    fun onClickPinProductBottomSheet(productId: String)

    fun onImpressPinProductLiveRoom(productId: String)

    fun onImpressPinProductBottomSheet(productId: String)

    fun onImpressFailPinProductLiveRoom()

    fun onImpressFailPinProductBottomSheet()

    fun onImpressFailUnPinProductLiveRoom()

    fun onImpressFailUnPinProductBottomSheet()

    fun onImpressColdDownPinProductSecondEvent(isLiveRoom: Boolean)
}
