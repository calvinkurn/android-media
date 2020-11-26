package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import com.tkpd.remoteresourcerequest.callback.CallbackDispatcher
import com.tkpd.remoteresourcerequest.callback.DeferredCallback
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.type.MultiDPIImageType
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tkpd.remoteresourcerequest.worker.DeferredWorker
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

class ResourceDownloadManagerTest {

    var task = mockk<DeferredResourceTask>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val manager = spyk<ResourceDownloadManager>()
    private val deferredCallback = mockk<DeferredCallback>(relaxed = true)
    private val taskCallback = mockk<DeferredTaskCallback>(relaxed = true)
    private val imageView = mockk<DeferredImageView>(relaxed = true)
    private val multiDPIImageType = mockk<MultiDPIImageType>(relaxed = true)
    private val handler = mockk<Handler>()
    private val weakImageViewReference = mockk<WeakReference<DeferredImageView>>(relaxed = true)
    private val dispatcher = mockk<CallbackDispatcher>()

    @Before
    fun setUp() {
        every { task.deferredImageView } returns weakImageViewReference
        every { manager.getMainHandler() } returns handler
        every { manager.getNewDownloadTaskInstance() } returns task
        every { manager.scheduleWorker(context, any()) } just Runs

    }

    @After
    fun tearDown() {
    }

    @Test
    fun initializeTest() {
        every { manager.deferredCallback } returns deferredCallback
        manager.initialize(context, 1)
        verify(exactly = 1) { manager.scheduleWorker(context, 1) }
    }

    @Test
    fun setBaseAndRelativeUrlTest() {
        manager.initialize(context, 1)
        manager.setBaseAndRelativeUrl("www.abc.com/", "relative/")
        every { multiDPIImageType.relativeFilePath } returns "mdpi/abc.png"
        assertEquals(
                "www.abc.com/relative/${DensityFinder.densityUrlPath}/abc.png",
                manager.getResourceUrl(multiDPIImageType)
        )
    }

    @Test
    fun startDownloadTest() {

        DensityFinder.initializeDensityPath(context)
        every { manager.pollForIdleTask() } returns task
        manager.setBaseAndRelativeUrl("www.abccc.com/", "def/")
        manager.initialize(context, 1)

        every { multiDPIImageType.relativeFilePath } returns "mdpi/abc.png"

        manager.startDownload(multiDPIImageType, taskCallback)

        verify { task.initTask("www.abccc.com/def/mdpi/abc.png", multiDPIImageType, taskCallback) }
        verify { task.getDownloadRunnable() }
    }

    @Test
    fun startDownloadCalledTwice() {
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)


        every { multiDPIImageType.relativeFilePath } returns "mdpi/abc.png"

        manager.startDownload(multiDPIImageType, null)
        manager.startDownload(multiDPIImageType, null)

        verify { task.getDownloadRunnable() }
        verify(exactly = 2) { manager.getNewDownloadTaskInstance() }
    }

    @Test(expected = IllegalStateException::class)
    fun `check call to startDownload throws IllegalStateException when called without initialize()`() {
        every { multiDPIImageType.relativeFilePath } returns "mdpi/abc.png"

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.startDownload(multiDPIImageType, null)
    }

    @Test
    fun workerScheduledTest() {
        val kk = mockkConstructor(DeferredWorker.Companion::class)

        val temp = mockk<DeferredWorker.Companion>(relaxed = true)
        every { temp.schedulePeriodicWorker(any(), any(), any()) } just Runs
        every { manager.deferredCallback } returns deferredCallback

        manager.initialize(context, 1)

        verify(exactly = 1) { manager.scheduleWorker(context, 1) }
    }

    @Test
    fun handleStateDownloadCompleteTest() {
        val msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        every { task.deferredImageView } returns null
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)

        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun `download state complete with non null weakreference and non null imageview`() {
        val msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        every { weakImageViewReference.get() } returns imageView
        every { task.deferredImageView } returns weakImageViewReference
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)

        verify(exactly = 2) { task.getDownloadUrl() }
        assertEquals(imageView.mRemoteFileName, "")

        assertTrue(value)
    }

    @Test
    fun `download state complete with non null weakreference and null imageview`() {
        val msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED

        val ref = WeakReference(imageView)
        every { task.deferredImageView } returns ref
        every { manager.getNewDownloadTaskInstance() } returns task


        every { msg.sendToTarget() } just Runs
        every { handler.obtainMessage(any(), any()) } returns msg

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        ref.clear()
        val value = manager.handleMessage(msg)

        verify(exactly = 2) { task.getDownloadUrl() }
        verify { handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, task) }
        verify { msg.sendToTarget() }

        assertTrue(value)
    }

    @Test
    fun `decode state complete with non null weakreference and null imageview`() {
        val bitmap = mockk<Bitmap>(relaxed = true)

        every { manager.getNewDownloadTaskInstance() } returns task
        every { weakImageViewReference.get() } returns null
        every { task.deferredImageView } returns weakImageViewReference

        val msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_COMPLETED
        every { msg.sendToTarget() } just Runs
        every { handler.obtainMessage(any(), any()) } returns msg

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }
        verify { handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, task) }
        verify { msg.sendToTarget() }

        assertEquals(task.requestedDensity, bitmap.density)
        assertTrue(value)
    }

    @Test
    fun `check density when decode state is completed with non null weakreference and null imageview`() {
        val bitmap = mockk<Bitmap>(relaxed = true)
        val msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_COMPLETED

        every { task.deferredImageView } returns WeakReference(imageView)
        every { task.getBitmap() } returns bitmap
        every { manager.getNewDownloadTaskInstance() } returns task
        every { msg.sendToTarget() } just Runs
        every { handler.obtainMessage(any(), any()) } returns msg

        task.requestedDensity = 2
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)

        verify { task.getBitmap() }
        verify { imageView.setImageBitmap(any()) }
        assertEquals(task.requestedDensity, bitmap.density)
        assertTrue(value)
    }

    @Test
    fun handleStateDecodeStartedTest() {
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_STARTED
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleRandomHandlerMessageTest() {
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = 0
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        val value = manager.handleMessage(msg)

        verify(exactly = 1) { task.getDownloadUrl() }

        assertFalse(value)
    }

    @Test
    fun handleStateDecodeCompleteTest() {
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs
        var msg = mockk<Message>(relaxed = true)
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_COMPLETED
        every { task.deferredImageView } returns weakImageViewReference
        every { weakImageViewReference.get() } returns imageView

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
        every {
            handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, any()).sendToTarget()
        } just Runs

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)

        verify(exactly = 2) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateTaskCompleteTest() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.TASK_COMPLETED

        every { task.deferredImageView } returns null
        every { handler.obtainMessage(any(), any()).sendToTarget() } just Runs

        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 1) { task.getDownloadUrl() }

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

    @Test
    fun `handleState() should pass correct parameter to obtainMessage() of handler`() {
        every { manager.getNewDownloadTaskInstance() } returns task
        every {
            handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, task).sendToTarget()
        } just Runs

        manager.initialize(context, 1)
        manager.handleState(task, ResourceDownloadManager.TASK_COMPLETED)

        verify { handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, task) }
    }
}
