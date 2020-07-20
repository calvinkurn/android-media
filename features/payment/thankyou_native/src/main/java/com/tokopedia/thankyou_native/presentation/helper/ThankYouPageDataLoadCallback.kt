package com.tokopedia.thankyou_native.presentation.helper

import com.tokopedia.thankyou_native.domain.model.ThanksPageData


interface ThankYouPageDataLoadCallback {
    fun onThankYouPageDataLoaded(thanksPageData: ThanksPageData)
    fun onInvalidThankYouPage()
}
