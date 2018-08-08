package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class Error {

    private int code;
    private String title;
    private String detail;

    private static final String KEY_CODE = "code";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DETAIL = "detail";

    public Error() {
    }

    public Error(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_CODE)) {
            setCode(object.getInt(KEY_CODE));
        }
        if(!object.isNull(KEY_TITLE)){
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_DETAIL)){
            setDetail(object.getString(KEY_DETAIL));
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
