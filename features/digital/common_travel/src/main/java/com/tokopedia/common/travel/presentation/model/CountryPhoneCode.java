package com.tokopedia.common.travel.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.common.travel.presentation.adapter.PhoneCodePickerAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class CountryPhoneCode implements Parcelable, Visitable<PhoneCodePickerAdapterTypeFactory> {

    private String countryId;
    private String countryName;
    private String countryPhoneCode;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryId);
        dest.writeString(this.countryName);
        dest.writeString(this.countryPhoneCode);
    }

    public CountryPhoneCode() {
    }

    protected CountryPhoneCode(Parcel in) {
        this.countryId = in.readString();
        this.countryName = in.readString();
        this.countryPhoneCode = in.readString();
    }

    public static final Parcelable.Creator<CountryPhoneCode> CREATOR = new Parcelable.Creator<CountryPhoneCode>() {
        @Override
        public CountryPhoneCode createFromParcel(Parcel source) {
            return new CountryPhoneCode(source);
        }

        @Override
        public CountryPhoneCode[] newArray(int size) {
            return new CountryPhoneCode[size];
        }
    };

    @Override
    public int type(PhoneCodePickerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
