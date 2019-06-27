package com.tokopedia.iris

import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by meta on 02/01/19.
 */
@RunWith(MockitoJUnitRunner::class)
class IrisMapperUnitTest {

    private lateinit var trackingMapper: TrackingMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        trackingMapper = TrackingMapper()
    }

    fun dataTest(): String {
        val test = JSONObject()
        test.put("event", "data sample")
        return test.toString()
    }

    @Test
    fun testTransformSingleEvent() {
        val result: String? = trackingMapper.transformSingleEvent(dataTest(), "session", "user", "device")
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)
        Assert.assertEquals("{\"data\":[{\"container\":\"gtm\",\"device_id\":\"device\",\"user_id\":\"user\",\"event_data\":[{\"iris_session_id\":\"session\",\"event\":\"data sample\"}],\"event_ga\":\"default_app\"}]}", result)
    }

    @Test
    fun test_TransformListEvent_SingleUserData() {
        val list: List<Tracking> = singleUserData()
        println("raw list: $list")
        Assert.assertEquals(3, list.size)

        val result: String? = trackingMapper.transformListEvent(list)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)
        println(result)

        val jsonResult = JSONObject(result)
        val data = jsonResult.getJSONArray("data")
        println("data.length: ${data.length()}")
        Assert.assertEquals(1, data.length())

        val data1 = data.getJSONObject(0)
        Assert.assertNotNull(data1)
        Assert.assertEquals("", data1.getString("user_id"))

        val event_data = data1.getJSONArray("event_data")
        println("event_data.length: ${event_data.length()}")
        Assert.assertNotNull(event_data)
        Assert.assertEquals(3, event_data.length())

        val event_data1 = event_data.getJSONObject(0)
        Assert.assertNotNull(event_data1)
        Assert.assertEquals("data0", event_data1.getString("event"))
    }

    @Test
    fun test_TransformListEvent_MultiUserDataj() {
        val list: List<Tracking> = multiUserData()
        println("raw list: $list")
        Assert.assertEquals(5, list.size)

        val result: String? = trackingMapper.transformListEvent(list)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)
        println(result)

        val jsonResult = JSONObject(result)
        val data = jsonResult.getJSONArray("data")
        println("data.length: ${data.length()}")
        Assert.assertEquals(3, data.length())

        val data0 = data.getJSONObject(0)
        Assert.assertNotNull(data0)
        Assert.assertEquals("123", data0.getString("user_id"))

        val event_data_0 = data0.getJSONArray("event_data")
        println("event_data_0.length: ${event_data_0.length()}")
        Assert.assertNotNull(event_data_0)
        Assert.assertEquals(2, event_data_0.length())

        val event_data_0_0 = event_data_0.getJSONObject(0)
        Assert.assertNotNull(event_data_0_0)
        Assert.assertEquals("data0", event_data_0_0.getString("event"))

        val data1 = data.getJSONObject(1)
        Assert.assertNotNull(data1)
        Assert.assertEquals("1234", data1.getString("user_id"))

        val event_data_1 = data1.getJSONArray("event_data")
        println("event_data_1.length: ${event_data_1.length()}")
        Assert.assertNotNull(event_data_1)
        Assert.assertEquals(2, event_data_1.length())

        val event_data_1_0 = event_data_1.getJSONObject(0)
        Assert.assertNotNull(event_data_1_0)
        Assert.assertEquals("data2", event_data_1_0.getString("event"))

        val data2 = data.getJSONObject(2)
        Assert.assertNotNull(data2)
        Assert.assertEquals("", data2.getString("user_id"))

        val event_data_2 = data2.getJSONArray("event_data")
        println("event_data_2.length: ${event_data_2.length()}")
        Assert.assertNotNull(event_data_2)
        Assert.assertEquals(1, event_data_2.length())

        val event_data_2_0 = event_data_2.getJSONObject(0)
        Assert.assertNotNull(event_data_2_0)
        Assert.assertEquals("data4", event_data_2_0.getString("event"))
    }

    private fun singleUserData(): List<Tracking> {
        val list: MutableList<Tracking> = mutableListOf()
        list.add(Tracking(
                this.dataList()[0].toString(),
                deviceId = "",
                userId = "",
                timeStamp = 1554380700197
        ))

        list.add(Tracking(
                this.dataList()[1].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700643
        ))

        list.add(Tracking(
                this.dataList()[2].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700717
        ))
        return list
    }

    private fun multiUserData(): List<Tracking> {
        val list: MutableList<Tracking> = mutableListOf()
        list.add(Tracking(
                this.dataList()[0].toString(),
                deviceId = "",
                userId = "123",
                timeStamp = 1554380700197
        ))

        list.add(Tracking(
                this.dataList()[1].toString(),
                userId = "123",
                deviceId = "",
                timeStamp = 1554380700643
        ))

        list.add(Tracking(
                this.dataList()[2].toString(),
                userId = "1234",
                deviceId = "",
                timeStamp = 1554380700717
        ))

        list.add(Tracking(
                this.dataList()[3].toString(),
                userId = "1234",
                deviceId = "",
                timeStamp = 1554380700717
        ))


        list.add(Tracking(
                this.dataList()[4].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700717
        ))
        return list
    }

    private fun dataList(): List<JSONObject> {
        return mutableListOf(
                JSONObject("{\"event\": \"data0\"}"),
                JSONObject("{\"event\": \"data1\"}"),
                JSONObject("{\"event\": \"data2\"}"),
                JSONObject("{\"event\": \"data3\"}"),
                JSONObject("{\"event\": \"data4\"}")
        )
    }
}