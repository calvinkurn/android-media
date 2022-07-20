package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.Tracking
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel

interface SearchAutoCompleteListener {
    fun impressCategoryListener(trackingUser: Tracking, trackingItem: Tracking)
    fun impressOperatorListener(trackingUser: Tracking, trackingItem: Tracking)
    fun clickCategoryListener(element: DigitalHomePageSearchCategoryModel, trackingUser: Tracking, trackingItem: Tracking)
    fun clickOperatorListener(trackingUser: Tracking, trackingItem: Tracking)
}