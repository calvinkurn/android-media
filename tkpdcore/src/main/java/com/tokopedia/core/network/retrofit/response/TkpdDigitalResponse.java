package com.tokopedia.core.network.retrofit.response;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class TkpdDigitalResponse {
    private static final String TAG = TkpdDigitalResponse.class.getSimpleName();

    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";
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
            if (jsonResponse.has(KEY_ERROR)) {
                try {
                    DigitalErrorResponse digitalErrorResponse =
                            new Gson().fromJson(strResponse, DigitalErrorResponse.class);
                    throw new ResponseErrorException(
                            digitalErrorResponse.getErrorMessageFormatted()
                    );
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    throw new ResponseErrorException();
                }
            }
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
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

    private void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    void setMessage(String message) {
        this.message = message;
    }

    private void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    private void setStrData(String strData) {
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
        @SerializedName("errors")
        @Expose
        private List<Error> errors = new ArrayList<>();

        public List<Error> getErrors() {
            return errors;
        }

        public String getErrorMessageFormatted() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                stringBuilder.append(errors.get(i).getTitle());
                if (i < errors.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString().trim();
        }

        public class Error {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("title")
            @Expose
            private String title;

            public String getId() {
                return id;
            }

            public String getStatus() {
                return status;
            }

            public String getTitle() {
                return title;
            }
        }
    }
}
