package com.tokopedia.digital.categorylist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 7/12/17.
 */

public class DigitalCategoryItemDataError implements Parcelable {
    private String title;
    private String message;
    private String buttonLabel;
    private int resIcon;

    private DigitalCategoryItemDataError(Builder builder) {
        setTitle(builder.title);
        setMessage(builder.message);
        setButtonLabel(builder.buttonLabel);
        setResIcon(builder.resIcon);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.buttonLabel);
        dest.writeInt(this.resIcon);
    }

    public DigitalCategoryItemDataError() {
    }

    protected DigitalCategoryItemDataError(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
        this.buttonLabel = in.readString();
        this.resIcon = in.readInt();
    }

    public static final Creator<DigitalCategoryItemDataError> CREATOR =
            new Creator<DigitalCategoryItemDataError>() {
                @Override
                public DigitalCategoryItemDataError createFromParcel(Parcel source) {
                    return new DigitalCategoryItemDataError(source);
                }

                @Override
                public DigitalCategoryItemDataError[] newArray(int size) {
                    return new DigitalCategoryItemDataError[size];
                }
            };


    public static final class Builder {
        private String title = "";
        private String message = "";
        private String buttonLabel = "";
        private int resIcon = 0;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder buttonLabel(String val) {
            buttonLabel = val;
            return this;
        }

        public Builder resIcon(int val) {
            resIcon = val;
            return this;
        }

        public DigitalCategoryItemDataError build() {
            return new DigitalCategoryItemDataError(this);
        }
    }
}
