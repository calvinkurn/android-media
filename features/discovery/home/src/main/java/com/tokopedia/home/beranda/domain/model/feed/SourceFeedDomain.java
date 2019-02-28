package com.tokopedia.home.beranda.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class SourceFeedDomain {
    private final @Nullable
    Integer type;

    public SourceFeedDomain(@Nullable Integer type) {
        this.type = type;
    }

    @Nullable
    public Integer getType() {
        return type;
    }
}
