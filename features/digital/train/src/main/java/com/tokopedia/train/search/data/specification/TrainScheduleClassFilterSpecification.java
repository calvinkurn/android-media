package com.tokopedia.train.search.data.specification;

import com.tokopedia.train.common.specification.RoomSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 14/03/18.
 */

public class TrainScheduleClassFilterSpecification implements RoomSpecification {

    private List<String> trainClasses;
    private List<Object> args;

    public TrainScheduleClassFilterSpecification(List<String> trainClasses) {
        this.trainClasses = trainClasses;
        args = new ArrayList<>();
    }

    @Override
    public String query() {
        args.clear();
        String query = "(";
        for (int i = 0; i < trainClasses.size(); i++) {
            query += " displayClass = ? ";
            args.add(trainClasses.get(i));
            if (i < trainClasses.size() - 1) {
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
