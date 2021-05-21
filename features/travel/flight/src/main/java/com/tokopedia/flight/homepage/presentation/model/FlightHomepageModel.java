package com.tokopedia.flight.homepage.presentation.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import androidx.core.content.ContextCompat;

import com.tokopedia.flight.airport.presentation.model.FlightAirportModel;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightHomepageModel implements Parcelable, Cloneable {
    private String departureDate;
    private String departureDateFmt;
    private String returnDate;
    private String returnDateFmt;
    private boolean isOneWay;
    private FlightPassengerModel flightPassengerViewModel;
    private String flightPassengerFmt;
    private FlightAirportModel departureAirport;
    private String departureAirportFmt;
    private FlightAirportModel arrivalAirport;
    private String arrivalAirportFmt;
    private FlightClassModel flightClass;

    public FlightHomepageModel() {
    }

    public FlightHomepageModel(String departureDate,
                               String departureDateFmt,
                               String returnDate,
                               String returnDateFmt,
                               boolean isOneWay,
                               FlightPassengerModel flightPassengerViewModel,
                               String flightPassengerFmt,
                               FlightAirportModel departureAirport,
                               String departureAirportFmt,
                               FlightAirportModel arrivalAirport,
                               String arrivalAirportFmt,
                               FlightClassModel flightClass) {
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.flightPassengerFmt = flightPassengerFmt;
        this.departureAirport = departureAirport;
        this.departureAirportFmt = departureAirportFmt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalAirportFmt = arrivalAirportFmt;
        this.flightClass = flightClass;
    }

    protected FlightHomepageModel(Parcel in) {
        departureDate = in.readString();
        departureDateFmt = in.readString();
        returnDate = in.readString();
        returnDateFmt = in.readString();
        isOneWay = in.readByte() != 0;
        flightPassengerViewModel = in.readParcelable(FlightPassengerModel.class.getClassLoader());
        flightPassengerFmt = in.readString();
        departureAirport = in.readParcelable(FlightAirportModel.class.getClassLoader());
        departureAirportFmt = in.readString();
        arrivalAirport = in.readParcelable(FlightAirportModel.class.getClassLoader());
        arrivalAirportFmt = in.readString();
        flightClass = in.readParcelable(FlightClassModel.class.getClassLoader());
    }

    public static final Creator<FlightHomepageModel> CREATOR = new Creator<FlightHomepageModel>() {
        @Override
        public FlightHomepageModel createFromParcel(Parcel in) {
            return new FlightHomepageModel(in);
        }

        @Override
        public FlightHomepageModel[] newArray(int size) {
            return new FlightHomepageModel[size];
        }
    };

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

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public FlightPassengerModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightPassengerModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
    }

    public FlightAirportModel getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportModel departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportModel getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportModel arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public CharSequence getDepartureDateFmt() {
        return departureDateFmt;
    }

    public CharSequence getReturnDateFmt() {
        return returnDateFmt;
    }

    public CharSequence getPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setDepartureDateFmt(String departureDateFmt) {
        this.departureDateFmt = departureDateFmt;
    }

    public void setReturnDateFmt(String returnDateFmt) {
        this.returnDateFmt = returnDateFmt;
    }

    public String getFlightPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setFlightPassengerFmt(String flightPassengerFmt) {
        this.flightPassengerFmt = flightPassengerFmt;
    }

    public String getDepartureAirportFmt() {
        return departureAirportFmt;
    }

    public CharSequence getAirportTextForView(Context context, boolean isDeparture) {
        FlightAirportModel flightAirportDB = isDeparture ? departureAirport : arrivalAirport;

        SpannableStringBuilder text = new SpannableStringBuilder();
        String depAirportID = flightAirportDB.getAirportCode();
        if (TextUtils.isEmpty(depAirportID)) {
            // id is more than one
            String cityCode = flightAirportDB.getCityCode();
            if (TextUtils.isEmpty(cityCode)) {
                text.append(flightAirportDB.getCityName());
                return makeBold(context, text);
            } else {
                text.append(cityCode);
            }
        } else {
            text.append(depAirportID);
        }
        makeBold(context, text);
        String cityName = flightAirportDB.getCityName();
        if (!TextUtils.isEmpty(cityName)) {
            SpannableStringBuilder cityNameText = new SpannableStringBuilder(cityName);
            makeSmall(cityNameText);
            text.append("\n");
            text.append(cityNameText);
        }
        return text;
    }

    private SpannableStringBuilder makeBold(Context context, SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.25f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)),
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private SpannableStringBuilder makeSmall(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new RelativeSizeSpan(0.75f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    public void setDepartureAirportFmt(String departureAirportFmt) {
        this.departureAirportFmt = departureAirportFmt;
    }

    public String getArrivalAirportFmt() {
        return arrivalAirportFmt;
    }

    public void setArrivalAirportFmt(String arrivalAirportFmt) {
        this.arrivalAirportFmt = arrivalAirportFmt;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Clone FlightDashboardModel");
        }
    }

    public FlightClassModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassModel flightClass) {
        this.flightClass = flightClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(departureDate);
        parcel.writeString(departureDateFmt);
        parcel.writeString(returnDate);
        parcel.writeString(returnDateFmt);
        parcel.writeByte((byte) (isOneWay ? 1 : 0));
        parcel.writeParcelable(flightPassengerViewModel, i);
        parcel.writeString(flightPassengerFmt);
        parcel.writeParcelable(departureAirport, i);
        parcel.writeString(departureAirportFmt);
        parcel.writeParcelable(arrivalAirport, i);
        parcel.writeString(arrivalAirportFmt);
        parcel.writeParcelable(flightClass, i);
    }

    public static class Builder {
        private String departureDate;
        private String departureDateFmt;
        private String returnDate;
        private String returnDateFmt;
        private boolean isOneWay;
        private FlightPassengerModel flightPassengerViewModel;
        private String flightPassengerFmt;
        private FlightAirportModel departureAirport;
        private String departureAirportFmt;
        private FlightAirportModel arrivalAirport;
        private String arrivalAirportFmt;
        private FlightClassModel flightClass;

        public Builder() {
        }

        public Builder setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public Builder setDepartureDateFmt(String departureDate) {
            this.departureDateFmt = departureDate;
            return this;
        }

        public Builder setReturnDate(String returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder setReturnDateFmt(String returnDate) {
            this.returnDateFmt = returnDate;
            return this;
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setFlightPassengerViewModel(FlightPassengerModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setFlightPassengerFmt(String passengerFmt) {
            this.flightPassengerFmt = passengerFmt;
            return this;
        }

        public Builder setDepartureAirport(FlightAirportModel departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder setDepartureAirportFmt(String departureAirportFmt) {
            this.departureAirportFmt = departureAirportFmt;
            return this;
        }

        public Builder setArrivalAirport(FlightAirportModel arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder setArrivalAirportFmt(String arrivalAirportFmt) {
            this.arrivalAirportFmt = arrivalAirportFmt;
            return this;
        }

        public Builder setFlightClass(FlightClassModel flightClass) {
            this.flightClass = flightClass;
            return this;
        }


        public FlightHomepageModel build() {
            return new FlightHomepageModel(departureDate,
                    departureDateFmt,
                    returnDate,
                    returnDateFmt,
                    isOneWay,
                    flightPassengerViewModel,
                    flightPassengerFmt,
                    departureAirport,
                    departureAirportFmt,
                    arrivalAirport,
                    arrivalAirportFmt,
                    flightClass);
        }
    }
}
