package com.tokopedia.thankyou_native.presentation.adapter.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.BankTransfer
import com.tokopedia.thankyou_native.data.mapper.Jenius
import com.tokopedia.thankyou_native.data.mapper.KlikBCA
import com.tokopedia.thankyou_native.data.mapper.PaymentTypeMapper
import com.tokopedia.thankyou_native.data.mapper.Retail
import com.tokopedia.thankyou_native.data.mapper.VirtualAccount
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import kotlinx.android.synthetic.main.thank_fragment_deferred.*

data class WaitingHeaderUiModel(
    val title: String,
    val description: String,
    val timeRemaining: Long,
    val accountIdLabel: String,
    val accountId: String,
    val accountImage: String,
    val amountLabel: String,
    val amount: Long,
    val note: String,
    val shouldHighlightLastThreeDigits: Boolean,
    val shouldHidePrimaryButton: Boolean,
    val primaryButtonText: String,
    val shouldHideSecondaryButton: Boolean,
    val secondaryButtonText: String
) : Visitable<BottomContentFactory> {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(thanksPageData: ThanksPageData, context: Context?): WaitingHeaderUiModel {

            val accountIdLabel = when (PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)) {
                VirtualAccount -> "Nomor Virtual Account"
                KlikBCA -> "User ID KlikBCA"
                Jenius -> "Cashtag"
                Retail -> "Kode Bayar"
                BankTransfer -> "Nomor Rekening"
                else -> "Nomor Virtual Account"
            }

            val note = if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage.wtvText.isNullOrBlank()) {
                context?.getString(R.string.thank_processing_payment_check_order)
            } else {
                 thanksPageData.customDataMessage.wtvText
            }

            val primaryButtonText = if (thanksPageData.customDataMessage?.wtvText.isNullOrEmpty()) context?.getString(R.string.thank_check_payment_status) else thanksPageData.customDataMessage?.wtvText

            return WaitingHeaderUiModel(
                "Bayar Sebelum",
                thanksPageData.expireTimeStr,
                thanksPageData.expireTimeUnix,
                accountIdLabel,
                thanksPageData.additionalInfo.accountDest,
                thanksPageData.gatewayImage,
                "Total Tagihan",
                thanksPageData.amount,
                note.orEmpty(),
                thanksPageData.paymentType == "BANKTRANSFER",
                thanksPageData.configFlagData?.shouldHideHomeButton == true,
                primaryButtonText.orEmpty(),
                false,
                context?.getString(R.string.thank_see_payment_methods).orEmpty()
            )
        }
    }
}
