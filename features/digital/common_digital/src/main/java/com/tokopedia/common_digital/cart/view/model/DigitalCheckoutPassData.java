package com.tokopedia.common_digital.cart.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 2/24/17.
 */

public class DigitalCheckoutPassData implements Parcelable {
    public static final int PARAM_WIDGET = 1;
    public static final int PARAM_NATIVE = 2;
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_CATEGORY_ID = "category_id";
    public static final String PARAM_CLIENT_NUMBER = "client_number";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_OPERATOR_ID = "operator_id";
    public static final String PARAM_IS_PROMO = "is_promo";
    public static final String PARAM_INSTANT_CHECKOUT = "instant_checkout";
    public static final String PARAM_UTM_SOURCE = "utm_source";
    public static final String PARAM_UTM_MEDIUM = "utm_medium";
    public static final String PARAM_UTM_CAMPAIGN = "utm_campaign";
    public static final String PARAM_UTM_CONTENT = "utm_content";
    public static final String PARAM_IDEM_POTENCY_KEY = "idem_potency_key";
    public static final String DEFAULT_ACTION = "init_data";
    public static final String UTM_SOURCE_ANDROID = "android";
    public static final String UTM_MEDIUM_WIDGET = "widget";


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
    private String idemPotencyKey;
    private String voucherCodeCopied;
    private int source;

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
        setIdemPotencyKey(builder.idemPotencyKey);
        setVoucherCodeCopied(builder.voucherCodeCopied);
        setSource(builder.source);
    }

    protected DigitalCheckoutPassData(Parcel in) {
        action = in.readString();
        categoryId = in.readString();
        clientNumber = in.readString();
        productId = in.readString();
        operatorId = in.readString();
        isPromo = in.readString();
        instantCheckout = in.readString();
        utmSource = in.readString();
        utmMedium = in.readString();
        utmCampaign = in.readString();
        utmContent = in.readString();
        idemPotencyKey = in.readString();
        voucherCodeCopied = in.readString();
        source = in.readInt();
    }

    public static final Creator<DigitalCheckoutPassData> CREATOR = new Creator<DigitalCheckoutPassData>() {
        @Override
        public DigitalCheckoutPassData createFromParcel(Parcel in) {
            return new DigitalCheckoutPassData(in);
        }

        @Override
        public DigitalCheckoutPassData[] newArray(int size) {
            return new DigitalCheckoutPassData[size];
        }
    };

    public void setSource(int source) {
        this.source = source;
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

    public String getIdemPotencyKey() {
        return idemPotencyKey;
    }

    public void setIdemPotencyKey(String idemPotencyKey) {
        this.idemPotencyKey = idemPotencyKey;
    }

    public String getVoucherCodeCopied() {
        return voucherCodeCopied;
    }

    public void setVoucherCodeCopied(String voucherCodeCopied) {
        this.voucherCodeCopied = voucherCodeCopied;
    }

    public int getSource() {
        return source;
    }

    public DigitalCheckoutPassData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(action);
        parcel.writeString(categoryId);
        parcel.writeString(clientNumber);
        parcel.writeString(productId);
        parcel.writeString(operatorId);
        parcel.writeString(isPromo);
        parcel.writeString(instantCheckout);
        parcel.writeString(utmSource);
        parcel.writeString(utmMedium);
        parcel.writeString(utmCampaign);
        parcel.writeString(utmContent);
        parcel.writeString(idemPotencyKey);
        parcel.writeString(voucherCodeCopied);
        parcel.writeInt(source);
    }

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
        private String idemPotencyKey;
        private String voucherCodeCopied;
        private int source;

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

        public Builder idemPotencyKey(String val) {
            idemPotencyKey = val;
            return this;
        }

        public Builder voucherCodeCopied(String val) {
            voucherCodeCopied = val;
            return this;
        }

        public Builder source(int val) {
            source = val;
            return this;
        }

        public DigitalCheckoutPassData build() {
            return new DigitalCheckoutPassData(this);
        }
    }

}
