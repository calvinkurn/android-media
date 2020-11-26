package com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class ListCourierUiModel implements Parcelable {

    private List<CourierUiModel> courierUiModelList;

    public ListCourierUiModel() {
    }

    protected ListCourierUiModel(Parcel in) {
        courierUiModelList = in.createTypedArrayList(CourierUiModel.CREATOR);
    }

    public static final Creator<ListCourierUiModel> CREATOR = new Creator<ListCourierUiModel>() {
        @Override
        public ListCourierUiModel createFromParcel(Parcel in) {
            return new ListCourierUiModel(in);
        }

        @Override
        public ListCourierUiModel[] newArray(int size) {
            return new ListCourierUiModel[size];
        }
    };

    public List<CourierUiModel> getCourierUiModelList() {
        return courierUiModelList;
    }

    public void setCourierUiModelList(List<CourierUiModel> courierUiModelList) {
        this.courierUiModelList = courierUiModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(courierUiModelList);
    }
}
