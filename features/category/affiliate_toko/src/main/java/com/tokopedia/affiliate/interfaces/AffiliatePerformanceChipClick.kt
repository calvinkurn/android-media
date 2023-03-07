package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.ItemTypesItem

interface AffiliatePerformanceChipClick {
    fun onChipClick(type: ItemTypesItem?)

}