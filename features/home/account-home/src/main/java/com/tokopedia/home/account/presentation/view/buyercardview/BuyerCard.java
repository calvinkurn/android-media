package com.tokopedia.home.account.presentation.view.buyercardview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author okasurya on 8/29/18.
 */
public class BuyerCard implements Parcelable {
    private String avatar;
    private int progress;
    private String username;
    private String tokopointAmount;
    private int couponAmount;
    private boolean isAffiliate;

    public BuyerCard() {

    }

    BuyerCard(String avatar, String username, int progress, String tokopointAmount, int couponAmount, boolean isAffiliate) {
        this.avatar = avatar;
        this.progress = progress;
        this.username = username;
        this.tokopointAmount = tokopointAmount;
        this.couponAmount = couponAmount;
        this.isAffiliate = isAffiliate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokopointAmount() {
        return tokopointAmount;
    }

    public boolean isAffiliate() {
        return isAffiliate;
    }

    public void setAffiliate(boolean affiliate) {
        isAffiliate = affiliate;
    }

    public void setTokopointAmount(String tokopointAmount) {
        this.tokopointAmount = tokopointAmount;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }


    public static class Builder {
        private String avatar;
        private String username;
        private int progress;
        private String tokopoint;
        private int coupon;
        private boolean isAffiliate;

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder tokopoint(String tokopoint) {
            this.tokopoint = tokopoint;
            return this;
        }

        public Builder coupons(int coupon) {
            this.coupon = coupon;
            return this;
        }

        public Builder isAffliate(boolean isAffiliate) {
            this.isAffiliate = isAffiliate;
            return this;
        }

        public BuyerCard build() {
            return new BuyerCard(avatar, username, progress, tokopoint, coupon, isAffiliate);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeInt(this.progress);
        dest.writeString(this.username);
        dest.writeString(this.tokopointAmount);
        dest.writeInt(this.couponAmount);
        dest.writeByte(this.isAffiliate ? (byte) 1 : (byte) 0);
    }

    protected BuyerCard(Parcel in) {
        this.avatar = in.readString();
        this.progress = in.readInt();
        this.username = in.readString();
        this.tokopointAmount = in.readString();
        this.couponAmount = in.readInt();
        this.isAffiliate = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BuyerCard> CREATOR = new Parcelable.Creator<BuyerCard>() {
        @Override
        public BuyerCard createFromParcel(Parcel source) {
            return new BuyerCard(source);
        }

        @Override
        public BuyerCard[] newArray(int size) {
            return new BuyerCard[size];
        }
    };
}
