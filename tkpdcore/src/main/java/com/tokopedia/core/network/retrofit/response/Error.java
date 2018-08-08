package com.tokopedia.core.network.retrofit.response;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hendry on 2/28/2017.
 */

public class Error {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("object")
    @Expose
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * example usage:
     * TextErrorObject textErrorObject = errorList.get(0).getObjectListParse(TextErrorObject.class);
     */
    public <T> T getObjectParse(@NonNull Class<T> responseObjectErrorClass){
        Gson gson = new Gson();
        try {
            JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
            return gson.fromJson(jsonObject.toString(), responseObjectErrorClass);
        } catch (JsonSyntaxException e) { // the json might not be the instance, so just return it
            return null;
        }
    }

    /**
     * example usage:
     * List<TextErrorObject> textErrorObject = errorList.get(0).getObjectListParse(TextErrorObject[].class);
     */
    public <T> List<T> getObjectListParse(@NonNull final Class<T[]> responseObjectErrorClass){
        Gson gson = new Gson();
        try {
            JsonArray jsonArray = gson.toJsonTree(object).getAsJsonArray();
            final T[] jsonToObject = gson.fromJson(jsonArray, responseObjectErrorClass);
            return Arrays.asList(jsonToObject);
        } catch (JsonSyntaxException e) { // the json might not be the instance, so just return it
            return null;
        }
    }

}
