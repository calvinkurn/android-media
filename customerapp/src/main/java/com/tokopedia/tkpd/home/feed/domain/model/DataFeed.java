package com.tokopedia.tkpd.home.feed.domain.model;

import java.util.List;

/**
 * @author kulomady on 12/8/16.
 */

public class DataFeed {
    private List<ProductFeed> recentProductList;
    private Feed mFeed;
    private List<TopAds> mTopAds;
    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void setRecentProductList(List<ProductFeed> recentProductList) {
        this.recentProductList = recentProductList;
    }

    public void setFeed(Feed feed) {
        mFeed = feed;
    }


    public List<ProductFeed> getRecentProductList() {
        return recentProductList;
    }

    public Feed getFeed() {
        return mFeed;
    }

    public List<TopAds> getTopAds() {
        return mTopAds;
    }

    public void setTopAds(List<TopAds> topAds) {
        mTopAds = topAds;
    }
}
