package com.tokopedia.home.beranda.domain.model.feed;

import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.List;

import javax.annotation.Nullable;

public class ContentFeedDomain {

    @Nullable
    private final
    String type;

    @Nullable
    private final
    int totalProduct;

    @Nullable
    private final
    String statusActivity;

    @Nullable
    private final List<InspirationDomain> inspirationDomains;

    @Nullable
    private final List<Data> topAdsList;

    public ContentFeedDomain(@Nullable String type, @Nullable int total_product,
                             @Nullable List<InspirationDomain> inspirationDomains,
                             @Nullable List<Data> topAdsList,
                             @Nullable String status_activity) {
        this.type = type;
        this.totalProduct = total_product;
        this.statusActivity = status_activity;
        this.inspirationDomains = inspirationDomains;
        this.topAdsList = topAdsList;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public int getTotalProduct() {
        return totalProduct;
    }

    @Nullable
    public String getStatusActivity() {
        return statusActivity;
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
