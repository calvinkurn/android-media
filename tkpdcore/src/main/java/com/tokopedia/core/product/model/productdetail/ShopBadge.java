package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 17/11/2015.
 */
@Deprecated
public class ShopBadge implements Parcelable {
    private static final String TAG = ShopBadge.class.getSimpleName();

    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("set")
    @Expose
    private Integer set;

    public ShopBadge() {
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    protected ShopBadge(Parcel in) {
        level = in.readByte() == 0x00 ? null : in.readInt();
        set = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (level == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(level);
        }
        if (set == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(set);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<ShopBadge> CREATOR = new Creator<ShopBadge>() {
        @Override
        public ShopBadge createFromParcel(Parcel in) {
            return new ShopBadge(in);
        }

        @Override
        public ShopBadge[] newArray(int size) {
            return new ShopBadge[size];
        }
    };


    public static class Builder {
        private Integer level;
        private Integer set;

        private Builder() {
        }

        public static Builder aShopBadge() {
            return new Builder();
        }

        public Builder setLevel(Integer level) {
            this.level = level;
            return this;
        }

        public Builder setSet(Integer set) {
            this.set = set;
            return this;
        }

        public Builder but() {
            return aShopBadge().setLevel(level).setSet(set);
        }

        public ShopBadge build() {
            ShopBadge shopBadge = new ShopBadge();
            shopBadge.setLevel(level);
            shopBadge.setSet(set);
            return shopBadge;
        }
    }
}
