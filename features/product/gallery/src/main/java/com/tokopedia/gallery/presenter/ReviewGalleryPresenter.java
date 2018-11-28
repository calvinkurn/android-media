package com.tokopedia.gallery.presenter;

import android.content.Context;

import java.util.List;

public interface ReviewGalleryPresenter {
    int DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE = 15;

    void cancelLoadDataRequest();
    void loadData(int productId, int page);
}
