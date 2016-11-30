package com.tokopedia.core.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionLast implements Parcelable {

    @SerializedName("last_resolution_id")
    @Expose
    private String lastResolutionId;
    @SerializedName("last_show_appeal_button")
    @Expose
    private Integer lastShowAppealButton;
    @SerializedName("last_refund_amt")
    @Expose
    private Integer lastRefundAmt;
    @SerializedName("last_user_name")
    @Expose
    private String lastUserName;
    @SerializedName("last_solution")
    @Expose
    private Integer lastSolution;
    @SerializedName("last_solution_string")
    @Expose
    private String lastSolutionString;
    @SerializedName("last_user_url")
    @Expose
    private String lastUserUrl;
    @SerializedName("last_show_input_resi_button")
    @Expose
    private Integer lastShowInputResiButton;
    @SerializedName("last_show_accept_button")
    @Expose
    private Integer lastShowAcceptButton;
    @SerializedName("last_user_label_id")
    @Expose
    private Integer lastUserLabelId;
    @SerializedName("last_action_by")
    @Expose
    private Integer lastActionBy;
    @SerializedName("last_refund_amt_idr")
    @Expose
    private String lastRefundAmtIdr;
    @SerializedName("last_rival_accepted")
    @Expose
    private Integer lastRivalAccepted;
    @SerializedName("last_create_time_str")
    @Expose
    private String lastCreateTimeStr;
    @SerializedName("last_show_finish_button")
    @Expose
    private Integer lastShowFinishButton;
    @SerializedName("last_trouble_type")
    @Expose
    private Integer lastTroubleType;
    @SerializedName("last_trouble_string")
    @Expose
    private String lastTroubleString;
    @SerializedName("last_show_accept_admin_button")
    @Expose
    private Integer lastShowAcceptAdminButton;
    @SerializedName("last_user_label")
    @Expose
    private String lastUserLabel;
    @SerializedName("last_create_time")
    @Expose
    private Integer lastCreateTime;
    @SerializedName("last_flag_received")
    @Expose
    private Integer lastFlagReceived;

    /**
     * 
     * @return
     *     The lastResolutionId
     */
    public String getLastResolutionId() {
        return lastResolutionId;
    }

    /**
     * 
     * @param lastResolutionId
     *     The last_resolution_id
     */
    public void setLastResolutionId(String lastResolutionId) {
        this.lastResolutionId = lastResolutionId;
    }

    /**
     * 
     * @return
     *     The lastShowAppealButton
     */
    public Integer getLastShowAppealButton() {
        return lastShowAppealButton;
    }

    /**
     * 
     * @param lastShowAppealButton
     *     The last_show_appeal_button
     */
    public void setLastShowAppealButton(Integer lastShowAppealButton) {
        this.lastShowAppealButton = lastShowAppealButton;
    }

    /**
     * 
     * @return
     *     The lastRefundAmt
     */
    public Integer getLastRefundAmt() {
        return lastRefundAmt;
    }

    /**
     * 
     * @param lastRefundAmt
     *     The last_refund_amt
     */
    public void setLastRefundAmt(Integer lastRefundAmt) {
        this.lastRefundAmt = lastRefundAmt;
    }

    /**
     * 
     * @return
     *     The lastUserName
     */
    public String getLastUserName() {
        return lastUserName;
    }

    /**
     * 
     * @param lastUserName
     *     The last_user_name
     */
    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    /**
     * 
     * @return
     *     The lastSolution
     */
    public Integer getLastSolution() {
        return lastSolution;
    }

    /**
     * 
     * @param lastSolution
     *     The last_solution
     */
    public void setLastSolution(Integer lastSolution) {
        this.lastSolution = lastSolution;
    }

    /**
     *
     * @return
     *     The lastSolutionString
     */
    public String getLastSolutionString() {
        return lastSolutionString;
    }

    /**
     *
     * @param lastSolutionString
     *     The last_solution_text
     */
    public void setLastSolutionString(String lastSolutionString) {
        this.lastSolutionString = lastSolutionString;
    }

    /**
     * 
     * @return
     *     The lastUserUrl
     */
    public String getLastUserUrl() {
        return lastUserUrl;
    }

    /**
     * 
     * @param lastUserUrl
     *     The last_user_url
     */
    public void setLastUserUrl(String lastUserUrl) {
        this.lastUserUrl = lastUserUrl;
    }

    /**
     * 
     * @return
     *     The lastShowInputResiButton
     */
    public Integer getLastShowInputResiButton() {
        return lastShowInputResiButton;
    }

    /**
     * 
     * @param lastShowInputResiButton
     *     The last_show_input_resi_button
     */
    public void setLastShowInputResiButton(Integer lastShowInputResiButton) {
        this.lastShowInputResiButton = lastShowInputResiButton;
    }

    /**
     * 
     * @return
     *     The lastShowAcceptButton
     */
    public Integer getLastShowAcceptButton() {
        return lastShowAcceptButton;
    }

    /**
     * 
     * @param lastShowAcceptButton
     *     The last_show_accept_button
     */
    public void setLastShowAcceptButton(Integer lastShowAcceptButton) {
        this.lastShowAcceptButton = lastShowAcceptButton;
    }

    /**
     * 
     * @return
     *     The lastUserLabelId
     */
    public Integer getLastUserLabelId() {
        return lastUserLabelId;
    }

    /**
     * 
     * @param lastUserLabelId
     *     The last_user_label_id
     */
    public void setLastUserLabelId(Integer lastUserLabelId) {
        this.lastUserLabelId = lastUserLabelId;
    }

    /**
     * 
     * @return
     *     The lastActionBy
     */
    public Integer getLastActionBy() {
        return lastActionBy;
    }

    /**
     * 
     * @param lastActionBy
     *     The last_action_by
     */
    public void setLastActionBy(Integer lastActionBy) {
        this.lastActionBy = lastActionBy;
    }

    /**
     * 
     * @return
     *     The lastRefundAmtIdr
     */
    public String getLastRefundAmtIdr() {
        return lastRefundAmtIdr;
    }

    /**
     * 
     * @param lastRefundAmtIdr
     *     The last_refund_amt_idr
     */
    public void setLastRefundAmtIdr(String lastRefundAmtIdr) {
        this.lastRefundAmtIdr = lastRefundAmtIdr;
    }

    /**
     * 
     * @return
     *     The lastRivalAccepted
     */
    public Integer getLastRivalAccepted() {
        return lastRivalAccepted;
    }

    /**
     * 
     * @param lastRivalAccepted
     *     The last_rival_accepted
     */
    public void setLastRivalAccepted(Integer lastRivalAccepted) {
        this.lastRivalAccepted = lastRivalAccepted;
    }

    /**
     * 
     * @return
     *     The lastCreateTimeStr
     */
    public String getLastCreateTimeStr() {
        return lastCreateTimeStr;
    }

    /**
     * 
     * @param lastCreateTimeStr
     *     The last_create_time_str
     */
    public void setLastCreateTimeStr(String lastCreateTimeStr) {
        this.lastCreateTimeStr = lastCreateTimeStr;
    }

    /**
     * 
     * @return
     *     The lastShowFinishButton
     */
    public Integer getLastShowFinishButton() {
        return lastShowFinishButton;
    }

    /**
     * 
     * @param lastShowFinishButton
     *     The last_show_finish_button
     */
    public void setLastShowFinishButton(Integer lastShowFinishButton) {
        this.lastShowFinishButton = lastShowFinishButton;
    }

    /**
     * 
     * @return
     *     The lastTroubleType
     */
    public Integer getLastTroubleType() {
        return lastTroubleType;
    }

    /**
     * 
     * @param lastTroubleType
     *     The last_trouble_type
     */
    public void setLastTroubleType(Integer lastTroubleType) {
        this.lastTroubleType = lastTroubleType;
    }

    /**
     *
     * @return
     *     The lastTroubleString
     */
    public String getLastTroubleString() {
        return lastTroubleString;
    }

    /**
     *
     * @param lastTroubleString
     *     The last_trouble_string
     */
    public void setLastTroubleString(String lastTroubleString) {
        this.lastTroubleString = lastTroubleString;
    }

    /**
     * 
     * @return
     *     The lastShowAcceptAdminButton
     */
    public Integer getLastShowAcceptAdminButton() {
        return lastShowAcceptAdminButton;
    }

    /**
     * 
     * @param lastShowAcceptAdminButton
     *     The last_show_accept_admin_button
     */
    public void setLastShowAcceptAdminButton(Integer lastShowAcceptAdminButton) {
        this.lastShowAcceptAdminButton = lastShowAcceptAdminButton;
    }

    /**
     * 
     * @return
     *     The lastUserLabel
     */
    public String getLastUserLabel() {
        return lastUserLabel;
    }

    /**
     * 
     * @param lastUserLabel
     *     The last_user_label
     */
    public void setLastUserLabel(String lastUserLabel) {
        this.lastUserLabel = lastUserLabel;
    }

    /**
     * 
     * @return
     *     The lastCreateTime
     */
    public Integer getLastCreateTime() {
        return lastCreateTime;
    }

    /**
     * 
     * @param lastCreateTime
     *     The last_create_time
     */
    public void setLastCreateTime(Integer lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    /**
     * 
     * @return
     *     The lastFlagReceived
     */
    public Integer getLastFlagReceived() {
        return lastFlagReceived;
    }

    /**
     * 
     * @param lastFlagReceived
     *     The last_flag_received
     */
    public void setLastFlagReceived(Integer lastFlagReceived) {
        this.lastFlagReceived = lastFlagReceived;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastResolutionId);
        dest.writeValue(this.lastShowAppealButton);
        dest.writeValue(this.lastRefundAmt);
        dest.writeString(this.lastUserName);
        dest.writeValue(this.lastSolution);
        dest.writeString(this.lastSolutionString);
        dest.writeString(this.lastUserUrl);
        dest.writeValue(this.lastShowInputResiButton);
        dest.writeValue(this.lastShowAcceptButton);
        dest.writeValue(this.lastUserLabelId);
        dest.writeValue(this.lastActionBy);
        dest.writeString(this.lastRefundAmtIdr);
        dest.writeValue(this.lastRivalAccepted);
        dest.writeString(this.lastCreateTimeStr);
        dest.writeValue(this.lastShowFinishButton);
        dest.writeValue(this.lastTroubleType);
        dest.writeString(this.lastTroubleString);
        dest.writeValue(this.lastShowAcceptAdminButton);
        dest.writeString(this.lastUserLabel);
        dest.writeValue(this.lastCreateTime);
        dest.writeValue(this.lastFlagReceived);
    }

    public ResolutionLast() {
    }

    protected ResolutionLast(Parcel in) {
        this.lastResolutionId = in.readString();
        this.lastShowAppealButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastRefundAmt = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastUserName = in.readString();
        this.lastSolution = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastSolutionString = in.readString();
        this.lastUserUrl = in.readString();
        this.lastShowInputResiButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastShowAcceptButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastUserLabelId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastActionBy = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastRefundAmtIdr = in.readString();
        this.lastRivalAccepted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastCreateTimeStr = in.readString();
        this.lastShowFinishButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastTroubleType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastTroubleString = in.readString();
        this.lastShowAcceptAdminButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastUserLabel = in.readString();
        this.lastCreateTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastFlagReceived = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResolutionLast> CREATOR = new Parcelable.Creator<ResolutionLast>() {
        @Override
        public ResolutionLast createFromParcel(Parcel source) {
            return new ResolutionLast(source);
        }

        @Override
        public ResolutionLast[] newArray(int size) {
            return new ResolutionLast[size];
        }
    };
}
