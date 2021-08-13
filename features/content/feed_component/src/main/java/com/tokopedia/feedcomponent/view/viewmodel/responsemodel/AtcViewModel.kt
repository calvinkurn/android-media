package com.tokopedia.feedcomponent.view.viewmodel.responsemodel

/**
 * @author by yoasfs on 2019-12-09
 */
data class AtcViewModel (
        var isSuccess: Boolean = false,
        var applink: String = "",
        var errorMsg:String = "",
        var activityId: String = "",
        var postType: String = "",
        var isFollowed: Boolean = false,
        var  shopId: String = ""

)