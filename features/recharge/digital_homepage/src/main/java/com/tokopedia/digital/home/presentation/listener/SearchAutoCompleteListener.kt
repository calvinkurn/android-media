package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.Tracking

interface SearchAutoCompleteListener {
    fun impressCategoryListener(trackingUser: Tracking, trackingItem: Tracking)
    fun impressOperatorListener(trackingUser: Tracking, trackingItem: Tracking)
    fun clickCategoryListener(trackingUser: Tracking, trackingItem: Tracking)
    fun clickOperatorListener(trackingUser: Tracking, trackingItem: Tracking)
}