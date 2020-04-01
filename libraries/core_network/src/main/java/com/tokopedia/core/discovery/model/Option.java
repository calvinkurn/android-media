package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */

@Deprecated
public class Option implements Parcelable {

    public static final String UID_FIRST_SEPARATOR_SYMBOL = "*";
    public static final String UID_SECOND_SEPARATOR_SYMBOL = "?";

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("key")
    @Expose
    String key;
    @SerializedName("value")
    @Expose
    String value;
    @SerializedName("input_type")
    @Expose
    String inputType;
    @SerializedName("hex_color")
    @Expose
    String hexColor;
    @SerializedName("metric")
    @Expose
    String metric;
    @SerializedName("total_data")
    @Expose
    String totalData;
    @SerializedName("key_min")
    @Expose
    String keyMin;
    @SerializedName("key_max")
    @Expose
    String keyMax;
    @SerializedName("val_min")
    @Expose
    String valMin;
    @SerializedName("val_max")
    @Expose
    String valMax;
    @SerializedName("icon")
    @Expose
    String iconUrl;
    @SerializedName("is_popular")
    @Expose
    boolean isPopular;
    @SerializedName("child")
    @Expose
    List<LevelTwoCategory> levelTwoCategoryList;

    String inputState = "";

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The inputType
     */
    public String getInputType() {
        return inputType;
    }

    /**
     * @param inputType The input_type
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public String getUniqueId() {
        return key + UID_FIRST_SEPARATOR_SYMBOL + value + UID_SECOND_SEPARATOR_SYMBOL + name;
    }

    public Option() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeString(this.inputType);
        dest.writeString(this.hexColor);
        dest.writeString(this.metric);
        dest.writeString(this.totalData);
        dest.writeString(this.keyMin);
        dest.writeString(this.keyMax);
        dest.writeString(this.valMin);
        dest.writeString(this.valMax);
        dest.writeString(this.iconUrl);
        dest.writeByte(this.isPopular ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.levelTwoCategoryList);
        dest.writeString(this.inputState);
    }

    protected Option(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.inputType = in.readString();
        this.hexColor = in.readString();
        this.metric = in.readString();
        this.totalData = in.readString();
        this.keyMin = in.readString();
        this.keyMax = in.readString();
        this.valMin = in.readString();
        this.valMax = in.readString();
        this.iconUrl = in.readString();
        this.isPopular = in.readByte() != 0;
        this.levelTwoCategoryList = in.createTypedArrayList(LevelTwoCategory.CREATOR);
        this.inputState = in.readString();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}
