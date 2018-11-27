package com.tokopedia.gallery.customview;

import com.tokopedia.gallery.viewmodel.ImageReviewItem;

import java.util.List;

public interface ImageReviewSliderView {
        void displayImage(int position);
        void onLoadDataSuccess(List<ImageReviewItem> imageReviewItems, boolean isHasNextPage);
        void onLoadDataFailed();
        boolean onBackPressed();
        void resetState();
        void onLoadingData();
}
