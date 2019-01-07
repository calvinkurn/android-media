package com.tokopedia.train.passenger.data;

import android.util.ArrayMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.specification.GqlNetworkSpecification;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;
import com.tokopedia.train.passenger.domain.requestmodel.TrainBuyerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainPassengerRequest;
import com.tokopedia.train.passenger.domain.requestmodel.TrainScheduleRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainDoSoftBookingSpecification implements GqlNetworkSpecification {
    private static final String TRIP_ORIGIN = "origin";
    private static final String TRIP_DESTINATION = "destination";
    private static final String TRIP_TRAIN_NAME = "trainName";
    private static final String TRIP_TRAIN_NO = "trainNo";
    private static final String TRIP_TRAIN_CLASS = "trainClass";
    private static final String TRIP_TRAIN_SUBCLASS = "subclass";
    private static final String TRIP_DEPARTURE_TIMESTAMP = "departureTimestamp";
    private static final String BUYER_NAME = "name";
    private static final String BUYER_PHONE = "phone";
    private static final String BUYER_EMAIL = "email";
    private static final String PASSENGER_ID = "idNumber";
    private static final String PASSENGER_NAME = "name";
    private static final String PASSENGER_PAXTYPE = "paxType";
    private static final String PASSENGER_SALUTATIONID = "salutationId";
    private static final String PASSENGER_PHONENUMBER = "phoneNumber";

    private Map<String, Object> mapParam;

    public TrainDoSoftBookingSpecification(Map<String, Object> mapParam) {
        this.mapParam = mapParam;
    }

    @Override
    public int rawFileNameQuery() {
        return R.raw.kai_softbooking_mutation;
    }

    @Override
    public Map<String, Object> mapVariable() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> parameters = (Map<String, Object>) mapParam;
        TrainScheduleRequest departure = (TrainScheduleRequest) parameters.get(TrainSoftBookingUseCase.DEPARTURE_TRIP);
        JsonObject departureTrip = new JsonObject();
        departureTrip.addProperty(TRIP_ORIGIN, departure.getOrigin());
        departureTrip.addProperty(TRIP_DESTINATION, departure.getDestination());
        departureTrip.addProperty(TRIP_TRAIN_NAME, departure.getTrainName());
        departureTrip.addProperty(TRIP_TRAIN_NO, departure.getTrainNo());
        departureTrip.addProperty(TRIP_TRAIN_CLASS, departure.getTrainClass());
        departureTrip.addProperty(TRIP_TRAIN_SUBCLASS, departure.getSubclass());
        departureTrip.addProperty(TRIP_DEPARTURE_TIMESTAMP, departure.getDepartureTimestamp());
        result.put(TrainSoftBookingUseCase.DEPARTURE_TRIP, departureTrip);
        TrainScheduleRequest returnTripOrg = (TrainScheduleRequest) parameters.get(TrainSoftBookingUseCase.RETURN_TRIP);
        if (returnTripOrg != null) {
            JsonObject returnTrip = new JsonObject();
            returnTrip.addProperty(TRIP_ORIGIN, returnTripOrg.getOrigin());
            returnTrip.addProperty(TRIP_DESTINATION, returnTripOrg.getDestination());
            returnTrip.addProperty(TRIP_TRAIN_NAME, returnTripOrg.getTrainName());
            returnTrip.addProperty(TRIP_TRAIN_NO, returnTripOrg.getTrainNo());
            returnTrip.addProperty(TRIP_TRAIN_CLASS, returnTripOrg.getTrainClass());
            returnTrip.addProperty(TRIP_TRAIN_SUBCLASS, returnTripOrg.getSubclass());
            returnTrip.addProperty(TRIP_DEPARTURE_TIMESTAMP, returnTripOrg.getDepartureTimestamp());
            result.put(TrainSoftBookingUseCase.RETURN_TRIP, returnTrip);
        }
        TrainBuyerRequest trainBuyerRequest = (TrainBuyerRequest) parameters.get(TrainSoftBookingUseCase.BUYER);

        JsonObject buyer = new JsonObject();
        buyer.addProperty(BUYER_NAME, trainBuyerRequest.getName());
        buyer.addProperty(BUYER_PHONE, trainBuyerRequest.getPhone());
        buyer.addProperty(BUYER_EMAIL, trainBuyerRequest.getEmail());
        result.put(TrainSoftBookingUseCase.BUYER, buyer);
        result.put(TrainSoftBookingUseCase.TOTAL_ADULT, parameters.get(TrainSoftBookingUseCase.TOTAL_ADULT));
        result.put(TrainSoftBookingUseCase.TOTAL_INFANT, parameters.get(TrainSoftBookingUseCase.TOTAL_INFANT));
        result.put(TrainSoftBookingUseCase.DEVICE, parameters.get(TrainSoftBookingUseCase.DEVICE));

        List<TrainPassengerRequest> trainPassengerRequests = (List<TrainPassengerRequest>) parameters.get(TrainSoftBookingUseCase.PASSENGERS);
        JsonArray jsonArray = new JsonArray();
        for (TrainPassengerRequest passengerRequest : trainPassengerRequests) {
            JsonObject passenger = new JsonObject();
            passenger.addProperty(PASSENGER_ID, passengerRequest.getIdNumber());
            passenger.addProperty(PASSENGER_NAME, passengerRequest.getName());
            passenger.addProperty(PASSENGER_PAXTYPE, passengerRequest.getPaxType());
            passenger.addProperty(PASSENGER_SALUTATIONID, passengerRequest.getSalutationId());
            passenger.addProperty(PASSENGER_PHONENUMBER, passengerRequest.getPhoneNumber());
            jsonArray.add(passenger);
        }
        result.put(TrainSoftBookingUseCase.PASSENGERS, jsonArray);
        Map<String, Object> newResult = new HashMap<>();
        newResult.put(TrainUrl.INPUT_GQL, result);
        return newResult;
    }
}
