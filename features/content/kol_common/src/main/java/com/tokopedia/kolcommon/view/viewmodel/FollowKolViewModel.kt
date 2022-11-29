package com.tokopedia.kolcommon.view.viewmodel

import com.tokopedia.kolcommon.data.pojo.FollowKolDomain

/**
 * @author by yoasfs on 2019-12-05
 */
data class FollowKolViewModel (
        val followKolDomain: FollowKolDomain = FollowKolDomain(),
        val id: String = "",
        var status: Int = 0,
        var rowNumber : Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false,
        var isFollow: Boolean = false,
        var position: Int = 0,
        var isFollowedFromFollowRestrictionBottomSheet: Boolean = false
)
