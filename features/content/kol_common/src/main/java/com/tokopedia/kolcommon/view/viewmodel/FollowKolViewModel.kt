package com.tokopedia.kolcommon.view.viewmodel

import com.tokopedia.kolcommon.data.pojo.FollowKolDomain

/**
 * @author by yoasfs on 2019-12-05
 */
data class FollowKolViewModel (
        val followKolDomain: FollowKolDomain = FollowKolDomain(),
        var id: Int = 0,
        var status: Int = 0,
        var rowNumber : Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false
)