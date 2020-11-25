package com.tkpd.remoteresourcerequest.utils
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class DensityFinderTest {

    private val context = mockk<Context>()
    private var metrics = mockk<DisplayMetrics>(relaxed = true)
    private val mockRes = mockk<Resources>()

    @Before
    fun setUp() {

        mockkStatic(DensityFinder::class)
    }

    @Test
    fun findDensityReturnNotLdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_LOW
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertNotEquals("ldpi", density)
    }

    @Test
    fun findDensityReturnMdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_MEDIUM
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("mdpi", density)
    }

    @Test
    fun findDensityReturnHdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_TV

        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("hdpi", density)


        metrics.densityDpi = DisplayMetrics.DENSITY_HIGH
        DensityFinder.initializeDensityPath(context)
        val densityNew = DensityFinder.densityUrlPath
        assertEquals("hdpi", densityNew)
    }

    @Test
    fun findDensityReturnXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XHIGH
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("xhdpi", density)
    }

    @Test
    fun findDensityReturnXXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XXHIGH
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("xxhdpi", density)
        metrics.densityDpi = DisplayMetrics.DENSITY_360
        DensityFinder.initializeDensityPath(context)
        val density1 = DensityFinder.densityUrlPath
        assertEquals("xxhdpi", density1)
        metrics.densityDpi = DisplayMetrics.DENSITY_420
        DensityFinder.initializeDensityPath(context)
        val density2 = DensityFinder.densityUrlPath
        assertEquals("xxhdpi", density2)
        metrics.densityDpi = DisplayMetrics.DENSITY_400
        DensityFinder.initializeDensityPath(context)
        val density3 = DensityFinder.densityUrlPath
        assertEquals("xxhdpi", density3)
    }

    @Test
    fun findDensityReturnXXXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XXXHIGH
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("xxxhdpi", density)
        metrics.densityDpi = DisplayMetrics.DENSITY_560
        DensityFinder.initializeDensityPath(context)
        val densityNew = DensityFinder.densityUrlPath

        assertEquals("xxxhdpi", densityNew)
    }

    @Test
    fun findDensityReturnEmpty() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_140
        assertEquals("mdpi", DensityFinder.densityUrlPath)
        metrics.densityDpi = 0
        DensityFinder.initializeDensityPath(context)
        val density = DensityFinder.densityUrlPath
        assertEquals("mdpi", density)
    }

    @After
    fun tearDown() {
    }
}
