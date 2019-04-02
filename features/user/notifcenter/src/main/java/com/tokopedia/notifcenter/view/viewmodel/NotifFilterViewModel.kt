package com.tokopedia.notifcenter.view.viewmodel

/**
 * @author by milhamj on 03/09/18.
 */

data class NotifFilterViewModel(
        val id : Int = 0,
        val text : String = "",
        var isSelected : Boolean = false
) {
    companion object {
        const val FILTER_ALL_ID = 0
        const val FILTER_ALL_TEXT = "Semua"

        const val FILTER_BUYER_ID = 1
        const val FILTER_BUYER_TEXT = "Pembeli"

        const val FILTER_SELLER_ID = 2
        const val FILTER_SELLER_TEXT = "Penjual"

        const val FILTER_FOR_YOU_ID = 4
        const val FILTER_FOR_YOU_TEXT = "For You"

        const val FILTER_PROMO_ID = 5
        const val FILTER_PROMO_TEXT = "Promo"

        const val FILTER_INSIGHT_ID = 6
        const val FILTER_INSIGHT_TEXT = "Insight"

        const val FILTER_FEATURE_UPDATE_ID = 7
        const val FILTER_FEATURE_UPDATE_TEXT = "Feature Update"

        const val FILTER_EVENT_ID = 8
        const val FILTER_EVENT_TEXT = "Event"
    }
}
