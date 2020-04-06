package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.bookingV2.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingAmenityMetaModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingAmenityModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingCartData;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingParamModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingPassengerModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingVoucherModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceModel;
import com.tokopedia.flight.bookingV2.presentation.model.SimpleModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel;
import com.tokopedia.flight.detail.view.model.FlightDetailModel;
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightBookingReviewModel implements Parcelable {

    private List<FlightBookingPassengerModel> detailPassengersData;
    private String id;
    private FlightDetailModel detailViewModelListDeparture;
    private FlightDetailModel detailViewModelListReturn;
    private List<FlightDetailPassenger> detailPassengers;
    private List<SimpleModel> flightReviewFares;
    private String dateFinishTime;
    private String totalPrice;
    private int totalPriceNumeric;
    private TravelCountryPhoneCode phoneCode;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private List<NewFarePrice> farePrices;
    private int adult;
    private int children;
    private int infant;
    private String returnTripId;
    private FlightClassModel flightClass;
    private String departureDate;
    private String returnDate;
    private String departureTripId;
    private FlightBookingVoucherModel voucherViewModel;
    private List<FlightInsuranceModel> insuranceIds;
    private String requestId;

    public FlightBookingReviewModel(FlightBookingParamModel flightBookingParamViewModel,
                                    FlightBookingCartData flightBookingCartData,
                                    String departureTripId,
                                    String returnTripId,
                                    String luggagePrefix,
                                    String mealPrefix,
                                    String birthdatePrefix,
                                    String passportNumberPrefix,
                                    String requestId) {
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
        setRequestId(requestId);
    }

    protected FlightBookingReviewModel(Parcel in) {
        detailPassengersData = in.createTypedArrayList(FlightBookingPassengerModel.CREATOR);
        id = in.readString();
        detailViewModelListDeparture = in.readParcelable(FlightDetailModel.class.getClassLoader());
        detailViewModelListReturn = in.readParcelable(FlightDetailModel.class.getClassLoader());
        detailPassengers = in.createTypedArrayList(FlightDetailPassenger.CREATOR);
        flightReviewFares = in.createTypedArrayList(SimpleModel.CREATOR);
        dateFinishTime = in.readString();
        totalPrice = in.readString();
        totalPriceNumeric = in.readInt();
        phoneCode = in.readParcelable(TravelCountryPhoneCode.class.getClassLoader());
        contactName = in.readString();
        contactEmail = in.readString();
        contactPhone = in.readString();
        farePrices = in.createTypedArrayList(NewFarePrice.CREATOR);
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
        returnTripId = in.readString();
        flightClass = in.readParcelable(FlightClassModel.class.getClassLoader());
        departureDate = in.readString();
        returnDate = in.readString();
        departureTripId = in.readString();
        voucherViewModel = in.readParcelable(FlightBookingVoucherModel.class.getClassLoader());
        insuranceIds = in.createTypedArrayList(FlightInsuranceModel.CREATOR);
        requestId = in.readString();
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
        dest.writeString(requestId);
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

    public void setInsuranceIds(List<FlightInsuranceModel> insuranceIds) {
        this.insuranceIds = insuranceIds;
    }

    public List<FlightInsuranceModel> getInsuranceIds() {
        return insuranceIds;
    }

    public TravelCountryPhoneCode getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(TravelCountryPhoneCode phoneCode) {
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

    public List<FlightBookingPassengerModel> getDetailPassengersData() {
        return detailPassengersData;
    }

    public void setDetailPassengersData(List<FlightBookingPassengerModel> detailPassengersData) {
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

    public FlightDetailModel getDetailViewModelListDeparture() {
        return detailViewModelListDeparture;
    }

    public void setDetailViewModelListDeparture(FlightDetailModel detailViewModelListDeparture) {
        this.detailViewModelListDeparture = detailViewModelListDeparture;
    }

    public FlightDetailModel getDetailViewModelListReturn() {
        return detailViewModelListReturn;
    }

    public void setDetailViewModelListReturn(FlightDetailModel detailViewModelListReturn) {
        this.detailViewModelListReturn = detailViewModelListReturn;
    }

    public List<FlightDetailPassenger> getDetailPassengers() {
        return detailPassengers;
    }

    public void setDetailPassengers(List<FlightDetailPassenger> detailPassengers) {
        this.detailPassengers = detailPassengers;
    }

    public List<SimpleModel> getFlightReviewFares() {
        return flightReviewFares;
    }

    public void setFlightReviewFares(List<SimpleModel> flightReviewFares) {
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

    public FlightClassModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassModel flightClass) {
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

    public FlightBookingVoucherModel getVoucherViewModel() {
        return voucherViewModel;
    }

    public void setVoucherViewModel(FlightBookingVoucherModel voucherViewModel) {
        this.voucherViewModel = voucherViewModel;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    private List<FlightDetailPassenger> generateFlightDetailPassenger(List<FlightBookingPassengerModel> passengerViewModels,
                                                                      String luggagePrefix,
                                                                      String mealPrefix,
                                                                      String birthdatePrefix,
                                                                      String passportNumberPrefix) {
        List<FlightDetailPassenger> flightDetailPassengers = new ArrayList<>();
        for (FlightBookingPassengerModel flightBookingPassengerViewModel : passengerViewModels) {
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

    private List<SimpleModel> generateDetailViewModelPassenger(String passengerBirthdate,
                                                               String passportNumber,
                                                               List<FlightBookingAmenityMetaModel> flightBookingLuggageMetaViewModels,
                                                               List<FlightBookingAmenityMetaModel> flightBookingAmenityMetaViewModels,
                                                               String luggagePrefix,
                                                               String mealPrefix,
                                                               String birthdatePrefix,
                                                               String passportNumberPrefix) {
        List<SimpleModel> simpleViewModels = new ArrayList<>();

        // add tanggal lahir
        if (passengerBirthdate != null && !passengerBirthdate.equals(""))
            simpleViewModels.add(new SimpleModel(String.valueOf(FlightDateUtil.formatDate(
                    FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, passengerBirthdate)), birthdatePrefix));

        if (passportNumber != null && !passportNumber.equals("")) {
            simpleViewModels.add(new SimpleModel(passportNumber, passportNumberPrefix));
        }

        for (FlightBookingAmenityMetaModel flightBookingLuggageMetaViewModel : flightBookingLuggageMetaViewModels) {
            SimpleModel simpleViewModel = new SimpleModel();
            simpleViewModel.setDescription(luggagePrefix + " " + flightBookingLuggageMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelLuggage(flightBookingLuggageMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }

        for (FlightBookingAmenityMetaModel flightBookingAmenityMetaViewModel : flightBookingAmenityMetaViewModels) {
            SimpleModel simpleViewModel = new SimpleModel();
            simpleViewModel.setDescription(mealPrefix + " " + flightBookingAmenityMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelMeal(flightBookingAmenityMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }

        return simpleViewModels;
    }

    private String generateLabelMeal(List<FlightBookingAmenityModel> mealViewModels) {
        String labelMeal = "";
        for (FlightBookingAmenityModel flightBookingMealViewModel : mealViewModels) {
            labelMeal = labelMeal + flightBookingMealViewModel.getTitle() + "\n";
        }
        return labelMeal;
    }

    private String generateLabelLuggage(List<FlightBookingAmenityModel> luggages) {
        String labelLuggage = "";
        for (FlightBookingAmenityModel flightBookingLuggageViewModel : luggages) {
            labelLuggage = labelLuggage + flightBookingLuggageViewModel.getTitle() + "\n";
        }
        return labelLuggage;
    }
}
