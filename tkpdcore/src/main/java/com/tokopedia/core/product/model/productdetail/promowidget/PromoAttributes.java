package com.tokopedia.core.product.model.productdetail.promowidget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoAttributes {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("code_html")
    @Expose
    private String codeHtml;
    @SerializedName("target_url")
    @Expose
    private String targetUrl;
    @SerializedName("short_desc_html")
    @Expose
    private String shortDescHtml;
    @SerializedName("short_cond_html")
    @Expose
    private String shortCondHtml;

    //below two attributes is used only for caching
    @SerializedName("target_type")
    @Expose
    private String targetType;
    @SerializedName("user_id")
    @Expose
    private String userId;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeHtml() {
        return codeHtml;
    }

    public void setCodeHtml(String codeHtml) {
        this.codeHtml = codeHtml;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getShortCondHtml() {
        return shortCondHtml;
    }

    public String getShortDescHtml() {
        return shortDescHtml;
    }

    public void setShortDescHtml(String shortDescHtml) {
        this.shortDescHtml = shortDescHtml;
    }

    public void setShortCondHtml(String shortCondHtml) {
        this.shortCondHtml = shortCondHtml;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
