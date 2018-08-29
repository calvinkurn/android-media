package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmModel {

    private static final String KEY_HEADER = "header";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_ERROR = "errors";

    private Error error;
    private Status status;
    private Header header;
    private List<CpmData> data = new ArrayList<>();

    public CpmModel() {
    }

    public CpmModel(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ERROR)){
            JSONObject error = object.getJSONArray(KEY_ERROR).getJSONObject(0);
            setError(new Error(error));
        }
        if(!object.isNull(KEY_HEADER)) {
            setHeader(new Header(object.getJSONObject(KEY_HEADER)));
        }
        if(!object.isNull(KEY_STATUS)) {
            setStatus(new Status(object.getJSONObject(KEY_STATUS)));
        }
        if(!object.isNull(KEY_DATA)) {
            JSONArray dataArray = object.getJSONArray(KEY_DATA);
            for (int i = 0; i < dataArray.length(); i++) {
                data.add(new CpmData(dataArray.getJSONObject(i)));
            }
        }
    }

    public List<CpmData> getData() {
        return data;
    }

    public void setData(List<CpmData> data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
