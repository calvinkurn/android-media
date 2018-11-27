package com.tokopedia.gallery.subscriber;

import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.gallery.viewmodel.ImageReviewListModel;

import java.util.List;

import rx.Subscriber;

public class GetImageReviewSubscriber extends Subscriber<ImageReviewListModel> {

    public final GalleryView galleryView;

    public GetImageReviewSubscriber(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        galleryView.handleErrorResult(e);
    }

    @Override
    public void onNext(ImageReviewListModel imageReviewListModel) {
        galleryView.handleItemResult(
                imageReviewListModel.getImageReviewItemList(),
                imageReviewListModel.isHasNextPage()
        );
    }
}
