package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.model.ReminderWidget
import com.tokopedia.kotlin.model.ImpressHolder

data class ReminderWidgetModel(
        val id: String = "",
        val data: ReminderWidget = ReminderWidget(),
        var source: ReminderEnum
): ImpressHolder(), HomeComponentVisitable {
    override fun visitableId(): String? {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is ReminderWidgetModel) {
            data == b.data
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}