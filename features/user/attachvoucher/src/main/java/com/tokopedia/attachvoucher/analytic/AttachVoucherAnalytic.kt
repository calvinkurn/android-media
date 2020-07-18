package com.tokopedia.attachvoucher.analytic

import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase.MVFilter.VoucherType
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel
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
        const val CLICK_FILTER_VOUCHER = "click filter shop voucher"
    }

    // #AV2
    fun trackOnChangeFilter(type: Int) {
        if (type == AttachVoucherViewModel.NO_FILTER) return
        val label = when (type) {
            VoucherType.paramCashback -> "cashback"
            VoucherType.paramFreeOngkir -> "gratis ongkir"
            else -> ""
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        Name.CHAT_DETAIL,
                        Category.CHAT_DETAIL,
                        Action.CLICK_FILTER_VOUCHER,
                        label
                )
        )
    }

    // #AV3
    fun trackOnAttachVoucher(voucher: VoucherUiModel) {
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