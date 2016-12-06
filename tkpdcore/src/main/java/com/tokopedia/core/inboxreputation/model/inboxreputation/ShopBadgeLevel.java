
package com.tokopedia.core.inboxreputation.model.inboxreputation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopBadgeLevel implements Parcelable {

    @SerializedName("level")
    @Expose
    int level;
    @SerializedName("set")
    @Expose
    int set;

    protected ShopBadgeLevel(Parcel in) {
        level = in.readInt();
        set = in.readInt();
    }

    public static final Creator<ShopBadgeLevel> CREATOR = new Creator<ShopBadgeLevel>() {
        @Override
        public ShopBadgeLevel createFromParcel(Parcel in) {
            return new ShopBadgeLevel(in);
        }

        @Override
        public ShopBadgeLevel[] newArray(int size) {
            return new ShopBadgeLevel[size];
        }
    };

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
        dest.writeInt(level);
        dest.writeInt(set);
    }
}
