package com.tokopedia.common_digital.cart.view.model.checkout;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutDataParameter implements Parcelable {

    private String voucherCode;
    private String cartId;
    private long transactionAmount;
    private String ipAddress;
    private String userAgent;
    private String accessToken;
    private String walletRefreshToken;
    private String relationType;
    private String relationId;
    private boolean needOtp;

    private CheckoutDataParameter(Builder builder) {
        setVoucherCode(builder.voucherCode);
        setCartId(builder.cartId);
        setTransactionAmount(builder.transactionAmount);
        setIpAddress(builder.ipAddress);
        setUserAgent(builder.userAgent);
        setAccessToken(builder.accessToken);
        setWalletRefreshToken(builder.walletRefreshToken);
        setRelationType(builder.relationType);
        setRelationId(builder.relationId);
        setNeedOtp(builder.needOtp);
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getCartId() {
        return cartId;
    }

    private void setCartId(String cartId) {
        this.cartId = cartId;
    }

    private void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public long getTransactionAmount() {
        return transactionAmount;
    }

    private void setTransactionAmount(long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public boolean isNeedOtp() {
        return needOtp;
    }

    private void setNeedOtp(boolean needOtp) {
        this.needOtp = needOtp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccessToken() {
        return accessToken;
    }

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getWalletRefreshToken() {
        return walletRefreshToken;
    }

    private void setWalletRefreshToken(String walletRefreshToken) {
        this.walletRefreshToken = walletRefreshToken;
    }

    public String getRelationType() {
        return relationType;
    }

    private void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationId() {
        return relationId;
    }

    private void setRelationId(String relationId) {
        this.relationId = relationId;
    }


    public static final class Builder {
        private String voucherCode;
        private String cartId;
        private long transactionAmount;
        private String ipAddress;
        private String userAgent;
        private String accessToken;
        private String walletRefreshToken;
        private String relationType;
        private String relationId;
        private boolean needOtp;

        public Builder() {
        }

        public Builder(CheckoutDataParameter checkoutData) {
            this.voucherCode = checkoutData.voucherCode;
            this.transactionAmount = checkoutData.transactionAmount;
            this.ipAddress = checkoutData.ipAddress;
            this.userAgent = checkoutData.userAgent;
            this.accessToken = checkoutData.accessToken;
            this.walletRefreshToken = checkoutData.walletRefreshToken;
            this.relationType = checkoutData.relationType;
            this.relationId = checkoutData.relationId;
            this.needOtp = checkoutData.isNeedOtp();
        }

        public Builder voucherCode(String val) {
            voucherCode = val;
            return this;
        }

        public Builder cartId(String val) {
            cartId = val;
            return this;
        }

        public Builder transactionAmount(long val) {
            transactionAmount = val;
            return this;
        }

        public Builder ipAddress(String val) {
            ipAddress = val;
            return this;
        }

        public Builder userAgent(String val) {
            userAgent = val;
            return this;
        }

        public Builder accessToken(String val) {
            accessToken = val;
            return this;
        }

        public Builder walletRefreshToken(String val) {
            walletRefreshToken = val;
            return this;
        }

        public Builder relationType(String val) {
            relationType = val;
            return this;
        }

        public Builder relationId(String val) {
            relationId = val;
            return this;
        }

        public Builder needOtp(boolean val) {
            needOtp = val;
            return this;
        }

        public CheckoutDataParameter build() {
            return new CheckoutDataParameter(this);
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.voucherCode);
        dest.writeString(this.cartId);
        dest.writeLong(this.transactionAmount);
        dest.writeString(this.ipAddress);
        dest.writeString(this.userAgent);
        dest.writeString(this.accessToken);
        dest.writeString(this.walletRefreshToken);
        dest.writeString(this.relationType);
        dest.writeString(this.relationId);
        dest.writeByte(this.needOtp ? (byte) 1 : (byte) 0);
    }

    protected CheckoutDataParameter(Parcel in) {
        this.voucherCode = in.readString();
        this.cartId = in.readString();
        this.transactionAmount = in.readLong();
        this.ipAddress = in.readString();
        this.userAgent = in.readString();
        this.accessToken = in.readString();
        this.walletRefreshToken = in.readString();
        this.relationType = in.readString();
        this.relationId = in.readString();
        this.needOtp = in.readByte() != 0;
    }

    public static final Creator<CheckoutDataParameter> CREATOR =
            new Creator<CheckoutDataParameter>() {
                @Override
                public CheckoutDataParameter createFromParcel(Parcel source) {
                    return new CheckoutDataParameter(source);
                }

                @Override
                public CheckoutDataParameter[] newArray(int size) {
                    return new CheckoutDataParameter[size];
                }
            };

}
