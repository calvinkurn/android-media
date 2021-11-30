package com.tokopedia.discovery2

import android.content.Context
import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import java.lang.AssertionError
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(Utils::class)
    }

//    Methods that need to be tested

//    extractDimension  -- Uses URI
//    shareData
//    getCountView -- Internally uses getDecimalFormatted and getDisplayValue
//    getDecimalFormatted   --- Private
//    getDisplayValue   --- Private
//    getQueryMap  --- complicated - uses DiscoveryDataMapper's discoComponentQuery
//    parseFlashSaleDate
//    setTimerBoxDynamicBackground

    @Test
    fun `date gets parsed`() {
        Assert.assertNotEquals(Utils.parseData("1996-01-20 21:59:00 +0000 +0000"), null)
    }

    @Test
    fun `date can't get parsed`() {
        Assert.assertEquals(Utils.parseData("21:59:00 1996-01-20 +0000 +0000"), null)
        Assert.assertEquals(Utils.parseData(""), null)
        Assert.assertEquals(Utils.parseData(null), null)
    }

    @Test
    fun `isn't Future Sale`() {
        Assert.assertEquals(Utils.isFutureSale(""), false)
        Assert.assertEquals(Utils.isFutureSale("1996-01-20 21:59:00 +0000 +0000"), false)
        Assert.assertEquals(Utils.isFutureSale("21:59:00 1996-01-20 +0000 +0000"), false)
        Assert.assertEquals(Utils.isFutureSale("null"), false)
    }

    @Test
    fun `is Future Sale`() {
        mockkObject(Utils)
        val cal = Calendar.getInstance()
        cal.add(Calendar.SECOND, 10)
        every { Utils.parseData(any(), any()) } returns cal.time
        Assert.assertEquals(Utils.isFutureSale("test date"), true)
        unmockkObject(Utils)
    }

    @Test
    fun `sale is over`() {
        Assert.assertEquals(Utils.isSaleOver(""), true)
        Assert.assertEquals(Utils.isSaleOver("1996-01-20 21:59:00 +0000 +0000"), true)
    }

    @Test
    fun `sale isn't over`() {
        Assert.assertEquals(Utils.isSaleOver("21:59:00 1996-01-20 +0000 +0000"), false)
        Assert.assertEquals(Utils.isSaleOver("null"), false)
        mockkObject(Utils)
        val cal = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10)
        }
        every { Utils.parseData(any(), any()) } returns cal.time
        Assert.assertEquals(Utils.isSaleOver("test data"), false)
        unmockkObject(Utils)

    }

    @Test
    fun `sale isn't onGoing`() {
        Assert.assertEquals(Utils.isFutureSaleOngoing("21:59:00 1996-01-20 +0000 +0000", ""), false)
        Assert.assertEquals(Utils.isFutureSaleOngoing("", "21:59:00 1996-01-20 +0000 +0000"), false)

        mockkObject(Utils)
        val date1 = "test date 1"
        val date2 = "test date 2"
//        Sale hasn't started yet
        val cal1 = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10)
        }
        val cal2 = Calendar.getInstance().apply {
            add(Calendar.SECOND, 20)
        }

        every { Utils.parseData(date1, any()) } returns cal1.time
        every { Utils.parseData(date2, any()) } returns cal2.time
        Assert.assertEquals(Utils.isFutureSaleOngoing(date1, date2), false)

//        Sale has finished
        cal1.add(Calendar.MINUTE, -2)
        cal2.add(Calendar.MINUTE, -1)
        Assert.assertEquals(Utils.isFutureSaleOngoing(date1, date2), false)
        unmockkObject(Utils)
    }

    @Test
    fun `sale is OnGoing`() {
        mockkObject(Utils)
        val date1 = "test date 1"
        val date2 = "test date 2"
        val cal1 = Calendar.getInstance().apply {
            add(Calendar.MINUTE, -1)
        }
        val cal2 = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        }

        every { Utils.parseData(date1, any()) } returns cal1.time
        every { Utils.parseData(date2, any()) } returns cal2.time
        Assert.assertEquals(Utils.isFutureSaleOngoing(date1, date2), true)
        unmockkObject(Utils)

    }

    @Test
    fun `get Elapsed Time`() {
        mockkConstructor(SimpleDateFormat::class)
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
        val endDate = "test date"
        val cal1 = Calendar.getInstance().apply {
            add(Calendar.SECOND, 1)
        }
        every { anyConstructed<SimpleDateFormat>().parse(endDate) } returns cal1.time
        Assert.assertEquals(Utils.getElapsedTime(endDate) <= 1000L, true)
        cal1.add(Calendar.SECOND, 1)
        Assert.assertEquals(Utils.getElapsedTime(endDate) != DEFAULT_TIME_DATA, true)
        unmockkConstructor(SimpleDateFormat::class)
    }

    @Test
    fun `cant get Elapsed Time`() {
//        Wrong Format - will throw exception
        Assert.assertEquals(Utils.getElapsedTime("1996-01-20 21:59:00 +0000 +0000"), DEFAULT_TIME_DATA)
//        Empty End Date returns Default value
        Assert.assertEquals(Utils.getElapsedTime(""), DEFAULT_TIME_DATA)

    }

    @Test
    fun `parsed Color`() {
        val context: Context = mockk()
        val fontColour = "#FFFFFF"
        val defaultColor = 564843
        mockkStatic(Color::class)
        every { Color.parseColor(fontColour) } returns 1
        Utils.parsedColor(context, fontColour, defaultColor)
        verify { Color.parseColor(fontColour) }
    }

    @Test
    fun `parse Color throws exception`(){
        val context: Context = mockk()
        val fontColour = "#FFFFFF"
        val defaultColor = 564843
        mockkStatic(Color::class)
        mockkStatic(MethodChecker::class)
        every { Color.parseColor(fontColour) } throws IllegalArgumentException("Couldn't get parsed")
        every { MethodChecker.getColor(context, defaultColor) } returns 1
        Utils.parsedColor(context, fontColour, defaultColor)
        verify { MethodChecker.getColor(context, defaultColor) }
    }


}