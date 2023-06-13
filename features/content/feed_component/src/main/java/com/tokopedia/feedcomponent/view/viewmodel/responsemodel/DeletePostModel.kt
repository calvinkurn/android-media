package com.tokopedia.feedcomponent.view.viewmodel.responsemodel

/**
 * @author by yoasfs on 2019-12-05
 */
data class DeletePostModel (
        var id: String = "0",
        var rowNumber: Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false
)
