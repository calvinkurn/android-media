package com.tokopedia.flight.bookingV2.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel;
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/9/17.
 */

public class FlightBookingParamModel implements Parcelable{
    private String id;
    private String orderDueTimestamp;
    private FlightSearchPassDataModel searchParam;
    private TravelCountryPhoneCode phoneCode;
    private List<FlightBookingPassengerModel> passengerViewModels;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private int totalPriceNumeric;
    private String totalPriceFmt;
    private List<SimpleModel> priceListDetails;
    private List<FlightInsuranceModel> insurances;

    public FlightBookingParamModel() {
        insurances = new ArrayList<>();
    }

    protected FlightBookingParamModel(Parcel in) {
        id = in.readString();
        orderDueTimestamp = in.readString();
        searchParam = in.readParcelable(FlightSearchPassDataModel.class.getClassLoader());
        phoneCode = in.readParcelable(TravelCountryPhoneCode.class.getClassLoader());
        passengerViewModels = in.createTypedArrayList(FlightBookingPassengerModel.CREATOR);
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhone = in.readString();
        totalPriceNumeric = in.readInt();
        totalPriceFmt = in.readString();
        priceListDetails = in.createTypedArrayList(SimpleModel.CREATOR);
        insurances = in.createTypedArrayList(FlightInsuranceModel.CREATOR);
    }

    public static final Creator<FlightBookingParamModel> CREATOR = new Creator<FlightBookingParamModel>() {
        @Override
        public FlightBookingParamModel createFromParcel(Parcel in) {
            return new FlightBookingParamModel(in);
        }

        @Override
        public FlightBookingParamModel[] newArray(int size) {
            return new FlightBookingParamModel[size];
        }
    };

    public TravelCountryPhoneCode getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(TravelCountryPhoneCode phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<FlightBookingPassengerModel> getPassengerViewModels() {
        return passengerViewModels;
    }

    public void setPassengerViewModels(List<FlightBookingPassengerModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    public FlightSearchPassDataModel getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(FlightSearchPassDataModel searchParam) {
        this.searchParam = searchParam;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOrderDueTimestamp(String timestamps) {
        this.orderDueTimestamp = timestamps;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getOrderDueTimestamp() {
        return orderDueTimestamp;
    }

    public int getTotalPriceNumeric() {
        return totalPriceNumeric;
    }

    public void setTotalPriceNumeric(int totalPriceNumeric) {
        this.totalPriceNumeric = totalPriceNumeric;
    }

    public String getTotalPriceFmt() {
        return totalPriceFmt;
    }

    public void setTotalPriceFmt(String totalPriceFmt) {
        this.totalPriceFmt = totalPriceFmt;
    }

    public List<SimpleModel> getPriceListDetails() {
        return priceListDetails;
    }

    public void setPriceListDetails(List<SimpleModel> priceListDetails) {
        this.priceListDetails = priceListDetails;
    }

    public List<FlightInsuranceModel> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<FlightInsuranceModel> insurances) {
        this.insurances = insurances;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(orderDueTimestamp);
        parcel.writeParcelable(searchParam, i);
        parcel.writeParcelable(phoneCode, i);
        parcel.writeTypedList(passengerViewModels);
        parcel.writeString(contactName);
        parcel.writeString(contactEmail);
        parcel.writeString(contactPhone);
        parcel.writeInt(totalPriceNumeric);
        parcel.writeString(totalPriceFmt);
        parcel.writeTypedList(priceListDetails);
        parcel.writeTypedList(insurances);
    }
}
