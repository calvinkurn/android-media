package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/22/16.
 */
public class Option implements Serializable, Parcelable {

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
    @SerializedName("child")
    @Expose
    List<LevelTwoCategory> levelTwoCategoryList;

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

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }

    public String getKeyMin() {
        return keyMin;
    }

    public void setKeyMin(String keyMin) {
        this.keyMin = keyMin;
    }

    public String getKeyMax() {
        return keyMax;
    }

    public void setKeyMax(String keyMax) {
        this.keyMax = keyMax;
    }

    public String getValMin() {
        return valMin;
    }

    public void setValMin(String valMin) {
        this.valMin = valMin;
    }

    public String getValMax() {
        return valMax;
    }

    public void setValMax(String valMax) {
        this.valMax = valMax;
    }

    public List<LevelTwoCategory> getLevelTwoCategoryList() {
        return levelTwoCategoryList;
    }

    public void setLevelTwoCategoryList(List<LevelTwoCategory> levelTwoCategoryList) {
        this.levelTwoCategoryList = levelTwoCategoryList;
    }

    protected Option(Parcel in) {
        name = in.readString();
        key = in.readString();
        value = in.readString();
        inputType = in.readString();
        hexColor = in.readString();
        metric = in.readString();
        totalData = in.readString();
        keyMin = in.readString();
        keyMax = in.readString();
        valMin = in.readString();
        valMax = in.readString();
        if (in.readByte() == 0x01) {
            levelTwoCategoryList = new ArrayList<>();
            in.readList(levelTwoCategoryList, LevelTwoCategory.class.getClassLoader());
        } else {
            levelTwoCategoryList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(value);
        dest.writeString(inputType);
        dest.writeString(hexColor);
        dest.writeString(metric);
        dest.writeString(totalData);
        dest.writeString(keyMin);
        dest.writeString(keyMax);
        dest.writeString(valMin);
        dest.writeString(valMax);
        if (levelTwoCategoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(levelTwoCategoryList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}
