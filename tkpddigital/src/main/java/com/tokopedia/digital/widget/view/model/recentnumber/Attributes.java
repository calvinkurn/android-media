package com.tokopedia.digital.widget.view.model.recentnumber;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Attributes implements Parcelable {

    private String clientNumber;

    protected Attributes(Parcel in) {
        clientNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }
}
