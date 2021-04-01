package com.tokopedia.oneclickcheckout.order.view.model

data class OccRemoveProfileData(
        val enable: Boolean = false,
        val type: Int = 0,
        val message: OccRemoveProfileMessageData = OccRemoveProfileMessageData()
) {
    companion object {
        const val TYPE_PRE = 1
        const val TYPE_POST = 2
    }
}

data class OccRemoveProfileMessageData(
        val title: String = "",
        val description: String = ""
) {
    fun hasMessage(): Boolean {
        return description.isNotEmpty() || title.isNotEmpty()
    }
}
