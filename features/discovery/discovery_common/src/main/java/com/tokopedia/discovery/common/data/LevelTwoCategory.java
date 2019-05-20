package com.tokopedia.discovery.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class LevelTwoCategory implements Parcelable {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInputType() {
        return inputType;
    }

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
        dest.writeString(this.totalData);
        dest.writeTypedList(this.levelThreeCategoryList);
    }

    public LevelTwoCategory() {
    }

    protected LevelTwoCategory(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.inputType = in.readString();
        this.totalData = in.readString();
        this.levelThreeCategoryList = in.createTypedArrayList(LevelThreeCategory.CREATOR);
    }

    public static final Creator<LevelTwoCategory> CREATOR = new Creator<LevelTwoCategory>() {
        @Override
        public LevelTwoCategory createFromParcel(Parcel source) {
            return new LevelTwoCategory(source);
        }

        @Override
        public LevelTwoCategory[] newArray(int size) {
            return new LevelTwoCategory[size];
        }
    };
}
