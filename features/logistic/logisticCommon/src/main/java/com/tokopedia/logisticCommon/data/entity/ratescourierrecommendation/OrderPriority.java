package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPriority implements Parcelable {
    @SerializedName("is_now")
    @Expose
    private Boolean isNow;

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    private Integer price;

    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;

    @SerializedName("inactive_message")
    @Expose
    private String inactiveMessage;

    @SerializedName("static_messages")
    @Expose
    private OrderPriorityStaticMessage staticMessage;

    protected OrderPriority(Parcel in) {
        byte tmpIsNow = in.readByte();
        isNow = tmpIsNow == 0 ? null : tmpIsNow == 1;
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        formattedPrice = in.readString();
        inactiveMessage = in.readString();
    }

    public static final Creator<OrderPriority> CREATOR = new Creator<OrderPriority>() {
        @Override
        public OrderPriority createFromParcel(Parcel in) {
            return new OrderPriority(in);
        }

        @Override
        public OrderPriority[] newArray(int size) {
            return new OrderPriority[size];
        }
    };

    public Boolean getNow() {
        return isNow;
    }

    public void setNow(Boolean now) {
        isNow = now;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getInactiveMessage() {
        return inactiveMessage;
    }

    public void setInactiveMessage(String inactiveMessage) {
        this.inactiveMessage = inactiveMessage;
    }

    public OrderPriorityStaticMessage getStaticMessage() {
        return staticMessage;
    }

    public void setStaticMessage(OrderPriorityStaticMessage staticMessage) {
        this.staticMessage = staticMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isNow == null ? 0 : isNow ? 1 : 2));
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(price);
        }
        parcel.writeString(formattedPrice);
        parcel.writeString(inactiveMessage);
    }
}
