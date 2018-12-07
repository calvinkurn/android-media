package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData;

/**
 * @author anggaprasetiyo on 15/02/18.
 */

public class CartPromoSuggestion implements Parcelable, ShipmentData {
    private String cta;
    private String ctaColor;
    private boolean isVisible;
    private String promoCode;
    private String text;

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getCtaColor() {
        return ctaColor;
    }

    public void setCtaColor(String ctaColor) {
        this.ctaColor = ctaColor;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public CartPromoSuggestion() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cta);
        dest.writeString(this.ctaColor);
        dest.writeByte(this.isVisible ? (byte) 1 : (byte) 0);
        dest.writeString(this.promoCode);
        dest.writeString(this.text);
    }

    protected CartPromoSuggestion(Parcel in) {
        this.cta = in.readString();
        this.ctaColor = in.readString();
        this.isVisible = in.readByte() != 0;
        this.promoCode = in.readString();
        this.text = in.readString();
    }

    public static final Creator<CartPromoSuggestion> CREATOR = new Creator<CartPromoSuggestion>() {
        @Override
        public CartPromoSuggestion createFromParcel(Parcel source) {
            return new CartPromoSuggestion(source);
        }

        @Override
        public CartPromoSuggestion[] newArray(int size) {
            return new CartPromoSuggestion[size];
        }
    };
}
