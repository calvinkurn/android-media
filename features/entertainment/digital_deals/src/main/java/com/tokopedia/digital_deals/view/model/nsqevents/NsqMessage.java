package com.tokopedia.digital_deals.view.model.nsqevents;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqMessage implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private long userId;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("use_case")
    @Expose
    private String useCase;

    @SerializedName("product_id")
    @Expose
    private long productId;

    @SerializedName("custom_message")
    @Expose
    private String message;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected NsqMessage(Parcel in) {
        userId = in.readLong();
        action = in.readString();
        useCase = in.readString();
        productId = in.readLong();
        message = in.readString();
    }

    public NsqMessage() {}

    public static final Creator<NsqMessage> CREATOR = new Creator<NsqMessage>() {
        @Override
        public NsqMessage createFromParcel(Parcel in) {
            return new NsqMessage(in);
        }

        @Override
        public NsqMessage[] newArray(int size) {
            return new NsqMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(action);
        dest.writeString(useCase);
        dest.writeLong(productId);
        dest.writeString(message);
    }
}
