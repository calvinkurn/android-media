package com.tokopedia.topads.keyword.data.model.tome;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantByCatModel implements Parcelable {

    public static final int COLOR_ID = 1; // from API

    public static final int STATUS_LEVEL_1 = 2; // from API
    public static final String SIZE_IDENTIFIER_STRING = "size"; // from API

    @SerializedName("variant_id")
    @Expose
    private int variantId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("status") // 2: level 1, 1: level 2
    @Expose
    private int status;
    @SerializedName("has_unit")
    @Expose
    private int hasUnit;
    @SerializedName("units")
    @Expose
    private List<ProductVariantUnit> unitList = null;

    public int getVariantId() {
        return variantId;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getStatus() {
        return status;
    }

    public int getLevel(){
        return status == STATUS_LEVEL_1 ? 1: 2;
    }

    public int getHasUnit() {
        return hasUnit;
    }

    public boolean hasUnit(){
        return hasUnit > 0 && unitList!= null && unitList.size() > 0;
    }

    public boolean isDataColorType(){
        return variantId == COLOR_ID;
    }

    public List<ProductVariantUnit> getUnitList() {
        return unitList;
    }

    public boolean isSizeIdentifier (){
        return identifier.equalsIgnoreCase(SIZE_IDENTIFIER_STRING);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.variantId);
        dest.writeString(this.name);
        dest.writeString(this.identifier);
        dest.writeInt(this.status);
        dest.writeInt(this.hasUnit);
        dest.writeTypedList(this.unitList);
    }

    public ProductVariantByCatModel() {
    }

    protected ProductVariantByCatModel(Parcel in) {
        this.variantId = in.readInt();
        this.name = in.readString();
        this.identifier = in.readString();
        this.status = in.readInt();
        this.hasUnit = in.readInt();
        this.unitList = in.createTypedArrayList(ProductVariantUnit.CREATOR);
    }

    public static final Creator<ProductVariantByCatModel> CREATOR = new Creator<ProductVariantByCatModel>() {
        @Override
        public ProductVariantByCatModel createFromParcel(Parcel source) {
            return new ProductVariantByCatModel(source);
        }

        @Override
        public ProductVariantByCatModel[] newArray(int size) {
            return new ProductVariantByCatModel[size];
        }
    };

}
