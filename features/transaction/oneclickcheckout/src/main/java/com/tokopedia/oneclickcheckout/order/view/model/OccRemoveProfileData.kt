package com.tokopedia.oneclickcheckout.order.view.model

data class OccRemoveProfileData(
        val enable: Boolean = false,
        val type: Int = 0,
        val message: OccRemoveProfileMessageData = OccRemoveProfileMessageData()
)

data class OccRemoveProfileMessageData(
        val title: String = "",
        val description: String = ""
) {
    fun hasMessage(): Boolean {
        return description.isNotEmpty() || title.isNotEmpty()
    }
}
