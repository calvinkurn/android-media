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
    }
}
