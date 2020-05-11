package com.tokopedia.common.topupbills.view.model

data class TopupBillsTrackImpressionItem<out T: Any> (
        val item: T,
        val position: Int = 0,
        var isTracked: Boolean = false
)