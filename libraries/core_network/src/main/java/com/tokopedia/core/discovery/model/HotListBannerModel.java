package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by noiz354 on 3/23/16.
 */

@Deprecated
public class HotListBannerModel implements Parcelable {
    @SerializedName("query")
    public Query query;

    @SerializedName("info")
    public Info info;

    @SerializedName("disable_topads")
    public int disableTopads;

    @SerializedName("promo_info")
    public PromoInfo promoInfo;

    public static class Query implements Parcelable {
        @SerializedName("ob")
        public String ob;

        @SerializedName("q")
        public String q;

        @SerializedName("terms")
        public String terms;

        @SerializedName("sc")
        public String sc;

        @SerializedName("negative_keyword")
        public String negative_keyword;

        @SerializedName("pmin")
        public String pmin;

        @SerializedName("fshop")
        public String fshop;

        @SerializedName("type")
        public String type;

        @SerializedName("pmax")
        public String pmax;

        @SerializedName("hot_id")
        public String hot_id;

        @SerializedName("shop_id")
        public String shop_id;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.ob);
            dest.writeString(this.q);
            dest.writeString(this.terms);
            dest.writeString(this.sc);
            dest.writeString(this.negative_keyword);
            dest.writeString(this.pmin);
            dest.writeString(this.fshop);
            dest.writeString(this.type);
            dest.writeString(this.pmax);
            dest.writeString(this.hot_id);
            dest.writeString(this.shop_id);
        }

        public Query() {
        }

        protected Query(android.os.Parcel in) {
            this.ob = in.readString();
            this.q = in.readString();
            this.terms = in.readString();
            this.sc = in.readString();
            this.negative_keyword = in.readString();
            this.pmin = in.readString();
            this.fshop = in.readString();
            this.type = in.readString();
            this.pmax = in.readString();
            this.hot_id = in.readString();
            this.shop_id = in.readString();
        }

        public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
            @Override
            public Query createFromParcel(android.os.Parcel source) {
                return new Query(source);
            }

            @Override
            public Query[] newArray(int size) {
                return new Query[size];
            }
        };
    }

    public static class Info implements Parcelable {
        @SerializedName("title")
        public String title;

        @SerializedName("meta_description")
        public String metaDescription;

        @SerializedName("share_file_path")
        public String shareFilePath;

        @SerializedName("meta_title")
        public String metaTitle;

        @SerializedName("file_name")
        public String fileName;

        @SerializedName("cover_img")
        public String coverImg;

        @SerializedName("alias_key")
        public String aliasKey;

        @SerializedName("hotlist_description")
        public String hotlistDescription;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.metaDescription);
            dest.writeString(this.shareFilePath);
            dest.writeString(this.metaTitle);
            dest.writeString(this.fileName);
            dest.writeString(this.coverImg);
            dest.writeString(this.aliasKey);
            dest.writeString(this.hotlistDescription);
        }

        public Info() {
        }

        protected Info(android.os.Parcel in) {
            this.title = in.readString();
            this.metaDescription = in.readString();
            this.shareFilePath = in.readString();
            this.metaTitle = in.readString();
            this.fileName = in.readString();
            this.coverImg = in.readString();
            this.aliasKey = in.readString();
            this.hotlistDescription = in.readString();
        }

        public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
            @Override
            public Info createFromParcel(android.os.Parcel source) {
                return new Info(source);
            }

            @Override
            public Info[] newArray(int size) {
                return new Info[size];
            }
        };
    }

    public static final class HotListBannerContainer implements ObjContainer<HotListBannerModel> {
        HotListBannerModel hotListBannerModel;

        public HotListBannerContainer(HotListBannerModel hotListBannerModel) {
            this.hotListBannerModel = hotListBannerModel;
        }

        @Override
        public HotListBannerModel body() {
            return hotListBannerModel;
        }
    }


    public HotListBannerModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.query, flags);
        dest.writeParcelable(this.info, flags);
        dest.writeInt(this.disableTopads);
    }

    protected HotListBannerModel(Parcel in) {
        this.query = in.readParcelable(Query.class.getClassLoader());
        this.info = in.readParcelable(Info.class.getClassLoader());
        this.disableTopads = in.readInt();
    }

    public static final Creator<HotListBannerModel> CREATOR = new Creator<HotListBannerModel>() {
        @Override
        public HotListBannerModel createFromParcel(Parcel source) {
            return new HotListBannerModel(source);
        }

        @Override
        public HotListBannerModel[] newArray(int size) {
            return new HotListBannerModel[size];
        }
    };

    public static class PromoInfo {

        @SerializedName("promo_period")
        private String promoPeriod;
        @SerializedName("text")
        private String text;
        @SerializedName("id")
        private String id;
        @SerializedName("tc_applink")
        private String tcApplink;
        @SerializedName("min_tx")
        private String minTx;
        @SerializedName("voucher_code")
        private String voucherCode;
        @SerializedName("tc_link")
        private String tcLink;

        public String getPromoPeriod() {
            return promoPeriod;
        }

        public void setPromoPeriod(String promoPeriod) {
            this.promoPeriod = promoPeriod;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTcApplink() {
            return tcApplink;
        }

        public void setTcApplink(String tcApplink) {
            this.tcApplink = tcApplink;
        }

        public String getMinTx() {
            return minTx;
        }

        public void setMinTx(String minTx) {
            this.minTx = minTx;
        }

        public String getVoucherCode() {
            return voucherCode;
        }

        public void setVoucherCode(String voucherCode) {
            this.voucherCode = voucherCode;
        }

        public String getTcLink() {
            return tcLink;
        }

        public void setTcLink(String tcLink) {
            this.tcLink = tcLink;
        }
    }
}
