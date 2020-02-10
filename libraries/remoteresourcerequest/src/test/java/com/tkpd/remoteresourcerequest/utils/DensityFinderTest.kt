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

    val context = mockk<Context>()
    var metrics = mockk<DisplayMetrics>(relaxed = true)
    val mockRes = mockk<Resources>()
    @Before
    fun setUp() {

        mockkStatic(DensityFinder::class)
    }

    @Test
    fun findDensityReturnLdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi= DisplayMetrics.DENSITY_LOW
        val density = DensityFinder.findDensity(context)

        assertEquals("ldpi", density)
    }

    @Test
    fun findDensityReturnMdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_MEDIUM
        val density = DensityFinder.findDensity(context)

        assertEquals("mdpi", density)
    }

    @Test
    fun findDensityReturnHdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_TV

        val density = DensityFinder.findDensity(context)

        assertEquals("hdpi", density)


        metrics.densityDpi = DisplayMetrics.DENSITY_HIGH
        val densityNew = DensityFinder.findDensity(context)

        assertEquals("hdpi",densityNew)
    }

    @Test
    fun findDensityReturnXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XHIGH
        val density = DensityFinder.findDensity(context)

        assertEquals("xhdpi", density)
    }

    @Test
    fun findDensityReturnXXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XXHIGH
        val density = DensityFinder.findDensity(context)

        assertEquals("xxhdpi", density)
        metrics.densityDpi = DisplayMetrics.DENSITY_360
        val density1 = DensityFinder.findDensity(context)

        assertEquals("xxhdpi", density1)
        metrics.densityDpi = DisplayMetrics.DENSITY_420
        val density2 = DensityFinder.findDensity(context)

        assertEquals("xxhdpi", density2)
        metrics.densityDpi = DisplayMetrics.DENSITY_400
        val density3 = DensityFinder.findDensity(context)

        assertEquals("xxhdpi", density3)
    }

    @Test
    fun findDensityReturnXXXhdpi() {
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_XXXHIGH
        val density = DensityFinder.findDensity(context)

        assertEquals("xxxhdpi", density)
        metrics.densityDpi = DisplayMetrics.DENSITY_560
        val densityNew = DensityFinder.findDensity(context)

        assertEquals("xxxhdpi", densityNew)
    }

    @Test
    fun findDensityReturnEmpty(){
        every { mockRes.displayMetrics } returns metrics
        every { context.resources } returns mockRes
        metrics.densityDpi = DisplayMetrics.DENSITY_140
        val density = DensityFinder.findDensity(context)

        assertEquals("", density)

    }
    @After
    fun tearDown() {
    }
}