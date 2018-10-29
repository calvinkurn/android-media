package com.tokopedia.flight.cancellation.data.cloud;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancelPassengerEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 29/10/18.
 */

public class FlightCancellationJsonDeserializer implements JsonDeserializer<CancelPassengerEntity> {

    private static final String KEY_DATA = "data";
    private static final String KEY_INCLUDED = "included";
    private static final String KEY_TYPE = "type";
    private static final String KEY_REASONS = "reasons";
    private static final String KEY_REASON = "reason";
    private static final String KEY_ATTRIBUTES = "attributes";
    private static final String KEY_ID = "id";
    private Gson gson;

    @Inject
    public FlightCancellationJsonDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public CancelPassengerEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        CancelPassengerEntity cancelPassengerEntity = new CancelPassengerEntity();

        JsonObject rootJsonObject = jsonElement.getAsJsonObject();
        if (rootJsonObject.has(KEY_DATA)) {
            JsonObject dataObject = rootJsonObject.getAsJsonObject(KEY_DATA);
            cancelPassengerEntity = FlightCancellationJsonDeserializer.this.gson.fromJson(
                    rootJsonObject.get(KEY_DATA).toString(), CancelPassengerEntity.class);
            List<Reason> reasonList = new ArrayList<>();
            if (rootJsonObject.has(KEY_INCLUDED)) {
                JsonArray includes = rootJsonObject.getAsJsonArray(KEY_INCLUDED);
                for (JsonElement include : includes) {
                    JsonObject jsonObject = include.getAsJsonObject();
                    if (jsonObject.get(KEY_TYPE).getAsString().equalsIgnoreCase(KEY_REASON)) {
                        Reason reason = FlightCancellationJsonDeserializer.this.gson.fromJson(
                                jsonObject.get(KEY_ATTRIBUTES).toString(), Reason.class);
                        reason.setId(jsonObject.get(KEY_ID).getAsString());
                        reasonList.add(reason);
                    }
                }
            }
            cancelPassengerEntity.getAttributes().setReasons(reasonList);
        }

        return cancelPassengerEntity;
    }
}
