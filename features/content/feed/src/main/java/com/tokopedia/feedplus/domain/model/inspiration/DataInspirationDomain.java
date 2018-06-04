package com.tokopedia.feedplus.domain.model.inspiration;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 6/21/17.
 */

public class DataInspirationDomain {

    private final @Nullable
    String source;

    private final @Nullable String title;

    private final @Nullable String foreign_title;

    private final @Nullable
    InspirationPaginationDomain pagination;

    private final @Nullable
    List<InspirationRecommendationDomain> recommendation;

    public DataInspirationDomain(String source,
                                 String title,
                                 String foreign_title,
                                 InspirationPaginationDomain pagination,
                                 List<InspirationRecommendationDomain> recommendation) {
        this.source = source;
        this.title = title;
        this.foreign_title = foreign_title;
        this.pagination = pagination;
        this.recommendation = recommendation;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getForeign_title() {
        return foreign_title;
    }

    @Nullable
    public InspirationPaginationDomain getPagination() {
        return pagination;
    }

    @Nullable
    public List<InspirationRecommendationDomain> getRecommendation() {
        return recommendation;
    }
}
