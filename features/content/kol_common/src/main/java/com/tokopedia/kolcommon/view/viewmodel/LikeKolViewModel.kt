package com.tokopedia.kolcommon.view.viewmodel

/**
 * @author by yoasfs on 2019-12-05
 */
data class LikeKolViewModel (
        var id: Long = 0,
        var rowNumber: Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false
)
