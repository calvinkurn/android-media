package com.tokopedia.digital_deals.view.activity.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DealDetailPassData implements Parcelable {
    private boolean enableBuy;
    private boolean enableRecommendation;
    private boolean enableShare;
    private boolean enableLike;
    private String slug;

    private DealDetailPassData(boolean enableBuy, boolean enableRecommendation, boolean enableLike, boolean enableShare, String slug) {

        this.enableBuy = enableBuy;
        this.enableRecommendation = enableRecommendation;
        this.enableLike = enableLike;
        this.enableShare = enableShare;
        this.slug = slug;
    }

    public DealDetailPassData() {
    }

    protected DealDetailPassData(Parcel in) {
        enableBuy = in.readByte() != 0;
        enableRecommendation = in.readByte() != 0;
        enableShare = in.readByte() != 0;
        enableLike = in.readByte() != 0;
        slug = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enableBuy ? 1 : 0));
        dest.writeByte((byte) (enableRecommendation ? 1 : 0));
        dest.writeByte((byte) (enableShare ? 1 : 0));
        dest.writeByte((byte) (enableLike ? 1 : 0));
        dest.writeString(slug);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DealDetailPassData> CREATOR = new Creator<DealDetailPassData>() {
        @Override
        public DealDetailPassData createFromParcel(Parcel in) {
            return new DealDetailPassData(in);
        }

        @Override
        public DealDetailPassData[] newArray(int size) {
            return new DealDetailPassData[size];
        }
    };

    public boolean isEnableBuy() {
        return enableBuy;
    }

    public boolean isEnableRecommendation() {
        return enableRecommendation;
    }

    public boolean isEnableShare() {
        return enableShare;
    }

    public boolean isEnableLike() {
        return enableLike;
    }

    public String getSlug() {
        return slug;
    }

    public static class Builder {
        private boolean enableBuy;
        private boolean enableRecommendation;
        private boolean enableShare;
        private boolean enableLike;
        private String slug;

        public Builder() {

        }

        public Builder enableBuy(boolean enable) {
            this.enableBuy = enable;
            return this;
        }

        public Builder enableRecommendation(boolean enable) {
            this.enableRecommendation = enable;
            return this;
        }

        public Builder enableShare(boolean enable) {
            this.enableShare = enable;
            return this;
        }

        public Builder enableLike(boolean enable) {
            this.enableLike = enable;
            return this;
        }
        public Builder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public DealDetailPassData build() {
            return new DealDetailPassData(enableBuy, enableRecommendation, enableLike, enableShare, slug);
        }
    }
}
