package com.tokopedia.digital.widget.view.model.status;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class Attributes implements Parcelable {

    private Version version;
    private boolean isMaintenance;

    public Attributes() {
    }

    protected Attributes(Parcel in) {
        version = in.readParcelable(Version.class.getClassLoader());
        isMaintenance = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(version, flags);
        dest.writeByte((byte) (isMaintenance ? 1 : 0));
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }
}
