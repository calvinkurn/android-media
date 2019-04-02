package com.tokopedia.recentview.domain.model;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewBadgeDomain {
    private final String title;
    private final String imageUrl;

    public RecentViewBadgeDomain(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
