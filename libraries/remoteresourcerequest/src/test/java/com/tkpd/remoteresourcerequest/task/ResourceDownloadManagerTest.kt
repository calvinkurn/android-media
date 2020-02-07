package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.os.Handler
import android.os.Message
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ResourceDownloadManagerTest {

    @MockK
    var task = mockk<DeferredResourceTask>(relaxed = true)
    val context = mockk<Context>(relaxed = true)
    val manager = spyk(ResourceDownloadManager.getManager())
    @Before
    fun setUp() {
        val handler = mockk<Handler>()
        every { manager.readFromFile(1) } just runs
        every { manager.getDisplayDensity(context) } returns "xhdpi"
        every { manager.getMainHandler() } returns handler
        every { manager.getNewDownloadTaskInstance() } returns task


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

        every { manager.pollForIdleTask() } returns task
        manager.setBaseAndRelativeUrl("www.abccc.com/","def/")
        manager.initialize(context, 1)
        manager.startDownload("abc.png",null)

        verify { task.initTask("www.abccc.com/def/xhdpi/abc.png", null) }
        verify { task.getDownloadRunnable() }
    }

    @Test
    fun startDownloadCalledTwice() {
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.initialize(context, 1)

        manager.startDownload("abc.png",null)
        manager.startDownload("abc.png",null)

        verify { task.removeDownloadRunnable() }
    }

    @Test(expected = IllegalStateException::class)
    fun `check call to startDownload throws IllegalStateException when called without initialize()`() {
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.startDownload("abc.png",null)
    }
    @Test
    fun handleStateDownloadComplete() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_COMPLETED
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify (exactly = 2){ task.getDownloadUrl() }

        assertTrue(value)
    }
    @Test
    fun handleStateDownloadStarted() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_STARTED
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify (exactly = 1){ task.getDownloadUrl() }

        assertTrue(value)

    }
    @Test
    fun handleStateDownloadSkipped() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_SKIPPED
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify (exactly = 2){ task.getDownloadUrl() }

        assertTrue(value)
    }
    @Test
    fun handleStateFailed() {
        val msg = Message()
        msg.obj = task
        msg.what = ResourceDownloadManager.DOWNLOAD_FAILED
        manager.setBaseAndRelativeUrl("www.abc.com/","def/")
        manager.initialize(context, 1)
        val value = manager.handleMessage(msg)
        verify (exactly = 3){ task.getDownloadUrl() }

        assertTrue(value)
    }

    @Test
    fun stopPendingDownload() {
    }
}