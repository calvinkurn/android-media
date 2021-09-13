package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

/**
 * @author by nisie on 8/28/17.
 */
class SmileyModel {
    var resId: Int
    var name: String
    var score: String
    var isChange: Boolean = false

    constructor(resId: Int, name: String, value: String) {
        this.resId = resId
        this.name = name
        score = value
        isChange = false
    }

    constructor(resId: Int, name: String, value: String, isChange: Boolean) {
        this.resId = resId
        this.name = name
        score = value
    }
}