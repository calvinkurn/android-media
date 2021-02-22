package com.tokopedia.gallery.presenter

import com.tokopedia.gallery.viewmodel.ImageReviewListModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers
import rx.observers.TestSubscriber

class ReviewGalleryPresenterTest : ReviewGalleryPresenterTestFixture() {

    @Test
    fun `when loadData success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ImageReviewListModel>(relaxed = true)
        val testSubscriber: TestSubscriber<ImageReviewListModel> = TestSubscriber()

        every {
            getImageReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.loadData(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetImageReviewUseCaseCalled()
    }

    @Test
    fun `when loadData fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<ImageReviewListModel> = TestSubscriber()

        every {
            getImageReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.loadData(ArgumentMatchers.anyLong(), ArgumentMatchers.anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetImageReviewUseCaseCalled()
    }

    private fun verifyGetImageReviewUseCaseCalled() {
        verify { getImageReviewUseCase.execute(any(), any()) }
    }
}