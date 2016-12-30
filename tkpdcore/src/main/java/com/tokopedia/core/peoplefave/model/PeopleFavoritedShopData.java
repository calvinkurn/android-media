package com.tokopedia.core.peoplefave.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.List;

/**
 * Created by hangnadi on 10/11/16.
 */
public class PeopleFavoritedShopData implements Parcelable {

    @SerializedName("paging")
    private Paging paging;
    @SerializedName("list")
    private List<ShopFavorited> list;
    @SerializedName("is_success")
    private int isSuccess;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<ShopFavorited> getList() {
        return list;
    }

    public void setList(List<ShopFavorited> list) {
        this.list = list;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static class Paging implements Parcelable {
        @SerializedName("uri_previous")
        private String uriPrevious;
        @SerializedName("uri_next")
        private String uriNext;

        public String getUriPrevious() {
            return uriPrevious;
        }

        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

        public String getUriNext() {
            return uriNext;
        }

        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uriPrevious);
            dest.writeString(this.uriNext);
        }

        public Paging() {
        }

        protected Paging(Parcel in) {
            this.uriPrevious = in.readString();
            this.uriNext = in.readString();
        }

        public static final Creator<Paging> CREATOR = new Creator<Paging>() {
            @Override
            public Paging createFromParcel(Parcel source) {
                return new Paging(source);
            }

            @Override
            public Paging[] newArray(int size) {
                return new Paging[size];
            }
        };
    }

    public static class ShopFavorited implements Parcelable {
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("shop_reputation")
        private ShopReputation shopReputation;
        @SerializedName("shop_id")
        private String shopId;
        @SerializedName("shop_location")
        private String shopLocation;
        @SerializedName("shop_total_etalase")
        private String shopTotalEtalase;
        @SerializedName("shop_total_product")
        private String shopTotalProduct;
        @SerializedName("shop_image")
        private String shopImage;
        @SerializedName("shop_total_sold")
        private String shopTotalSold;

        public String getShopName() {
            return MethodChecker.fromHtml(shopName).toString();
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public ShopReputation getShopReputation() {
            return shopReputation;
        }

        public void setShopReputation(ShopReputation shopReputation) {
            this.shopReputation = shopReputation;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopLocation() {
            return shopLocation;
        }

        public void setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
        }

        public String getShopTotalEtalase() {
            return shopTotalEtalase;
        }

        public void setShopTotalEtalase(String shopTotalEtalase) {
            this.shopTotalEtalase = shopTotalEtalase;
        }

        public String getShopTotalProduct() {
            return shopTotalProduct;
        }

        public void setShopTotalProduct(String shopTotalProduct) {
            this.shopTotalProduct = shopTotalProduct;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getShopTotalSold() {
            return shopTotalSold;
        }

        public void setShopTotalSold(String shopTotalSold) {
            this.shopTotalSold = shopTotalSold;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.shopName);
            dest.writeParcelable(this.shopReputation, flags);
            dest.writeString(this.shopId);
            dest.writeString(this.shopLocation);
            dest.writeString(this.shopTotalEtalase);
            dest.writeString(this.shopTotalProduct);
            dest.writeString(this.shopImage);
            dest.writeString(this.shopTotalSold);
        }

        public ShopFavorited() {
        }

        protected ShopFavorited(Parcel in) {
            this.shopName = in.readString();
            this.shopReputation = in.readParcelable(ShopReputation.class.getClassLoader());
            this.shopId = in.readString();
            this.shopLocation = in.readString();
            this.shopTotalEtalase = in.readString();
            this.shopTotalProduct = in.readString();
            this.shopImage = in.readString();
            this.shopTotalSold = in.readString();
        }

        public static final Creator<ShopFavorited> CREATOR = new Creator<ShopFavorited>() {
            @Override
            public ShopFavorited createFromParcel(Parcel source) {
                return new ShopFavorited(source);
            }

            @Override
            public ShopFavorited[] newArray(int size) {
                return new ShopFavorited[size];
            }
        };
    }

    public static class ReputationBadge implements Parcelable {
        @SerializedName("level")
        private int level;
        @SerializedName("set")
        private int set;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getSet() {
            return set;
        }

        public void setSet(int set) {
            this.set = set;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.level);
            dest.writeInt(this.set);
        }

        public ReputationBadge() {
        }

        protected ReputationBadge(Parcel in) {
            this.level = in.readInt();
            this.set = in.readInt();
        }

        public static final Creator<ReputationBadge> CREATOR = new Creator<ReputationBadge>() {
            @Override
            public ReputationBadge createFromParcel(Parcel source) {
                return new ReputationBadge(source);
            }

            @Override
            public ReputationBadge[] newArray(int size) {
                return new ReputationBadge[size];
            }
        };
    }

    public static class ShopReputation implements Parcelable {
        @SerializedName("score")
        private String score;
        @SerializedName("min_badge_score")
        private int minBadgeScore;
        @SerializedName("reputation_badge")
        private ReputationBadge reputationBadge;
        @SerializedName("tooltip")
        private String tooltip;
        @SerializedName("reputation_score")
        private String reputationScore;
        @SerializedName("badge_level")
        private int badgeLevel;

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getMinBadgeScore() {
            return minBadgeScore;
        }

        public void setMinBadgeScore(int minBadgeScore) {
            this.minBadgeScore = minBadgeScore;
        }

        public ReputationBadge getReputationBadge() {
            return reputationBadge;
        }

        public void setReputationBadge(ReputationBadge reputationBadge) {
            this.reputationBadge = reputationBadge;
        }

        public String getTooltip() {
            return tooltip;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public String getReputationScore() {
            return reputationScore;
        }

        public void setReputationScore(String reputationScore) {
            this.reputationScore = reputationScore;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        public void setBadgeLevel(int badgeLevel) {
            this.badgeLevel = badgeLevel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.score);
            dest.writeInt(this.minBadgeScore);
            dest.writeParcelable(this.reputationBadge, flags);
            dest.writeString(this.tooltip);
            dest.writeString(this.reputationScore);
            dest.writeInt(this.badgeLevel);
        }

        public ShopReputation() {
        }

        protected ShopReputation(Parcel in) {
            this.score = in.readString();
            this.minBadgeScore = in.readInt();
            this.reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
            this.tooltip = in.readString();
            this.reputationScore = in.readString();
            this.badgeLevel = in.readInt();
        }

        public static final Creator<ShopReputation> CREATOR = new Creator<ShopReputation>() {
            @Override
            public ShopReputation createFromParcel(Parcel source) {
                return new ShopReputation(source);
            }

            @Override
            public ShopReputation[] newArray(int size) {
                return new ShopReputation[size];
            }
        };
    }

    public PeopleFavoritedShopData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.paging, flags);
        dest.writeTypedList(this.list);
        dest.writeInt(this.isSuccess);
    }

    protected PeopleFavoritedShopData(Parcel in) {
        this.paging = in.readParcelable(Paging.class.getClassLoader());
        this.list = in.createTypedArrayList(ShopFavorited.CREATOR);
        this.isSuccess = in.readInt();
    }

    public static final Creator<PeopleFavoritedShopData> CREATOR = new Creator<PeopleFavoritedShopData>() {
        @Override
        public PeopleFavoritedShopData createFromParcel(Parcel source) {
            return new PeopleFavoritedShopData(source);
        }

        @Override
        public PeopleFavoritedShopData[] newArray(int size) {
            return new PeopleFavoritedShopData[size];
        }
    };
}
