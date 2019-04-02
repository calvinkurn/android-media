package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 28/06/18.
 */

public class GuideData implements Parcelable {

    private int id;
    private String type;
    private String title;
    private String sourceLink;

    public GuideData() {
    }

    public GuideData(Builder builder) {
        setId(builder.id);
        setType(builder.type);
        setTitle(builder.title);
        setSourceLink(builder.sourceLink);
    }

    protected GuideData(Parcel in) {
        id = in.readInt();
        type = in.readString();
        title = in.readString();
        sourceLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(sourceLink);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public static final class Builder {
        private int id;
        private String type;
        private String title;
        private String sourceLink;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder sourceLink(String val) {
            sourceLink = val;
            return this;
        }

        public GuideData build() {
            return new GuideData(this);
        }
    }
}
