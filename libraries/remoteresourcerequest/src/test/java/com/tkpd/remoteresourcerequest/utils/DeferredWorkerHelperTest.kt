package com.tkpd.remoteresourcerequest.utils

import android.content.Context
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.database.ResourceEntry
import com.tkpd.remoteresourcerequest.type.MultiDPIImageType
import io.mockk.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class DeferredWorkerHelperTest {

    private var context = mockk<Context>()

    @Before
    fun setUp() {
        every { context.resources.openRawResource(any()) } returns mockk()
    }

    @Test
    fun `test return null from getPendingDeferredResourceURLs() when version maches`() {
        mockkObject(ResourceDB)
        val db = mockk<ResourceDB>()
        every { ResourceDB.getDatabase(context) } returns db
        val imageType = mockk<MultiDPIImageType>()
        val deferredWorkerHelper = spyk(DeferredWorkerHelper(context))
//        every { deferredWorkerHelper.resourceDB } returns db
        every { imageType.remoteFileName } returns "abc"
        every { imageType.resourceVersion } returns "0"
        every { imageType.isUsedAnywhere } returns true
        every { deferredWorkerHelper.getDeferredResourceFromFile(any()) } returns arrayListOf(
                imageType
        )
        val dbList = ArrayList<ResourceEntry>()
        val entry = mockk<ResourceEntry>()
        every { entry.appVersion } returns "0"
        every { entry.url } returns "abc"
        dbList.add(entry)
        every { db.resourceEntryDao.getAllResourceEntry() } returns dbList
        every { db.resourceEntryDao.deleteEntry(any()) } just Runs

        val list = deferredWorkerHelper.getPendingDeferredResourceURLs(1)

        verify { db.resourceEntryDao.getAllResourceEntry() }

        assertTrue(list.isNullOrEmpty())
    }

    @Test
    fun `test return non null from getPendingDeferredResourceURLs() when version mismatch`() {
        mockkObject(ResourceDB)
        val db = mockk<ResourceDB>()
        every { ResourceDB.getDatabase(context) } returns db
        val imageType = mockk<MultiDPIImageType>()
        val deferredWorkerHelper = spyk(DeferredWorkerHelper(context))
//        every { deferredWorkerHelper.resourceDB } returns db
        every { imageType.remoteFileName } returns "abc"
        every { imageType.resourceVersion } returns "1.0"
        every { deferredWorkerHelper.getDeferredResourceFromFile(any()) } returns arrayListOf(
                imageType
        )
        val dbList = ArrayList<ResourceEntry>()
        val entry = mockk<ResourceEntry>()
        every { entry.appVersion } returns "0"
        every { entry.url } returns "abc"
        dbList.add(entry)
        every { db.resourceEntryDao.getAllResourceEntry() } returns dbList
        every { db.resourceEntryDao.deleteEntry(any()) } just Runs

        val list = deferredWorkerHelper.getPendingDeferredResourceURLs(1)

        verify { db.resourceEntryDao.getAllResourceEntry() }

        assertFalse(list.isNullOrEmpty())
        assertEquals(list[0].remoteFileName, "abc")
    }

    @Test
    fun `test return null from getPendingDeferredResourceURLs() when res version mismatch and not used`() {
        mockkObject(ResourceDB)
        val db = mockk<ResourceDB>()
        every { ResourceDB.getDatabase(context) } returns db
        val imageType = mockk<MultiDPIImageType>()
        val deferredWorkerHelper = spyk(DeferredWorkerHelper(context))
//        every { deferredWorkerHelper.resourceDB } returns db
        every { imageType.remoteFileName } returns "abc"
        every { imageType.resourceVersion } returns "0"
        every { imageType.isUsedAnywhere } returns false

        every { deferredWorkerHelper.getDeferredResourceFromFile(any()) } returns arrayListOf(
                imageType
        )
        val dbList = ArrayList<ResourceEntry>()
        val entry = mockk<ResourceEntry>()
        every { entry.appVersion } returns "0"
        every { entry.url } returns "abc"
        dbList.add(entry)
        every { db.resourceEntryDao.getAllResourceEntry() } returns dbList
        every { db.resourceEntryDao.deleteEntry(any()) } just Runs

        val list = deferredWorkerHelper.getPendingDeferredResourceURLs(1)

        verify { db.resourceEntryDao.getAllResourceEntry() }

        assertTrue(list.isNullOrEmpty())
    }

    @Test
    fun getContext() {
    }
}
