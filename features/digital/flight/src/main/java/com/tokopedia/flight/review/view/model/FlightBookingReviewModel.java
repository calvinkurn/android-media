package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingVoucherViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightBookingReviewModel implements Parcelable {

    private List<FlightBookingPassengerViewModel> detailPassengersData;
    private String id;
    private FlightDetailViewModel detailViewModelListDeparture;
    private FlightDetailViewModel detailViewModelListReturn;
    private List<FlightDetailPassenger> detailPassengers;
    private List<SimpleViewModel> flightReviewFares;
    private String dateFinishTime;
    private String totalPrice;
    private int totalPriceNumeric;
    private CountryPhoneCode phoneCode;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private List<NewFarePrice> farePrices;
    private int adult;
    private int children;
    private int infant;
    private String returnTripId;
    private FlightClassViewModel flightClass;
    private String departureDate;
    private String returnDate;
    private String departureTripId;
    private FlightBookingVoucherViewModel voucherViewModel;
    private List<FlightInsuranceViewModel> insuranceIds;

    public FlightBookingReviewModel(FlightBookingParamViewModel flightBookingParamViewModel,
                                    FlightBookingCartData flightBookingCartData,
                                    String departureTripId,
                                    String returnTripId,
                                    String luggagePrefix,
                                    String mealPrefix,
                                    String birthdatePrefix,
                                    String passportNumberPrefix) {
        setId(flightBookingParamViewModel.getId());
        setDetailViewModelListDeparture(flightBookingCartData.getDepartureTrip());
        setDetailViewModelListReturn(flightBookingCartData.getReturnTrip());
        setDetailPassengers(
                generateFlightDetailPassenger(
                        flightBookingParamViewModel.getPassengerViewModels(),
                        luggagePrefix,
                        mealPrefix,
                        birthdatePrefix,
                        passportNumberPrefix
                )
        );
        setFlightReviewFares(flightBookingParamViewModel.getPriceListDetails());
        setTotalPrice(flightBookingParamViewModel.getTotalPriceFmt());
        setTotalPriceNumeric(flightBookingParamViewModel.getTotalPriceNumeric());
        setDateFinishTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT, flightBookingParamViewModel.getOrderDueTimestamp()));
        setDetailPassengersData(flightBookingParamViewModel.getPassengerViewModels());
        setPhoneCode(flightBookingParamViewModel.getPhoneCode());
        setContactName(flightBookingParamViewModel.getContactName());
        setContactEmail(flightBookingParamViewModel.getContactEmail());
        setContactPhone(flightBookingParamViewModel.getContactPhone());
        setFarePrices(flightBookingCartData.getNewFarePrices());
        setAdult(flightBookingParamViewModel.getSearchParam().getFlightPassengerViewModel().getAdult());
        setChildren(flightBookingParamViewModel.getSearchParam().getFlightPassengerViewModel().getChildren());
        setInfant(flightBookingParamViewModel.getSearchParam().getFlightPassengerViewModel().getInfant());
        setReturnTripId(returnTripId);
        setDepartureTripId(departureTripId);
        setFlightClass(flightBookingParamViewModel.getSearchParam().getFlightClass());
        setDepartureDate(flightBookingParamViewModel.getSearchParam().getDepartureDate());
        setReturnDate(flightBookingParamViewModel.getSearchParam().getReturnDate());
        setVoucherViewModel(flightBookingCartData.getVoucherViewModel());
        setInsuranceIds(flightBookingParamViewModel.getInsurances());
    }

    protected FlightBookingReviewModel(Parcel in) {
        detailPassengersData = in.createTypedArrayList(FlightBookingPassengerViewModel.CREATOR);
        id = in.readString();
        detailViewModelListDeparture = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        detailViewModelListReturn = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        detailPassengers = in.createTypedArrayList(FlightDetailPassenger.CREATOR);
        flightReviewFares = in.createTypedArrayList(SimpleViewModel.CREATOR);
        dateFinishTime = in.readString();
        totalPrice = in.readString();
        totalPriceNumeric = in.readInt();
        phoneCode = in.readParcelable(CountryPhoneCode.class.getClassLoader());
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhone = in.readString();
        farePrices = in.createTypedArrayList(NewFarePrice.CREATOR);
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
        returnTripId = in.readString();
        flightClass = in.readParcelable(FlightClassViewModel.class.getClassLoader());
        departureDate = in.readString();
        returnDate = in.readString();
        departureTripId = in.readString();
        voucherViewModel = in.readParcelable(FlightBookingVoucherViewModel.class.getClassLoader());
        insuranceIds = in.createTypedArrayList(FlightInsuranceViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(detailPassengersData);
        dest.writeString(id);
        dest.writeParcelable(detailViewModelListDeparture, flags);
        dest.writeParcelable(detailViewModelListReturn, flags);
        dest.writeTypedList(detailPassengers);
        dest.writeTypedList(flightReviewFares);
        dest.writeString(dateFinishTime);
        dest.writeString(totalPrice);
        dest.writeInt(totalPriceNumeric);
        dest.writeParcelable(phoneCode, flags);
        dest.writeString(contactName);
        dest.writeString(contactEmail);
        dest.writeString(contactPhone);
        dest.writeTypedList(farePrices);
        dest.writeInt(adult);
        dest.writeInt(children);
        dest.writeInt(infant);
        dest.writeString(returnTripId);
        dest.writeParcelable(flightClass, flags);
        dest.writeString(departureDate);
        dest.writeString(returnDate);
        dest.writeString(departureTripId);
        dest.writeParcelable(voucherViewModel, flags);
        dest.writeTypedList(insuranceIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightBookingReviewModel> CREATOR = new Creator<FlightBookingReviewModel>() {
        @Override
        public FlightBookingReviewModel createFromParcel(Parcel in) {
            return new FlightBookingReviewModel(in);
        }

        @Override
        public FlightBookingReviewModel[] newArray(int size) {
            return new FlightBookingReviewModel[size];
        }
    };

    public void setInsuranceIds(List<FlightInsuranceViewModel> insuranceIds) {
        this.insuranceIds = insuranceIds;
    }

    public List<FlightInsuranceViewModel> getInsuranceIds() {
        return insuranceIds;
    }

    public CountryPhoneCode getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(CountryPhoneCode phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getTotalPriceNumeric() {
        return totalPriceNumeric;
    }

    public void setTotalPriceNumeric(int totalPriceNumeric) {
        this.totalPriceNumeric = totalPriceNumeric;
    }

    public List<FlightBookingPassengerViewModel> getDetailPassengersData() {
        return detailPassengersData;
    }

    public void setDetailPassengersData(List<FlightBookingPassengerViewModel> detailPassengersData) {
        this.detailPassengersData = detailPassengersData;
    }

    public Date getDateFinishTime() {
        return FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT, dateFinishTime);
    }

    public void setDateFinishTime(Date dateFinishTime) {
        this.dateFinishTime = FlightDateUtil.dateToString(dateFinishTime, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlightDetailViewModel getDetailViewModelListDeparture() {
        return detailViewModelListDeparture;
    }

    public void setDetailViewModelListDeparture(FlightDetailViewModel detailViewModelListDeparture) {
        this.detailViewModelListDeparture = detailViewModelListDeparture;
    }

    public FlightDetailViewModel getDetailViewModelListReturn() {
        return detailViewModelListReturn;
    }

    public void setDetailViewModelListReturn(FlightDetailViewModel detailViewModelListReturn) {
        this.detailViewModelListReturn = detailViewModelListReturn;
    }

    public List<FlightDetailPassenger> getDetailPassengers() {
        return detailPassengers;
    }

    public void setDetailPassengers(List<FlightDetailPassenger> detailPassengers) {
        this.detailPassengers = detailPassengers;
    }

    public List<SimpleViewModel> getFlightReviewFares() {
        return flightReviewFares;
    }

    public void setFlightReviewFares(List<SimpleViewModel> flightReviewFares) {
        this.flightReviewFares = flightReviewFares;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<NewFarePrice> getFarePrices() {
        return farePrices;
    }

    public void setFarePrices(List<NewFarePrice> farePrices) {
        this.farePrices = farePrices;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    public String getReturnTripId() {
        return returnTripId;
    }

    public void setReturnTripId(String returnTripId) {
        this.returnTripId = returnTripId;
    }

    public FlightClassViewModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassViewModel flightClass) {
        this.flightClass = flightClass;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDepartureTripId() {
        return departureTripId;
    }

    public void setDepartureTripId(String departureTripId) {
        this.departureTripId = departureTripId;
    }

    public FlightBookingVoucherViewModel getVoucherViewModel() {
        return voucherViewModel;
    }

    public void setVoucherViewModel(FlightBookingVoucherViewModel voucherViewModel) {
        this.voucherViewModel = voucherViewModel;
    }

    private List<FlightDetailPassenger> generateFlightDetailPassenger(List<FlightBookingPassengerViewModel> passengerViewModels,
                                                                      String luggagePrefix,
                                                                      String mealPrefix,
                                                                      String birthdatePrefix,
                                                                      String passportNumberPrefix) {
        List<FlightDetailPassenger> flightDetailPassengers = new ArrayList<>();
        for (FlightBookingPassengerViewModel flightBookingPassengerViewModel : passengerViewModels) {
            FlightDetailPassenger flightDetailPassenger = new FlightDetailPassenger();
            flightDetailPassenger.setPassengerName(flightBookingPassengerViewModel.getPassengerTitle() + " " + flightBookingPassengerViewModel.getPassengerFirstName() + " " + flightBookingPassengerViewModel.getPassengerLastName());
            flightDetailPassenger.setPassengerType(flightBookingPassengerViewModel.getType());
            flightDetailPassenger.setInfoPassengerList(
                    generateDetailViewModelPassenger(flightBookingPassengerViewModel.getPassengerBirthdate(),
                            flightBookingPassengerViewModel.getPassportNumber(),
                            flightBookingPassengerViewModel.getFlightBookingLuggageMetaViewModels(),
                            flightBookingPassengerViewModel.getFlightBookingMealMetaViewModels(),
                            luggagePrefix,
                            mealPrefix,
                            birthdatePrefix,
                            passportNumberPrefix
                    )
            );
            flightDetailPassengers.add(flightDetailPassenger);
        }
        return flightDetailPassengers;
    }

    private List<SimpleViewModel> generateDetailViewModelPassenger(String passengerBirthdate,
                                                                   String passportNumber,
                                                                   List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels,
                                                                   List<FlightBookingAmenityMetaViewModel> flightBookingAmenityMetaViewModels,
                                                                   String luggagePrefix,
                                                                   String mealPrefix,
                                                                   String birthdatePrefix,
                                                                   String passportNumberPrefix) {
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();

        // add tanggal lahir
        if (passengerBirthdate != null && !passengerBirthdate.equals(""))
            simpleViewModels.add(new SimpleViewModel(String.valueOf(FlightDateUtil.formatDate(
                    FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, passengerBirthdate)), birthdatePrefix));

        if (passportNumber != null && !passportNumber.equals("")) {
            simpleViewModels.add(new SimpleViewModel(passportNumber, passportNumberPrefix));
        }

        for (FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel : flightBookingLuggageMetaViewModels) {
            SimpleViewModel simpleViewModel = new SimpleViewModel();
            simpleViewModel.setDescription(luggagePrefix + " " + flightBookingLuggageMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelLuggage(flightBookingLuggageMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }

        for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : flightBookingAmenityMetaViewModels) {
            SimpleViewModel simpleViewModel = new SimpleViewModel();
            simpleViewModel.setDescription(mealPrefix + " " + flightBookingAmenityMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelMeal(flightBookingAmenityMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }

        return simpleViewModels;
    }

    private String generateLabelMeal(List<FlightBookingAmenityViewModel> mealViewModels) {
        String labelMeal = "";
        for (FlightBookingAmenityViewModel flightBookingMealViewModel : mealViewModels) {
            labelMeal = labelMeal + flightBookingMealViewModel.getTitle() + "\n";
        }
        return labelMeal;
    }

    private String generateLabelLuggage(List<FlightBookingAmenityViewModel> luggages) {
        String labelLuggage = "";
        for (FlightBookingAmenityViewModel flightBookingLuggageViewModel : luggages) {
            labelLuggage = labelLuggage + flightBookingLuggageViewModel.getTitle() + "\n";
        }
        return labelLuggage;
    }
}
