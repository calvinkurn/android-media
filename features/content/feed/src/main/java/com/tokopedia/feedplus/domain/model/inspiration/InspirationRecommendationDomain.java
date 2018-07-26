package com.tokopedia.feedplus.domain.model.inspiration;

import javax.annotation.Nullable;

/**
 * @author by nisie on 6/21/17.
 */

public class InspirationRecommendationDomain {

    private final @Nullable
    String id;

    private final @Nullable String name;

    private final @Nullable String url;

    private final @Nullable String image_url;

    private final @Nullable String price;


    public InspirationRecommendationDomain(String id,
                                           String name,
                                           String url,
                                           String image_url,
                                           String price) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image_url = image_url;
        this.price = price;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getImage_url() {
        return image_url;
    }

    @Nullable
    public String getPrice() {
        return price;
    }
}
