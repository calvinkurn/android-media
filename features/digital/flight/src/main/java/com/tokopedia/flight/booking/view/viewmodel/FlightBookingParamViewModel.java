package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/9/17.
 */

public class FlightBookingParamViewModel implements Parcelable{
    private String id;
    private String orderDueTimestamp;
    private FlightSearchPassDataViewModel searchParam;
    private FlightBookingPhoneCodeViewModel phoneCodeViewModel;
    private List<FlightBookingPassengerViewModel> passengerViewModels;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private int totalPriceNumeric;
    private String totalPriceFmt;
    private List<SimpleViewModel> priceListDetails;
    private List<FlightInsuranceViewModel> insurances;

    public FlightBookingParamViewModel() {
        insurances = new ArrayList<>();
    }

    protected FlightBookingParamViewModel(Parcel in) {
        id = in.readString();
        orderDueTimestamp = in.readString();
        searchParam = in.readParcelable(FlightSearchPassDataViewModel.class.getClassLoader());
        phoneCodeViewModel = in.readParcelable(FlightBookingPhoneCodeViewModel.class.getClassLoader());
        passengerViewModels = in.createTypedArrayList(FlightBookingPassengerViewModel.CREATOR);
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhone = in.readString();
        totalPriceNumeric = in.readInt();
        totalPriceFmt = in.readString();
        priceListDetails = in.createTypedArrayList(SimpleViewModel.CREATOR);
        insurances = in.createTypedArrayList(FlightInsuranceViewModel.CREATOR);
    }

    public static final Creator<FlightBookingParamViewModel> CREATOR = new Creator<FlightBookingParamViewModel>() {
        @Override
        public FlightBookingParamViewModel createFromParcel(Parcel in) {
            return new FlightBookingParamViewModel(in);
        }

        @Override
        public FlightBookingParamViewModel[] newArray(int size) {
            return new FlightBookingParamViewModel[size];
        }
    };

    public FlightBookingPhoneCodeViewModel getPhoneCodeViewModel() {
        return phoneCodeViewModel;
    }

    public void setPhoneCodeViewModel(FlightBookingPhoneCodeViewModel phoneCodeViewModel) {
        this.phoneCodeViewModel = phoneCodeViewModel;
    }

    public List<FlightBookingPassengerViewModel> getPassengerViewModels() {
        return passengerViewModels;
    }

    public void setPassengerViewModels(List<FlightBookingPassengerViewModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    public FlightSearchPassDataViewModel getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(FlightSearchPassDataViewModel searchParam) {
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

    public List<SimpleViewModel> getPriceListDetails() {
        return priceListDetails;
    }

    public void setPriceListDetails(List<SimpleViewModel> priceListDetails) {
        this.priceListDetails = priceListDetails;
    }

    public List<FlightInsuranceViewModel> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<FlightInsuranceViewModel> insurances) {
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
        parcel.writeParcelable(phoneCodeViewModel, i);
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
