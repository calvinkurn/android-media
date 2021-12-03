package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

/**
 * @author by nisie on 8/28/17.
 */
data class SmileyModel(
    val resId: Int,
    val name: String,
    val score: String,
    val isChange: Boolean = false
)