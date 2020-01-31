package com.tokopedia.flight.bookingV2.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.bookingV2.presentation.adapter.FlightBookingPassengerTypeFactory;
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode;

import java.util.List;

/**
 * @author by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerViewModel implements Parcelable, Visitable<FlightBookingPassengerTypeFactory> {
    private int passengerLocalId; //passengerLocalNumber
    private int type;
    private String passengerId;
    private String passengerTitle;
    private String headerTitle;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerBirthdate;
    private List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels;
    private List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels;
    private int passengerTitleId;
    private String passportNumber;
    private String passportExpiredDate;
    private TravelCountryPhoneCode passportNationality;
    private TravelCountryPhoneCode passportIssuerCountry;

    public FlightBookingPassengerViewModel() {
    }

    protected FlightBookingPassengerViewModel(Parcel in) {
        passengerLocalId = in.readInt();
        type = in.readInt();
        passengerId = in.readString();
        passengerTitle = in.readString();
        headerTitle = in.readString();
        passengerFirstName = in.readString();
        passengerLastName = in.readString();
        passengerBirthdate = in.readString();
        flightBookingLuggageMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        flightBookingMealMetaViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        passengerTitleId = in.readInt();
        passportNumber = in.readString();
        passportExpiredDate = in.readString();
        passportNationality = in.readParcelable(TravelCountryPhoneCode.class.getClassLoader());
        passportIssuerCountry = in.readParcelable(TravelCountryPhoneCode.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(passengerLocalId);
        dest.writeInt(type);
        dest.writeString(passengerId);
        dest.writeString(passengerTitle);
        dest.writeString(headerTitle);
        dest.writeString(passengerFirstName);
        dest.writeString(passengerLastName);
        dest.writeString(passengerBirthdate);
        dest.writeTypedList(flightBookingLuggageMetaViewModels);
        dest.writeTypedList(flightBookingMealMetaViewModels);
        dest.writeInt(passengerTitleId);
        dest.writeString(passportNumber);
        dest.writeString(passportExpiredDate);
        dest.writeParcelable(passportNationality, flags);
        dest.writeParcelable(passportIssuerCountry, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightBookingPassengerViewModel> CREATOR = new Creator<FlightBookingPassengerViewModel>() {
        @Override
        public FlightBookingPassengerViewModel createFromParcel(Parcel in) {
            return new FlightBookingPassengerViewModel(in);
        }

        @Override
        public FlightBookingPassengerViewModel[] newArray(int size) {
            return new FlightBookingPassengerViewModel[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
    }

    public String getPassengerBirthdate() {
        return passengerBirthdate;
    }

    public void setPassengerBirthdate(String passengerBirthdate) {
        this.passengerBirthdate = passengerBirthdate;
    }

    public List<FlightBookingAmenityMetaViewModel> getFlightBookingMealMetaViewModels() {
        return flightBookingMealMetaViewModels;
    }

    public void setFlightBookingMealMetaViewModels(List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels) {
        this.flightBookingMealMetaViewModels = flightBookingMealMetaViewModels;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingPassengerViewModel && ((FlightBookingPassengerViewModel) obj).getPassengerLocalId() == passengerLocalId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime * passengerLocalId * type;
        return result;
    }

    public int getPassengerLocalId() {
        return passengerLocalId;
    }

    public void setPassengerLocalId(int passengerLocalId) {
        this.passengerLocalId = passengerLocalId;
    }

    public List<FlightBookingAmenityMetaViewModel> getFlightBookingLuggageMetaViewModels() {
        return flightBookingLuggageMetaViewModels;
    }

    public void setFlightBookingLuggageMetaViewModels(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels) {
        this.flightBookingLuggageMetaViewModels = flightBookingLuggageMetaViewModels;
    }

    public String getPassengerTitle() {
        return passengerTitle;
    }

    public void setPassengerTitle(String passengerTitle) {
        this.passengerTitle = passengerTitle;
    }

    public int getPassengerTitleId() {
        return passengerTitleId;
    }

    public void setPassengerTitleId(int passengerTitleId) {
        this.passengerTitleId = passengerTitleId;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportExpiredDate() {
        return passportExpiredDate;
    }

    public void setPassportExpiredDate(String passportExpiredDate) {
        this.passportExpiredDate = passportExpiredDate;
    }

    public TravelCountryPhoneCode getPassportNationality() {
        return passportNationality;
    }

    public void setPassportNationality(TravelCountryPhoneCode passportNationality) {
        this.passportNationality = passportNationality;
    }

    public TravelCountryPhoneCode getPassportIssuerCountry() {
        return passportIssuerCountry;
    }

    public void setPassportIssuerCountry(TravelCountryPhoneCode passportIssuerCountry) {
        this.passportIssuerCountry = passportIssuerCountry;
    }

    @Override
    public int type(FlightBookingPassengerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
