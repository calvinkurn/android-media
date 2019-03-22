package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GqlDetailsResponse implements Parcelable {


    @SerializedName("is_eligible")
    private boolean isEligible;

    @SerializedName("status")
    private int status;

    @SerializedName("is_enabled")
    private boolean isEnabled;

    @SerializedName("show_new_logo")
    private boolean showNewLogo;

    @SerializedName("title")
    private String title;

    @SerializedName("show_toggle")
    private boolean showToggle;

    @SerializedName("box_type")
    private String boxType;

    @SerializedName("description")
    private String description;

    @SerializedName("box_right_arrow")
    private boolean showRightArrow;

    @SerializedName("box_title")
    private String boxTitle;

    @SerializedName("box_desc")
    private String boxDesc;

    @SerializedName("box_show_popup")
    private boolean boxShowPopup;

    @SerializedName("popup_title")
    private String popupTitle;

    @SerializedName("popup_desc")
    private String popupDesc;

    @SerializedName("popup_button_text")
    private String popupButtonText;

    @SerializedName("infoList")
    private List<GqlInfoListResponse> infoList = null;

    @SerializedName("anchorList")
    private List<GqlSpAnchorListResponse> anchorList = null;

    public static final Creator<GqlDetailsResponse> CREATOR = new Creator<GqlDetailsResponse>() {
        @Override
        public GqlDetailsResponse createFromParcel(Parcel in) {
            return new GqlDetailsResponse(in);
        }

        @Override
        public GqlDetailsResponse[] newArray(int size) {
            return new GqlDetailsResponse[size];
        }
    };


    protected GqlDetailsResponse(Parcel in) {
        this.isEligible = in.readByte() != 0;
        this.status = in.readInt();
        this.isEnabled = in.readByte() != 0;
        this.showNewLogo = in.readByte() != 0;
        this.title = in.readString();
        this.showToggle = in.readByte() != 0;
        this.boxType = in.readString();
        this.description = in.readString();
        this.showRightArrow = in.readByte() != 0;
        this.boxTitle = in.readString();
        this.boxDesc = in.readString();
        this.boxShowPopup = in.readByte() != 0;
        this.popupTitle = in.readString();
        this.popupDesc = in.readString();
        this.popupButtonText = in.readString();
        infoList = in.createTypedArrayList(GqlInfoListResponse.CREATOR);
        anchorList = in.createTypedArrayList(GqlSpAnchorListResponse.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isEligible ? 1 : 0));
        dest.writeInt(status);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeByte((byte) (showNewLogo ? 1 : 0));
        dest.writeString(title);
        dest.writeByte((byte) (showToggle ? 1 : 0));
        dest.writeString(boxType);
        dest.writeString(description);
        dest.writeByte((byte) (showRightArrow ? 1 : 0));
        dest.writeString(boxTitle);
        dest.writeString(boxDesc);
        dest.writeByte((byte) (boxShowPopup ? 1 : 0));
        dest.writeString(popupTitle);
        dest.writeString(popupDesc);
        dest.writeString(popupButtonText);
        dest.writeList(infoList);
        dest.writeList(anchorList);
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isShowNewLogo() {
        return showNewLogo;
    }

    public void setShowNewLogo(boolean showNewLogo) {
        this.showNewLogo = showNewLogo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowToggle() {
        return showToggle;
    }

    public void setShowToggle(boolean showToggle) {
        this.showToggle = showToggle;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowRightArrow() {
        return showRightArrow;
    }

    public void setShowRightArrow(boolean showRightArrow) {
        this.showRightArrow = showRightArrow;
    }

    public String getBoxTitle() {
        return boxTitle;
    }

    public void setBoxTitle(String boxTitle) {
        this.boxTitle = boxTitle;
    }

    public String getBoxDesc() {
        return boxDesc;
    }

    public void setBoxDesc(String boxDesc) {
        this.boxDesc = boxDesc;
    }

    public boolean isBoxShowPopup() {
        return boxShowPopup;
    }

    public void setBoxShowPopup(boolean boxShowPopup) {
        this.boxShowPopup = boxShowPopup;
    }

    public String getPopupTitle() {
        return popupTitle;
    }

    public void setPopupTitle(String popupTitle) {
        this.popupTitle = popupTitle;
    }

    public String getPopupDesc() {
        return popupDesc;
    }

    public void setPopupDesc(String popupDesc) {
        this.popupDesc = popupDesc;
    }

    public String getPopupButtonText() {
        return popupButtonText;
    }

    public void setPopupButtonText(String popupButtonText) {
        this.popupButtonText = popupButtonText;
    }

    public List<GqlInfoListResponse> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<GqlInfoListResponse> infoList) {
        this.infoList = infoList;
    }

    public List<GqlSpAnchorListResponse> getAnchorList() {
        return anchorList;
    }

    public void setAnchorList(List<GqlSpAnchorListResponse> anchorList) {
        this.anchorList = anchorList;
    }
}
