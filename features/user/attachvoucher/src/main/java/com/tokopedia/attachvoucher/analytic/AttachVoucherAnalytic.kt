package com.tokopedia.attachvoucher.analytic

import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class AttachVoucherAnalytic @Inject constructor() {

    object Name {
        const val CHAT_DETAIL = "clickChatDetail"
    }

    object Category {
        const val CHAT_DETAIL = "chat detail"
    }

    object Action {
        const val CLICK_ATTACH_VOUCHER = "click pilih shop voucher"
    }

    fun trackOnAttachVoucher(voucher: Voucher) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        Name.CHAT_DETAIL,
                        Category.CHAT_DETAIL,
                        Action.CLICK_ATTACH_VOUCHER,
                        ""
                )
        )
    }
}