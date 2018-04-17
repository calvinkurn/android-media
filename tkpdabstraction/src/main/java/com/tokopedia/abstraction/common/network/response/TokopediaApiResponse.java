package com.tokopedia.abstraction.common.network.response;

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
 * Created by kris on 3/29/18. Tokopedia
 */

public class TokopediaApiResponse {

    private static final String KEY_DATA = "data";
    private static final String KEY_HEADER = "header";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";

    private JsonElement jsonElementData;
    private TokopediaApiHeaderResponse tokopediaApiHeaderResponse;
    private Object objData;
    private String strData;
    private String strResponse;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TokopediaApiResponse factory(String strResponse) throws IOException {

        TokopediaApiResponse tokopediaApiResponse = new TokopediaApiResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;

        TokopediaApiHeaderResponse tokopediaApiHeaderResponse = tokopediaApiResponse.getGson().fromJson(
                jsonResponse.get(KEY_HEADER).getAsJsonObject().toString(), TokopediaApiHeaderResponse.class
        );

        if ((!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull())
                && (tokopediaApiHeaderResponse != null && tokopediaApiHeaderResponse.getErrorCode() != null
                && tokopediaApiHeaderResponse.getMessage() != null)) {
            if (!TextUtils.isEmpty(tokopediaApiHeaderResponse.getMessageFormatted()))
                throw new ResponseErrorException(tokopediaApiHeaderResponse.getMessageFormatted());
            else
                throw new ResponseErrorException();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }
        tokopediaApiResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tokopediaApiResponse.setStrData(strData);
        tokopediaApiResponse.setTokoPointHeaderResponse(tokopediaApiHeaderResponse);
        tokopediaApiResponse.setStrResponse(strResponse);
        return tokopediaApiResponse;
    }

    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    private void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    public TokopediaApiHeaderResponse getTokoPointHeaderResponse() {
        return tokopediaApiHeaderResponse;
    }

    private void setTokoPointHeaderResponse(TokopediaApiHeaderResponse tokopediaApiHeaderResponse) {
        this.tokopediaApiHeaderResponse = tokopediaApiHeaderResponse;
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
