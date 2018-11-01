package com.tokopedia.loyalty.domain.entity.response;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@SuppressWarnings("unused")
public class TokoPointResponse {
    private static final String KEY_DATA = "data";
    private static final String KEY_HEADER = "header";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";

    private JsonElement jsonElementData;
    private TokoPointHeaderResponse tokoPointHeaderResponse;
    private Object objData;
    private String strData;
    private String strResponse;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TokoPointResponse factory(String strResponse) throws IOException {

        TokoPointResponse tokoPointResponse = new TokoPointResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;

        TokoPointHeaderResponse tokoPointHeaderResponse = tokoPointResponse.getGson().fromJson(
                jsonResponse.get(KEY_HEADER).getAsJsonObject().toString(), TokoPointHeaderResponse.class
        );

        if ((!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull())
                && (tokoPointHeaderResponse != null && tokoPointHeaderResponse.getErrorCode() != null
                && tokoPointHeaderResponse.getMessage() != null)) {
            if (!TextUtils.isEmpty(tokoPointHeaderResponse.getMessageFormatted()))
                throw new ResponseErrorException(tokoPointHeaderResponse.getMessageFormatted());
            else
                throw new ResponseErrorException();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }
        tokoPointResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tokoPointResponse.setStrData(strData);
        tokoPointResponse.setTokoPointHeaderResponse(tokoPointHeaderResponse);
        tokoPointResponse.setStrResponse(strResponse);
        return tokoPointResponse;
    }

    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    private void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    public TokoPointHeaderResponse getTokoPointHeaderResponse() {
        return tokoPointHeaderResponse;
    }

    private void setTokoPointHeaderResponse(TokoPointHeaderResponse tokoPointHeaderResponse) {
        this.tokoPointHeaderResponse = tokoPointHeaderResponse;
    }

    public Object getObjData() {
        return objData;
    }

    public String getStrResponse() {
        return strResponse;
    }

    private void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    public void setObjData(Object objData) {
        this.objData = objData;
    }

    public String getStrData() {
        return strData;
    }

    private void setStrData(String strData) {
        this.strData = strData;
    }

    public Gson getGson() {
        return gson;
    }

    @SuppressWarnings("unchecked")
    public <T> T convertDataObj(Class<T> clazz) {
        if (objData == null) {
            try {
                this.objData = gson.fromJson(strData, clazz);
                return (T) objData;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objData;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertDataList(Class<T[]> clazz) {
        if (objData == null) {
            try {
                this.objData = Arrays.asList((T[]) (this.objData = gson.fromJson(strData, clazz)));
                return (List<T>) objData;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objData;
        }
    }
}
