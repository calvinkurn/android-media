package com.tokopedia.digital.widget.view.model.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Promo implements Parcelable {

    private String bonusText;
    private String newPrice;
    private long newPricePlain;
    private String tag;
    private String terms;
    private String valueText;

    public Promo() {
    }

    protected Promo(Parcel in) {
        bonusText = in.readString();
        newPrice = in.readString();
        newPricePlain = in.readLong();
        tag = in.readString();
        terms = in.readString();
        valueText = in.readString();
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };

    public String getBonusText() {
        return bonusText;
    }

    public void setBonusText(String bonusText) {
        this.bonusText = bonusText;
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
        dest.writeString(bonusText);
        dest.writeString(newPrice);
        dest.writeLong(newPricePlain);
        dest.writeString(tag);
        dest.writeString(terms);
        dest.writeString(valueText);
    }
}