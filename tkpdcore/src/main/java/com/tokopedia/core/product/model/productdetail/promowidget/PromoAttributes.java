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
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("short_cond_html")
    @Expose
    private String shortCondHtml;
    @SerializedName("applink")
    @Expose
    private String applinks;

    //below two attributes is used only for caching
    @SerializedName("target_type")
    @Expose
    private String targetType;
    @SerializedName("user_id")
    @Expose
    private String userId;

    private String shopType;
    @SerializedName("custom_promo_id")
    @Expose
    private String customPromoId;

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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setCustomPromoId(String customPromoId) {
        this.customPromoId = customPromoId;
    }

    public String getCustomPromoId() {
        return customPromoId;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.codeHtml);
        dest.writeString(this.targetUrl);
        dest.writeString(this.shortDescHtml);
        dest.writeString(this.shortDesc);
        dest.writeString(this.shortCondHtml);
        dest.writeString(this.targetType);
        dest.writeString(this.userId);
        dest.writeString(this.shopType);
        dest.writeString(this.customPromoId);
        dest.writeString(this.applinks);
    }

    protected PromoAttributes(Parcel in) {
        this.code = in.readString();
        this.codeHtml = in.readString();
        this.targetUrl = in.readString();
        this.shortDescHtml = in.readString();
        this.shortDesc = in.readString();
        this.shortCondHtml = in.readString();
        this.targetType = in.readString();
        this.userId = in.readString();
        this.shopType = in.readString();
        this.customPromoId = in.readString();
        this.applinks = in.readString();
    }

    public static final Creator<PromoAttributes> CREATOR = new Creator<PromoAttributes>() {
        @Override
        public PromoAttributes createFromParcel(Parcel source) {
            return new PromoAttributes(source);
        }

        @Override
        public PromoAttributes[] newArray(int size) {
            return new PromoAttributes[size];
        }
    };
}
