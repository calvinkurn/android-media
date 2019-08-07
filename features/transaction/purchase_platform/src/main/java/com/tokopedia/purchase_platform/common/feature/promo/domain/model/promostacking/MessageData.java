package com.tokopedia.purchase_platform.common.feature.promo.domain.model.promostacking;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fwidjaja on 20/03/19.
 */
public class MessageData implements Parcelable {
    private String state;
    private String color;
    private String text;

    private MessageData(Parcel in) {
        state = in.readString();
        color = in.readString();
        text = in.readString();
    }

    public MessageData() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(state);
        dest.writeString(color);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageData> CREATOR = new Creator<MessageData>() {
        @Override
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        @Override
        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
