package com.tokopedia.thankyou_native.helper

import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment.Companion.CARD_NUMBER_MASKING_UNICODE
import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment.Companion.LAST_NUMBERS

fun String.getMaskedNumberSubStringPayment(): String {
    return CARD_NUMBER_MASKING_UNICODE + this.substring(this.length - LAST_NUMBERS)
}
