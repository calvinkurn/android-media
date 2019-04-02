package com.tokopedia.home.beranda.domain.model.feed;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FeedDomain {
    @Nullable
    private final List<DataFeedDomain> listFeed;

    @Nullable
    private final boolean hasNext;

    @Nullable
    private String title;

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
}
