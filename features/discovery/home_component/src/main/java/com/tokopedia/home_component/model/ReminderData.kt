package com.tokopedia.home_component.model

/**
 * @author by firman on 10-06-2020
 */

data class ReminderWidget(
        val reminders : List<ReminderData> = emptyList()
)

data class ReminderData(
        val appLink: String = "",
        val backgroundColor: String = "",
        val buttonText: String = "",
        val id: String = "",
        val iconURL: String = "",
        val link: String = "",
        val mainText: String = "",
        val subText: String = "",
        val title: String = "",
        val UUID:String = ""
)

