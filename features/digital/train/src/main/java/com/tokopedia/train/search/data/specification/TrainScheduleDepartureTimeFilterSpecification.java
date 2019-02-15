package com.tokopedia.train.search.data.specification;

import com.tokopedia.train.common.specification.RoomSpecification;
import com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class TrainScheduleDepartureTimeFilterSpecification implements RoomSpecification {

    private List<String> departureTrains;
    private List<Object> args;

    public TrainScheduleDepartureTimeFilterSpecification(List<String> departureTrains) {
        this.departureTrains = departureTrains;
        args = new ArrayList<>();
    }

    @Override
    public String query() {
        String query = "(";
        args.clear();
        for (int i = 0; i < departureTrains.size(); i++) {
            String departureTrain = departureTrains.get(i);
            switch (departureTrain) {
                case DepartureTimeTypeDef.MORNING:
                    query += " departureHour BETWEEN ? AND ? ";
                    args.add("00");
                    args.add("12");
                    break;
                case DepartureTimeTypeDef.AFTERNOON:
                    query += " departureHour BETWEEN ? AND ? ";
                    args.add("12");
                    args.add("18");
                    break;
                case DepartureTimeTypeDef.NIGHT:
                    query += " departureHour BETWEEN ? AND ? ";
                    args.add("18");
                    args.add("23");
                    break;
            }
            if (i < departureTrains.size() - 1) {
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
