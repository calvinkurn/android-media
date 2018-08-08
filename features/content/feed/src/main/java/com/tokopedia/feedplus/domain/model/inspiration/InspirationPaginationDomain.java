package com.tokopedia.feedplus.domain.model.inspiration;

import javax.annotation.Nullable;

/**
 * @author by nisie on 6/21/17.
 */

public class InspirationPaginationDomain {
    private final
    @Nullable
    Integer current_page;

    private final
    @Nullable
    Integer next_page;

    private final
    @Nullable
    Integer prev_page;

    public InspirationPaginationDomain(Integer current_page,
                                       Integer next_page,
                                       Integer prev_page) {
        this.current_page = current_page;
        this.next_page = next_page;
        this.prev_page = prev_page;
    }
}
