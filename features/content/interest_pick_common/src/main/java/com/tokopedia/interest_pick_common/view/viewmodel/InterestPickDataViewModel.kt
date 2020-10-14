package com.tokopedia.interest_pick_common.view.viewmodel

/**
 * @author by yoasfs on 2019-08-30
 */
data class InterestPickDataViewModel (
         val id: Int = 0,
         val name: String = "",
         val image: String = "",
         var isSelected: Boolean = false,
         val isLihatSemuaItem: Boolean = false,
         var isClickable: Boolean = true
) {
    companion object {
        const val defaultLihatSemuaText: String = "Lihat Semua"
    }
}