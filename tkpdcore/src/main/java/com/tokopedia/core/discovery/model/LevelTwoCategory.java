package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/7/17.
 */

public class LevelTwoCategory implements Serializable, Parcelable {

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
    @SerializedName("total_data")
    @Expose
    String totalData;
    @SerializedName("child")
    @Expose
    List<LevelThreeCategory> levelThreeCategoryList;

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

    public List<LevelThreeCategory> getLevelThreeCategoryList() {
        return levelThreeCategoryList;
    }

    public void setLevelThreeCategoryList(List<LevelThreeCategory> levelThreeCategoryList) {
        this.levelThreeCategoryList = levelThreeCategoryList;
    }

    protected LevelTwoCategory(Parcel in) {
        name = in.readString();
        key = in.readString();
        value = in.readString();
        inputType = in.readString();
        totalData = in.readString();
        if (in.readByte() == 0x01) {
            levelThreeCategoryList = new ArrayList<>();
            in.readList(levelThreeCategoryList, LevelThreeCategory.class.getClassLoader());
        } else {
            levelThreeCategoryList = null;
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
        dest.writeString(totalData);
        if (levelThreeCategoryList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(levelThreeCategoryList);
        }
    }

    public static final Parcelable.Creator<LevelTwoCategory> CREATOR
            = new Parcelable.Creator<LevelTwoCategory>() {
        @Override
        public LevelTwoCategory createFromParcel(Parcel in) {
            return new LevelTwoCategory(in);
        }

        @Override
        public LevelTwoCategory[] newArray(int size) {
            return new LevelTwoCategory[size];
        }
    };
}
