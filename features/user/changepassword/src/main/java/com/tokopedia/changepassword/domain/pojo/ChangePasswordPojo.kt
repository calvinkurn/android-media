package com.tokopedia.changepassword.domain.pojo

/**
 * @author by nisie on 7/25/18.
 */

data class ChangePasswordPojo(
        val data: Data? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class Data(
        val is_success: Int = 0
)