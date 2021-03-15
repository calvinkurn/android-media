package com.tokopedia.gallery.presenter

import com.tokopedia.gallery.GalleryView
import com.tokopedia.gallery.domain.GetImageReviewUseCase
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber

class ReviewGalleryPresenter(private val getImageReviewUseCase: GetImageReviewUseCase, private val galleryView: GalleryView) : ReviewGalleryPresenterContract {

    override fun loadData(productId: Long, page: Int) {
        getImageReviewUseCase.execute(
                GetImageReviewUseCase.createRequestParams(page,
                        ReviewGalleryPresenterContract.DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE,
                        productId), GetImageReviewSubscriber(galleryView))
    }
}
