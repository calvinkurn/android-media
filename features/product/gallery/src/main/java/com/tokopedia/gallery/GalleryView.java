package com.tokopedia.gallery;

import com.tokopedia.gallery.viewmodel.ImageReviewItem;

import java.util.List;

public interface GalleryView {
    void onGalleryItemClicked(int position);
    void handleItemResult(List<ImageReviewItem> imageReviewItemList, boolean isHasNextPage);
    void handleErrorResult(Throwable e);
}
