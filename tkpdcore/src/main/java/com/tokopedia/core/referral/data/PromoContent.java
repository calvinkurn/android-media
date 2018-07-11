package com.tokopedia.core.referral.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 08/11/17.
 */

public class PromoContent {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("content")
    @Expose
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
