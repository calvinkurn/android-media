package com.tokopedia.core.network.retrofit.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.exception.ResponseDataNullException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class TkpdDigitalResponse {
    private static final String TAG = TkpdDigitalResponse.class.getSimpleName();

    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";
    private static final String KEY_TITLE = "title";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";


    private JsonElement jsonElementData;
    private Object objData;
    private String message;
    private String strResponse;
    private String strData;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TkpdDigitalResponse factory(String strResponse) throws IOException {
        Log.d(TAG, strResponse);

        TkpdDigitalResponse tkpdDigitalResponse = new TkpdDigitalResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;
        if (!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull()) {
            String errorDefault = DEFAULT_ERROR_MESSAGE_DATA_NULL;
            if (jsonResponse.has(KEY_ERROR)) {
                StringBuilder stringBuilder = new StringBuilder();
                JsonArray jsonArrayError = jsonResponse.get(KEY_ERROR).getAsJsonArray();
                for (JsonElement jsonElementError : jsonArrayError) {
                    String messageError = jsonElementError.getAsJsonObject()
                            .get(KEY_TITLE).getAsString();
                    stringBuilder.append(messageError).append(", ");
                }
                errorDefault = stringBuilder.toString();
            }
            throw new ResponseDataNullException(errorDefault);
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
            throw new ResponseDataNullException(strData);
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }
        tkpdDigitalResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tkpdDigitalResponse.setMessage("");
        tkpdDigitalResponse.setStrData(strData);
        tkpdDigitalResponse.setStrResponse(strResponse);
        return tkpdDigitalResponse;
    }

    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    public String getMessage() {
        return message;
    }

    public String getStrResponse() {
        return strResponse;
    }

    public String getStrData() {
        return strData;
    }

    void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    void setStrData(String strData) {
        this.strData = strData;
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

    /**
     * @author anggaprasetiyo on 3/7/17.
     */

    public static class DigitalErrorResponse {
        @SerializedName("error")
        @Expose
        private Error error;

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }

        public class Error {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("status")
            @Expose
            private int status;
            @SerializedName("title")
            @Expose
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

        }
    }
}
