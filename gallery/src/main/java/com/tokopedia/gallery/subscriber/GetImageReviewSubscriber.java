package com.tokopedia.gallery.subscriber;

import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

import java.util.List;

import rx.Subscriber;

public class GetImageReviewSubscriber extends Subscriber<List<ImageReviewItem>> {

    public final GalleryView galleryView;
    public final int startRow;

    public GetImageReviewSubscriber(GalleryView galleryView, int startRow) {
        this.galleryView = galleryView;
        this.startRow = startRow;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        galleryView.handleErrorResult(startRow);
    }

    @Override
    public void onNext(List<ImageReviewItem> imageReviewItems) {
        if (!imageReviewItems.isEmpty()) {
            galleryView.handleItemResult(imageReviewItems);
        } else {
            galleryView.handleEmptyResult();
        }
    }
}
