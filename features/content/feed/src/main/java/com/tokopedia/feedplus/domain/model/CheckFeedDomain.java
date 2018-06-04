package com.tokopedia.feedplus.domain.model;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckFeedDomain {
    private final String totalData;

    public CheckFeedDomain(String totalData) {
        this.totalData = totalData;
    }

    public String getTotalData() {
        return totalData;
    }
}
