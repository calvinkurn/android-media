
package com.tokopedia.core.drawer.model.topcastItem;

import com.google.gson.annotations.SerializedName;

public class TopCashItem {

    @SerializedName("code")
    private String mCode;
    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
