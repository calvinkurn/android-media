package com.tokopedia.gamification.cracktoken.presentation.model;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/29/18.
 */
public class CrackResultStatus {

    private String code;
    private List<String> message;
    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
