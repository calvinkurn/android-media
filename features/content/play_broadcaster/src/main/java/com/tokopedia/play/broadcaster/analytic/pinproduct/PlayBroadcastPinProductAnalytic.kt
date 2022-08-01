package com.tokopedia.play.broadcaster.analytic.pinproduct

/**
 * Created by fachrizalmrsln on 01/08/22.
 */
interface PlayBroadcastPinProductAnalytic {

    fun onClickPinProductLiveRoom(channelId: String, productId: String)

    fun onClickPinProductBottomSheet(channelId: String, productId: String)

    fun onImpressPinProductLiveRoom(channelId: String, productId: String)

    fun onImpressPinProductBottomSheet(channelId: String, productId: String)

    fun onImpressFailPinProductLiveRoom(channelId: String)

    fun onImpressFailPinProductBottomSheet(channelId: String)

    fun onImpressFailUnPinProductLiveRoom(channelId: String)

    fun onImpressFailUnPinProductBottomSheet(channelId: String)

}