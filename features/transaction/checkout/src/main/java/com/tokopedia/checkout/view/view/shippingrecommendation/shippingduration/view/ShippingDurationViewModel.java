package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationViewModel implements Parcelable {

    private ServiceData serviceData;
    private List<ShippingCourierViewModel> shippingCourierViewModelList;
    private boolean selected;

    public ShippingDurationViewModel() {
    }

    protected ShippingDurationViewModel(Parcel in) {
        serviceData = in.readParcelable(ServiceData.class.getClassLoader());
        shippingCourierViewModelList = in.createTypedArrayList(ShippingCourierViewModel.CREATOR);
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(serviceData, flags);
        dest.writeTypedList(shippingCourierViewModelList);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingDurationViewModel> CREATOR = new Creator<ShippingDurationViewModel>() {
        @Override
        public ShippingDurationViewModel createFromParcel(Parcel in) {
            return new ShippingDurationViewModel(in);
        }

        @Override
        public ShippingDurationViewModel[] newArray(int size) {
            return new ShippingDurationViewModel[size];
        }
    };

    public ServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ShippingCourierViewModel> getShippingCourierViewModelList() {
        return shippingCourierViewModelList;
    }

    public void setShippingCourierViewModelList(List<ShippingCourierViewModel> shippingCourierViewModelList) {
        this.shippingCourierViewModelList = shippingCourierViewModelList;
    }
}
