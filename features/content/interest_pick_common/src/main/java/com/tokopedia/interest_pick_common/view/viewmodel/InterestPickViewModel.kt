package com.tokopedia.interest_pick_common.view.viewmodel

/**
 * @author by yoasfs on 2019-11-09
 */

interface InterestPickViewModel {
    var isEnableOnboarding: Boolean
    val minimumPick: Int
    val source: String
    val titleIntro: String
    val titleFull: String
    val instruction: String
    val buttonCta: String
    var dataList: MutableList<InterestPickDataViewModel>
}