package com.tokopedia.flight.search.presentation.model.filter;

/**
 * Created by User on 11/1/2017.
 */

public enum TransitEnum {
    DIRECT(1,com.tokopedia.flight.R.string.direct),
    ONE(2,com.tokopedia.flight.R.string.one_trasit),
    TWO(3,com.tokopedia.flight.R.string.two_transit),
    THREE_OR_MORE(4,com.tokopedia.flight.R.string.more_than_2_transit);

    private int id;
    private int valueRes;
    private TransitEnum(int id, int valueRes) {
        this.id = id;
        this.valueRes = valueRes;
    }

    public int getId() {
        return id;
    }

    public int getValueRes() {
        return valueRes;
    }
}