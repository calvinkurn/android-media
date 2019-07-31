package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Campaign implements Parcelable {

    private static final String KEY_ORIGINAL_PRICE = "original_price";
    private static final String KEY_DISCOUNT_PERCENTAGE = "discount_percentage";

    @SerializedName(KEY_ORIGINAL_PRICE)
    @Expose
    private String originalPrice = "";

    @SerializedName(KEY_DISCOUNT_PERCENTAGE)
    @Expose
    private int discountPercentage = 0;

    public Campaign() {

    }

    public Campaign(JSONObject object) throws JSONException {
        setOriginalPriceFromJSONObject(object);
        setDiscountPercentageFromJSONObject(object);
    }

    private void setOriginalPriceFromJSONObject(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ORIGINAL_PRICE)) {
            setOriginalPrice(object.getString(KEY_ORIGINAL_PRICE));
        }
    }

    private void setDiscountPercentageFromJSONObject(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_DISCOUNT_PERCENTAGE)) {
            setDiscountPercentage(object.getInt(KEY_DISCOUNT_PERCENTAGE));
        }
    }

    protected Campaign(Parcel in) {
        originalPrice = in.readString();
        discountPercentage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalPrice);
        dest.writeInt(discountPercentage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Campaign> CREATOR = new Creator<Campaign>() {
        @Override
        public Campaign createFromParcel(Parcel in) {
            return new Campaign(in);
        }

        @Override
        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getOriginalPrice() {
        return this.originalPrice;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getDiscountPercentage() {
        return this.discountPercentage;
    }
}
