package com.tokopedia.iris

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.util.Cache
import com.tokopedia.track.TrackApp
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.json.JSONObject
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

/**
 * Created by meta on 02/01/19.
 */
@RunWith(RobolectricTestRunner::class)
class IrisMapperUnitTest {

    private lateinit var trackingMapper: TrackingMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        trackingMapper = TrackingMapper()
    }

    @After
    fun finish() {
        unmockkAll()
    }

    fun dataTest(): String {
        val test = JSONObject()
        test.put("event", "data sample")
        return test.toString()
    }

    @Test
    fun testTransformSingleEvent() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val cache = Cache(context)

        TrackApp.initTrackApp(context as Application)

        mockkObject(TrackApp.getInstance())
        mockkStatic(Calendar::class)

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, DummyAnalyticsTest::class.java)

        every {
            TrackApp.getInstance().gtm.clientIDString
        } returns "abc1234567"

        every {
            Calendar.getInstance().timeInMillis
        } returns 1705590748754L

        val result: String? = trackingMapper.transformSingleEvent(context, dataTest(), "session", "user", "device", cache)
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)

        Assert.assertEquals("{\"data\":[{\"device_id\":\"device\",\"user_id\":\"user\",\"event_data\":[{\"event_ga\":\"data sample\",\"clientId\":\"abc1234567\",\"iris_session_id\":\"session\",\"container\":\"gtm\",\"event\":\"default_app\",\"hits_time\":1705590748754}],\"app_version\":\"android-1.0\"}]}", result)
    }

    @Test
    fun test_TransformListEvent_SingleUserData() {
        val list: List<Tracking> = singleUserData()
        println("raw list: $list")
        Assert.assertEquals(3, list.size)

        val result: String? = trackingMapper.transformListEvent(list).first
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

        val result: String? = trackingMapper.transformListEvent(list).first
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)
        println(result)

        val jsonResult = JSONObject(result)
        val data = jsonResult.getJSONArray("data")
        println("data.length: ${data.length()}")
        Assert.assertEquals(1, data.length())

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
    }

    private fun singleUserData(): List<Tracking> {
        val list: MutableList<Tracking> = mutableListOf()
        list.add(
            Tracking(
                this.dataList()[0].toString(),
                deviceId = "",
                userId = "",
                timeStamp = 1554380700197
            )
        )

        list.add(
            Tracking(
                this.dataList()[1].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700643
            )
        )

        list.add(
            Tracking(
                this.dataList()[2].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700717
            )
        )
        return list
    }

    private fun multiUserData(): List<Tracking> {
        val list: MutableList<Tracking> = mutableListOf()
        list.add(
            Tracking(
                this.dataList()[0].toString(),
                deviceId = "",
                userId = "123",
                timeStamp = 1554380700197
            )
        )

        list.add(
            Tracking(
                this.dataList()[1].toString(),
                userId = "123",
                deviceId = "",
                timeStamp = 1554380700643
            )
        )

        list.add(
            Tracking(
                this.dataList()[2].toString(),
                userId = "1234",
                deviceId = "",
                timeStamp = 1554380700717
            )
        )

        list.add(
            Tracking(
                this.dataList()[3].toString(),
                userId = "1234",
                deviceId = "",
                timeStamp = 1554380700717
            )
        )

        list.add(
            Tracking(
                this.dataList()[4].toString(),
                userId = "",
                deviceId = "",
                timeStamp = 1554380700717
            )
        )
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
