package com.tokopedia.analytics

import com.tokopedia.analyticsdebugger.debugger.data.mapper.TetraMapper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@RunWith(MockitoJUnitRunner::class)
class TetraMapperUnitTest {

    private lateinit var mapper: TetraMapper

    private var sampleInitRequest: String = "{\"deviceType\":\"android\",\"deviceId\":\"098765421\"}"
    private var sampleInitResponse: String = "{\"isWhitelisted\": true}"

    private var userId: String = "1234"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapper = TetraMapper()
        mapper.deviceId = "098765421"
    }

    @Test
    fun parseInitRequestTest() {
        val result: String? = mapper.parseInitRequest()
        Assert.assertNotNull(result)
        Assert.assertEquals(sampleInitRequest, result)
    }

    @Test
    fun parseInitResponseTest() {
        val result: Boolean = mapper.parseInitResponse(sampleInitResponse)
        Assert.assertNotNull(result)
        Assert.assertEquals(true, result)
    }

    @Test
    fun parseDebugRequestTest() {
        val result: String? = mapper.parseDebugRequest(userId, sampleMapData())
        Assert.assertNotNull(result)
        Assert.assertEquals(result, sampleStringData())
    }

    private fun sampleStringData(): String {
        return "{\"deviceType\":\"android\",\"data\":" +
                "\"{\\\"obj2\\\":\\\"event2\\\",\\\"obj1\\\":\\\"event1\\\"}\"," +
                "\"deviceId\":\"098765421\",\"userId\":\"1234\",\"timestamp\":\"" +
                date() + "\"}"
    }

    private fun sampleMapData(): Map<String, String> {
        val map = HashMap<String, String>()
        map["obj1"] = "event1"
        map["obj2"] = "event2"
        return map
    }

    private fun date(): String {
        return SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z",
                Locale.getDefault()).format(Date())
    }
}