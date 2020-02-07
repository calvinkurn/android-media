package com.tokopedia.notifications

import com.tokopedia.notifications.model.NotificationStatus
import com.tokopedia.notifications.database.convertors.JsonObjectConverter
import com.tokopedia.notifications.database.convertors.NotificationStatusConverter
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test

class RoomConverterTest {

    @Test
    fun toJsonObject() {
        Assert.assertNull(JsonObjectConverter.instances.toJsonObject(null))
        Assert.assertNull(JsonObjectConverter.instances.toJsonObject(" "))
    }

    @Test
    fun toJsonString() {
        Assert.assertNotNull(JsonObjectConverter.instances.toJsonString(JSONObject()))
        Assert.assertNull(JsonObjectConverter.instances.toJsonString(null))
    }

    @Test
    fun toInteger() {
        Assert.assertEquals(NotificationStatusConverter.instances.toInteger(null), 0)
        Assert.assertEquals(NotificationStatusConverter.instances.toInteger(NotificationStatus.PENDING),
                NotificationStatus.PENDING.statusInt)
    }
    @Test
    fun toStatus() {
        Assert.assertEquals(NotificationStatusConverter.instances.toStatus(null), NotificationStatus.PENDING)
        Assert.assertEquals(NotificationStatusConverter.instances.toStatus(NotificationStatus.PENDING.statusInt), NotificationStatus.PENDING)
    }


}