package com.tokopedia.review.feature.reading.presentation.viewmodel

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ReadReviewSortFilterViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReadReviewSortFilterViewModel

    val originalFilter = setOf("Kualitas Produk", "Pelayanan Toko", "Kemasan Produk", "Harga", "Pengiriman")

    @Before
    fun setup() {
        mockkStatic(Resources::class)
        val resources = mockk<Resources>()
        val displayMetrics = mockk<DisplayMetrics>()
        every { Resources.getSystem() } returns resources
        every { Resources.getSystem().displayMetrics } returns displayMetrics

        viewModel = ReadReviewSortFilterViewModel()
        viewModel.buttonState.observeForever { }
        viewModel.resetButtonState.observeForever {  }
    }

    @After
    fun finish() {
        unmockkStatic(Resources::class)
    }
}