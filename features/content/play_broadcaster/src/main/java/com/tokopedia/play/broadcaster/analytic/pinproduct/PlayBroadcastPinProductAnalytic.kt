package com.tokopedia.play.broadcaster.analytic.pinproduct

/**
 * Created by fachrizalmrsln on 01/08/22.
 */
interface PlayBroadcastPinProductAnalytic {

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