package com.tokopedia.flight.bookingV2.data.cloud;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tokopedia.flight.bookingV2.data.cloud.entity.CartEntity;
import com.tokopedia.flight.bookingV2.data.cloud.entity.InsuranceEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightCartJsonDeserializer implements JsonDeserializer<CartEntity> {
    private static final String KEY_DATA = "data";
    private static final String KEY_RELATIONSHIPS = "relationships";
    private static final String KEY_INCLUDED = "included";
    private static final String KEY_TYPE = "type";
    private static final String KEY_INSURANCE = "insurance_benefit";
    private static final String KEY_ATTRIBUTES = "attributes";
    private static final String KEY_ID = "id";
    private Gson gson;

    @Inject
    public FlightCartJsonDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public CartEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CartEntity cartEntity = new CartEntity();
        JsonObject rootJsonObject = json.getAsJsonObject();
        if (rootJsonObject.has(KEY_DATA)) {
            JsonObject dataObject = rootJsonObject.getAsJsonObject(KEY_DATA);
            cartEntity = FlightCartJsonDeserializer.this.gson.fromJson(rootJsonObject.get(KEY_DATA).toString(), CartEntity.class);
            if (dataObject.has(KEY_RELATIONSHIPS)) {
                List<InsuranceEntity> insuranceEntities = new ArrayList<>();
                if (rootJsonObject.has(KEY_INCLUDED)) {
                    JsonArray includes = rootJsonObject.getAsJsonArray(KEY_INCLUDED);
                    for (JsonElement include : includes) {
                        JsonObject jsonObject = include.getAsJsonObject();
                        if (jsonObject.get(KEY_TYPE).getAsString().equalsIgnoreCase(KEY_INSURANCE)) {
                            // insurance
                            InsuranceEntity insuranceEntity = FlightCartJsonDeserializer.this.gson.fromJson(jsonObject.get(KEY_ATTRIBUTES).toString(), InsuranceEntity.class);
                            insuranceEntity.setId(jsonObject.get(KEY_ID).getAsString());
                            insuranceEntities.add(insuranceEntity);
                        }
                    }
                }
                cartEntity.setInsurances(insuranceEntities);
            } else {
                cartEntity.setInsurances(new ArrayList<InsuranceEntity>());
            }
        }
        return cartEntity;
    }
}
