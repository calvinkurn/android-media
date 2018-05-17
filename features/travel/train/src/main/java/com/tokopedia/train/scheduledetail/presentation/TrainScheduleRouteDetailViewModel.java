package com.tokopedia.train.scheduledetail.presentation;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 14/05/18.
 */
public class TrainScheduleRouteDetailViewModel implements Parcelable, Visitable<TrainScheduleRouteDetailTypeFactory> {

    @Override
    public int type(TrainScheduleRouteDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<TrainScheduleRouteDetailViewModel> CREATOR = new Creator<TrainScheduleRouteDetailViewModel>() {
        @Override
        public TrainScheduleRouteDetailViewModel createFromParcel(Parcel in) {
            return new TrainScheduleRouteDetailViewModel(in);
        }

        @Override
        public TrainScheduleRouteDetailViewModel[] newArray(int size) {
            return new TrainScheduleRouteDetailViewModel[size];
        }
    };

    private TrainScheduleRouteDetailViewModel(Parcel in) {

    }

}
