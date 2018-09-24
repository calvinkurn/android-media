package com.tokopedia.feedplus.domain.model.feed;

import java.util.List;

import javax.annotation.Nonnull;
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
    private WhitelistDomain whitelist;

    @Nonnull
    private Boolean isInterestWhitelist = false;

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

    @Nullable
    public WhitelistDomain getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(@Nullable WhitelistDomain whitelist) {
        this.whitelist = whitelist;
    }

    @Nonnull
    public Boolean getInterestWhitelist() {
        return isInterestWhitelist;
    }

    public void setInterestWhitelist(@Nonnull Boolean interestWhitelist) {
        isInterestWhitelist = interestWhitelist;
    }
}
