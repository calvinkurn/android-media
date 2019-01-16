package com.tokopedia.train.homepage.presentation.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;

/**
 * @author Rizky on 21/02/18.
 */

public class TrainHomepageViewModel implements Parcelable, Cloneable {

    private boolean isOneWay;
    private TrainStationAndCityViewModel originStation;
    private TrainStationAndCityViewModel destinationStation;
    private String departureDate;
    private String departureDateFmt;
    private String returnDate;
    private String returnDateFmt;
    private TrainPassengerViewModel trainPassengerViewModel;
    private String passengerFmt;

    public TrainHomepageViewModel() {
    }

    public TrainHomepageViewModel(boolean isOneWay,
                                  TrainStationAndCityViewModel originStation,
                                  TrainStationAndCityViewModel destinationStation,
                                  String departureDate,
                                  String departureDateFmt,
                                  String returnDate,
                                  String returnDateFmt,
                                  TrainPassengerViewModel trainPassengerViewModel,
                                  String passengerFmt
    ) {
        this.isOneWay = isOneWay;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.trainPassengerViewModel = trainPassengerViewModel;
        this.passengerFmt = passengerFmt;
    }

    protected TrainHomepageViewModel(Parcel in) {
        isOneWay = in.readByte() != 0;
        originStation = in.readParcelable(TrainStationAndCityViewModel.class.getClassLoader());
        destinationStation = in.readParcelable(TrainStationAndCityViewModel.class.getClassLoader());
        departureDate = in.readString();
        departureDateFmt = in.readString();
        returnDate = in.readString();
        returnDateFmt = in.readString();
        trainPassengerViewModel = in.readParcelable(TrainPassengerViewModel.class.getClassLoader());
        passengerFmt = in.readString();
    }

    public static final Creator<TrainHomepageViewModel> CREATOR = new Creator<TrainHomepageViewModel>() {
        @Override
        public TrainHomepageViewModel createFromParcel(Parcel in) {
            return new TrainHomepageViewModel(in);
        }

        @Override
        public TrainHomepageViewModel[] newArray(int size) {
            return new TrainHomepageViewModel[size];
        }
    };

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public TrainStationAndCityViewModel getOriginStation() {
        return originStation;
    }

    public void setOriginStation(TrainStationAndCityViewModel originStation) {
        this.originStation = originStation;
    }

    public TrainStationAndCityViewModel getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(TrainStationAndCityViewModel destinationStation) {
        this.destinationStation = destinationStation;
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

    public CharSequence getDepartureDateFmt() {
        return departureDateFmt;
    }

    public void setDepartureDateFmt(String departureDateFmt) {
        this.departureDateFmt = departureDateFmt;
    }

    public CharSequence getReturnDateFmt() {
        return returnDateFmt;
    }

    public void setReturnDateFmt(String returnDateFmt) {
        this.returnDateFmt = returnDateFmt;
    }

    public TrainPassengerViewModel getTrainPassengerViewModel() {
        return trainPassengerViewModel;
    }

    public void setTrainPassengerViewModel(TrainPassengerViewModel trainPassengerViewModel) {
        this.trainPassengerViewModel = trainPassengerViewModel;
    }

    public String getPassengerFmt() {
        return passengerFmt;
    }

    public void setPassengerFmt(String passengerFmt) {
        this.passengerFmt = passengerFmt;
    }

    public CharSequence getStationTextForView(Context context, boolean isDeparture) {
        TrainStationAndCityViewModel trainStationViewModel = isDeparture ? originStation : destinationStation;

        SpannableStringBuilder text = new SpannableStringBuilder();
        if (TextUtils.isEmpty(trainStationViewModel.getStationCode())) {
            text.append(trainStationViewModel.getCityName());
            return makeBold(context, text);
        } else {
            text.append(trainStationViewModel.getStationCode());
        }
        makeBold(context, text);
        String cityName = trainStationViewModel.getCityName();
        if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(trainStationViewModel.getStationCode())) {
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
                new ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)),
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isOneWay ? 1 : 0));
        parcel.writeParcelable(originStation, i);
        parcel.writeParcelable(destinationStation, i);
        parcel.writeString(departureDate);
        parcel.writeString(departureDateFmt);
        parcel.writeString(returnDate);
        parcel.writeString(returnDateFmt);
        parcel.writeParcelable(trainPassengerViewModel, i);
        parcel.writeString(passengerFmt);
    }

    public static class Builder {
        private String departureDate;
        private String departureDateFmt;
        private String returnDate;
        private String returnDateFmt;
        private boolean isOneWay;
        private TrainPassengerViewModel trainPassengerViewModel;
        private TrainStationAndCityViewModel originStation;
        private TrainStationAndCityViewModel destinationStation;
        private String passengerFmt;

        public Builder() {
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setOriginStation(TrainStationAndCityViewModel originStation) {
            this.originStation = originStation;
            return this;
        }

        public Builder setDestinationStation(TrainStationAndCityViewModel destinationStation) {
            this.destinationStation = destinationStation;
            return this;
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

        public Builder setTrainPassengerViewModel(TrainPassengerViewModel trainPassengerViewModel) {
            this.trainPassengerViewModel = trainPassengerViewModel;
            return this;
        }

        public Builder setPassengerFmt(String passengerFmt) {
            this.passengerFmt = passengerFmt;
            return this;
        }

        public TrainHomepageViewModel build() {
            return new TrainHomepageViewModel(
                    isOneWay,
                    originStation,
                    destinationStation,
                    departureDate,
                    departureDateFmt,
                    returnDate,
                    returnDateFmt,
                    trainPassengerViewModel,
                    passengerFmt
            );
        }

    }

}
