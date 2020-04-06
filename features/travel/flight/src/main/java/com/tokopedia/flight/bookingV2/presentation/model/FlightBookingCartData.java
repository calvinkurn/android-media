package com.tokopedia.flight.bookingV2.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.bookingV2.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.detail.view.model.FlightDetailModel;
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode;

import java.util.List;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingCartData implements Parcelable {
    private String id;
    private int refreshTime;
    private TravelCountryPhoneCode defaultPhoneCode;
    private FlightDetailModel departureTrip;
    private FlightDetailModel returnTrip;
    private List<FlightBookingAmenityMetaModel> luggageViewModels;
    private List<FlightBookingAmenityMetaModel> mealViewModels;
    private List<NewFarePrice> newFarePrices;
    private boolean isMandatoryDob;

    private FlightBookingVoucherModel voucherViewModel;
    private boolean isDomestic;

    protected FlightBookingCartData(Parcel in) {
        id = in.readString();
        refreshTime = in.readInt();
        defaultPhoneCode = in.readParcelable(TravelCountryPhoneCode.class.getClassLoader());
        departureTrip = in.readParcelable(FlightDetailModel.class.getClassLoader());
        returnTrip = in.readParcelable(FlightDetailModel.class.getClassLoader());
        luggageViewModels = in.createTypedArrayList(FlightBookingAmenityMetaModel.CREATOR);
        mealViewModels = in.createTypedArrayList(FlightBookingAmenityMetaModel.CREATOR);
        newFarePrices = in.createTypedArrayList(NewFarePrice.CREATOR);
        isDomestic = in.readByte() != 0;
        insurances = in.createTypedArrayList(FlightInsuranceModel.CREATOR);
        voucherViewModel = in.readParcelable(FlightBookingVoucherModel.class.getClassLoader());
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

    public List<FlightInsuranceModel> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<FlightInsuranceModel> insurances) {
        this.insurances = insurances;
    }

    private List<FlightInsuranceModel> insurances;

    public FlightBookingCartData() {
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<FlightBookingAmenityMetaModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    public void setLuggageViewModels(List<FlightBookingAmenityMetaModel> luggageViewModels) {
        this.luggageViewModels = luggageViewModels;
    }

    public List<FlightBookingAmenityMetaModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingAmenityMetaModel> mealViewModels) {
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

    public FlightDetailModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(FlightDetailModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public FlightDetailModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(FlightDetailModel returnTrip) {
        this.returnTrip = returnTrip;
    }

    public TravelCountryPhoneCode getDefaultPhoneCode() {
        return defaultPhoneCode;
    }

    public void setDefaultPhoneCode(TravelCountryPhoneCode defaultPhoneCode) {
        this.defaultPhoneCode = defaultPhoneCode;
    }

    public FlightBookingVoucherModel getVoucherViewModel() {
        return voucherViewModel;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public void setVoucherViewModel(FlightBookingVoucherModel voucherViewModel) {
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
