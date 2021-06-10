package com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter

import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InAppDataProviderSubscriberTest {

    private val elapsedTime : ElapsedTime = mockk(relaxed = true)
    private val dataProvider: DataProvider = mockk(relaxed = true)
    private val inAppDataProviderSubscriber = spyk(InAppDataProviderSubscriber(elapsedTime, dataProvider))
    private val slot = slot<ArrayList<CMInApp>>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onNextWhenListIsEmpty() {
        val inAppDataList = ArrayList<CMInApp>()

        /*mockkStatic(RulesUtil::class)
        every { RulesUtil.isValidTimeFrame(any(),any(),any(),any()) } returns false
*/
        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(capture(slot), any(), any()) }
        assertEquals(slot.captured.size, 0 )

    }

    /*@Test
    fun onNextWithStartTimeEndTime() {
        val inAppDataList = ArrayList<CMInApp>()
        val cmInApp = CMInApp()
        cmInApp.startTime = 1L
        cmInApp.endTime = 1L
        inAppDataList.add(cmInApp)

        mockkStatic(RulesUtil::class)
        every { RulesUtil.isValidTimeFrame(any(),any(),any(),any()) } returns false

        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(capture(slot)) }
        assertEquals(slot.captured.size, 0 )

    }*/

    /*@Test
    fun onNextWithoutStartTimeEndTimeTrueFalseTrueCase1() {
        val inAppDataList = ArrayList<CMInApp>()
        val cmInApp = CMInApp()
        cmInApp.startTime = 0L
        cmInApp.endTime = 0L
        cmInApp.isShown = true
        cmInApp.freq = 1
        inAppDataList.add(cmInApp)

        *//*mockkStatic(RulesUtil::class)
        every { RulesUtil.isValidTimeFrame(any(),any(),any(),any()) } returns false*//*

        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(capture(slot)) }
        assertEquals(slot.captured.size, 0)

    }*/

    /*@Test
    fun onNextWithoutStartTimeEndTimeTrueFalseTrueCase2() {
        val inAppDataList = ArrayList<CMInApp>()
        val cmInApp = CMInApp()
        cmInApp.startTime = 0L
        cmInApp.endTime = 0L
        cmInApp.isShown = true
        cmInApp.freq = RulesUtil.Constants.DEFAULT_FREQ - 1
        inAppDataList.add(cmInApp)

        *//*mockkStatic(RulesUtil::class)
        every { RulesUtil.isValidTimeFrame(any(),any(),any(),any()) } returns false*//*

        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(capture(slot)) }
        assertEquals(slot.captured.size, 0)

    }*/

    /*@Test
    fun onNextWithoutStartTimeEndTimeTrueFalseTrueCase3() {
        val inAppDataList = ArrayList<CMInApp>()
        val cmInApp = CMInApp()
        cmInApp.startTime = 0L
        cmInApp.endTime = 0L
        cmInApp.isShown = true
        cmInApp.freq = RulesUtil.Constants.DEFAULT_FREQ - 1
        inAppDataList.add(cmInApp)

        *//*mockkStatic(RulesUtil::class)
        every { RulesUtil.isValidTimeFrame(any(),any(),any(),any()) } returns false*//*

        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(capture(slot)) }
        assertEquals(slot.captured.size, 0)

    }*/

    @Test
    fun onNextWhenListIsNull() {
        val inAppDataList = null

        inAppDataProviderSubscriber.onNext(inAppDataList)

        verify { dataProvider.notificationsDataResult(inAppDataList, any(), any()) }
        //assertNull(slot.captured)

    }

    @Test
    fun onCompleted() {
    }

    @Test
    fun onError() {
    }
}