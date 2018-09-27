package com.tokopedia.train.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 23/07/18.
 */
public class TrainPromoViewModel implements Parcelable {

    private String id;
    private TrainPromoAttributesViewModel attributes;
    private boolean promoCodeCopied;

    public TrainPromoViewModel(String id, TrainPromoAttributesViewModel attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    protected TrainPromoViewModel(Parcel in) {
        id = in.readString();
        attributes = in.readParcelable(TrainPromoAttributesViewModel.class.getClassLoader());
        promoCodeCopied = in.readByte() != 0;
    }

    public static final Creator<TrainPromoViewModel> CREATOR = new Creator<TrainPromoViewModel>() {
        @Override
        public TrainPromoViewModel createFromParcel(Parcel in) {
            return new TrainPromoViewModel(in);
        }

        @Override
        public TrainPromoViewModel[] newArray(int size) {
            return new TrainPromoViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrainPromoAttributesViewModel getAttributes() {
        return attributes;
    }

    public void setAttributes(TrainPromoAttributesViewModel attributes) {
        this.attributes = attributes;
    }

    public boolean isPromoCodeCopied() {
        return promoCodeCopied;
    }

    public void setPromoCodeCopied(boolean promoCodeCopied) {
        this.promoCodeCopied = promoCodeCopied;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(attributes, i);
        parcel.writeByte((byte) (promoCodeCopied ? 1 : 0));
    }
}
