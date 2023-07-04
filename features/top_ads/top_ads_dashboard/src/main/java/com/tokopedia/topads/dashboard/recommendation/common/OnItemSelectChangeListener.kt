package com.tokopedia.topads.dashboard.recommendation.common

interface OnItemSelectChangeListener {
    fun onClickItemListener(adType: Int, groupId: String, groupName: String){}
    fun onSubmitSelectItemListener(adType: Int, groupId: String, groupName: String){}
}
