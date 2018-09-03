package com.tokopedia.feedplus.domain.model.feed;

import com.tokopedia.feedplus.domain.model.recentview.RecentViewProductDomain;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class FeedDomain {
    @Nullable
    private final List<DataFeedDomain> listFeed;

    @Nullable
    private final boolean hasNext;

    @Nullable
    private String title;

    @Nullable
    private List<RecentViewProductDomain> recentProduct;

    @Nullable
    private WhitelistDomain whitelist;

    public FeedDomain(@Nullable List<DataFeedDomain> listFeed,
                      boolean hasNext) {
        this.listFeed = listFeed;
        this.hasNext = hasNext;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public List<DataFeedDomain> getListFeed() {
        return listFeed;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setRecentProduct(@Nullable List<RecentViewProductDomain> recentProduct) {
        this.recentProduct = recentProduct;
    }

    @Nullable
    public List<RecentViewProductDomain> getRecentProduct() {
        return recentProduct;
    }

    @Nullable
    public WhitelistDomain getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(@Nullable WhitelistDomain whitelist) {
        this.whitelist = whitelist;
    }
}
