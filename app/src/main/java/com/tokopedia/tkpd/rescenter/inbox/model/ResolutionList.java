package com.tokopedia.tkpd.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 4/6/16.
 */
public class ResolutionList extends ResCenterInboxItem {

    @SerializedName("resolution_detail")
    @Expose
    private ResolutionDetail resolutionDetail;
    @SerializedName("resolution_read_status")
    @Expose
    private Integer resolutionReadStatus;

    /**
     *
     * @return
     *     The resolutionDetail
     */
    public ResolutionDetail getResolutionDetail() {
        return resolutionDetail;
    }

    /**
     *
     * @param resolutionDetail
     *     The resolution_detail
     */
    public void setResolutionDetail(ResolutionDetail resolutionDetail) {
        this.resolutionDetail = resolutionDetail;
    }

    /**
     *
     * @return
     *     The resolutionReadStatus
     */
    public Integer getResolutionReadStatus() {
        return resolutionReadStatus;
    }

    /**
     *
     * @param resolutionReadStatus
     *     The resolution_read_status
     */
    public void setResolutionReadStatus(Integer resolutionReadStatus) {
        this.resolutionReadStatus = resolutionReadStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resolutionDetail, flags);
        dest.writeValue(this.resolutionReadStatus);
    }

    public ResolutionList() {
        setItemType(ResCenterInboxItem.TYPE_MAIN);
    }

    protected ResolutionList(Parcel in) {
        this.resolutionDetail = in.readParcelable(ResolutionDetail.class.getClassLoader());
        this.resolutionReadStatus = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResolutionList> CREATOR = new Parcelable.Creator<ResolutionList>() {
        @Override
        public ResolutionList createFromParcel(Parcel source) {
            return new ResolutionList(source);
        }

        @Override
        public ResolutionList[] newArray(int size) {
            return new ResolutionList[size];
        }
    };
}
