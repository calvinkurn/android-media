package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lavekush on 20/03/18.
 */

public class ResponsePhoneData {

    @SerializedName("data")
    @Expose
    private PhoneDataEntity data;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("latency")
    @Expose
    private String latency;

    public PhoneDataEntity getData() {
        return data;
    }

    public void setData(PhoneDataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return "ResponsePhoneData{" +
                "data=" + data +
                ", code=" + code +
                ", latency=" + latency +
                '}';
    }
}
