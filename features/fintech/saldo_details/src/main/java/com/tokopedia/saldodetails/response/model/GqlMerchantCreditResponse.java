package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GqlMerchantCreditResponse implements Parcelable {


    @SerializedName("is_eligible")
    private boolean isEligible;

    @SerializedName("status")
    private int status;

    @SerializedName("title")
    private String title;

    @SerializedName("logo_url")
    private String logoURL;

    @SerializedName("show_new_logo")
    private boolean showNewLogo;

    @SerializedName("message")
    private String message;

    @SerializedName("limit")
    private long creditLimit;

    @SerializedName("loan_amount")
    private long loanAmount;

    @SerializedName("ticker_message")
    private String tickerMessage;

    @SerializedName("bottom_message")
    private String bottomMessage;

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


    protected GqlMerchantCreditResponse(Parcel in) {
        this.isEligible = in.readByte() != 0;
        this.status = in.readInt();
        this.title = in.readString();
        this.logoURL = in.readString();
        this.showNewLogo = in.readByte() != 0;
        this.message = in.readString();
        this.creditLimit = in.readLong();
        this.loanAmount = in.readLong();
        this.tickerMessage = in.readString();
        this.bottomMessage = in.readString();
        anchorList = in.createTypedArrayList(GqlAnchorListResponse.CREATOR);
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
        dest.writeString(logoURL);
        dest.writeByte((byte) (showNewLogo ? 1 : 0));
        dest.writeString(message);
        dest.writeLong(creditLimit);
        dest.writeLong(loanAmount);
        dest.writeString(tickerMessage);
        dest.writeString(bottomMessage);
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

    public List<GqlAnchorListResponse> getAnchorList() {
        return anchorList;
    }

    public void setAnchorList(List<GqlAnchorListResponse> anchorList) {
        this.anchorList = anchorList;
    }
}
