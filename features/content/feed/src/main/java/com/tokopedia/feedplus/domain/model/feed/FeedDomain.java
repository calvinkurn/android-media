package com.tokopedia.feedplus.domain.model.feed;

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
    private WhitelistDomain whitelist;

    public FeedDomain(@Nullable List<DataFeedDomain> listFeed,
                      boolean hasNext) {
        this.listFeed = listFeed;
        this.hasNext = hasNext;
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
}
