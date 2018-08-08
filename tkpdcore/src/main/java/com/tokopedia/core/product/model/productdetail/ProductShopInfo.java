package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
public class ProductShopInfo implements Parcelable{
    private static final String TAG = ProductShopInfo.class.getSimpleName();

    public static final int SHOP_OFFICIAL_VALUE = 1;
    public static final int SHOP_STATUS_ACTIVE = 1;

    @SerializedName("shop_is_closed_reason")
    @Expose
    private String shopIsClosedReason;
    @SerializedName("shop_lucky")
    @Expose
    private String shopLucky;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_owner_last_login")
    @Expose
    private String shopOwnerLastLogin;
    @SerializedName("shop_tagline")
    @Expose
    private String shopTagline;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_description")
    @Expose
    private String shopDescription;
    @SerializedName("shop_is_official")
    @Expose
    private Integer shopIsOfficial;
    @SerializedName("shop_cover")
    @Expose
    private String shopCover;
    @SerializedName("shop_has_terms")
    @Expose
    private Integer shopHasTerms;
    @SerializedName("shop_is_gold")
    @Expose
    private Integer shopIsGold;
    @SerializedName("shop_is_gold_badge")
    @Expose
    private boolean shopIsGoldBadge;
    @SerializedName("shop_open_since")
    @Expose
    private String shopOpenSince;
    @SerializedName("shop_min_badge_score")
    @Expose
    private Integer shopMinBadgeScore;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("shop_is_closed_until")
    @Expose
    private String shopIsClosedUntil;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_reputation")
    @Expose
    private Integer shopReputation;
    @SerializedName("shop_stats")
    @Expose
    private ShopStats shopStats;
    @SerializedName("shop_owner_id")
    @Expose
    private Integer shopOwnerId;
    @SerializedName("shop_already_favorited")
    @Expose
    private Integer shopAlreadyFavorited;
    @SerializedName("shop_is_owner")
    @Expose
    private Integer shopIsOwner;
    @SerializedName("shop_is_allow_manage")
    @Expose
    private Integer shopIsAllowManage;
    @SerializedName("shop_status")
    @Expose
    private Integer shopStatus;
    @SerializedName("shop_is_closed_note")
    @Expose
    private String shopIsClosedNote;
    @SerializedName("shop_reputation_badge")
    @Expose
    private Integer shopReputationBadge;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar;
    @SerializedName("shop_total_favorit")
    @Expose
    private Integer shopTotalFavorit;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_status_message")
    @Expose
    private String shopStatusMessage;
    @SerializedName("shop_status_title")
    @Expose
    private String shopStatusTitle;
    @SerializedName("shop_shipments")
    @Expose
    private ArrayList<ShopShipment> shopShipments;

    public ProductShopInfo() {
    }

    public String getShopIsClosedReason() {
        return shopIsClosedReason;
    }

    public void setShopIsClosedReason(String shopIsClosedReason) {
        this.shopIsClosedReason = shopIsClosedReason;
    }

    public String getShopLucky() {
        return shopLucky;
    }

