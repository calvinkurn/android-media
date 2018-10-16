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
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

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
    private static final String KEY_INCLUDED = "included";
    private static final String KEY_META = "meta";
    private static final String KEY_ERROR = "errors";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";

    private JsonElement jsonElementData;
    private JsonElement jsonElementIncluded;
    private JsonElement jsonElementMeta;
    private Object objData;
    private Object objIncluded;
    private Object objMeta;
    private String message;
    private String strResponse;
    private String strData;
    private String strIncluded;
    private String strMeta;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TkpdDigitalResponse factory(String strResponse) throws IOException {
        Log.d(TAG, strResponse);
        TkpdDigitalResponse tkpdDigitalResponse = new TkpdDigitalResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;
        String strIncluded;
        String strMeta;
        if (!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull()) {
            if (jsonResponse.has(KEY_ERROR)) {
                try {
                    DigitalErrorResponse digitalErrorResponse =
                            new Gson().fromJson(strResponse, DigitalErrorResponse.class);
                    throw new ResponseErrorException(
                            digitalErrorResponse.getDigitalErrorMessageFormatted()
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
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }

        if (!jsonResponse.has(KEY_INCLUDED) || jsonResponse.get(KEY_INCLUDED).isJsonNull()) {
            strIncluded = null;
        } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse.get(KEY_INCLUDED).isJsonObject()) {
            strIncluded = jsonResponse.get(KEY_INCLUDED).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse.get(KEY_INCLUDED).isJsonArray()) {
            strIncluded = jsonResponse.get(KEY_INCLUDED).getAsJsonArray().toString();
        } else {
            strIncluded = null;
        }

        if (!jsonResponse.has(KEY_META) || jsonResponse.get(KEY_META).isJsonNull()) {
            strMeta = null;
        } else if (jsonResponse.has(KEY_META) && jsonResponse.get(KEY_META).isJsonObject()) {
            strMeta = jsonResponse.get(KEY_META).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_META) && jsonResponse.get(KEY_META).isJsonArray()) {
            strMeta = jsonResponse.get(KEY_META).getAsJsonArray().toString();
        } else {
            strMeta = null;
        }
        tkpdDigitalResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tkpdDigitalResponse.setJsonElementIncluded(jsonResponse.get(KEY_INCLUDED));
        tkpdDigitalResponse.setJsonElementMeta(jsonResponse.get(KEY_META));
        tkpdDigitalResponse.setMessage("");
        tkpdDigitalResponse.setStrData(strData);
        tkpdDigitalResponse.setStrIncluded(strIncluded);
        tkpdDigitalResponse.setStrResponse(strResponse);
        tkpdDigitalResponse.setStrMeta(strMeta);
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

    public Object getObjMeta() {
        return objMeta;
    }

    public void setObjMeta(Object objMeta) {
        this.objMeta = objMeta;
    }

    public String getStrMeta() {
        return strMeta;
    }

    public void setStrMeta(String strMeta) {
        this.strMeta = strMeta;
    }

    public JsonElement getJsonElementMeta() {
        return jsonElementMeta;
    }

    public void setJsonElementMeta(JsonElement jsonElementMeta) {
        this.jsonElementMeta = jsonElementMeta;
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

    public Object getObjIncluded() {
        return objIncluded;
    }

    public void setObjIncluded(Object objIncluded) {
        this.objIncluded = objIncluded;
    }

    public String getStrIncluded() {
        return strIncluded;
    }

    private void setStrIncluded(String strIncluded) {
        this.strIncluded = strIncluded;
    }

    public JsonElement getJsonElementIncluded() {
        return jsonElementIncluded;
    }

    private void setJsonElementIncluded(JsonElement jsonElementIncluded) {
        this.jsonElementIncluded = jsonElementIncluded;
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


    @SuppressWarnings("unchecked")
    public <T> T convertIncludedObj(Class<T> clazz) {
        if (objIncluded == null) {
            try {
                this.objIncluded = gson.fromJson(strIncluded, clazz);
                return (T) objIncluded;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objIncluded;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertIncludedList(Class<T[]> clazz) {
        if (objIncluded == null) {
            try {
                this.objIncluded = Arrays.asList((T[])
                        (this.objIncluded = gson.fromJson(strIncluded, clazz)));
                return (List<T>) objIncluded;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objIncluded;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertMetaObj(Class<T> clazz) {
        if (objMeta == null) {
            try {
                this.objMeta = gson.fromJson(strMeta, clazz);
                return (T) objMeta;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objMeta;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertMetaList(Class<T[]> clazz) {
        if (objMeta == null) {
            try {
                this.objMeta = Arrays.asList((T[])
                        (this.objMeta = gson.fromJson(strMeta, clazz)));
                return (List<T>) objMeta;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objMeta;
        }
    }


    /**
     * @author anggaprasetiyo on 3/7/17.
     */

    public static class DigitalErrorResponse {
        public static final int ERROR_DIGITAL = 1;
        public static final int ERROR_SERVER = 2;

        @SerializedName("errors")
        @Expose
        private List<Error> errors = new ArrayList<>();
        @SerializedName("message_error")
        @Expose
        private List<String> messageError = new ArrayList<>();
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;

        private int errorCode;

        public List<Error> getErrors() {
            return errors;
        }

        public void setErrors(List<Error> errors) {
            this.errors = errors;
        }

        public List<String> getMessageError() {
            return messageError;
        }

        public void setMessageError(List<String> messageError) {
            this.messageError = messageError;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public int getTypeOfError() {
            if (!errors.isEmpty()) return ERROR_DIGITAL;
            else return ERROR_SERVER;
        }

        public String getDigitalErrorMessageFormatted() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                stringBuilder.append(errors.get(i).getTitle());
                if (i < errors.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString().trim();
        }

        public String getServerErrorMessageFormatted() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < messageError.size(); i++) {
                stringBuilder.append(messageError.get(i));
                if (i < errors.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString().trim();
        }

        public static DigitalErrorResponse factory(String errorBody, int code) {
            try {
                DigitalErrorResponse digitalErrorResponse =
                        new Gson().fromJson(errorBody, DigitalErrorResponse.class);
                digitalErrorResponse.setErrorCode(code);
                return digitalErrorResponse;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return DigitalErrorResponse.factoryDefault(
                        ErrorNetMessage.MESSAGE_ERROR_DEFAULT, code
                );
            }
        }

        private static DigitalErrorResponse factoryDefault(
                String messageErrorDefault, int errorCode
        ) {
            DigitalErrorResponse digitalErrorResponse = new DigitalErrorResponse();
            List<DigitalErrorResponse.Error> errorList = new ArrayList<>();
            DigitalErrorResponse.Error error = new Error();
            error.setId("0");
            error.setStatus("0");
            error.setTitle(messageErrorDefault);
            errorList.add(error);
            digitalErrorResponse.setErrors(errorList);
            digitalErrorResponse.setErrorCode(errorCode);
            return digitalErrorResponse;
        }

        public static class Error {

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

            public void setId(String id) {
                this.id = id;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
