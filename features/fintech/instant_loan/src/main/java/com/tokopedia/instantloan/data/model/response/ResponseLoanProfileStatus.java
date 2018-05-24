package com.tokopedia.instantloan.data.model.response;

/**
 * Created by lavekush on 20/03/18.
 */

public class ResponseLoanProfileStatus {
    private LoanProfileStatusEntity data;
    private int code;
    private String latency;

    public LoanProfileStatusEntity getData() {
        return data;
    }

    public void setData(LoanProfileStatusEntity data) {
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
        return "ResponseLoanProfileStatus{" +
                "data=" + data +
                ", code=" + code +
                ", latency=" + latency +
                '}';
    }
}
