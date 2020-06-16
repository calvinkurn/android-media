package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.os.Handler
import android.os.Message
import com.tkpd.remoteresourcerequest.BuildConfig
import com.tkpd.remoteresourcerequest.callback.DeferredCallback
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.type.ImageType
import com.tkpd.remoteresourcerequest.type.MultiDPIImageType
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

class ResourceDownloadManagerTest {

    var task = mockk<DeferredResourceTask>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val manager = spyk<ResourceDownloadManager>()
    lateinit var imageType: ImageType
    private val deferredCallback = mockk<DeferredCallback>(relaxed = true)
    private val taskCallback = mockk<DeferredTaskCallback>(relaxed = true)
    private val imageView = mockk<DeferredImageView>(relaxed = true)
    private val handler = mockk<Handler>()

    @Before
    fun setUp() {
        every { task.deferredImageView } returns WeakReference(imageView)
        every { manager.getMainHandler() } returns handler
        every { manager.getNewDownloadTaskInstance() } returns task
        every { manager.scheduleWorker(context, any()) } just Runs

    }

    @After
    fun tearDown() {
    }

    @Test
    fun initialize() {
        every { manager.deferredCallback } returns deferredCallback
        manager.initialize(context, 1)
        verify(exactly = 1) { manager.scheduleWorker(context, 1) }
    }

    @Test
    fun setBaseAndRelativeUrl() {
        manager.initialize(context, 1)
        imageType = MultiDPIImageType(null, "abc.png")
        manager.setBaseAndRelativeUrl("www.abc.com/", "relative/")

        assertEquals(
                "www.abc.com/relative/${DensityFinder.densityUrlPath}/abc.png",
                manager.getResourceUrl(imageType)
        )
    }

    @Test
    fun startDownload() {

        DensityFinder.initializeDensityPath(context)
        every { manager.pollForIdleTask() } returns task
        manager.setBaseAndRelativeUrl("www.abccc.com/", "def/")
        manager.initialize(context, 1)
        imageType = MultiDPIImageType(null, "abc.png")

        manager.startDownload(imageType, taskCallback)

        verify { task.initTask("www.abccc.com/def/mdpi/abc.png", imageType, taskCallback) }
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
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDecodeStarted() {
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_STARTED
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDecodeComplete() {
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_COMPLETED

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)

        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDecodeFailed() {
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_FAILED

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)

        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }
        verify { task.recycleTask() }

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
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

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
    fun stopDeferredImageViewRenderingWithNull() {
        every { task.deferredImageView } returns null
        every { task.interruptDecodeRunnable() } just Runs

        manager.stopDeferredImageViewRendering(task)
        // in case no imageview is set
        verify(exactly = 0) { task.interruptDecodeRunnable() }

    }

    @Test
    fun stopDeferredImageViewRenderingWithNonNull() {
        every { task.deferredImageView } returns WeakReference(imageView)
        every { task.interruptDecodeRunnable() } just Runs

        manager.stopDeferredImageViewRendering(task)
        // in case no imageview is set
        verify(exactly = 1) { task.interruptDecodeRunnable() }

    }
}
