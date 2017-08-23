package com.tokopedia.digital.wallets;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositPassData implements Parcelable {

    private String title;
    private String method;
    private String url;
    private Params params;
    private String name;
    private String amountFormatted;
    private int amount;

    public String getTitle() {
        return title;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Params getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public int getAmount() {
        return amount;
    }

    private WalletToDepositPassData(Builder builder) {
        title = builder.title;
        method = builder.method;
        url = builder.url;
        params = builder.params;
        name = builder.name;
        amountFormatted = builder.amountFormatted;
        amount = builder.amount;
    }


    public static class Params implements Parcelable {
        private String refundId;
        private String refundType;

        public String getRefundId() {
            return refundId;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public String getRefundType() {
            return refundType;
        }

        public void setRefundType(String refundType) {
            this.refundType = refundType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.refundId);
            dest.writeString(this.refundType);
        }

        public Params() {
        }

        protected Params(Parcel in) {
            this.refundId = in.readString();
            this.refundType = in.readString();
        }

        public static final Creator<Params> CREATOR = new Creator<Params>() {
            @Override
            public Params createFromParcel(Parcel source) {
                return new Params(source);
            }

            @Override
            public Params[] newArray(int size) {
                return new Params[size];
            }
        };
    }

    public static final class Builder {
        private String title;
        private String method;
        private String url;
        private Params params;
        private String name;
        private String amountFormatted;
        private int amount;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder method(String val) {
            method = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder params(Params val) {
            params = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder amountFormatted(String val) {
            amountFormatted = val;
            return this;
        }

        public Builder amount(int val) {
            amount = val;
            return this;
        }

        public WalletToDepositPassData build() {
            return new WalletToDepositPassData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.method);
        dest.writeString(this.url);
        dest.writeParcelable(this.params, flags);
        dest.writeString(this.name);
        dest.writeString(this.amountFormatted);
        dest.writeInt(this.amount);
    }

    protected WalletToDepositPassData(Parcel in) {
        this.title = in.readString();
        this.method = in.readString();
        this.url = in.readString();
        this.params = in.readParcelable(Params.class.getClassLoader());
        this.name = in.readString();
        this.amountFormatted = in.readString();
        this.amount = in.readInt();
    }

    public static final Creator<WalletToDepositPassData> CREATOR = new Creator<WalletToDepositPassData>() {
        @Override
        public WalletToDepositPassData createFromParcel(Parcel source) {
            return new WalletToDepositPassData(source);
        }

        @Override
        public WalletToDepositPassData[] newArray(int size) {
            return new WalletToDepositPassData[size];
        }
    };

}
