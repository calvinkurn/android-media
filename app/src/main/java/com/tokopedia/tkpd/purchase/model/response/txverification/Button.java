package com.tokopedia.tkpd.purchase.model.response.txverification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 25/05/2016.
 */
public class Button implements Parcelable {
    private static final String TAG = Button.class.getSimpleName();

    @SerializedName("button_view_proof")
    @Expose
    private Integer buttonViewProof;
    @SerializedName("button_edit_payment")
    @Expose
    private Integer buttonEditPayment;
    @SerializedName("button_upload_proof")
    @Expose
    private Integer buttonUploadProof;

    public Integer getButtonViewProof() {
        return buttonViewProof;
    }

    public void setButtonViewProof(Integer buttonViewProof) {
        this.buttonViewProof = buttonViewProof;
    }

    public Integer getButtonEditPayment() {
        return buttonEditPayment;
    }

    public void setButtonEditPayment(Integer buttonEditPayment) {
        this.buttonEditPayment = buttonEditPayment;
    }

    public Integer getButtonUploadProof() {
        return buttonUploadProof;
    }

    public void setButtonUploadProof(Integer buttonUploadProof) {
        this.buttonUploadProof = buttonUploadProof;
    }

    protected Button(Parcel in) {
        buttonViewProof = in.readByte() == 0x00 ? null : in.readInt();
        buttonEditPayment = in.readByte() == 0x00 ? null : in.readInt();
        buttonUploadProof = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (buttonViewProof == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(buttonViewProof);
        }
        if (buttonEditPayment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(buttonEditPayment);
        }
        if (buttonUploadProof == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(buttonUploadProof);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Button> CREATOR = new Parcelable.Creator<Button>() {
        @Override
        public Button createFromParcel(Parcel in) {
            return new Button(in);
        }

        @Override
        public Button[] newArray(int size) {
            return new Button[size];
        }
    };
}
