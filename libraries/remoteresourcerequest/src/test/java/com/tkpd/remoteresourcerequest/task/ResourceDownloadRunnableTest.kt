package com.tkpd.remoteresourcerequest.task

import android.util.Log
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.database.ResourceEntry
import com.tkpd.remoteresourcerequest.database.ResourceEntryDao
import com.tkpd.remoteresourcerequest.runnable.ResourceDownloadRunnable
import io.mockk.*
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.io.File

class ResourceDownloadRunnableTest {

    private var okHttpClient = mockk<OkHttpClient>(relaxed = true)
    private var roomdb = mockk<ResourceDB>()
    private var task = mockk<ResourceDownloadRunnable.TaskDownloadProperties>(relaxed = true)
    private lateinit var resourceDownloadRunnable: ResourceDownloadRunnable
    private lateinit var dao: ResourceEntryDao

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        every { roomdb.resourceEntryDao } returns dao
        resourceDownloadRunnable = spyk(
                ResourceDownloadRunnable(
                        task,
                        okHttpClient,
                        roomdb
                )
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun downloadFileSync() {
        every { task.getDownloadUrl() } returns "https://www.tokopedia.com"
        every { dao.getResourceEntry(any()) } returns null
        verify { task.getDownloadUrl() }

        val mockByte = byteArrayOf()
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        val entry = mockk<ResourceEntry>(relaxed = true)
        every { resourceDownloadRunnable.fileFromAbsolutePath(any()) } returns mockByte
        every { dao.getResourceEntry(any()) } returns entry
        resourceDownloadRunnable.downloadFileSync()
        verify { dao.updateLastAccess(any(), any()) }
        verify { task.setByteBuffer(mockByte) }

    }


    @Test
    fun getFileFromAbsolutePathTestPassed() {
        resourceDownloadRunnable.fileFromAbsolutePath("/home/sandeepgupta-xps/studioProject/android-tokopedia-core/libraries/remoteresourcerequest/src/test/resources/test_file.txt")

    }

    @Test(expected = Exception::class)
    fun getFileFromAbsolutePathExceptionTest() {
        resourceDownloadRunnable.fileFromAbsolutePath("")

    }

    @Test
    fun saveToCacheDirTest() {
        val file = mockk<File>(relaxed = true)
        every { task.getFileLocationFromDirectory() } returns file
        every { file.exists() } returns true
        val result = resourceDownloadRunnable.saveToCacheDirectory(byteArrayOf())
        assertEquals(result, file.absolutePath)
    }

    @Test
    fun saveToCacheDirTestNewCreated() {
        val path = "/home/sandeepgupta-xps/studioProject/android-tokopedia-core/libraries/remoteresourcerequest/src/test/resources/test_file.txt"
        val file = spyk(File(path))
        every { task.getFileLocationFromDirectory() } returns file
        every { file.exists() } returns false
        val result = resourceDownloadRunnable.saveToCacheDirectory(byteArrayOf(Byte.MIN_VALUE))
        assertEquals(result, file.absolutePath)
        assertEquals(result, path)
    }
}
