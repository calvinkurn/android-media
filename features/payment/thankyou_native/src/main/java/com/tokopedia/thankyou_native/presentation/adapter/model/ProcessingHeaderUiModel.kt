package com.tokopedia.thankyou_native.presentation.adapter.model

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.GatewayAdditionalData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.utils.currency.CurrencyFormatUtil
import okhttp3.internal.format

data class ProcessingHeaderUiModel(
    val title: String,
    val description: String,
    val methodLabel: String,
    val method: String,
    val methodImage: String,
    val amountLabel: String,
    val amount: String,
    val installment: String,
    val note: List<String>,
    val shouldHidePrimaryButton: Boolean,
    val primaryButtonText: String,
    val primaryButtonApplink: String,
    val primaryButtonType: String,
    val shouldHideSecondaryButton: Boolean,
    val secondaryButtonText: String,
    val secondaryButtonApplink: String,
    val secondaryButtonType: String,
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(thanksPageData: ThanksPageData, context: Context?): ProcessingHeaderUiModel {

            val title = if (thanksPageData.customDataMessage?.title.isNullOrEmpty())
                context?.getString(R.string.thank_payment_in_progress_v2)
            else thanksPageData.customDataMessage?.title

            val description = if (thanksPageData.customDataMessage?.customSubtitleV2.isNullOrEmpty())
                format(context?.getString(R.string.thank_payment_in_progress_time).orEmpty(), thanksPageData.gatewayName)
            else thanksPageData.customDataMessage?.customSubtitleV2

            val note = Gson().fromJson(thanksPageData.customDataMessage?.customNotes, Array<String>::class.java)

            val installment = thanksPageData.gatewayAdditionalDataList?.firstOrNull {
                thanksPageData.gatewayName == it.key
            }?.value

            return ProcessingHeaderUiModel(
                title.orEmpty(),
                description.orEmpty(),
                context?.getString(R.string.thank_pay_method).orEmpty(),
                thanksPageData.gatewayName,
                thanksPageData.gatewayImage,
                context?.getString(R.string.thank_total_pay).orEmpty(),
                CurrencyFormatUtil.convertPriceValueToIdrFormat(thanksPageData.amount, false),
                installment.orEmpty(),
                note.orEmpty().toList(),
                thanksPageData.ctaDataThanksPage.primary.hideButton,
                thanksPageData.ctaDataThanksPage.primary.text,
                thanksPageData.ctaDataThanksPage.primary.applink,
                thanksPageData.ctaDataThanksPage.primary.type,
                thanksPageData.ctaDataThanksPage.secondary.hideButton,
                thanksPageData.ctaDataThanksPage.secondary.text,
                thanksPageData.ctaDataThanksPage.secondary.applink,
                thanksPageData.ctaDataThanksPage.secondary.type,
            )
        }

        const val TAG = "processing_header"
    }
}

