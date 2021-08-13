package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class BuyerCardViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String userId;
    private String name;
    private String shopName;
    private String imageUrl;
    private String tokopointTitle;
    private String tokopoint;
    private String tokopointImageUrl;
    private String tokopointAppplink;
    private String couponTitle;
    private String coupons;
    private String couponImageUrl;
    private String couponApplink;
    private String tokomemberTitle;
    private String tokomember;
    private String tokomemberImageUrl;
    private String tokomemberApplink;
    private String eggImageUrl;
    private int tokopointSize;
    private int tokomemberSize;
    private int couponSize;
    private String memberStatus;
    private int progress;
    private boolean isAffiliate;
    private boolean hasShop;
    private String roleName;

    public BuyerCardViewModel() {
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTokopoint() {
        return tokopoint;
    }

    public void setTokopoint(String tokopoint) {
        this.tokopoint = tokopoint;
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

    public String getTokopointTitle() {
        return tokopointTitle;
    }

    public void setTokopointTitle(String tokopointTitle) {
        this.tokopointTitle = tokopointTitle;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getTokomemberTitle() {
        return tokomemberTitle;
    }

    public void setTokomemberTitle(String tokomemberTitle) {
        this.tokomemberTitle = tokomemberTitle;
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

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isHasShop() {
        return hasShop;
    }

    public void setHasShop(boolean hasShop) {
        this.hasShop = hasShop;
    }

    public String getRoleName() { return roleName; }

    public void setRoleName(String roleName) { this.roleName = roleName; }

    public boolean isAffiliate() {
        return isAffiliate;
    }

    public void setAffiliate(boolean affiliate) {
        isAffiliate = affiliate;
    }

    public String getEggImageUrl() {
        return eggImageUrl;
    }

    public void setEggImageUrl(String eggImageUrl) {
        this.eggImageUrl = eggImageUrl;
    }

    public String getTokomember() {
        return tokomember;
    }

    public void setTokomember(String tokomember) {
        this.tokomember = tokomember;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.shopName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.tokopointTitle);
        dest.writeString(this.tokopoint);
        dest.writeString(this.tokopointImageUrl);
        dest.writeString(this.tokopointAppplink);
        dest.writeString(this.couponTitle);
        dest.writeString(this.coupons);
        dest.writeString(this.couponImageUrl);
        dest.writeString(this.couponApplink);
        dest.writeString(this.tokomemberTitle);
        dest.writeString(this.tokomember);
        dest.writeString(this.tokomemberImageUrl);
        dest.writeString(this.tokomemberApplink);
        dest.writeString(this.eggImageUrl);
        dest.writeString(this.roleName);
        dest.writeInt(this.tokopointSize);
        dest.writeInt(this.tokomemberSize);
        dest.writeInt(this.couponSize);
        dest.writeString(this.memberStatus);
        dest.writeInt(this.progress);
        dest.writeByte(this.isAffiliate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasShop ? (byte) 1 : (byte) 0);
    }

    protected BuyerCardViewModel(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.shopName = in.readString();
        this.imageUrl = in.readString();
        this.tokopointTitle = in.readString();
        this.tokopoint = in.readString();
        this.tokopointImageUrl = in.readString();
        this.tokopointAppplink = in.readString();
        this.couponTitle = in.readString();
        this.coupons = in.readString();
        this.couponImageUrl = in.readString();
        this.couponApplink = in.readString();
        this.tokomemberTitle = in.readString();
        this.tokomember = in.readString();
        this.tokomemberImageUrl = in.readString();
        this.tokomemberApplink = in.readString();
        this.eggImageUrl = in.readString();
        this.roleName = in.readString();
        this.tokopointSize = in.readInt();
        this.tokomemberSize = in.readInt();
        this.couponSize = in.readInt();
        this.memberStatus = in.readString();
        this.progress = in.readInt();
        this.isAffiliate = in.readByte() != 0;
        this.hasShop = in.readByte() != 0;
    }

    public static final Creator<BuyerCardViewModel> CREATOR = new Creator<BuyerCardViewModel>() {
        @Override
        public BuyerCardViewModel createFromParcel(Parcel source) {
            return new BuyerCardViewModel(source);
        }

        @Override
        public BuyerCardViewModel[] newArray(int size) {
            return new BuyerCardViewModel[size];
        }
    };
}
