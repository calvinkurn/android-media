package com.tokopedia.core.network.retrofit.response;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 01/12/2015.
 */
@Deprecated
/**
 @see com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
 */
public class TkpdResponse {

    private boolean isNullData;
    private boolean isError;
    private String status;
    private String strResponse;
    private String stringData = "";
    private JSONObject jsonData;
    private List<String> errorMessages = new ArrayList<>();
    private List<String> statusMessages = new ArrayList<>();

    private Gson gson = new GsonBuilder().disableHtmlEscaping()
            .setPrettyPrinting().create();
    private Object objData;

    public static TkpdResponse factory(String strResponse) {
        List<String> msgError = new ArrayList<>();
        List<String> msgStatus = new ArrayList<>();
        boolean isNullData = false;
        boolean isError = false;
        String status = "";
        JSONObject jsonResponse;
        JSONObject jsonData;
        JSONArray jsonDataArray = null;

        try {
            jsonResponse = new JSONObject(strResponse);
            status = jsonResponse.getString("status");
        } catch (Throwable e) {
            // Do nothing
            return null;
        }


        try {
            if (!jsonResponse.isNull("message_error")) {
                JSONArray jArray = jsonResponse.getJSONArray("message_error");
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        msgError.add(jArray.get(i).toString());
                    }
                    isError = true;
                }
            } else {
                msgError.add("");
                isError = false;
            }
        } catch (Throwable e) {
            // Do nothing
        }

        try {
            if (!jsonResponse.isNull("data")) {
                jsonData = jsonResponse.getJSONObject("data");
            } else {
                jsonData = null;
            }

            isNullData = jsonData == null && jsonDataArray == null;
        } catch (Throwable e) {
            // Do nothing
            jsonData = null;
        }


        try {
            if (!jsonResponse.isNull("data")) {
                jsonDataArray = jsonResponse.getJSONArray("data");
            } else {
                jsonDataArray = null;
            }

            isNullData = jsonDataArray == null && jsonData == null;
        } catch (Throwable e) {
            jsonDataArray = null;
        }


        if (jsonData == null && jsonDataArray == null) {
            isError = true;
            if (msgError.isEmpty()) msgError.add("Data Tidak Ditemukan");
        }


        try {
            if (!jsonResponse.isNull("message_status")) {
                JSONArray jArray = jsonResponse.getJSONArray("message_status");
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        msgStatus.add(jArray.get(i).toString());
                    }
                }
            } else {
                msgStatus.add("");
            }
        } catch (Throwable e) {
            // Do nothing
        }

        TkpdResponse tkpdResponse = new TkpdResponse();
        if (!isNullData & jsonData != null) tkpdResponse.setJsonData(jsonData);
        if (!isNullData & jsonDataArray != null) tkpdResponse.setJsonDataArray(jsonDataArray);
        tkpdResponse.setErrorMessages(msgError);
        tkpdResponse.setIsError(isError || isNullData);
        tkpdResponse.setStatus(status);
        tkpdResponse.setIsNullData(isNullData);
        tkpdResponse.setStrResponse(strResponse);
        tkpdResponse.setStatusMessages(msgStatus);
        return tkpdResponse;
    }

    public boolean isNullData() {
        return isNullData;
    }

    private void setIsNullData(boolean isNullData) {
        this.isNullData = isNullData;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public String getStrResponse() {
        return strResponse;
    }

    private void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    private void setJsonData(@NonNull JSONObject jsonData) {
        try {
            this.stringData = jsonData.toString();
            this.jsonData = jsonData;
        } catch (Throwable throwable) {

        }
    }

    private void setJsonDataArray(@NonNull JSONArray jsonDataArray) {
        this.stringData = jsonDataArray.toString();
    }

    public boolean isError() {
        return isError;
    }

    private void setIsError(boolean isError) {
        this.isError = isError;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getStatusMessages() {
        return statusMessages;
    }

    public void setStatusMessages(List<String> statusMessages) {
        this.statusMessages = statusMessages;
    }

    public String getStatusMessageJoined() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!statusMessages.isEmpty()) {
            for (int i = 0, statusMessagesSize = statusMessages.size(); i < statusMessagesSize; i++) {
                String string = statusMessages.get(i);
                stringBuilder.append(string);
                if (i != statusMessages.size() - 1
                        && !statusMessages.get(i).equals("")
                        && !statusMessages.get(i + 1).equals("")) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    public String getErrorMessageJoined() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!errorMessages.isEmpty()) {
            for (int i = 0, statusMessagesSize = errorMessages.size(); i < statusMessagesSize; i++) {
                String string = errorMessages.get(i);
                stringBuilder.append(string);
                if (i != errorMessages.size() - 1
                        && !errorMessages.get(i).equals("")
                        && !errorMessages.get(i + 1).equals("")) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    @SuppressWarnings("unchecked")
    public <T> T convertDataObj(Class<T> clazz) {
        if (objData == null) {
            try {
                this.objData = gson.fromJson(stringData, clazz);
                return (T) objData;
            } catch (ClassCastException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objData;
        }
    }
}
