package com.tokopedia.events.domain.model.scanticket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScanTicketResponse implements Parcelable {

    @SerializedName("product")
    @Expose
    private ScanProductDetail product;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("action")
    @Expose
    private List<Action> action = null;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    protected ScanTicketResponse(Parcel in) {
        action = in.createTypedArrayList(Action.CREATOR);
    }

    public static final Creator<ScanTicketResponse> CREATOR = new Creator<ScanTicketResponse>() {
        @Override
        public ScanTicketResponse createFromParcel(Parcel in) {
            return new ScanTicketResponse(in);
        }

        @Override
        public ScanTicketResponse[] newArray(int size) {
            return new ScanTicketResponse[size];
        }
    };

    public ScanProductDetail getProduct() {
        return product;
    }

    public void setProduct(ScanProductDetail product) {
        this.product = product;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Action> getAction() {
        return action;
    }

    public void setAction(List<Action> action) {
        this.action = action;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(action);
    }
}

