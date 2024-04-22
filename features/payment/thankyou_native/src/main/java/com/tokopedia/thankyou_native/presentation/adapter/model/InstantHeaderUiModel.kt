package com.tokopedia.thankyou_native.presentation.adapter.model

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.utils.currency.CurrencyFormatUtil
import okhttp3.internal.format

data class InstantHeaderUiModel(
    val gatewayImage: String,
    val title: String,
    val description: String,
    val note: List<String>,
    val shouldHidePrimaryButton: Boolean,
    val primaryButtonText: String,
    val primaryButtonApplink: String,
    val primaryButtonType: String,
    val shouldHideSecondaryButton: Boolean,
    val secondaryButtonText: String,
    val secondaryButtonApplink: String,
    val secondaryButtonType: String,
    val promoFlags: String,
    val totalDeduction: Float
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(thanksPageData: ThanksPageData, context: Context?): InstantHeaderUiModel {

            val title = if (thanksPageData.customDataMessage?.title.isNullOrEmpty())
                    context?.getString(R.string.thank_instant_payment_successful_v2)
                else thanksPageData.customDataMessage?.title

            val description = if (thanksPageData.customDataMessage?.customSubtitleV2.isNullOrEmpty())
                format(context?.getString(R.string.thank_total_pay_rp).orEmpty(), CurrencyFormatUtil.convertPriceValueToIdrFormat(thanksPageData.amount, false))
            else thanksPageData.customDataMessage?.customSubtitleV2

            val note = Gson().fromJson(thanksPageData.customDataMessage?.customNotes, Array<String>::class.java)

            return InstantHeaderUiModel(
                thanksPageData.gatewayImage,
                title.orEmpty(),
                description.orEmpty(),
                note.orEmpty().toList(),
                thanksPageData.ctaDataThanksPage.primary.hideButton,
                thanksPageData.ctaDataThanksPage.primary.text,
                thanksPageData.ctaDataThanksPage.primary.applink,
                thanksPageData.ctaDataThanksPage.primary.type,
                thanksPageData.ctaDataThanksPage.secondary.hideButton,
                thanksPageData.ctaDataThanksPage.secondary.text,
                thanksPageData.ctaDataThanksPage.secondary.applink,
                thanksPageData.ctaDataThanksPage.secondary.type,
                thanksPageData.customDataOther?.promoFlags.orEmpty(),
                getTotalDeduction(thanksPageData)
            )
        }

        private fun getTotalDeduction(thanksPageData: ThanksPageData): Float {
            var totalDeduction = 0F
            if (thanksPageData.paymentDeductions == null) return 0f

            val allowedItemNames = arrayOf(
                PaymentDeductionKey.TOTAL_SHIPPING_DISCOUNT,
                PaymentDeductionKey.TOTAL_DISCOUNT,
                PaymentDeductionKey.CASH_BACK_OVO_POINT,
                PaymentDeductionKey.CASHBACK_STACKED
            )

            thanksPageData.paymentDeductions.filter { allowedItemNames.contains(it.itemName) }.forEach {
                totalDeduction += it.amount
            }

            return totalDeduction
        }

        const val TAG = "instant_header"
    }
}
