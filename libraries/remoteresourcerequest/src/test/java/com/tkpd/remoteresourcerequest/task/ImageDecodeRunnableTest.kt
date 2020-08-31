package com.tkpd.remoteresourcerequest.task

import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import com.tkpd.remoteresourcerequest.runnable.ImageDecodeRunnable
import io.mockk.*
import org.junit.Before
import org.junit.Test

class ImageDecodeRunnableTest {
    private var task = mockk< ImageDecodeRunnable.TaskDecodeProperties>(relaxed = true)
    private lateinit var decodeRunnable : ImageDecodeRunnable

    @Before
    fun setup(){
        decodeRunnable =
                ImageDecodeRunnable(task)
    }
    @Test
    fun runTest(){
        val metrics = mockk<DisplayMetrics>(relaxed = true)
        mockkStatic(BitmapFactory::class)
        metrics.heightPixels = 100
        metrics.widthPixels = 100
        every { task.getDisplayMetrics() } returns metrics
        every { BitmapFactory.decodeByteArray(any(), any(), any(), any()) } returns null
        decodeRunnable.run()
        verify { task.setCurrentThread(any()) }
        verify { task.getByteBuffer() }
        verify { task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_STARTED) }
        verify { task.getDisplayMetrics() }
        verify { task.handleDecodeState(ImageDecodeRunnable.DECODE_STATE_FAILED) }
    }
}
