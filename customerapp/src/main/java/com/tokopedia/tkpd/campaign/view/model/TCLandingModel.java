package com.tokopedia.tkpd.campaign.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 16/04/18.
 */

public class TCLandingModel implements Parcelable {
    public final static String TITLE = "title";
    public final static String ERROR_MESSAGE = "error_message";
    public final static String MESSAGE = "message";
    public final static String APPLINK = "applink";
    public final static String BUTTON = "button";

    private String title;
    private String errorMessage;
    private String message;
    private String applink;
    private String button;

    TCLandingModel(String title, String errorMessage, String message, String applink, String button) {
        this.title = title;
        this.errorMessage = errorMessage;
        this.message = message;
        this.applink = applink;
        this.button = button;
    }

    private TCLandingModel(Parcel in) {
        title = in.readString();
        errorMessage = in.readString();
        message = in.readString();
        applink = in.readString();
        button = in.readString();
    }

    public static final Creator<TCLandingModel> CREATOR = new Creator<TCLandingModel>() {
        @Override
        public TCLandingModel createFromParcel(Parcel in) {
            return new TCLandingModel(in);
        }

        @Override
        public TCLandingModel[] newArray(int size) {
            return new TCLandingModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getApplink() {
        return applink;
    }

    public String getButton() {
        return button;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(errorMessage);
        dest.writeString(message);
        dest.writeString(applink);
        dest.writeString(button);
    }

    public static class Builder {
        private String title;
        private String errorMessage;
        private String message;
        private String applink;
        private String button;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder applink(String applink) {
            this.applink = applink;
            return this;
        }

        public Builder button(String button) {
            this.button = button;
            return this;
        }

        public TCLandingModel build() {
            return new TCLandingModel(title, errorMessage, message, applink, button);
        }
    }

}
