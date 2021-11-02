package com.tokopedia.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gallery.domain.GetReviewImagesUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class GalleryViewModelTestFixture {

    @RelaxedMockK
    lateinit var getReviewImagesUseCase: GetReviewImagesUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: GalleryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = GalleryViewModel(
            getReviewImagesUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.reviewImages.observeForever { }
    }
}