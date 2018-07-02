package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 02/07/18.
 */

public class GuideAttributeData implements Parcelable {

    private String title;
    private String source_link;

    protected GuideAttributeData(Parcel in) {
        title = in.readString();
        source_link = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(source_link);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GuideAttributeData> CREATOR = new Creator<GuideAttributeData>() {
        @Override
        public GuideAttributeData createFromParcel(Parcel in) {
            return new GuideAttributeData(in);
        }

        @Override
        public GuideAttributeData[] newArray(int size) {
            return new GuideAttributeData[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_link() {
        return source_link;
    }

    public void setSource_link(String source_link) {
        this.source_link = source_link;
    }
}
