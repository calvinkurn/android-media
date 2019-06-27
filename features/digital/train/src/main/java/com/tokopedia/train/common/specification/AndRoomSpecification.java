package com.tokopedia.train.common.specification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 14/03/18.
 */

public class AndRoomSpecification implements RoomSpecification {

    private Specification first;
    private Specification second;
    private List<Object> args;

    public AndRoomSpecification(Specification first, Specification second) {
        this.first = first;
        this.second = second;
        args = new ArrayList<>();
    }

    @Override
    public String query() {
        String query = "";
        args.clear();
        if (first instanceof RoomSpecification) {
            RoomSpecification firstSpec = (RoomSpecification) first;
            if (!firstSpec.query().isEmpty()) {
                query += firstSpec.query();
                args.addAll(firstSpec.getArgs());
            }
        }
        if (second instanceof RoomSpecification) {
            RoomSpecification secondSpec = (RoomSpecification) second;
            if (!secondSpec.query().isEmpty()) {
                query += " AND " + secondSpec.query();
                args.addAll(secondSpec.getArgs());
            }
        }
        return query;
    }

    @Override
    public List<Object> getArgs() {
        return args;
    }
}