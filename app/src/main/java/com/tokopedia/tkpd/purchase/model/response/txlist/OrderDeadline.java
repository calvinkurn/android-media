package com.tokopedia.tkpd.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderDeadline implements Parcelable {
    private static final String TAG = OrderDeadline.class.getSimpleName();

    @SerializedName("deadline_finish_day_left")
    @Expose
    private String deadlineFinishDayLeft;
    @SerializedName("deadline_process_day_left")
    @Expose
    private String deadlineProcessDayLeft;
    @SerializedName("deadline_po_process_day_left")
    @Expose
    private String deadlinePoProcessDayLeft;
    @SerializedName("deadline_finish_date")
    @Expose
    private String deadlineFinishDate;
    @SerializedName("deadline_shipping_day_left")
    @Expose
    private String deadlineShippingDayLeft;

    public String getDeadlineFinishDayLeft() {
        return deadlineFinishDayLeft;
    }

    public void setDeadlineFinishDayLeft(String deadlineFinishDayLeft) {
        this.deadlineFinishDayLeft = deadlineFinishDayLeft;
    }

    public String getDeadlineProcessDayLeft() {
        return deadlineProcessDayLeft;
    }

    public void setDeadlineProcessDayLeft(String deadlineProcessDayLeft) {
        this.deadlineProcessDayLeft = deadlineProcessDayLeft;
    }

    public String getDeadlinePoProcessDayLeft() {
        return deadlinePoProcessDayLeft;
    }

    public void setDeadlinePoProcessDayLeft(String deadlinePoProcessDayLeft) {
        this.deadlinePoProcessDayLeft = deadlinePoProcessDayLeft;
    }

    public String getDeadlineFinishDate() {
        return deadlineFinishDate;
    }

    public void setDeadlineFinishDate(String deadlineFinishDate) {
        this.deadlineFinishDate = deadlineFinishDate;
    }

    public String getDeadlineShippingDayLeft() {
        return deadlineShippingDayLeft;
    }

    public void setDeadlineShippingDayLeft(String deadlineShippingDayLeft) {
        this.deadlineShippingDayLeft = deadlineShippingDayLeft;
    }

    protected OrderDeadline(Parcel in) {
        deadlineFinishDayLeft = in.readString();
        deadlineProcessDayLeft = in.readString();
        deadlinePoProcessDayLeft = in.readString();
        deadlineFinishDate = in.readString();
        deadlineShippingDayLeft = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deadlineFinishDayLeft);
        dest.writeString(deadlineProcessDayLeft);
        dest.writeString(deadlinePoProcessDayLeft);
        dest.writeString(deadlineFinishDate);
        dest.writeString(deadlineShippingDayLeft);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderDeadline> CREATOR = new Parcelable.Creator<OrderDeadline>() {
        @Override
        public OrderDeadline createFromParcel(Parcel in) {
            return new OrderDeadline(in);
        }

        @Override
        public OrderDeadline[] newArray(int size) {
            return new OrderDeadline[size];
        }
    };
}