    public void setShopLucky(String shopLucky) {
        this.shopLucky = shopLucky;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopOwnerLastLogin() {
        return shopOwnerLastLogin;
    }

    public void setShopOwnerLastLogin(String shopOwnerLastLogin) {
        this.shopOwnerLastLogin = shopOwnerLastLogin;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public Integer getShopIsOfficial() {
        return shopIsOfficial;
    }

    public void setShopIsOfficial(Integer shopIsOfficial) {
        this.shopIsOfficial = shopIsOfficial;
    }

    public String getShopCover() {
        return shopCover;
    }

    public void setShopCover(String shopCover) {
        this.shopCover = shopCover;
    }

    public Integer getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(Integer shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    public boolean shopIsGoldBadge() {
        return shopIsGoldBadge;
    }

    public void setShopIsGoldBadge(boolean shopIsGoldBadge) {
        this.shopIsGoldBadge = shopIsGoldBadge;
    }

    public Integer getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(Integer shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public String getShopOpenSince() {
        return shopOpenSince;
    }

    public void setShopOpenSince(String shopOpenSince) {
        this.shopOpenSince = shopOpenSince;
    }

    public Integer getShopMinBadgeScore() {
        return shopMinBadgeScore;
    }

    public void setShopMinBadgeScore(Integer shopMinBadgeScore) {
        this.shopMinBadgeScore = shopMinBadgeScore;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopIsClosedUntil() {
        return shopIsClosedUntil;
    }

    public void setShopIsClosedUntil(String shopIsClosedUntil) {
        this.shopIsClosedUntil = shopIsClosedUntil;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(Integer shopReputation) {
        this.shopReputation = shopReputation;
    }

    public ShopStats getShopStats() {
        return shopStats;
    }

    public void setShopStats(ShopStats shopStats) {
        this.shopStats = shopStats;
    }

    public Integer getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(Integer shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public Integer getShopAlreadyFavorited() {
        return shopAlreadyFavorited;
    }

    public void setShopAlreadyFavorited(Integer shopAlreadyFavorited) {
        this.shopAlreadyFavorited = shopAlreadyFavorited;
    }

    public Integer getShopIsOwner() {
        return shopIsOwner;
    }

    public void setShopIsOwner(Integer shopIsOwner) {
        this.shopIsOwner = shopIsOwner;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getShopIsClosedNote() {
        return shopIsClosedNote;
    }

    public void setShopIsClosedNote(String shopIsClosedNote) {
        this.shopIsClosedNote = shopIsClosedNote;
    }

    public Integer getShopReputationBadge() {
        return shopReputationBadge;
    }

    public void setShopReputationBadge(Integer shopReputationBadge) {
        this.shopReputationBadge = shopReputationBadge;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public Integer getShopTotalFavorit() {
        return shopTotalFavorit;
    }

    public void setShopTotalFavorit(Integer shopTotalFavorit) {
        this.shopTotalFavorit = shopTotalFavorit;
    }

    public String getShopStatusMessage() {
        return MethodChecker.fromHtml(shopStatusMessage).toString();
    }

    public void setShopStatusMessage(String shopStatusMessage) {
        this.shopStatusMessage = shopStatusMessage;
    }

    public String getShopStatusTitle() {
        return MethodChecker.fromHtml(shopStatusTitle).toString();
    }

    public void setShopStatusTitle(String shopStatusTitle) {
        this.shopStatusTitle = shopStatusTitle;
    }

    public int getShopIsAllowManage() {
        return shopIsAllowManage;
    }

    public void setShopIsAllowManage(int shopIsAllowManage) {
        this.shopIsAllowManage = shopIsAllowManage;
    }

    public ArrayList<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public void setShopShipments(ArrayList<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    protected ProductShopInfo(Parcel in) {
        shopIsClosedReason = in.readString();
        shopLucky = in.readString();
        shopId = in.readString();
        shopOwnerLastLogin = in.readString();
        shopTagline = in.readString();
        shopUrl = in.readString();
        shopDescription = in.readString();
        shopIsOfficial = in.readByte() == 0x00 ? null : in.readInt();
        shopCover = in.readString();
        shopHasTerms = in.readByte() == 0x00 ? null : in.readInt();
        shopIsGold = in.readByte() == 0x00 ? null : in.readInt();
        shopIsGoldBadge = in.readByte() != 0x00;
        shopOpenSince = in.readString();
        shopMinBadgeScore = in.readByte() == 0x00 ? null : in.readInt();
        shopLocation = in.readString();
        shopIsClosedUntil = in.readString();
        shopName = in.readString();
        shopReputation = in.readByte() == 0x00 ? null : in.readInt();
        shopStats = (ShopStats) in.readValue(ShopStats.class.getClassLoader());
        shopOwnerId = in.readByte() == 0x00 ? null : in.readInt();
        shopAlreadyFavorited = in.readByte() == 0x00 ? null : in.readInt();
        shopIsOwner = in.readByte() == 0x00 ? null : in.readInt();
        shopIsAllowManage = in.readByte() == 0x00 ? null : in.readInt();
        shopStatus = in.readByte() == 0x00 ? null : in.readInt();
        shopIsClosedNote = in.readString();
        shopReputationBadge = in.readByte() == 0x00 ? null : in.readInt();
        shopAvatar = in.readString();
        shopTotalFavorit = in.readByte() == 0x00 ? null : in.readInt();
        shopDomain = in.readString();
        shopStatusMessage = in.readString();
        shopStatusTitle = in.readString();
        if (in.readByte() == 0x01) {
            shopShipments = new ArrayList<>();
            in.readList(shopShipments, ShopShipment.class.getClassLoader());
        } else {
            shopShipments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopIsClosedReason);
        dest.writeString(shopLucky);
        dest.writeString(shopId);
        dest.writeString(shopOwnerLastLogin);
        dest.writeString(shopTagline);
        dest.writeString(shopUrl);
        dest.writeString(shopDescription);
        if (shopIsOfficial == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopIsOfficial);
        }
        dest.writeString(shopCover);
        if (shopHasTerms == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopHasTerms);
        }
        if (shopIsGold == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopIsGold);
        }
        dest.writeByte((byte) (shopIsGoldBadge ? 0x01 : 0x00));
        dest.writeString(shopOpenSince);
        if (shopMinBadgeScore == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopMinBadgeScore);
        }
        dest.writeString(shopLocation);
        dest.writeString(shopIsClosedUntil);
        dest.writeString(shopName);
        if (shopReputation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopReputation);
        }
        dest.writeValue(shopStats);
        if (shopOwnerId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopOwnerId);
        }
        if (shopAlreadyFavorited == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopAlreadyFavorited);
        }
        if (shopIsOwner == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopIsOwner);
        }
        if (shopIsAllowManage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopIsAllowManage);
        }
        if (shopStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopStatus);
        }
        dest.writeString(shopIsClosedNote);
        if (shopReputationBadge == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopReputationBadge);
        }
        dest.writeString(shopAvatar);
        if (shopTotalFavorit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopTotalFavorit);
        }
        dest.writeString(shopDomain);
        dest.writeString(shopStatusMessage);
        dest.writeString(shopStatusTitle);
        if (shopShipments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(shopShipments);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductShopInfo> CREATOR = new Creator<ProductShopInfo>() {
        @Override
        public ProductShopInfo createFromParcel(Parcel in) {
            return new ProductShopInfo(in);
        }

        @Override
        public ProductShopInfo[] newArray(int size) {
            return new ProductShopInfo[size];
        }
    };


    public static class Builder {
        private String shopIsClosedReason;
        private String shopLucky;
        private String shopId;
        private String shopOwnerLastLogin;
        private String shopTagline;
        private String shopUrl;
        private String shopDescription;
        private Integer shopIsOfficial;
        private String shopCover;
        private Integer shopHasTerms;
        private Integer shopIsGold;
        private String shopOpenSince;
        private Integer shopMinBadgeScore;
        private String shopLocation;
        private String shopIsClosedUntil;
        private String shopName;
        private Integer shopReputation;
        private ShopStats shopStats;
        private Integer shopOwnerId;
        private Integer shopAlreadyFavorited;
        private Integer shopIsOwner;
        private Integer shopStatus;
        private String shopIsClosedNote;
        private Integer shopReputationBadge;
        private String shopAvatar;
        private Integer shopTotalFavorit;
        private String shopDomain;

        private Builder() {
        }

        public static Builder aProductShopInfo() {
            return new Builder();
        }

        public Builder setShopIsClosedReason(String shopIsClosedReason) {
            this.shopIsClosedReason = shopIsClosedReason;
            return this;
        }

        public Builder setShopLucky(String shopLucky) {
            this.shopLucky = shopLucky;
            return this;
        }

        public Builder setShopId(String shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder setShopOwnerLastLogin(String shopOwnerLastLogin) {
            this.shopOwnerLastLogin = shopOwnerLastLogin;
            return this;
        }

        public Builder setShopTagline(String shopTagline) {
            this.shopTagline = shopTagline;
            return this;
        }

        public Builder setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
            return this;
        }

        public Builder setShopDescription(String shopDescription) {
            this.shopDescription = shopDescription;
            return this;
        }

        public Builder setShopIsOfficial(Integer shopIsOfficial) {
            this.shopIsOfficial = shopIsOfficial;
            return this;
        }

        public Builder setShopCover(String shopCover) {
            this.shopCover = shopCover;
            return this;
        }

        public Builder setShopHasTerms(Integer shopHasTerms) {
            this.shopHasTerms = shopHasTerms;
            return this;
        }

        public Builder setShopIsGold(Integer shopIsGold) {
            this.shopIsGold = shopIsGold;
            return this;
        }

        public Builder setShopOpenSince(String shopOpenSince) {
            this.shopOpenSince = shopOpenSince;
            return this;
        }

        public Builder setShopMinBadgeScore(Integer shopMinBadgeScore) {
            this.shopMinBadgeScore = shopMinBadgeScore;
            return this;
        }

        public Builder setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
            return this;
        }

        public Builder setShopIsClosedUntil(String shopIsClosedUntil) {
            this.shopIsClosedUntil = shopIsClosedUntil;
            return this;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder setShopReputation(Integer shopReputation) {
            this.shopReputation = shopReputation;
            return this;
        }

        public Builder setShopStats(ShopStats shopStats) {
            this.shopStats = shopStats;
            return this;
        }

        public Builder setShopOwnerId(Integer shopOwnerId) {
            this.shopOwnerId = shopOwnerId;
            return this;
        }

        public Builder setShopAlreadyFavorited(Integer shopAlreadyFavorited) {
            this.shopAlreadyFavorited = shopAlreadyFavorited;
            return this;
        }

        public Builder setShopIsOwner(Integer shopIsOwner) {
            this.shopIsOwner = shopIsOwner;
            return this;
        }

        public Builder setShopStatus(Integer shopStatus) {
            this.shopStatus = shopStatus;
            return this;
        }

        public Builder setShopIsClosedNote(String shopIsClosedNote) {
            this.shopIsClosedNote = shopIsClosedNote;
            return this;
        }

        public Builder setShopReputationBadge(Integer shopReputationBadge) {
            this.shopReputationBadge = shopReputationBadge;
            return this;
        }

        public Builder setShopAvatar(String shopAvatar) {
            this.shopAvatar = shopAvatar;
            return this;
        }

        public Builder setShopTotalFavorit(Integer shopTotalFavorit) {
            this.shopTotalFavorit = shopTotalFavorit;
            return this;
        }

        public Builder setShopDomain(String shopDomain) {
            this.shopDomain = shopDomain;
            return this;
        }

        public Builder but() {
            return aProductShopInfo()
                    .setShopIsClosedReason(shopIsClosedReason)
                    .setShopLucky(shopLucky).setShopId(shopId)
                    .setShopOwnerLastLogin(shopOwnerLastLogin)
                    .setShopTagline(shopTagline)
                    .setShopUrl(shopUrl)
                    .setShopDescription(shopDescription)
                    .setShopCover(shopCover)
                    .setShopHasTerms(shopHasTerms)
                    .setShopIsGold(shopIsGold)
                    .setShopOpenSince(shopOpenSince)
                    .setShopMinBadgeScore(shopMinBadgeScore)
                    .setShopLocation(shopLocation)
                    .setShopIsClosedUntil(shopIsClosedUntil)
                    .setShopName(shopName)
                    .setShopReputation(shopReputation)
                    .setShopStats(shopStats)
                    .setShopOwnerId(shopOwnerId)
                    .setShopAlreadyFavorited(shopAlreadyFavorited)
                    .setShopIsOwner(shopIsOwner)
                    .setShopStatus(shopStatus)
                    .setShopIsClosedNote(shopIsClosedNote)
                    .setShopReputationBadge(shopReputationBadge)
                    .setShopAvatar(shopAvatar)
                    .setShopTotalFavorit(shopTotalFavorit)
                    .setShopDomain(shopDomain)
                    .setShopIsOfficial(shopIsOfficial);
        }

        public ProductShopInfo build() {
            ProductShopInfo productShopInfo = new ProductShopInfo();
            productShopInfo.setShopIsClosedReason(shopIsClosedReason);
            productShopInfo.setShopLucky(shopLucky);
            productShopInfo.setShopId(shopId);
            productShopInfo.setShopOwnerLastLogin(shopOwnerLastLogin);
            productShopInfo.setShopTagline(shopTagline);
            productShopInfo.setShopUrl(shopUrl);
            productShopInfo.setShopDescription(shopDescription);
            productShopInfo.setShopIsOfficial(shopIsOfficial);
            productShopInfo.setShopCover(shopCover);
            productShopInfo.setShopHasTerms(shopHasTerms);
            productShopInfo.setShopIsGold(shopIsGold);
            productShopInfo.setShopOpenSince(shopOpenSince);
            productShopInfo.setShopMinBadgeScore(shopMinBadgeScore);
            productShopInfo.setShopLocation(shopLocation);
            productShopInfo.setShopIsClosedUntil(shopIsClosedUntil);
            productShopInfo.setShopName(shopName);
            productShopInfo.setShopReputation(shopReputation);
            productShopInfo.setShopStats(shopStats);
            productShopInfo.setShopOwnerId(shopOwnerId);
            productShopInfo.setShopAlreadyFavorited(shopAlreadyFavorited);
            productShopInfo.setShopIsOwner(shopIsOwner);
            productShopInfo.setShopStatus(shopStatus);
            productShopInfo.setShopIsClosedNote(shopIsClosedNote);
            productShopInfo.setShopReputationBadge(shopReputationBadge);
            productShopInfo.setShopAvatar(shopAvatar);
            productShopInfo.setShopTotalFavorit(shopTotalFavorit);
            productShopInfo.setShopDomain(shopDomain);
            return productShopInfo;
        }
    }
}
