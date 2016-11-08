package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderListData implements Parcelable {
    private static final String TAG = OrderListData.class.getSimpleName();

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private List<OrderData> orderDataList = new ArrayList<>();

    protected OrderListData(Parcel in) {
        paging = in.readParcelable(PagingHandler.PagingHandlerModel.class.getClassLoader());
        orderDataList = in.createTypedArrayList(OrderData.CREATOR);
    }

    public static final Creator<OrderListData> CREATOR = new Creator<OrderListData>() {
        @Override
        public OrderListData createFromParcel(Parcel in) {
            return new OrderListData(in);
        }

        @Override
        public OrderListData[] newArray(int size) {
            return new OrderListData[size];
        }
    };

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    public List<OrderData> getOrderDataList() {
        return orderDataList;
    }

    public void setOrderDataList(List<OrderData> orderDataList) {
        this.orderDataList = orderDataList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(paging, flags);
        dest.writeTypedList(orderDataList);
    }
}
