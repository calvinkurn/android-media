package com.tokopedia.core.router.digitalmodule.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class DigitalCheckoutPassData implements Parcelable {
    private String action;
    private String categoryId;
    private String clientNumber;
    private String productId;
    private String operatorId;
    private String isPromo;
    private String instantCheckout;
    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmContent;

    private DigitalCheckoutPassData(Builder builder) {
        setAction(builder.action);
        setCategoryId(builder.categoryId);
        setClientNumber(builder.clientNumber);
        setProductId(builder.productId);
        setOperatorId(builder.operatorId);
        setIsPromo(builder.isPromo);
        setInstantCheckout(builder.instantCheckout);
        setUtmSource(builder.utmSource);
        setUtmMedium(builder.utmMedium);
        setUtmCampaign(builder.utmCampaign);
        setUtmContent(builder.utmContent);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(String isPromo) {
        this.isPromo = isPromo;
    }

    public String getInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(String instantCheckout) {
        this.instantCheckout = instantCheckout;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.categoryId);
        dest.writeString(this.clientNumber);
        dest.writeString(this.productId);
        dest.writeString(this.operatorId);
        dest.writeString(this.isPromo);
        dest.writeString(this.instantCheckout);
        dest.writeString(this.utmSource);
        dest.writeString(this.utmMedium);
        dest.writeString(this.utmCampaign);
        dest.writeString(this.utmContent);
    }

    public DigitalCheckoutPassData() {
    }

    protected DigitalCheckoutPassData(Parcel in) {
        this.action = in.readString();
        this.categoryId = in.readString();
        this.clientNumber = in.readString();
        this.productId = in.readString();
        this.operatorId = in.readString();
        this.isPromo = in.readString();
        this.instantCheckout = in.readString();
        this.utmSource = in.readString();
        this.utmMedium = in.readString();
        this.utmCampaign = in.readString();
        this.utmContent = in.readString();
    }

    public static final Creator<DigitalCheckoutPassData> CREATOR =
            new Creator<DigitalCheckoutPassData>() {
                @Override
                public DigitalCheckoutPassData createFromParcel(Parcel source) {
                    return new DigitalCheckoutPassData(source);
                }

                @Override
                public DigitalCheckoutPassData[] newArray(int size) {
                    return new DigitalCheckoutPassData[size];
                }
            };

    public static final class Builder {
        private String action;
        private String categoryId;
        private String clientNumber;
        private String productId;
        private String operatorId;
        private String isPromo;
        private String instantCheckout;
        private String utmSource;
        private String utmMedium;
        private String utmCampaign;
        private String utmContent;

        public Builder() {
        }

        public Builder action(String val) {
            action = val;
            return this;
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder clientNumber(String val) {
            clientNumber = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder operatorId(String val) {
            operatorId = val;
            return this;
        }

        public Builder isPromo(String val) {
            isPromo = val;
            return this;
        }

        public Builder instantCheckout(String val) {
            instantCheckout = val;
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

        public DigitalCheckoutPassData build() {
            return new DigitalCheckoutPassData(this);
        }
    }
}
