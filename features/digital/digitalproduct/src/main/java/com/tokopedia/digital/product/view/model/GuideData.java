package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 28/06/18.
 */

public class GuideData implements Parcelable {

    private int id;
    private String type;
    private GuideAttributeData attribute;

    protected GuideData(Parcel in) {
        id = in.readInt();
        type = in.readString();
        attribute = in.readParcelable(GuideAttributeData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeParcelable(attribute, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuideData> CREATOR = new Creator<GuideData>() {
        @Override
        public GuideData createFromParcel(Parcel in) {
            return new GuideData(in);
        }

        @Override
        public GuideData[] newArray(int size) {
            return new GuideData[size];
        }
    };
}
