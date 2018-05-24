package com.tokopedia.tokocash.autosweepmf.view.model;

/**
 * Base model class for all sub model
 */
public class BaseModel {
    protected String message;
    protected String error;
    protected int code;
    protected String latency;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
        return "BaseModel{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", code=" + code +
                ", latency='" + latency + '\'' +
                '}';
    }
}
