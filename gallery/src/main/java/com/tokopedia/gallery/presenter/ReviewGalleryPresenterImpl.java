package com.tokopedia.gallery.presenter;

import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.subscriber.GetImageReviewSubscriber;
import com.tokopedia.gallery.domain.GetImageReviewUseCase;

public class ReviewGalleryPresenterImpl implements ReviewGalleryPresenter {

    private GetImageReviewUseCase getImageReviewUseCase;
    private GalleryView galleryView;

    public ReviewGalleryPresenterImpl(GetImageReviewUseCase getImageReviewUseCase, GalleryView galleryView) {
        this.getImageReviewUseCase = getImageReviewUseCase;
        this.galleryView = galleryView;
    }

    @Override
    public void cancelLoadDataRequest() {
        getImageReviewUseCase.unsubscribe();
    }

    @Override
    public void loadData(int productId, int startRow) {
        int page = startRow / DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE + 1;
        getImageReviewUseCase.execute(
                GetImageReviewUseCase.createRequestParams(page,
                        DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE,
                        productId), new GetImageReviewSubscriber(galleryView, startRow));
    }
}
