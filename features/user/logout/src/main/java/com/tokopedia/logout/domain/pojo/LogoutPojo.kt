package com.tokopedia.logout.domain.pojo

/**
 * @author by nisie on 5/30/18.
 */
data class LogoutPojo(
        val data: Data? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class Data(
        val is_success: Int? = 0
)