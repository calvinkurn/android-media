package com.tokopedia.feedplus.domain.model.feeddetail;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailContentDomain {

    private final
    @Nullable
    String type;

    private final
    @Nullable
    Integer total_product;

    private final
    @Nullable
    List<FeedDetailProductDomain> products;

    private final
    @Nullable
    String status_activity;

    public FeedDetailContentDomain(String type,
                                   Integer total_product,
                                   List<FeedDetailProductDomain> products,
                                   String status_activity) {
        this.type = type;
        this.total_product = total_product;
        this.products = products;
        this.status_activity = status_activity;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public Integer getTotal_product() {
        return total_product;
    }

    @Nullable
    public List<FeedDetailProductDomain> getProducts() {
        return products;
    }

    @Nullable
    public String getStatus_activity() {
        return status_activity;
    }
}
