package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GqlMerchantCreditResponse implements Parcelable {


    @SerializedName("is_eligible")
    private boolean isEligible;

    @SerializedName("status")
    private int status;

    @SerializedName("title")
    private String title;

    @SerializedName("main_redirect_url")
    private String mainRedirectUrl;

    @SerializedName("image_url")
    private String logoURL;

    @SerializedName("body_description")
    private String bodyDesc;

    @SerializedName("anchor_list")
    private ArrayList<GqlAnchorListResponse> anchorList = new ArrayList<>();

    @SerializedName("info_list")
    private ArrayList<GqlInfoListResponse> infoList = new ArrayList<>();

    @SerializedName("show_box")
    private boolean showBox;

    @SerializedName("box_info")
    private GqlBoxInfoResponse boxInfo;

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


    protected GqlMerchantCreditResponse(Parcel in) {
        this.isEligible = in.readByte() != 0;
        this.status = in.readInt();
        this.title = in.readString();
        this.mainRedirectUrl = in.readString();
        this.logoURL = in.readString();
        this.bodyDesc = in.readString();
        this.anchorList = in.createTypedArrayList(GqlAnchorListResponse.CREATOR);
        this.infoList = in.createTypedArrayList(GqlInfoListResponse.CREATOR);
        this.showBox = in.readByte() != 0;
        this.boxInfo = ((GqlBoxInfoResponse) in.readValue((GqlBoxInfoResponse.class.getClassLoader())));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isEligible ? 1 : 0));
        dest.writeInt(status);
        dest.writeString(title);
        dest.writeString(mainRedirectUrl);
        dest.writeString(logoURL);
        dest.writeString(bodyDesc);
        dest.writeList(anchorList);
        dest.writeList(infoList);
        dest.writeByte((byte) (showBox ? 1 : 0));
        dest.writeValue(boxInfo);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainRedirectUrl() {
        return mainRedirectUrl;
    }

    public void setMainRedirectUrl(String mainRedirectUrl) {
        this.mainRedirectUrl = mainRedirectUrl;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getBodyDesc() {
        return bodyDesc;
    }

    public void setBodyDesc(String bodyDesc) {
        this.bodyDesc = bodyDesc;
    }

    public ArrayList<GqlAnchorListResponse> getAnchorList() {
        return anchorList;
    }

    public void setAnchorList(ArrayList<GqlAnchorListResponse> anchorList) {
        this.anchorList = anchorList;
    }

    public ArrayList<GqlInfoListResponse> getInfoList() {
        return infoList;
    }

    public void setInfoList(ArrayList<GqlInfoListResponse> infoList) {
        this.infoList = infoList;
    }

    public boolean isShowBox() {
        return showBox;
    }

    public void setShowBox(boolean showBox) {
        this.showBox = showBox;
    }

    public GqlBoxInfoResponse getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(GqlBoxInfoResponse boxInfo) {
        this.boxInfo = boxInfo;
    }
}
