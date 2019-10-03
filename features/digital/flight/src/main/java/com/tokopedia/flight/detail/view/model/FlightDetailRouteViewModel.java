package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteViewModel;

/**
 * @author alvarisi
 */

public class FlightDetailRouteViewModel extends FlightOrderDetailRouteViewModel implements Parcelable, Visitable<FlightDetailRouteTypeFactory> {

    public FlightDetailRouteViewModel() {
    }

    public FlightDetailRouteViewModel(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightDetailRouteViewModel> CREATOR = new Creator<FlightDetailRouteViewModel>() {
        @Override
        public FlightDetailRouteViewModel createFromParcel(Parcel in) {
            return new FlightDetailRouteViewModel(in);
        }

        @Override
        public FlightDetailRouteViewModel[] newArray(int size) {
            return new FlightDetailRouteViewModel[size];
        }
    };

    @Override
    public int type(FlightDetailRouteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
