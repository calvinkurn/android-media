package com.tokopedia.purchase_platform.common.data.api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public class CartResponse {
    private static final String KEY_DATA = "data";
    private static final String KEY_HEADER = "header";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";

    private JsonElement jsonElementData;
    private CartHeaderResponse cartHeaderResponse;
    private Object objData;
    private String strData;
    private String strResponse;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static CartResponse factory(String strResponse) throws IOException {
        CartResponse cartResponse = new CartResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;

        CartHeaderResponse cartHeaderResponse = cartResponse.getGson().fromJson(
                jsonResponse.get(KEY_HEADER).getAsJsonObject().toString(), CartHeaderResponse.class
        );

        if ((!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull())
                && (cartHeaderResponse != null && cartHeaderResponse.getErrorCode() != null
                && cartHeaderResponse.getMessage() != null)) {
            if (!TextUtils.isEmpty(cartHeaderResponse.getMessageFormatted()))
                throw new CartResponseErrorException(cartHeaderResponse.getMessageFormatted());
            else
                throw new CartResponseErrorException();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
        } else {
            throw new CartResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }
        cartResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        cartResponse.setStrData(strData);
        cartResponse.setCartHeaderResponse(cartHeaderResponse);
        cartResponse.setStrResponse(strResponse);
        return cartResponse;
    }


    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    private void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    public CartHeaderResponse getCartHeaderResponse() {
        return cartHeaderResponse;
    }

    public void setCartHeaderResponse(CartHeaderResponse cartHeaderResponse) {
        this.cartHeaderResponse = cartHeaderResponse;
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
    public <T> T convertResponseObj(Class<T> clazz) {
        if (objData == null) {
            try {
                this.objData = gson.fromJson(strResponse, clazz);
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
