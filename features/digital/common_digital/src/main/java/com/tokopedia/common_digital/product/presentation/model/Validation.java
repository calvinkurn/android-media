package com.tokopedia.common_digital.product.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Validation implements Parcelable {

    private String regex;
    private String error;

    public Validation(String regex, String error) {
        this.regex = regex;
        this.error = error;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.regex);
        dest.writeString(this.error);
    }

    public Validation() {
    }

    protected Validation(Parcel in) {
        this.regex = in.readString();
        this.error = in.readString();
    }

    public static final Creator<Validation> CREATOR = new Creator<Validation>() {
        @Override
        public Validation createFromParcel(Parcel source) {
            return new Validation(source);
        }

        @Override
        public Validation[] newArray(int size) {
            return new Validation[size];
        }
    };
}
