package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositThanksData implements Parcelable {

    private WalletToDepositThanksData(Builder builder) {
        setIconResId(builder.iconResId);
        setTitle(builder.title);
        setSubTitle(builder.subTitle);
        setDescription(builder.description);
        setTitleButtonPositive(builder.titleButtonPositive);
        setTitleButtonNegative(builder.titleButtonNegative);
        setTypeResult(builder.typeResult);
    }

    public enum TypeResult {
        SUCCESS, FAILED
    }

    private int iconResId;
    private String title;
    private String subTitle;
    private String description;
    private String titleButtonPositive;
    private String titleButtonNegative;
    private TypeResult typeResult;

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitleButtonPositive() {
        return titleButtonPositive;
    }

    public void setTitleButtonPositive(String titleButtonPositive) {
        this.titleButtonPositive = titleButtonPositive;
    }

    public String getTitleButtonNegative() {
        return titleButtonNegative;
    }

    public void setTitleButtonNegative(String titleButtonNegative) {
        this.titleButtonNegative = titleButtonNegative;
    }

    public TypeResult getTypeResult() {
        return typeResult;
    }

    public void setTypeResult(TypeResult typeResult) {
        this.typeResult = typeResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WalletToDepositThanksData() {
    }


    public static final class Builder {
        private int iconResId;
        private String title;
        private String subTitle;
        private String description;
        private String titleButtonPositive;
        private String titleButtonNegative;
        private TypeResult typeResult;

        public Builder() {
        }

        public Builder iconResId(int val) {
            iconResId = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder subTitle(String val) {
            subTitle = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder titleButtonPositive(String val) {
            titleButtonPositive = val;
            return this;
        }

        public Builder titleButtonNegative(String val) {
            titleButtonNegative = val;
            return this;
        }

        public Builder typeResult(TypeResult val) {
            typeResult = val;
            return this;
        }

        public WalletToDepositThanksData build() {
            return new WalletToDepositThanksData(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconResId);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.description);
        dest.writeString(this.titleButtonPositive);
        dest.writeString(this.titleButtonNegative);
        dest.writeInt(this.typeResult == null ? -1 : this.typeResult.ordinal());
    }

    protected WalletToDepositThanksData(Parcel in) {
        this.iconResId = in.readInt();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.description = in.readString();
        this.titleButtonPositive = in.readString();
        this.titleButtonNegative = in.readString();
        int tmpTypeResult = in.readInt();
        this.typeResult = tmpTypeResult == -1 ? null : TypeResult.values()[tmpTypeResult];
    }

    public static final Creator<WalletToDepositThanksData> CREATOR = new Creator<WalletToDepositThanksData>() {
        @Override
        public WalletToDepositThanksData createFromParcel(Parcel source) {
            return new WalletToDepositThanksData(source);
        }

        @Override
        public WalletToDepositThanksData[] newArray(int size) {
            return new WalletToDepositThanksData[size];
        }
    };


}
