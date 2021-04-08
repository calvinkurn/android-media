package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.view.type.MerchantVoucherType

/**
 * Created by jegul on 03/03/20
 */
sealed class PlayVoucherUiModel

data class MerchantVoucherUiModel(
        val id: String,
        val type: MerchantVoucherType,
        val title: String,
        val description: String,
        val code: String,
        val copyable: Boolean,
        val highlighted: Boolean,
        @TrackingField val impressHolder: ImpressHolder = ImpressHolder()
) : PlayVoucherUiModel()

object VoucherPlaceholderUiModel : PlayVoucherUiModel()