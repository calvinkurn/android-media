package com.tokopedia.play.analytic.voucher

import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel

/**
 * @author by astidhiyaa on 06/09/22
 */
interface PlayVoucherAnalytic {

    fun impressVoucherWidget(voucherId: String)
    fun clickVoucherWidget(voucherId: String)
    fun impressVoucherBottomSheet(voucherId: String)
    fun impressToasterPrivate(voucherId: String)
    fun clickToasterPrivate(voucherId: String)
    fun clickToasterPublic()
    fun impressToasterPublic()
    fun swipeWidget(voucherId: String)

    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel
        ): PlayVoucherAnalytic
    }
}