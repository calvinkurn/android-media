package com.tokopedia.thankyou_native.helper

import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment.Companion.CARD_NUMBER_MASKING_UNICODE

fun String.getMaskedNumberSubStringPayment(): String {
    val LAST_NUMBERS = 4
    val FOUR_CROSS = CARD_NUMBER_MASKING_UNICODE
    return FOUR_CROSS + this.substring(this.length - LAST_NUMBERS)
}
