package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class HistoryClientNumber implements Parcelable {

    private List<OrderClientNumber> recentClientNumberList = new ArrayList<>();
    private OrderClientNumber lastOrderClientNumber;

    private HistoryClientNumber(Builder builder) {
        setRecentClientNumberList(builder.recentClientNumberList);
        setLastOrderClientNumber(builder.lastOrderClientNumber);
    }

    public List<OrderClientNumber> getRecentClientNumberList() {
        return recentClientNumberList;
    }

    public void setRecentClientNumberList(List<OrderClientNumber> recentClientNumberList) {
        this.recentClientNumberList = recentClientNumberList;
    }

    public OrderClientNumber getLastOrderClientNumber() {
        return lastOrderClientNumber;
    }

    public void setLastOrderClientNumber(OrderClientNumber lastOrderClientNumber) {
        this.lastOrderClientNumber = lastOrderClientNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.recentClientNumberList);
        dest.writeParcelable(this.lastOrderClientNumber, flags);
    }

    public HistoryClientNumber() {
    }

    protected HistoryClientNumber(Parcel in) {
        this.recentClientNumberList = in.createTypedArrayList(OrderClientNumber.CREATOR);
        this.lastOrderClientNumber = in.readParcelable(OrderClientNumber.class.getClassLoader());
    }

    public static final Creator<HistoryClientNumber> CREATOR =
            new Creator<HistoryClientNumber>() {
                @Override
                public HistoryClientNumber createFromParcel(Parcel source) {
                    return new HistoryClientNumber(source);
                }

                @Override
                public HistoryClientNumber[] newArray(int size) {
                    return new HistoryClientNumber[size];
                }
            };


    public static final class Builder {
        private List<OrderClientNumber> recentClientNumberList;
        private OrderClientNumber lastOrderClientNumber;

        public Builder() {
        }

        public Builder recentClientNumberList(List<OrderClientNumber> val) {
            recentClientNumberList = val;
            return this;
        }

        public Builder lastOrderClientNumber(OrderClientNumber val) {
            lastOrderClientNumber = val;
            return this;
        }

        public HistoryClientNumber build() {
            return new HistoryClientNumber(this);
        }
    }
}
