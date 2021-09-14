package com.tokopedia.gallery.presenter

import com.tokopedia.gallery.GalleryView
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

abstract class ReviewGalleryPresenterTestFixture {

    @RelaxedMockK
    lateinit var getImageReviewUseCase: GetImageReviewUseCase

    @RelaxedMockK
    lateinit var galleryView: GalleryView

    protected lateinit var presenter: ReviewGalleryPresenterContract

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = ReviewGalleryPresenter(getImageReviewUseCase, galleryView)
    }

}