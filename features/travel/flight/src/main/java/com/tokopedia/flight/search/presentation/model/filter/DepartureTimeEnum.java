package com.tokopedia.flight.search.presentation.model.filter;

/**
 * Created by User on 11/1/2017.
 */

public enum DepartureTimeEnum {
    _00 (1, com.tokopedia.flight.R.string.departure_0000_to_0600),
    _06 (2, com.tokopedia.flight.R.string.departure_0600_to_1200),
    _12 (3, com.tokopedia.flight.R.string.departure_1200_to_1800),
    _18 (4, com.tokopedia.flight.R.string.departure_1800_to_2400);

    private int id;
    private int valueRes;
    private DepartureTimeEnum(int id, int valueRes) {
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