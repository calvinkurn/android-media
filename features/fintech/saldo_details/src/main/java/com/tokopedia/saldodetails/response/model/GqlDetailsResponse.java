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
    private List<GqlAnchorListResponse> anchorList = null;

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
        this.isEligible = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.status = ((int) in.readValue((Integer.class.getClassLoader())));
        this.isEnabled = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.showNewLogo = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.showToggle = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.boxType = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.showRightArrow = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.boxTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.boxDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.boxShowPopup = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.popupTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.popupDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.popupButtonText = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.infoList, (GqlInfoListResponse.class.getClassLoader()));
        in.readList(this.anchorList, (GqlAnchorListResponse.class.getClassLoader()));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(isEligible);
        dest.writeValue(status);
        dest.writeValue(isEnabled);
        dest.writeValue(showNewLogo);
        dest.writeValue(title);
        dest.writeValue(showToggle);
        dest.writeValue(boxType);
        dest.writeValue(description);
        dest.writeValue(showRightArrow);
        dest.writeValue(boxTitle);
        dest.writeValue(boxDesc);
        dest.writeValue(boxShowPopup);
        dest.writeValue(popupTitle);
        dest.writeValue(popupDesc);
        dest.writeValue(popupButtonText);
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

    public List<GqlAnchorListResponse> getAnchorList() {
        return anchorList;
    }

    public void setAnchorList(List<GqlAnchorListResponse> anchorList) {
        this.anchorList = anchorList;
    }
}
