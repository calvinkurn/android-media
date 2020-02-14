package com.tokopedia.flight.search.presentation.model.filter;

/**
 * Created by User on 11/1/2017.
 */

public enum RefundableEnum {

    REFUNDABLE(1,com.tokopedia.flight.R.string.refundable),
    PARTIAL_REFUNDABLE(2,com.tokopedia.flight.R.string.partial_refundable),
    NOT_REFUNDABLE(3,com.tokopedia.flight.R.string.non_refundable);

    private int id;
    private int valueRes;

    RefundableEnum(int id, int valueRes) {
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