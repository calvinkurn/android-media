package com.tokopedia.checkout.domain.datamodel.cartmultipleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class SetShippingAddressData implements Parcelable {
    private boolean success;
    private List<String> messages = new ArrayList<>();

    private SetShippingAddressData(Builder builder) {
        success = builder.success;
        messages = builder.messages;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeStringList(messages);
    }

    public SetShippingAddressData() {
    }

    protected SetShippingAddressData(Parcel in) {
        success = in.readByte() != 0;
        messages = in.createStringArrayList();
    }

    public static final Creator<SetShippingAddressData> CREATOR = new Creator<SetShippingAddressData>() {
        @Override
        public SetShippingAddressData createFromParcel(Parcel source) {
            return new SetShippingAddressData(source);
        }

        @Override
        public SetShippingAddressData[] newArray(int size) {
            return new SetShippingAddressData[size];
        }
    };

    public static final class Builder {
        private boolean success;
        private List<String> messages;

        public Builder() {
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public Builder messages(List<String> val) {
            messages = val;
            return this;
        }

        public SetShippingAddressData build() {
            return new SetShippingAddressData(this);
        }
    }
}
