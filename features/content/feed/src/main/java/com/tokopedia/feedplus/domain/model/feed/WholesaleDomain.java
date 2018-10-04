package com.tokopedia.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class WholesaleDomain {
    private @Nullable String quantityMinFmt;

    public WholesaleDomain(String quantityMinFmt) {
        this.quantityMinFmt = quantityMinFmt;
    }

    @Nullable
    public String getQuantityMinFmt() {
        return quantityMinFmt;
    }

    public void setQuantityMinFmt(@Nullable String quantityMinFmt) {
        this.quantityMinFmt = quantityMinFmt;
    }
}
