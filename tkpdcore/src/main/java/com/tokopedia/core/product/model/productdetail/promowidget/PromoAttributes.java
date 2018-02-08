package com.tokopedia.core.product.model.productdetail.promowidget;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoAttributes implements Parcelable{

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

    private String shopType;

    public PromoAttributes() {
    }

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

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    protected PromoAttributes(Parcel in) {
        code = in.readString();
        codeHtml = in.readString();
        targetUrl = in.readString();
        shortDescHtml = in.readString();
        shortCondHtml = in.readString();
        targetType = in.readString();
        userId = in.readString();
        shopType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(codeHtml);
        dest.writeString(targetUrl);
        dest.writeString(shortDescHtml);
        dest.writeString(shortCondHtml);
        dest.writeString(targetType);
        dest.writeString(userId);
        dest.writeString(shopType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PromoAttributes> CREATOR = new Parcelable.Creator<PromoAttributes>() {
        @Override
        public PromoAttributes createFromParcel(Parcel in) {
            return new PromoAttributes(in);
        }

        @Override
        public PromoAttributes[] newArray(int size) {
            return new PromoAttributes[size];
        }
    };
}
