package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import java.io.File

class DeferredResourceTaskTest {


    private lateinit var task: DeferredResourceTask
    private var resourceDownloadManager = mockk<ResourceDownloadManager>(relaxed = true)
    private var okHttpClient = mockk<OkHttpClient>()
    private var roomDb = mockk<ResourceDB>()
    private var deferredImageView = mockk<DeferredImageView>()
    @Before
    fun setup() {
        task = spyk(DeferredResourceTask(resourceDownloadManager, okHttpClient, roomDb))

    }

    @Test
    fun initTest() {
        task.initTask("abc", deferredImageView, null)
        assert(task.deferredImageView!!.get() == deferredImageView)
        assert(task.getDownloadUrl() == "abc")
    }

    @Test
    fun bitmapTest() {
        assert(task.getBitmap() == null)
        val image = mockk<Bitmap>()
        task.setImage(image)
        assert(task.getBitmap() == image)
    }

    @Test
    fun testByteBuffer() {
        val byteArray = byteArrayOf()
        task.setByteBuffer(byteArray)
        assert(task.getByteBuffer()!!.contentEquals(byteArray))
    }

    @Test
    fun testHeightWidth() {
        task.initTask("abc", deferredImageView, null)
        every { deferredImageView.width } returns 50
        every { deferredImageView.height } returns 10
        assert(task.mTargetWidth == deferredImageView.width)
        assert(task.mTargetHeight == 10)
        task.initTask("abc", null, null)
        assert(task.mTargetHeight == 0)
        assert(task.mTargetWidth == 0)

    }

    @Test
    fun downloadRunnableTest() {
        val runnable = mockk<Runnable>()
        task.mDownloadRunnable = runnable

        assert(task.getDownloadRunnable() == runnable)

    }

    @Test
    fun handleDownloadStateTest() {

        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_STARTED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DOWNLOAD_STARTED) }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_COMPLETED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DOWNLOAD_COMPLETED) }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_FAILED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DOWNLOAD_FAILED) }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_SKIPPED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DOWNLOAD_SKIPPED) }
    }

    @Test
    fun handleDecodeStateTest() {

        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_STARTED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DECODE_STARTED) }
        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_COMPLETED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DECODE_COMPLETED) }
        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_FAILED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DECODE_FAILED) }
    }

    @Test
    fun getFileLocationFromDirectoryTest() {
        val wrapper = mockk<ContextWrapper>()
        val mockedFile = File("abc","def")
        val directory = mockk<File>(relaxed = true)
        every { directory.absoluteFile } returns mockedFile
        every { resourceDownloadManager.getContextWrapper() } returns wrapper
        every { wrapper.getDir("downloads", Context.MODE_PRIVATE) } returns directory

        task.initTask("/def", null, null)
        val result = task.getFileLocationFromDirectory()
        verify { resourceDownloadManager.getContextWrapper() }
        verify { directory.mkdir() }

        assert(result.path == mockedFile.path +"/def")
        assert(result.name == "def")
    }
    @Test
    fun recycleTaskTest(){
        task.recycleTask()

        assert(task.deferredImageView == null)
        assert(task.getByteBuffer() == null)
        assert(task.mDownloadRunnable == null)
        assert(task.deferredTaskCallback == null)
        assert(task.getBitmap() == null)
    }
}