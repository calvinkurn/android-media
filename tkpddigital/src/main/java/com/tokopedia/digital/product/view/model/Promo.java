package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Promo implements Parcelable {

    @SerializedName("bonus_text")
    @Expose
    private String bonusText;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("new_price")
    @Expose
    private String newPrice;

    @SerializedName("new_price_plain")
    @Expose
    private long newPricePlain;

    @SerializedName("tag")
    @Expose
    private String tag;

    @SerializedName("terms")
    @Expose
    private String terms;

    @SerializedName("value_text")
    @Expose
    private String valueText;

    public String getBonusText() {
        return bonusText;
    }

    public void setBonusText(String bonusText) {
        this.bonusText = bonusText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public long getNewPricePlain() {
        return newPricePlain;
    }

    public void setNewPricePlain(long newPricePlain) {
        this.newPricePlain = newPricePlain;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bonusText);
        dest.writeString(this.id);
        dest.writeString(this.newPrice);
        dest.writeLong(this.newPricePlain);
        dest.writeString(this.tag);
        dest.writeString(this.terms);
        dest.writeString(this.valueText);
    }

    public Promo() {
    }

    protected Promo(Parcel in) {
        this.bonusText = in.readString();
        this.id = in.readString();
        this.newPrice = in.readString();
        this.newPricePlain = in.readLong();
        this.tag = in.readString();
        this.terms = in.readString();
        this.valueText = in.readString();
    }

    public static final Parcelable.Creator<Promo> CREATOR = new Parcelable.Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel source) {
            return new Promo(source);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };
}
