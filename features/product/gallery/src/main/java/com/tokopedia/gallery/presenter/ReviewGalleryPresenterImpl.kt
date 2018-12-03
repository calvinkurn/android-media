package com.tokopedia.gallery.presenter

import com.tokopedia.gallery.GalleryView
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber
import com.tokopedia.gallery.domain.GetImageReviewUseCase

class ReviewGalleryPresenterImpl(private val getImageReviewUseCase: GetImageReviewUseCase, private val galleryView: GalleryView) : ReviewGalleryPresenter {

    override fun cancelLoadDataRequest() {
        getImageReviewUseCase.unsubscribe()
    }

    override fun loadData(productId: Int, page: Int) {
        getImageReviewUseCase.execute(
                GetImageReviewUseCase.createRequestParams(page,
                        ReviewGalleryPresenter.DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE,
                        productId), GetImageReviewSubscriber(galleryView))
    }
}
