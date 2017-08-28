package com.tokopedia.core.database.o2o;

/**
 * Created by okasurya on 8/28/17.
 */

public class QueryParameter {
    private int offset;
    private int limit;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
