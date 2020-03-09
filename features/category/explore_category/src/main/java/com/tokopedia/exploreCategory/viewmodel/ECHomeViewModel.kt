package com.tokopedia.exploreCategory.viewmodel

import com.tokopedia.exploreCategory.ECAnalytics
import javax.inject.Inject

class ECHomeViewModel @Inject constructor() : BaseECViewModel() {
    fun fireBackEvent() {
        ECAnalytics.trackEventClickBack()
    }
}