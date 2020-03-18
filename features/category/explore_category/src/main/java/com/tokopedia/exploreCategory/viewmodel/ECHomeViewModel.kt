package com.tokopedia.exploreCategory.viewmodel

import com.tokopedia.exploreCategory.ECAnalytics

class ECHomeViewModel : BaseECViewModel() {
    fun fireBackEvent() {
        ECAnalytics.trackEventClickBack()
    }
}