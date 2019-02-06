package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.List;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingCartData implements Parcelable {
    private String id;
    private int refreshTime;
    private FlightBookingPhoneCodeViewModel defaultPhoneCode;
    private FlightDetailViewModel departureTrip;
    private FlightDetailViewModel returnTrip;
    private List<FlightBookingAmenityMetaViewModel> luggageViewModels;
    private List<FlightBookingAmenityMetaViewModel> mealViewModels;
    private List<NewFarePrice> newFarePrices;
    private boolean isMandatoryDob;

    private FlightBookingVoucherViewModel voucherViewModel;
    private boolean isDomestic;

    protected FlightBookingCartData(Parcel in) {
        id = in.readString();
        refreshTime = in.readInt();
        defaultPhoneCode = in.readParcelable(FlightBookingPhoneCodeViewModel.class.getClassLoader());
        departureTrip = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        returnTrip = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        luggageViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        mealViewModels = in.createTypedArrayList(FlightBookingAmenityMetaViewModel.CREATOR);
        newFarePrices = in.createTypedArrayList(NewFarePrice.CREATOR);
        isDomestic = in.readByte() != 0;
        insurances = in.createTypedArrayList(FlightInsuranceViewModel.CREATOR);
        voucherViewModel = in.readParcelable(FlightBookingVoucherViewModel.class.getClassLoader());
        isMandatoryDob = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(refreshTime);
        dest.writeParcelable(defaultPhoneCode, flags);
        dest.writeParcelable(departureTrip, flags);
        dest.writeParcelable(returnTrip, flags);
        dest.writeTypedList(luggageViewModels);
        dest.writeTypedList(mealViewModels);
        dest.writeTypedList(newFarePrices);
        dest.writeByte((byte) (isDomestic ? 1 : 0));
        dest.writeTypedList(insurances);
        dest.writeParcelable(voucherViewModel, flags);
        dest.writeByte((byte) (isMandatoryDob ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightBookingCartData> CREATOR = new Creator<FlightBookingCartData>() {
        @Override
        public FlightBookingCartData createFromParcel(Parcel in) {
            return new FlightBookingCartData(in);
        }

        @Override
        public FlightBookingCartData[] newArray(int size) {
            return new FlightBookingCartData[size];
        }
    };

    public List<FlightInsuranceViewModel> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<FlightInsuranceViewModel> insurances) {
        this.insurances = insurances;
    }

    private List<FlightInsuranceViewModel> insurances;

    public FlightBookingCartData() {
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<FlightBookingAmenityMetaViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    public void setLuggageViewModels(List<FlightBookingAmenityMetaViewModel> luggageViewModels) {
        this.luggageViewModels = luggageViewModels;
    }

    public List<FlightBookingAmenityMetaViewModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingAmenityMetaViewModel> mealViewModels) {
        this.mealViewModels = mealViewModels;
    }

    public List<NewFarePrice> getNewFarePrices() {
        return newFarePrices;
    }

    public void setNewFarePrices(List<NewFarePrice> newFarePrices) {
        this.newFarePrices = newFarePrices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlightDetailViewModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(FlightDetailViewModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public FlightDetailViewModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(FlightDetailViewModel returnTrip) {
        this.returnTrip = returnTrip;
    }

    public FlightBookingPhoneCodeViewModel getDefaultPhoneCode() {
        return defaultPhoneCode;
    }

    public void setDefaultPhoneCode(FlightBookingPhoneCodeViewModel defaultPhoneCode) {
        this.defaultPhoneCode = defaultPhoneCode;
    }

    public FlightBookingVoucherViewModel getVoucherViewModel() {
        return voucherViewModel;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public void setVoucherViewModel(FlightBookingVoucherViewModel voucherViewModel) {
        this.voucherViewModel = voucherViewModel;
    }

    public void setDomestic(boolean domestic) {
        isDomestic = domestic;
    }

    public boolean isMandatoryDob() {
        return isMandatoryDob;
    }

    public void setMandatoryDob(boolean mandatoryDob) {
        isMandatoryDob = mandatoryDob;
    }
}
