package com.tokopedia.gallery

import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber
import com.tokopedia.usecase.RequestParams

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ReviewGalleryPresenterTest {

    private var getImageReviewUseCase: GetImageReviewUseCase? = null
    private var galleryView: GalleryView? = null
    private var reviewGalleryPresenter: ReviewGalleryPresenter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        getImageReviewUseCase = Mockito.mock(GetImageReviewUseCase::class.java)
        galleryView = Mockito.mock(GalleryView::class.java)
        reviewGalleryPresenter = ReviewGalleryPresenterImpl(getImageReviewUseCase, galleryView)
    }

    @Test
    fun cancelLoadDataRequest_unsubscribeUseCase() {
        //when
        reviewGalleryPresenter!!.cancelLoadDataRequest()

        //then
        Mockito.verify<GetImageReviewUseCase>(getImageReviewUseCase).unsubscribe()
    }

    @Test
    fun loadData_executeUseCase() {
        //given
        val productId = 1234
        val page = 1

        val requestParams = ArgumentCaptor.forClass(RequestParams::class.java)
        val getImageReviewSubscriber = ArgumentCaptor.forClass(GetImageReviewSubscriber::class.java)

        //when
        reviewGalleryPresenter!!.loadData(productId, page)

        //then
        Mockito.verify<GetImageReviewUseCase>(getImageReviewUseCase).execute(
                requestParams.capture(),
                getImageReviewSubscriber.capture()
        )
        assertEquals(productId.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_PRODUCT_ID, 0).toLong())
        assertEquals(page.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_PAGE, 0).toLong())
        assertEquals(ReviewGalleryPresenter.DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_TOTAL, 0).toLong())
        assertEquals(galleryView, getImageReviewSubscriber.value.galleryView)
    }
}