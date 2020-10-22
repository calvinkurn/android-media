package com.tokopedia.home_component.model

import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by firman on 10-06-2020
 */

data class ReminderWidget(
        val id: String = "",
        val reminders : List<ReminderData> = emptyList()
)

data class ReminderData(
        val appLink: String = "",
        val backgroundColor: List<String> = listOf(),
        val buttonText: String = "",
        val id: String = "",
        val iconURL: String = "",
        val link: String = "",
        val mainText: String = "",
        val subText: String = "",
        val title: String = "",
        val UUID:String = "",
        val state: ReminderState = ReminderState.NEUTRAL,
        val buttonType: Int = UnifyButton.Type.TRANSACTION
)

enum class ReminderState(val type: String){
    NEUTRAL("neutral"),
    ATTENTION("attention")
}

