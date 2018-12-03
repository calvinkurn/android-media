package com.tokopedia.gallery.subscriber

import com.tokopedia.gallery.GalleryView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.gallery.viewmodel.ImageReviewListModel

import rx.Subscriber

open class GetImageReviewSubscriber(val galleryView: GalleryView) : Subscriber<ImageReviewListModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        galleryView.handleErrorResult(e)
    }

    override fun onNext(imageReviewListModel: ImageReviewListModel) {
        galleryView.handleItemResult(
                imageReviewListModel.imageReviewItemList,
                imageReviewListModel.isHasNextPage
        )
    }
}
