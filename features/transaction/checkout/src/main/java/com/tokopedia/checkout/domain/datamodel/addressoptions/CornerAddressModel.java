package com.tokopedia.checkout.domain.datamodel.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerAddressModel implements Parcelable {

    private int cornerId;
    private String cornerName;
    private String cornerBranchName;
    private String cornerBranchDesc;
    private boolean isSelected;

    public CornerAddressModel() {
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }

    public String getCornerName() {
        return cornerName;
    }

    public void setCornerName(String cornerName) {
        this.cornerName = cornerName;
    }

    public String getCornerBranchName() {
        return cornerBranchName;
    }

    public void setCornerBranchName(String cornerBranchName) {
        this.cornerBranchName = cornerBranchName;
    }

    public String getCornerBranchDesc() {
        return cornerBranchDesc;
    }

    public void setCornerBranchDesc(String cornerBranchDesc) {
        this.cornerBranchDesc = cornerBranchDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cornerId);
        dest.writeString(this.cornerName);
        dest.writeString(this.cornerBranchName);
        dest.writeString(this.cornerBranchDesc);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected CornerAddressModel(Parcel in) {
        this.cornerId = in.readInt();
        this.cornerName = in.readString();
        this.cornerBranchName = in.readString();
        this.cornerBranchDesc = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CornerAddressModel> CREATOR = new Parcelable.Creator<CornerAddressModel>() {
        @Override
        public CornerAddressModel createFromParcel(Parcel source) {
            return new CornerAddressModel(source);
        }

        @Override
        public CornerAddressModel[] newArray(int size) {
            return new CornerAddressModel[size];
        }
    };
}
