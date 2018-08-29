package com.tokopedia.train.seat.data.specification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainChangeSeatSpecification implements GqlNetworkSpecification {
    private List<ChangeSeatMapRequest> requests;

    public TrainChangeSeatSpecification(List<ChangeSeatMapRequest> requests) {
        this.requests = requests;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_change_seat_mutation;
    }

    @Override
    public Map<String, Object> mapVariable() {
        Map<String, Object> parameters = new HashMap<>();
        JsonArray passengers = new JsonArray();
        for (ChangeSeatMapRequest request : requests) {
            JsonObject passenger = new JsonObject();
            passenger.addProperty("bookCode", request.getBookCode());
            passenger.addProperty("name", request.getName());
            passenger.addProperty("wagonCode", request.getWagonCode());
            passenger.addProperty("seat", request.getSeat());
            passengers.add(passenger);
        }
        parameters.put("changeSeatmap", passengers);
        Map<String, Object> result = new HashMap<>();
        result.put(TrainUrl.INPUT_GQL, parameters);
        return result;
    }
}