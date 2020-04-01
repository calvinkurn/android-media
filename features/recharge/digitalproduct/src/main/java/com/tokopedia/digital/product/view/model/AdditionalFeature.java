package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 22/05/18.
 */
public class AdditionalFeature implements Parcelable {

    private String text;
    private String buttonText;
    private int featureId;

    public AdditionalFeature(String text, String buttonText, int feature) {
        this.text = text;
        this.buttonText = buttonText;
        this.featureId = feature;
    }

    public String getText() {
        return text;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getFeatureId() {
        return featureId;
    }

    public AdditionalFeature(Parcel in) {
        this.text = in.readString();
        this.buttonText = in.readString();
        this.featureId = in.readInt();
    }

    public static final Creator<AdditionalFeature> CREATOR = new Creator<AdditionalFeature>() {
        @Override
        public AdditionalFeature createFromParcel(Parcel in) {
            return new AdditionalFeature(in);
        }

        @Override
        public AdditionalFeature[] newArray(int size) {
            return new AdditionalFeature[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.buttonText);
        dest.writeInt(this.featureId);
    }
}
