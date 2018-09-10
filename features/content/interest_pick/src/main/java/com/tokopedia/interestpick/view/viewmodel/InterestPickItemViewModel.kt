package com.tokopedia.interestpick.view.viewmodel

/**
 * @author by milhamj on 07/09/18.
 */

data class InterestPickItemViewModel (
        val categoryId: Int = 0,
        val categoryName: String = "",
        val image: String = "",
        val isSelected: Boolean = false
)
