package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.os.Handler
import android.os.Message
import com.tkpd.remoteresourcerequest.utils.CallbackDispatcher
import com.tkpd.remoteresourcerequest.utils.DeferredCallback
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ResourceDownloadManagerTest {

    @MockK
    var task = mockk<DeferredResourceTask>(relaxed = true)
    val context = mockk<Context>(relaxed = true)
    val manager = spyk(ResourceDownloadManager.getManager())
    val callback = mockk<DeferredCallback>(relaxed = true)
    val handler = mockk<Handler>()

    @Before
    fun setUp() {
        every { manager.getDisplayDensity(context) } returns "xhdpi"
        every { manager.getMainHandler() } returns handler
        every { manager.getNewDownloadTaskInstance() } returns task
        every { manager.scheduleWorker(any(), any()) } just Runs
    }

    @After
    fun tearDown() {
    }

    @Test
    fun startDownload() {

        every { manager.pollForIdleTask() } returns task
        manager.setBaseAndRelativeUrl("www.abccc.com/", "def/")
        manager.initialize(context, 1)
        manager.startDownload("abc.png", null, null)

        verify { task.initTask("www.abccc.com/def/xhdpi/abc.png", null, null) }
        verify { task.getDownloadRunnable() }
    }

    @Test
    fun startDownloadCalledTwice() {
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)

        manager.startDownload("abc.png", null, null)
        manager.startDownload("abc.png", null, null)

        verify { task.removeDownloadRunnable() }
    }

    @Test(expected = IllegalStateException::class)
    fun `check call to startDownload throws IllegalStateException when called without initialize()`() {
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.startDownload("abc.png", null, null)
    }

    @Test
    fun handleStateDownloadComplete() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 3) { task.getDownloadUrl() }

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
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 3) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDecodeStarted() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DECODE_STARTED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 1) { task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun handleStateDecodeCompleted() {
        val msg = Message()
        every { task.getBitmap() } returns null
        every { handler.obtainMessage(ResourceDownloadManager.TASK_COMPLETED, task) } returns msg
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
        val msg = Message()
        msg.obj = task
        mockkStatic(CallbackDispatcher::class)
        every { CallbackDispatcher.dispatchLog(callback, "DECODE_FAILED ${task.getDownloadUrl()}, startedFromWorker = ${task.isRequestedFromWorker} ") } just Runs
        msg.what = ResourceDownloadManager.DECODE_FAILED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.initialize(context, 1)
        manager.addDeferredCallback(callback)
        val value = manager.handleMessage(msg)
        verify(exactly = 3) { task.getDownloadUrl() }
        verify { CallbackDispatcher.dispatchLog(callback, "DECODE_FAILED ${task.getDownloadUrl()}, startedFromWorker = ${task.isRequestedFromWorker} ") }
        assertTrue(value)
    }

    @Test
    fun handleStateFailed() {
        val msg = Message()
        msg.obj = task

        mockkStatic(CallbackDispatcher::class)
        every { CallbackDispatcher.dispatchLog(callback, "DOWNLOAD_FAILED ${task.getDownloadUrl()},  startedFromWorker = ${task.isRequestedFromWorker} ") } just Runs
        msg.what = ResourceDownloadManager.DOWNLOAD_FAILED
        manager.setBaseAndRelativeUrl("www.abc.com/", "def/")
        manager.addDeferredCallback(callback)
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify(exactly = 4) { task.getDownloadUrl() }

        verify { CallbackDispatcher.dispatchLog(callback, "DOWNLOAD_FAILED ${task.getDownloadUrl()},  startedFromWorker = ${task.isRequestedFromWorker} ") }
        assertTrue(value)
    }
}