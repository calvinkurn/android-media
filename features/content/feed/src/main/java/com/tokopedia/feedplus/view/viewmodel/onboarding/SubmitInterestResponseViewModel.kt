package com.tokopedia.feedplus.view.viewmodel.onboarding

/**
 * @author by yoasfs on 2019-09-20
 */
data class SubmitInterestResponseViewModel (
        var success: Boolean = false,
        var error: String = "",
        var source: String = "",
        var requestInt: Int = 0,
        var idList: List<Int> = ArrayList()
)