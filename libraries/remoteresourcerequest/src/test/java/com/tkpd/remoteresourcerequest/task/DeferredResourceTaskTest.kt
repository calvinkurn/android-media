package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.runnable.ImageDecodeRunnable
import com.tkpd.remoteresourcerequest.runnable.ResourceDownloadRunnable
import com.tkpd.remoteresourcerequest.type.ImageType
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class DeferredResourceTaskTest {


    private lateinit var task: DeferredResourceTask
    private var resourceDownloadManager = mockk<ResourceDownloadManager>(relaxed = true)
    private var okHttpClient = mockk<OkHttpClient>()
    private var roomDb = mockk<ResourceDB>()

    private var imageView = mockk<DeferredImageView>(relaxed = true)
    private var imageType = mockk<ImageType>(relaxed = true)
    private var callback = mockk<DeferredTaskCallback>()

    @Before
    fun setup() {
        task = DeferredResourceTask(resourceDownloadManager, okHttpClient, roomDb)
        every { imageType.imageView } returns imageView

    }

    @Test
    fun initTestForUrl() {
        task.initTask("abc", imageType, callback)
        assert(task.deferredImageView!!.get() == imageType.imageView)
        assert(task.getDownloadUrl() == "abc")
    }

    @Test
    fun initTestForVersion() {
        task.initTask("abc", imageType, callback)
        assert(task.deferredImageView!!.get() == imageType.imageView)
        assert(task.resourceVersion() == imageType.resourceVersion)
    }

    @Test
    fun initTestForWorkerInfo() {
        task.initTask("abc", imageType, callback)
        assert(task.deferredImageView!!.get() == imageType.imageView)
        assert(task.isRequestedFromWorker == imageType.isRequestedFromWorker)
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
        assertTrue(task.getByteBuffer()?.contentEquals(byteArray) ?: false)
    }

    @Test
    fun testHeightWidth() {
        task.initTask("abc", imageType, callback)
        every { imageView.width } returns 50
        every { imageView.height } returns 10
        assert(task.mTargetWidth == imageType.imageView?.width)
        assertEquals(10, task.mTargetHeight)
        every { imageType.imageView } returns null
        task.initTask("abc", imageType, callback)
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
        verify {
            resourceDownloadManager.handleState(
                    task,
                    ResourceDownloadManager.DOWNLOAD_STARTED
            )
        }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_COMPLETED)
        verify {
            resourceDownloadManager.handleState(
                    task,
                    ResourceDownloadManager.DOWNLOAD_COMPLETED
            )
        }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_FAILED)
        verify {
            resourceDownloadManager.handleState(
                    task,
                    ResourceDownloadManager.DOWNLOAD_FAILED
            )
        }
        task.handleDownloadState(ResourceDownloadRunnable.DOWNLOAD_STATE_SKIPPED)
        verify {
            resourceDownloadManager.handleState(
                    task,
                    ResourceDownloadManager.DOWNLOAD_SKIPPED
            )
        }
    }

    @Test
    fun handleDecodeStateTest() {

        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_STARTED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DECODE_STARTED) }
        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_COMPLETED)
        verify {
            resourceDownloadManager.handleState(
                    task,
                    ResourceDownloadManager.DECODE_COMPLETED
            )
        }
        task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_FAILED)
        verify { resourceDownloadManager.handleState(task, ResourceDownloadManager.DECODE_FAILED) }
    }

    @Test
    fun getFileLocationFromDirectoryTest() {
        val wrapper = mockk<ContextWrapper>()
        val mockedFile = File("abc", "def")
        val directory = mockk<File>(relaxed = true)
        every { directory.absoluteFile } returns mockedFile
        every { resourceDownloadManager.getContextWrapper() } returns wrapper
        every { wrapper.getDir("downloads", Context.MODE_PRIVATE) } returns directory

        imageType.imageView = null
        task.initTask("/def", imageType, callback)
        val result = task.getFileLocationFromDirectory()
        verify { resourceDownloadManager.getContextWrapper() }
        verify { directory.mkdir() }

        assert(result.path == mockedFile.path + "/def")
        assert(result.name == "def")
    }
}
