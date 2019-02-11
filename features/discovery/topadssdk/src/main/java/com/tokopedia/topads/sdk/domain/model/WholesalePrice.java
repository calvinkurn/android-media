package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class WholesalePrice implements Parcelable {

    private static final String KEY_QUANTITY_MIN = "quantity_min_format";
    private static final String KEY_QUANTITY_MAX = "quantity_max_format";
    private static final String KEY_PRICE = "price_format";

    @SerializedName(KEY_QUANTITY_MIN)
    private String quantityMinFormat = "";
    @SerializedName(KEY_QUANTITY_MAX)
    private String quantityMaxFormat = "";
    @SerializedName(KEY_PRICE)
    private String priceFormat = "";

    public WholesalePrice(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_QUANTITY_MIN)){
            setQuantityMinFormat(object.getString(KEY_QUANTITY_MIN));
        }
        if(!object.isNull(KEY_QUANTITY_MAX)){
            setQuantityMaxFormat(object.getString(KEY_QUANTITY_MAX));
        }
        if(!object.isNull(KEY_PRICE)){
            setPriceFormat(object.getString(KEY_PRICE));
        }
    }

    protected WholesalePrice(Parcel in) {
        quantityMinFormat = in.readString();
        quantityMaxFormat = in.readString();
        priceFormat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantityMinFormat);
        dest.writeString(quantityMaxFormat);
        dest.writeString(priceFormat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WholesalePrice> CREATOR = new Creator<WholesalePrice>() {
        @Override
        public WholesalePrice createFromParcel(Parcel in) {
            return new WholesalePrice(in);
        }

        @Override
        public WholesalePrice[] newArray(int size) {
            return new WholesalePrice[size];
        }
    };

    public String getQuantityMinFormat() {
        return quantityMinFormat;
    }

    public void setQuantityMinFormat(String quantityMinFormat) {
        this.quantityMinFormat = quantityMinFormat;
    }

    public String getQuantityMaxFormat() {
        return quantityMaxFormat;
    }

    public void setQuantityMaxFormat(String quantityMaxFormat) {
        this.quantityMaxFormat = quantityMaxFormat;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }
}
