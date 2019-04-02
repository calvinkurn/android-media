
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Shop implements Parcelable {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("clover")
    @Expose
    private String clover;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_gold")
    @Expose
    private Boolean isGold;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reputation")
    @Expose
    private String reputation;
    @SerializedName("url")
    @Expose
    private String url;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClover() {
        return clover;
    }

    public void setClover(String clover) {
        this.clover = clover;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsGold() {
        return isGold;
    }

    public void setIsGold(Boolean isGold) {
        this.isGold = isGold;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    protected Shop(Parcel in) {
        city = in.readString();
        clover = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        byte isGoldVal = in.readByte();
        isGold = isGoldVal == 0x02 ? null : isGoldVal != 0x00;
        location = in.readString();
        name = in.readString();
        reputation = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(clover);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (isGold == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isGold ? 0x01 : 0x00));
        }
        dest.writeString(location);
        dest.writeString(name);
        dest.writeString(reputation);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
}