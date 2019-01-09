package com.tokopedia.home.beranda.domain.model.feed;

import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.List;

import javax.annotation.Nullable;

public class ContentFeedDomain {

    @Nullable
    private final String type;

    @Nullable
    private final List<InspirationDomain> inspirationDomains;

    @Nullable
    private final List<Data> topAdsList;

    public ContentFeedDomain(@Nullable String type,
                             @Nullable List<InspirationDomain> inspirationDomains,
                             @Nullable List<Data> topAdsList) {
        this.type = type;
        this.inspirationDomains = inspirationDomains;
        this.topAdsList = topAdsList;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public List<InspirationDomain> getInspirationDomains() {
        return inspirationDomains;
    }

    @Nullable
    public List<Data> getTopAdsList() {
        return topAdsList;
    }
}
