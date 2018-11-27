package com.tokopedia.gallery.viewmodel;

import java.util.List;

public class ImageReviewListModel {
    private List<ImageReviewItem> imageReviewItemList;
    private boolean hasNextPage;

    public ImageReviewListModel(List<ImageReviewItem> imageReviewItemList, boolean hasNextPage) {
        this.imageReviewItemList = imageReviewItemList;
        this.hasNextPage = hasNextPage;
    }

    public List<ImageReviewItem> getImageReviewItemList() {
        return imageReviewItemList;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
