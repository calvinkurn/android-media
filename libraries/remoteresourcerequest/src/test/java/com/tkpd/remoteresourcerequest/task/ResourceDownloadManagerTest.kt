package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.os.Handler
import android.os.Message
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.type.ImageType
import com.tkpd.remoteresourcerequest.type.MultiDPIImageType
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.lang.ref.WeakReference

class ResourceDownloadManagerTest {

    var task = mockk<DeferredResourceTask>(relaxed = true)
    val context = mockk<Context>(relaxed = true)
    val manager = spyk<ResourceDownloadManager>()
    lateinit var imageType: ImageType
    val callback = mockk<DeferredTaskCallback>(relaxed = true)
    private val imageView = mockk<DeferredImageView>(relaxed = true)
    val handler = mockk<Handler>()

    @Before
    fun setUp() {
        every { task.deferredImageView } returns WeakReference(imageView)
        every { manager.getMainHandler() } returns handler
        every { manager.getNewDownloadTaskInstance() } returns task
        every { manager.scheduleWorker(context, 1) } just Runs


    }

    @After
    fun tearDown() {
    }

    @Test
    fun initialize() {
    }

    @Test
    fun setBaseAndRelativeUrl() {
    }

    @Test
    fun startDownload() {

        DensityFinder.initializeDensityPath(context)
        every { manager.pollForIdleTask() } returns task
        manager.setBaseAndRelativeUrl("www.abccc.com/", "def/")
        manager.initialize(context, 1)
        imageType = MultiDPIImageType(null, "abc.png")

        manager.startDownload(imageType, callback)

        verify { task.initTask("www.abccc.com/def/mdpi/abc.png", imageType, callback) }
        verify { task.getDownloadRunnable() }
    }

    @Test
    fun startDownloadCalledTwice() {
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)

        imageType = MultiDPIImageType(null, "abc.png")
        manager.startDownload(imageType, null)
        manager.startDownload(imageType, null)

        verify { task.getDownloadRunnable() }
    }

    @Test(expected = IllegalStateException::class)
    fun `check call to startDownload throws IllegalStateException when called without initialize()`() {
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")

        imageType = MultiDPIImageType(null, "abc.png")
        manager.startDownload(imageType, null)
    }

    @Test
    fun handleStateDownloadComplete() {
        val msg = mockk<Message>()
        msg.obj = task
        every { task.deferredImageView } returns null
        every { handler.obtainMessage(any(),any()).sendToTarget() } just Runs
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDownloadStarted() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_STARTED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 1) { task.getDownloadUrl() }

        assertTrue(value)

    }

    @Test
    fun handleStateDownloadSkipped() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_SKIPPED

        every { task.deferredImageView } returns null
        every { handler.obtainMessage(any(),any()).sendToTarget() } just Runs

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateFailed() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_FAILED
        every { task.deferredImageView } returns null
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun stopPendingDownload() {
    }
}