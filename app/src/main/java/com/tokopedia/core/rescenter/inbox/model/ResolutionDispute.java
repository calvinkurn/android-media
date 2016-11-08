
package com.tokopedia.core.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionDispute implements Parcelable {

    @SerializedName("dispute_30_days")
    @Expose
    private Integer dispute30Days;
    @SerializedName("dispute_update_time")
    @Expose
    private String disputeUpdateTime;
    @SerializedName("dispute_is_responded")
    @Expose
    private Integer disputeIsResponded;
    @SerializedName("dispute_create_time")
    @Expose
    private String disputeCreateTime;
    @SerializedName("dispute_is_expired")
    @Expose
    private Integer disputeIsExpired;
    @SerializedName("dispute_update_time_short")
    @Expose
    private String disputeUpdateTimeShort;
    @SerializedName("dispute_is_call_admin")
    @Expose
    private Integer disputeIsCallAdmin;
    @SerializedName("dispute_status")
    @Expose
    private Integer disputeStatus;
    @SerializedName("dispute_deadline")
    @Expose
    private Integer disputeDeadline;
    @SerializedName("dispute_resolution_id")
    @Expose
    private Integer disputeResolutionId;
    @SerializedName("dispute_detail_url")
    @Expose
    private String disputeDetailUrl;

    /**
     * 
     * @return
     *     The dispute30Days
     */
    public Integer getDispute30Days() {
        return dispute30Days;
    }

    /**
     * 
     * @param dispute30Days
     *     The dispute_30_days
     */
    public void setDispute30Days(Integer dispute30Days) {
        this.dispute30Days = dispute30Days;
    }

    /**
     * 
     * @return
     *     The disputeUpdateTime
     */
    public String getDisputeUpdateTime() {
        return disputeUpdateTime;
    }

    /**
     * 
     * @param disputeUpdateTime
     *     The dispute_update_time
     */
    public void setDisputeUpdateTime(String disputeUpdateTime) {
        this.disputeUpdateTime = disputeUpdateTime;
    }

    /**
     * 
     * @return
     *     The disputeIsResponded
     */
    public Integer getDisputeIsResponded() {
        return disputeIsResponded;
    }

    /**
     * 
     * @param disputeIsResponded
     *     The dispute_is_responded
     */
    public void setDisputeIsResponded(Integer disputeIsResponded) {
        this.disputeIsResponded = disputeIsResponded;
    }

    /**
     * 
     * @return
     *     The disputeCreateTime
     */
    public String getDisputeCreateTime() {
        return disputeCreateTime;
    }

    /**
     * 
     * @param disputeCreateTime
     *     The dispute_create_time
     */
    public void setDisputeCreateTime(String disputeCreateTime) {
        this.disputeCreateTime = disputeCreateTime;
    }

    /**
     * 
     * @return
     *     The disputeIsExpired
     */
    public Integer getDisputeIsExpired() {
        return disputeIsExpired;
    }

    /**
     * 
     * @param disputeIsExpired
     *     The dispute_is_expired
     */
    public void setDisputeIsExpired(Integer disputeIsExpired) {
        this.disputeIsExpired = disputeIsExpired;
    }

    /**
     * 
     * @return
     *     The disputeUpdateTimeShort
     */
    public String getDisputeUpdateTimeShort() {
        return disputeUpdateTimeShort;
    }

    /**
     * 
     * @param disputeUpdateTimeShort
     *     The dispute_update_time_short
     */
    public void setDisputeUpdateTimeShort(String disputeUpdateTimeShort) {
        this.disputeUpdateTimeShort = disputeUpdateTimeShort;
    }

    /**
     * 
     * @return
     *     The disputeIsCallAdmin
     */
    public Integer getDisputeIsCallAdmin() {
        return disputeIsCallAdmin;
    }

    /**
     * 
     * @param disputeIsCallAdmin
     *     The dispute_is_call_admin
     */
    public void setDisputeIsCallAdmin(Integer disputeIsCallAdmin) {
        this.disputeIsCallAdmin = disputeIsCallAdmin;
    }

    /**
     * 
     * @return
     *     The disputeStatus
     */
    public Integer getDisputeStatus() {
        return disputeStatus;
    }

    /**
     * 
     * @param disputeStatus
     *     The dispute_status
     */
    public void setDisputeStatus(Integer disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    /**
     * 
     * @return
     *     The disputeDeadline
     */
    public Integer getDisputeDeadline() {
        return disputeDeadline;
    }

    /**
     * 
     * @param disputeDeadline
     *     The dispute_deadline
     */
    public void setDisputeDeadline(Integer disputeDeadline) {
        this.disputeDeadline = disputeDeadline;
    }

    /**
     * 
     * @return
     *     The disputeResolutionId
     */
    public Integer getDisputeResolutionId() {
        return disputeResolutionId;
    }

    /**
     * 
     * @param disputeResolutionId
     *     The dispute_resolution_id
     */
    public void setDisputeResolutionId(Integer disputeResolutionId) {
        this.disputeResolutionId = disputeResolutionId;
    }

    /**
     * 
     * @return
     *     The disputeDetailUrl
     */
    public String getDisputeDetailUrl() {
        return disputeDetailUrl;
    }

    /**
     * 
     * @param disputeDetailUrl
     *     The dispute_detail_url
     */
    public void setDisputeDetailUrl(String disputeDetailUrl) {
        this.disputeDetailUrl = disputeDetailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.dispute30Days);
        dest.writeString(this.disputeUpdateTime);
        dest.writeValue(this.disputeIsResponded);
        dest.writeString(this.disputeCreateTime);
        dest.writeValue(this.disputeIsExpired);
        dest.writeString(this.disputeUpdateTimeShort);
        dest.writeValue(this.disputeIsCallAdmin);
        dest.writeValue(this.disputeStatus);
        dest.writeValue(this.disputeDeadline);
        dest.writeValue(this.disputeResolutionId);
        dest.writeString(this.disputeDetailUrl);
    }

    public ResolutionDispute() {
    }

    protected ResolutionDispute(Parcel in) {
        this.dispute30Days = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeUpdateTime = in.readString();
        this.disputeIsResponded = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeCreateTime = in.readString();
        this.disputeIsExpired = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeUpdateTimeShort = in.readString();
        this.disputeIsCallAdmin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeDeadline = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeResolutionId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.disputeDetailUrl = in.readString();
    }

    public static final Parcelable.Creator<ResolutionDispute> CREATOR = new Parcelable.Creator<ResolutionDispute>() {
        @Override
        public ResolutionDispute createFromParcel(Parcel source) {
            return new ResolutionDispute(source);
        }

        @Override
        public ResolutionDispute[] newArray(int size) {
            return new ResolutionDispute[size];
        }
    };
}
