
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class GamiPlayWithPoints {

    @Expose
    private String code;
    @Expose
    private List<String> message;
    @Expose
    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
