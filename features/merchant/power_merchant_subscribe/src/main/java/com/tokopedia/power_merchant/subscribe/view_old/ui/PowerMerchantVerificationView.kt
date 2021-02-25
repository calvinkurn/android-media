package com.tokopedia.power_merchant.subscribe.view_old.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo.*
import kotlinx.android.synthetic.main.layout_power_merchant_verification.view.*

class PowerMerchantVerificationView: LinearLayout {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_power_merchant_verification, this)
    }

    fun show(kycInfo: KycProjectInfo) {
        val status = when(kycInfo.status) {
            KYCConstant.STATUS_REJECTED -> context.getString(R.string.power_merchant_kyc_rejected)
            KYCConstant.STATUS_PENDING -> context.getString(R.string.power_merchant_kyc_pending)
            else -> context.getString(R.string.power_merchant_kyc_not_verified)
        }

        textStatus.text = MethodChecker.fromHtml(status)
    }
}