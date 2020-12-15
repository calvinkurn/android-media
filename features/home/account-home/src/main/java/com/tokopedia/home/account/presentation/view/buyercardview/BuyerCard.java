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
    private String shopName;
    private String tokopointTitle;
    private String tokopointAmount;
    private String tokopointImageUrl;
    private String tokopointAppplink;
    private String couponTitle;
    private String couponAmount;
    private String couponImageUrl;
    private String couponApplink;
    private String tokomemberTitle;
    private String tokoMemberAmount;
    private String tokomemberImageUrl;
    private String tokomemberApplink;
    private String eggImageUrl;
    private int tokopointSize;
    private int tokomemberSize;
    private int couponSize;
    private String memberStatus;
    private boolean isAffiliate;
    private boolean isHasShop;
    private String roleName;

    public BuyerCard() {

    }

    BuyerCard(String avatar,
              String username,
              String shopName,
              int progress,
              String tokopointTitle,
              String tokopointAmount,
              String tokopointImageUrl,
              String tokopointAppplink,
              String couponTitle,
              String couponAmount,
              String couponImageUrl,
              String couponApplink,
              boolean isAffiliate,
              boolean isHasShop,
              String roleName,
              String tokomemberTitle,
              String tokoMemberAmount,
              String tokomemberImageUrl,
              String tokomemberApplink,
              int tokopointSize,
              int tokomemberSize,
              int couponSize,
              String memberStatus,
              String eggImageUrl) {
        this.avatar = avatar;
        this.progress = progress;
        this.username = username;
        this.shopName = shopName;
        this.tokopointTitle = tokopointTitle;
        this.tokopointAmount = tokopointAmount;
        this.tokopointImageUrl = tokopointImageUrl;
        this.tokopointAppplink = tokopointAppplink;
        this.couponTitle = couponTitle;
        this.couponAmount = couponAmount;
        this.couponImageUrl = couponImageUrl;
        this.couponApplink = couponApplink;
        this.isAffiliate = isAffiliate;
        this.isHasShop = isHasShop;
        this.roleName = roleName;
        this.tokomemberTitle = tokomemberTitle;
        this.tokoMemberAmount = tokoMemberAmount;
        this.tokomemberImageUrl = tokomemberImageUrl;
        this.tokomemberApplink = tokomemberApplink;
        this.tokopointSize = tokopointSize;
        this.tokomemberSize = tokomemberSize;
        this.couponSize = couponSize;
        this.memberStatus = memberStatus;
        this.eggImageUrl = eggImageUrl;
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

    public String getShopName() {
        return shopName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokopointAmount() {
        return tokopointAmount;
    }

    public boolean isHasShop() {
        return isHasShop;
    }

    public String getRoleName() {
        return roleName;
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

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getEggImageUrl() {
        return eggImageUrl;
    }

    public void setEggImageUrl(String eggImageUrl) {
        this.eggImageUrl = eggImageUrl;
    }

    public String getTokopointTitle() {
        return tokopointTitle;
    }

    public void setTokopointTitle(String tokopointTitle) {
        this.tokopointTitle = tokopointTitle;
    }

    public String getTokopointImageUrl() {
        return tokopointImageUrl;
    }

    public void setTokopointImageUrl(String tokopointImageUrl) {
        this.tokopointImageUrl = tokopointImageUrl;
    }

    public String getTokopointAppplink() {
        return tokopointAppplink;
    }

    public void setTokopointAppplink(String tokopointAppplink) {
        this.tokopointAppplink = tokopointAppplink;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponImageUrl() {
        return couponImageUrl;
    }

    public void setCouponImageUrl(String couponImageUrl) {
        this.couponImageUrl = couponImageUrl;
    }

    public String getCouponApplink() {
        return couponApplink;
    }

    public void setCouponApplink(String couponApplink) {
        this.couponApplink = couponApplink;
    }

    public String getTokomemberTitle() {
        return tokomemberTitle;
    }

    public void setTokomemberTitle(String tokomemberTitle) {
        this.tokomemberTitle = tokomemberTitle;
    }

    public String getTokomemberImageUrl() {
        return tokomemberImageUrl;
    }

    public void setTokomemberImageUrl(String tokomemberImageUrl) {
        this.tokomemberImageUrl = tokomemberImageUrl;
    }

    public String getTokomemberApplink() {
        return tokomemberApplink;
    }

    public void setTokomemberApplink(String tokomemberApplink) {
        this.tokomemberApplink = tokomemberApplink;
    }

    public int getTokopointSize() {
        return tokopointSize;
    }

    public void setTokopointSize(int tokopointSize) {
        this.tokopointSize = tokopointSize;
    }

    public int getTokomemberSize() {
        return tokomemberSize;
    }

    public void setTokomemberSize(int tokomemberSize) {
        this.tokomemberSize = tokomemberSize;
    }

    public int getCouponSize() {
        return couponSize;
    }

    public void setCouponSize(int couponSize) {
        this.couponSize = couponSize;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public static class Builder {
        private String avatar;
        private String username;
        private String shopName;
        private int progress;
        private String tokopointTitle;
        private String tokopointAmount;
        private String tokopointImageUrl;
        private String tokopointAppplink;
        private String couponTitle;
        private String couponAmount;
        private String couponImageUrl;
        private String couponApplink;
        private String tokomemberTitle;
        private String tokoMemberAmount;
        private String tokomemberImageUrl;
        private String tokomemberApplink;
        private String eggImageUrl;
        private int tokopointSize;
        private int tokomemberSize;
        private int couponSize;
        private String memberStatus;
        private boolean isAffiliate;
        private boolean isHasShop;
        private String roleName;

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder shopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder tokopointTitle(String title) {
            this.tokopointTitle = title;
            return this;
        }

        public Builder tokopoint(String amount) {
            this.tokopointAmount = amount;
            return this;
        }

        public Builder tokopointImageUrl(String imageUrl) {
            this.tokopointImageUrl = imageUrl;
            return this;
        }

        public Builder tokopointApplink(String applink) {
            this.tokopointAppplink = applink;
            return this;
        }

        public Builder couponsTitle(String title) {
            this.couponTitle = title;
            return this;
        }

        public Builder coupons(String coupon) {
            this.couponAmount = coupon;
            return this;
        }

        public Builder couponImageUrl(String imageUrl) {
            this.couponImageUrl = imageUrl;
            return this;
        }

        public Builder couponApplink(String applink) {
            this.couponApplink = applink;
            return this;
        }

        public Builder isAffliate(boolean isAffiliate) {
            this.isAffiliate = isAffiliate;
            return this;
        }

        public Builder isHasShop(boolean isHasShop) {
            this.isHasShop = isHasShop;
            return this;
        }

        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder tokomemberTitle(String title) {
            this.tokomemberTitle = title;
            return this;
        }

        public Builder tokomemberImageUrl(String imageUrl) {
            this.tokomemberImageUrl = imageUrl;
            return this;
        }

        public Builder tokomember(String tokoMemberAmount) {
            this.tokoMemberAmount = tokoMemberAmount;
            return this;
        }

        public Builder tokomemberApplink(String applink) {
            this.tokomemberApplink = applink;
            return this;
        }

        public Builder eggImageUrl(String eggImageUrl) {
            this.eggImageUrl = eggImageUrl;
            return this;
        }

        public Builder tokopointSize(int size) {
            this.tokopointSize = size;
            return this;
        }

        public Builder tokomemberSize(int size) {
            this.tokomemberSize = size;
            return this;
        }

        public Builder couponSize(int size) {
            this.couponSize = size;
            return this;
        }

        public Builder memberStatus(String memberStatus) {
            this.memberStatus = memberStatus;
            return this;
        }

        public BuyerCard build() {
            return new BuyerCard(avatar, username, shopName, progress, tokopointTitle, tokopointAmount, tokopointImageUrl,
                    tokopointAppplink, couponTitle, couponAmount, couponImageUrl, couponApplink, isAffiliate, isHasShop,
                    roleName, tokomemberTitle, tokoMemberAmount, tokomemberImageUrl, tokomemberApplink,
                    tokopointSize, tokomemberSize, couponSize, memberStatus,eggImageUrl);
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
        dest.writeString(this.shopName);
        dest.writeString(this.tokopointTitle);
        dest.writeString(this.tokopointAmount);
        dest.writeString(this.tokopointImageUrl);
        dest.writeString(this.tokopointAppplink);
        dest.writeString(this.couponTitle);
        dest.writeString(this.couponAmount);
        dest.writeString(this.couponImageUrl);
        dest.writeString(this.tokopointAppplink);
        dest.writeString(this.tokomemberTitle);
        dest.writeString(this.tokoMemberAmount);
        dest.writeString(this.tokomemberImageUrl);
        dest.writeString(this.tokomemberApplink);
        dest.writeString(this.eggImageUrl);
        dest.writeString(this.roleName);
        dest.writeInt(this.tokopointSize);
        dest.writeInt(this.tokomemberSize);
        dest.writeInt(this.couponSize);
        dest.writeString(this.memberStatus);
        dest.writeByte(this.isAffiliate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHasShop ? (byte) 1 : (byte) 0);
    }

    protected BuyerCard(Parcel in) {
        this.avatar = in.readString();
        this.progress = in.readInt();
        this.username = in.readString();
        this.shopName = in.readString();
        this.tokopointTitle = in.readString();
        this.tokopointAmount = in.readString();
        this.tokopointImageUrl = in.readString();
        this.tokopointAppplink = in.readString();
        this.couponTitle = in.readString();
        this.couponAmount = in.readString();
        this.couponImageUrl = in.readString();
        this.couponApplink = in.readString();
        this.tokomemberTitle = in.readString();
        this.tokoMemberAmount = in.readString();
        this.tokomemberImageUrl = in.readString();
        this.tokomemberApplink = in.readString();
        this.eggImageUrl = in.readString();
        this.roleName = in.readString();
        this.tokopointSize = in.readInt();
        this.tokomemberSize = in.readInt();
        this.couponSize = in.readInt();
        this.memberStatus = in.readString();
        this.isAffiliate = in.readByte() != 0;
        this.isHasShop = in.readByte() != 0;
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

    public String getTokoMemberAmount() {
        return tokoMemberAmount;
    }

    public void setTokoMemberAmount(String tokoMemberAmount) {
        this.tokoMemberAmount = tokoMemberAmount;
    }
}
