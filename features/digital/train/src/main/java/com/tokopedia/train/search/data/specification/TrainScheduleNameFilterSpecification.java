package com.tokopedia.train.search.data.specification;

import com.tokopedia.train.common.specification.RoomSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleNameFilterSpecification implements RoomSpecification {

    private List<String> trains;
    private List<Object> args;

    public TrainScheduleNameFilterSpecification(List<String> trains) {
        this.trains = trains;
        args = new ArrayList<>();
    }

    @Override
    public String query() {
        String query = "(";
        args.clear();
        for (int i = 0; i < trains.size(); i++) {
            query += " trainName = ? ";
            args.add(trains.get(i));
            if (i < trains.size() - 1) {
                query += " OR ";
            }
        }
        query += ")";
        return query;
    }

    @Override
    public List<Object> getArgs() {
        return args;
    }
}