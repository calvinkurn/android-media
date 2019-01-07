package com.tokopedia.iris

import com.tokopedia.iris.data.db.mapper.TrackingMapper
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
        trackingMapper  = TrackingMapper()
    }

    fun dataTest() : String {
        val test = JSONObject()
        test.put("event", "data sample")
        return test.toString()
    }

    @Test
    fun testAddSessionToEvent() {
        val result : JSONObject? = trackingMapper.addSessionToEvent(dataTest(), "session")
        Assert.assertNotNull(result)
        Assert.assertTrue(result is JSONObject)
        Assert.assertEquals("session", result!!.getString("iris_session_id"))
    }

    @Test
    fun testTransformSingleEvent() {
        val result: String? = trackingMapper.transformSingleEvent(dataTest(), "session", "user", "device")
        Assert.assertNotNull(result)
        Assert.assertTrue(result is String)
        Assert.assertEquals("{\"data\":[{\"container\":\"gtm\",\"device_id\":\"device\",\"user_id\":\"user\",\"event_data\":[{\"iris_session_id\":\"session\",\"event\":\"data sample\"}],\"event_ga\":\"default_app\"}]}", result)
    }

    @Test
    fun testTransformListEvent() {

    }
}