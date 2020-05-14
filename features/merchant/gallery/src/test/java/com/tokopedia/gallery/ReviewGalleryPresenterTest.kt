package com.tokopedia.gallery

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.presenter.ReviewGalleryPresenter
import com.tokopedia.gallery.presenter.ReviewGalleryPresenterImpl
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.gallery.viewmodel.ImageReviewListModel
import com.tokopedia.usecase.RequestParams
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

import org.junit.Assert.*
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.util.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(JUnitPlatform::class)
class ReviewGalleryPresenterTest : Spek ({

    given("presenter invoked") {

        val getImageReviewUseCase: GetImageReviewUseCase = mock()
        val galleryView: GalleryView = mock()
        val reviewGalleryPresenter = ReviewGalleryPresenterImpl(getImageReviewUseCase, galleryView)

        val productId = 1234
        val page = 1

        val requestParams = ArgumentCaptor.forClass(RequestParams::class.java)
        val getImageReviewSubscriber = ArgumentCaptor.forClass(GetImageReviewSubscriber::class.java)

        on("cancel load data request") {
            reviewGalleryPresenter.cancelLoadDataRequest()

            it("unsubscribe use case") {
                verify(getImageReviewUseCase).unsubscribe()
            }
        }

        on("load data") {
            reviewGalleryPresenter.loadData(productId, page)

            it("execute use case") {
                verify(getImageReviewUseCase).execute(
                        requestParams.capture(),
                        getImageReviewSubscriber.capture()
                )
                assertEquals(productId.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_PRODUCT_ID, 0).toLong())
                assertEquals(page.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_PAGE, 0).toLong())
                assertEquals(ReviewGalleryPresenter.DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE.toLong(), requestParams.value.getInt(GetImageReviewUseCase.KEY_TOTAL, 0).toLong())
                assertEquals(galleryView, getImageReviewSubscriber.value.galleryView)
            }
        }
    }
})