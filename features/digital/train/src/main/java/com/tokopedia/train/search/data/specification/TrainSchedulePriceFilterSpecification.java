package com.tokopedia.train.search.data.specification;

import com.tokopedia.train.common.specification.RoomSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 14/03/18.
 */

public class TrainSchedulePriceFilterSpecification implements RoomSpecification {

    private long minPriceFilterValue;
    private long maxPriceFilterValue;
    private List<Object> args;

    public TrainSchedulePriceFilterSpecification(long minPriceFilterValue, long maxPriceFilterValue) {
        this.minPriceFilterValue = minPriceFilterValue;
        this.maxPriceFilterValue = maxPriceFilterValue;
        args = new ArrayList<>();
    }

    @Override
    public String query() {
        args.clear();
        return " adultFare <= ? AND adultFare >= ? ";
    }

    @Override
    public List<Object> getArgs() {
        args.add(maxPriceFilterValue);
        args.add(minPriceFilterValue);
        return args;
    }
}
