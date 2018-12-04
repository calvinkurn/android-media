package com.tokopedia.tokocash.ovoactivation.view;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 01/10/18.
 */
public class PhoneActionModel implements Parcelable {

    private String titlePhoneAction;
    private String descPhoneAction;
    private String labelBtnPhoneAction;
    private String applinkPhoneAction;

    public PhoneActionModel() {
    }

    protected PhoneActionModel(Parcel in) {
        titlePhoneAction = in.readString();
        descPhoneAction = in.readString();
        labelBtnPhoneAction = in.readString();
        applinkPhoneAction = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titlePhoneAction);
        dest.writeString(descPhoneAction);
        dest.writeString(labelBtnPhoneAction);
        dest.writeString(applinkPhoneAction);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneActionModel> CREATOR = new Creator<PhoneActionModel>() {
        @Override
        public PhoneActionModel createFromParcel(Parcel in) {
            return new PhoneActionModel(in);
        }

        @Override
        public PhoneActionModel[] newArray(int size) {
            return new PhoneActionModel[size];
        }
    };

    public String getTitlePhoneAction() {
        return titlePhoneAction;
    }

    public void setTitlePhoneAction(String titlePhoneAction) {
        this.titlePhoneAction = titlePhoneAction;
    }

    public String getDescPhoneAction() {
        return descPhoneAction;
    }

    public void setDescPhoneAction(String descPhoneAction) {
        this.descPhoneAction = descPhoneAction;
    }

    public String getLabelBtnPhoneAction() {
        return labelBtnPhoneAction;
    }

    public void setLabelBtnPhoneAction(String labelBtnPhoneAction) {
        this.labelBtnPhoneAction = labelBtnPhoneAction;
    }

    public String getApplinkPhoneAction() {
        return applinkPhoneAction;
    }

    public void setApplinkPhoneAction(String applinkPhoneAction) {
        this.applinkPhoneAction = applinkPhoneAction;
    }
}
