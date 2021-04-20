package com.tokopedia.notifications.inApp.ruleEngine.storage

import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.After
import org.junit.Before

import org.junit.Test
import rx.observers.TestSubscriber
import kotlin.jvm.Throws

class StorageProviderTest {

    private val elapsedTimeDao : ElapsedTimeDao = mockk(relaxed = true)
    private val inAppDataDao : InAppDataDao = mockk(relaxed = true)
    private val context : StorageProvider.StorageProviderListener = spyk()
    private val storageProvider : StorageProvider = StorageProvider(inAppDataDao, elapsedTimeDao, context)

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    /*@Test
    fun putDataToStoreForCMInApp() {
        val cmInApp = mockk<CMInApp>()

        val completable = storageProvider.putDataToStore(cmInApp)
        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        completable.subscribe(testSubscriber)

        every { inAppDataDao.getDataFromParentIdForPerstOff(any()) } returns null
        every { inAppDataDao.insert(any<CMInApp>()) } returns Unit

        testSubscriber.assertCompleted()

    }*/

    @Test
    fun putDataToStoreForListOfCMInApp() {
        val cmInApps = mockk<List<CMInApp>>()

        val completable = storageProvider.putDataToStore(cmInApps)
        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        completable.subscribe(testSubscriber)

        every { inAppDataDao.insert(any<List<CMInApp>>()) } returns Unit

        testSubscriber.assertCompleted()

    }

    @Test
    fun getDataFromStore() {
        val key = ""

        every { inAppDataDao.getDataForScreen(any()) } returns mockk<List<CMInApp>>()

        storageProvider.getDataFromStore(key, true)

        verify { inAppDataDao.getDataForScreen(any()) }
    }

    @Test
    fun putElapsedTimeToStore() {
        val elapsedTime = mockk<ElapsedTime>()

        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        val completable = storageProvider.putElapsedTimeToStore(elapsedTime)
        completable.subscribe(testSubscriber)

        every { elapsedTimeDao.insert(any()) } returns Unit


        testSubscriber.assertCompleted()
    }

    @Test
    fun getElapsedTimeFromStore() {

        every { elapsedTimeDao.lastElapsedTime } returns mockk()

        storageProvider.elapsedTimeFromStore

        verify { elapsedTimeDao.lastElapsedTime }
    }

    @Test
    fun deleteRecord() {
        val id: Long = 123

        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        val completable = storageProvider.deleteRecord(id)
        completable.subscribe(testSubscriber)

        every { inAppDataDao.deleteRecord(any()) } returns Unit

        testSubscriber.assertCompleted()
    }

    @Test
    fun getInAppData() {

        every { inAppDataDao.getInAppData(any()) } returns mockk()

        storageProvider.getInAppData(123)

        verify { inAppDataDao.getInAppData(any()) }
    }

    @Test
    fun updateInAppDataFreqWithId() {
        val id: Long = 123

        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        val completable = storageProvider.updateInAppDataFreq(id)
        completable.subscribe(testSubscriber)

        every { inAppDataDao.updateFrequencyWithShownTime(any(), any()) } returns Unit

        testSubscriber.assertCompleted()
    }

    @Test
    fun viewDismissed() {
        val id: Long = 123

        val testSubscriber: TestSubscriber<Any> = TestSubscriber()
        val completable = storageProvider.viewDismissed(id)
        completable.subscribe(testSubscriber)

        every { inAppDataDao.updateVisibleState(any()) } returns Unit

        testSubscriber.assertCompleted()
    }


}