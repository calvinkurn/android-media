package com.tokopedia.home_account.linkaccount.data

/**
 * Created by Yoris on 05/08/21.
 */

data class UserAccountDataView (
    val isLinked: Boolean = false,
    val status: String = "",
    val partnerName: String = "",
    val linkDate: String = ""
)