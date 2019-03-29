package com.tokopedia.posapp.payment.cardscanner.view.viewmodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by okasurya on 8/18/17.
 */

public class CreditCardViewModel implements Parcelable {
    private String cardNumber;
    private String formattedCardNumber;
    private String redactedCardNumber;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String cvv;
    private String postalCode;
    private String cardholderName;
    private String cardType;
    private int cvvLength;
    private Bitmap imageBitmap;
    private boolean expiryValid;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFormattedCardNumber() {
        return formattedCardNumber;
    }

    public void setFormattedCardNumber(String formattedCardNumber) {
        this.formattedCardNumber = formattedCardNumber;
    }

    public String getRedactedCardNumber() {
        return redactedCardNumber;
    }

    public void setRedactedCardNumber(String redactedCardNumber) {
        this.redactedCardNumber = redactedCardNumber;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCvvLength() {
        return cvvLength;
    }

    public void setCvvLength(int cvvLength) {
        this.cvvLength = cvvLength;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public boolean isExpiryValid() {
        return expiryValid;
    }

    public void setExpiryValid(boolean expiryValid) {
        this.expiryValid = expiryValid;
    }

    public String getFormattedExpiryMonth() {
        return String.format("%02d", expiryMonth);
    }

    public String getFormattedExpiryYear() {
        String year = "";
        try {
            year = String.valueOf(expiryYear);
            if (year.length() == 4) {
                year = year.substring(2, 4);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return year;
    }

    public CreditCardViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardNumber);
        dest.writeString(this.formattedCardNumber);
        dest.writeString(this.redactedCardNumber);
        dest.writeInt(this.expiryMonth);
        dest.writeInt(this.expiryYear);
        dest.writeString(this.cvv);
        dest.writeString(this.postalCode);
        dest.writeString(this.cardholderName);
        dest.writeString(this.cardType);
        dest.writeInt(this.cvvLength);
        dest.writeParcelable(this.imageBitmap, flags);
        dest.writeByte(this.expiryValid ? (byte) 1 : (byte) 0);
    }

    protected CreditCardViewModel(Parcel in) {
        this.cardNumber = in.readString();
        this.formattedCardNumber = in.readString();
        this.redactedCardNumber = in.readString();
        this.expiryMonth = in.readInt();
        this.expiryYear = in.readInt();
        this.cvv = in.readString();
        this.postalCode = in.readString();
        this.cardholderName = in.readString();
        this.cardType = in.readString();
        this.cvvLength = in.readInt();
        this.imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.expiryValid = in.readByte() != 0;
    }

    public static final Creator<CreditCardViewModel> CREATOR = new Creator<CreditCardViewModel>() {
        @Override
        public CreditCardViewModel createFromParcel(Parcel source) {
            return new CreditCardViewModel(source);
        }

        @Override
        public CreditCardViewModel[] newArray(int size) {
            return new CreditCardViewModel[size];
        }
    };

    @Override
    public String toString() {
        String s = "{" + cardType + ": " + redactedCardNumber;
        if (expiryMonth > 0 || expiryYear > 0) {
            s += "  expiry:" + expiryMonth + "/" + expiryYear;
        }
        if (postalCode != null) {
            s += "  postalCode:" + postalCode;
        }
        if (cardholderName != null) {
            s += "  cardholderName:" + cardholderName;
        }
        if (cvv != null) {
            s += "  cvvLength:" + ((cvv != null) ? cvv.length() : 0);
        }
        s += "}";
        return s;
    }
}
