package com.tokopedia.thankyou_native.presentation.adapter.model

import android.content.Context
import com.google.gson.Gson
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
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment.Companion.JENIUS
import com.tokopedia.unifycomponents.ticker.TickerData
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
    val note: List<String>,
    val shouldHidePrimaryButton: Boolean,
    val primaryButtonText: String,
    val primaryButtonApplink: String,
    val primaryButtonType: String,
    val shouldHideSecondaryButton: Boolean,
    val secondaryButtonText: String,
    val secondaryButtonApplink: String,
    val secondaryButtonType: String,
    val highlightLastThreeDigits: Boolean,
    val bankBranch: String,
    val tickerData: List<TickerData> = arrayListOf(),
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(thanksPageData: ThanksPageData, context: Context?): WaitingHeaderUiModel {

            val accountIdLabel = when (PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)) {
                VirtualAccount -> {
                    if (thanksPageData.gatewayName == DeferredPaymentFragment.GATEWAY_KLIK_BCA)
                        context?.getString(R.string.thank_klikBCA_virtual_account_tag).orEmpty()
                    else if (thanksPageData.gatewayName == JENIUS)
                        context?.getString(R.string.cashtag).orEmpty()
                    else
                        context?.getString(R.string.thank_virtual_account_tag).orEmpty()
                }
                Retail -> context?.getString(R.string.thankyou_retail_account_label).orEmpty()
                BankTransfer -> context?.getString(R.string.thank_account_number).orEmpty()
                else -> context?.getString(R.string.thank_virtual_account_tag).orEmpty()
            }

            val amount = if (PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType) == VirtualAccount
                && thanksPageData.combinedAmount > thanksPageData.amount) {
                thanksPageData.combinedAmount
            } else {
                thanksPageData.amount
            }

            val note = Gson().fromJson(thanksPageData.customDataMessage?.customNotes, Array<String>::class.java)

            return WaitingHeaderUiModel(
                context?.getString(R.string.thank_pay_before).orEmpty(),
                thanksPageData.expireTimeStr,
                thanksPageData.expireTimeUnix - System.currentTimeMillis() / 1000,
                accountIdLabel,
                thanksPageData.additionalInfo.accountDest,
                thanksPageData.gatewayImage,
                context?.getString(R.string.thank_invoice_total_bill).orEmpty(),
                amount.toLong(),
                note.orEmpty().toList(),
                thanksPageData.ctaDataThanksPage.primary.hideButton,
                thanksPageData.ctaDataThanksPage.primary.text,
                thanksPageData.ctaDataThanksPage.primary.applink,
                thanksPageData.ctaDataThanksPage.primary.type,
                thanksPageData.ctaDataThanksPage.secondary.hideButton,
                thanksPageData.ctaDataThanksPage.secondary.text,
                thanksPageData.ctaDataThanksPage.secondary.applink,
                thanksPageData.ctaDataThanksPage.secondary.type,
                PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType) == BankTransfer,
                thanksPageData.additionalInfo.bankBranch
            )
        }

        const val TAG = "waiting_header"
    }
}
