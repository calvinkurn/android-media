package com.tokopedia.home_component.mock.reminder_widget

import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderWidget

object MockReminderWidget {
    fun get(): ReminderWidget {
        val reminderData = ReminderData(
                appLink = "tokopedia://home",
                backgroundColor = listOf(),
                buttonText = "Mock Button",
                id = "1",
                iconURL = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2020/3/22/1579059/1579059_86946c79-26f2-40da-a7c3-a6286936345f_700_700",
                link = "https://www.tokopedia.com/",
                mainText = "Mock Main Text",
                subText = "Mock Sub Text",
                title = "Mock Title",
                UUID = "1"
        )
        val listReminderData = mutableListOf<ReminderData>()
        for (index in 1..5) {
            listReminderData.add(reminderData)
        }
        return ReminderWidget("1", listReminderData)
    }
}