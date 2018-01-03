package com.tokopedia.core.router.digitalmodule.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class DigitalCategoryDetailPassData implements Parcelable {
    public static final String PARAM_CATEGORY_ID = "category_id";
    public static final String PARAM_OPERATOR_ID = "operator_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_CLIENT_NUMBER = "client_number";
    public static final String PARAM_UTM_SOURCE = "utm_source";
    public static final String PARAM_UTM_MEDIUM = "utm_medium";
    public static final String PARAM_UTM_CAMPAIGN = "utm_campaign";
    public static final String PARAM_UTM_CONTENT = "utm_content";

    private String categoryId;
    private String operatorId;
    private String productId;
    private String clientNumber;
    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmContent;
    private String url;
    private String appLinks;
    private String categoryName;

    private DigitalCategoryDetailPassData(Builder builder) {
        setCategoryId(builder.categoryId);
        setOperatorId(builder.operatorId);
        setProductId(builder.productId);
        setClientNumber(builder.clientNumber);
        setUtmSource(builder.utmSource);
        setUtmMedium(builder.utmMedium);
        setUtmCampaign(builder.utmCampaign);
        setUtmContent(builder.utmContent);
        setUrl(builder.url);
        setAppLinks(builder.appLinks);
        setCategoryName(builder.categoryName);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public String getUtmContent() {
        return utmContent;
    }

    public void setUtmContent(String utmContent) {
        this.utmContent = utmContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.operatorId);
        dest.writeString(this.productId);
        dest.writeString(this.clientNumber);
        dest.writeString(this.utmSource);
        dest.writeString(this.utmMedium);
        dest.writeString(this.utmCampaign);
        dest.writeString(this.utmContent);
        dest.writeString(this.url);
        dest.writeString(this.appLinks);
        dest.writeString(this.categoryName);
    }

    public DigitalCategoryDetailPassData() {
    }

    protected DigitalCategoryDetailPassData(Parcel in) {
        this.categoryId = in.readString();
        this.operatorId = in.readString();
        this.productId = in.readString();
        this.clientNumber = in.readString();
        this.utmSource = in.readString();
        this.utmMedium = in.readString();
        this.utmCampaign = in.readString();
        this.utmContent = in.readString();
        this.url = in.readString();
        this.appLinks = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<DigitalCategoryDetailPassData> CREATOR =
            new Creator<DigitalCategoryDetailPassData>() {
                @Override
                public DigitalCategoryDetailPassData createFromParcel(Parcel source) {
                    return new DigitalCategoryDetailPassData(source);
                }

                @Override
                public DigitalCategoryDetailPassData[] newArray(int size) {
                    return new DigitalCategoryDetailPassData[size];
                }
            };


    public static final class Builder {
        private String categoryId;
        private String operatorId;
        private String productId;
        private String clientNumber;
        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmContent;
        private String url;
        private String appLinks;
        private String categoryName;

        public Builder() {
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder operatorId(String val) {
            operatorId = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder clientNumber(String val) {
            clientNumber = val;
            return this;
        }

        public Builder utmSource(String val) {
            utmSource = val;
            return this;
        }

        public Builder utmMedium(String val) {
            utmMedium = val;
            return this;
        }

        public Builder utmCampaign(String val) {
            utmCampaign = val;
            return this;
        }

        public Builder utmContent(String val) {
            utmContent = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder appLinks(String val) {
            appLinks = val;
            return this;
        }

        public Builder categoryName(String val) {
            categoryName = val;
            return this;
        }

        public DigitalCategoryDetailPassData build() {
            return new DigitalCategoryDetailPassData(this);
        }
    }
}
