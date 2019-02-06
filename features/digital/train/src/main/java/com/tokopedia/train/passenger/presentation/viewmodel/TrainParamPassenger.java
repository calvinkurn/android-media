package com.tokopedia.train.passenger.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TrainParamPassenger implements Parcelable {

    private List<TrainPassengerViewModel> trainPassengerViewModelList;

    public TrainParamPassenger() {
    }


    protected TrainParamPassenger(Parcel in) {
        trainPassengerViewModelList = in.createTypedArrayList(TrainPassengerViewModel.CREATOR);
    }

    public static final Creator<TrainParamPassenger> CREATOR = new Creator<TrainParamPassenger>() {
        @Override
        public TrainParamPassenger createFromParcel(Parcel in) {
            return new TrainParamPassenger(in);
        }

        @Override
        public TrainParamPassenger[] newArray(int size) {
            return new TrainParamPassenger[size];
        }
    };

    public List<TrainPassengerViewModel> getTrainPassengerViewModelList() {
        return trainPassengerViewModelList;
    }

    public void setTrainPassengerViewModelList(List<TrainPassengerViewModel> trainPassengerViewModelList) {
        this.trainPassengerViewModelList = trainPassengerViewModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(trainPassengerViewModelList);
    }
}
