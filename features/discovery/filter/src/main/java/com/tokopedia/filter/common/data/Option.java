package com.tokopedia.filter.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class Option implements Parcelable {

    public static final String KEY_PRICE_MIN = "pmin";
    public static final String KEY_PRICE_MAX = "pmax";
    public static final String KEY_PRICE_MIN_MAX_RANGE = "pmin-pmax";
    public static final String KEY_PRICE_WHOLESALE = "wholesale";
    public static final String KEY_PRICE_RANGE_1 = "price_range_1";
    public static final String KEY_PRICE_RANGE_2 = "price_range_2";
    public static final String KEY_PRICE_RANGE_3 = "price_range_3";
    public static final String KEY_CATEGORY = "sc";
    public static final String KEY_OFFICIAL = "official";
    public static final String KEY_RATING = "rt";
    public static final String KEY_ANNOTATION_ID = "annotation_id";

    public static final String INPUT_TYPE_TEXTBOX = "textbox";
    public static final String INPUT_TYPE_CHECKBOX = "checkbox";
    public static final String UID_FIRST_SEPARATOR_SYMBOL = "*";
    public static final String UID_SECOND_SEPARATOR_SYMBOL = "?";
    public static final String METRIC_INTERNATIONAL = "International";

    public static final String RATING_ABOVE_FOUR_NAME = "4 Keatas";
    public static final String RATING_ABOVE_FOUR_VALUE = "4,5";

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("key")
    @Expose
    String key;
    @SerializedName("value")
    @Expose
    String value;
    @SerializedName(value="input_type", alternate={"inputType"})
    @Expose
    String inputType;
    @SerializedName(value="hex_color", alternate={"hexColor"})
    @Expose
    String hexColor;
    @SerializedName("metric")
    @Expose
    String metric = "";
    @SerializedName(value="total_data", alternate={"totalData"})
    @Expose
    String totalData;
    @SerializedName(value="val_min", alternate={"valMin"})
    @Expose
    String valMin;
    @SerializedName(value="val_max", alternate={"valMax"})
    @Expose
    String valMax;
    @SerializedName("icon")
    @Expose
    String iconUrl;
    @SerializedName(value="description", alternate={"Description"})
    @Expose
    String description;
    @SerializedName(value="is_popular", alternate={"isPopular"})
    @Expose
    boolean isPopular;
    @SerializedName(value="is_new", alternate={"isNew"})
    @Expose
    boolean isNew;
    @SerializedName("child")
    @Expose
    List<LevelTwoCategory> levelTwoCategoryList;

    String inputState = "";

    public boolean isAnnotation() {
        return Option.KEY_ANNOTATION_ID.equals(getKey());
    }

    public boolean isCategoryOption() {
        return Option.KEY_CATEGORY.equals(getKey());
    }

    public boolean isOfficialOption() {
        return Option.KEY_OFFICIAL.equals(getKey());
    }

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputState() {
        return inputState;
    }

    public void setInputState(String inputState) {
        this.inputState = inputState;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public List<LevelTwoCategory> getLevelTwoCategoryList() {
        return levelTwoCategoryList;
    }

    public void setLevelTwoCategoryList(List<LevelTwoCategory> levelTwoCategoryList) {
        this.levelTwoCategoryList = levelTwoCategoryList;
    }

    public String getUniqueId() {
        return key + UID_FIRST_SEPARATOR_SYMBOL + value + UID_SECOND_SEPARATOR_SYMBOL + name;
    }

    public Option() {}

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;

        if(obj == null || obj.getClass()!= this.getClass()) return false;

        Option option = (Option) obj;

        return this.key.equals(option.key)
                && this.value.equals(option.value)
                && this.name.equals(option.name);
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
        dest.writeString(this.hexColor);
        dest.writeString(this.metric);
        dest.writeString(this.totalData);
        dest.writeString(this.valMin);
        dest.writeString(this.valMax);
        dest.writeString(this.iconUrl);
        dest.writeString(this.description);
        dest.writeByte(this.isPopular ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
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
        this.valMin = in.readString();
        this.valMax = in.readString();
        this.iconUrl = in.readString();
        this.description = in.readString();
        this.isPopular = in.readByte() != 0;
        this.isNew = in.readByte() != 0;
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
