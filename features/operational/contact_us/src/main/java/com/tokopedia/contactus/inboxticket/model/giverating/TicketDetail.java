
package com.tokopedia.contactus.inboxticket.model.giverating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketDetail {

    @SerializedName("ticket_detail_id")
    @Expose
    private String ticketDetailId;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("user_pic")
    @Expose
    private String userPic;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("create_time_fmt")
    @Expose
    private String createTimeFmt;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("ticket_new_status")
    @Expose
    private String ticketNewStatus;
    @SerializedName("create_time_ago")
    @Expose
    private String createTimeAgo;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("is_cs")
    @Expose
    private int isCs;
    @SerializedName("ticket_new_rating")
    @Expose
    private String ticketNewRating;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ticket_detail_msg")
    @Expose
    private Object ticketDetailMsg;

    /**
     * 
     * @return
     *     The ticketDetailId
     */
    public String getTicketDetailId() {
        return ticketDetailId;
    }

    /**
     * 
     * @param ticketDetailId
     *     The ticket_detail_id
     */
    public void setTicketDetailId(String ticketDetailId) {
        this.ticketDetailId = ticketDetailId;
    }

    /**
     * 
     * @return
     *     The userLabelId
     */
    public int getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @param createTime
     *     The create_time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @return
     *     The userPic
     */
    public String getUserPic() {
        return userPic;
    }

    /**
     * 
     * @param userPic
     *     The user_pic
     */
    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    /**
     * 
     * @return
     *     The userUrl
     */
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * 
     * @param userUrl
     *     The user_url
     */
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * 
     * @return
     *     The createTimeFmt
     */
    public String getCreateTimeFmt() {
        return createTimeFmt;
    }

    /**
     * 
     * @param createTimeFmt
     *     The create_time_fmt
     */
    public void setCreateTimeFmt(String createTimeFmt) {
        this.createTimeFmt = createTimeFmt;
    }

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * @return
     *     The ticketNewStatus
     */
    public String getTicketNewStatus() {
        return ticketNewStatus;
    }

    /**
     * 
     * @param ticketNewStatus
     *     The ticket_new_status
     */
    public void setTicketNewStatus(String ticketNewStatus) {
        this.ticketNewStatus = ticketNewStatus;
    }

    /**
     * 
     * @return
     *     The createTimeAgo
     */
    public String getCreateTimeAgo() {
        return createTimeAgo;
    }

    /**
     * 
     * @param createTimeAgo
     *     The create_time_ago
     */
    public void setCreateTimeAgo(String createTimeAgo) {
        this.createTimeAgo = createTimeAgo;
    }

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

//    /**
//     *
//     * @return
//     *     The userReputation
//     */
//    public UserReputation getUserReputation() {
//        return userReputation;
//    }
//
//    /**
//     *
//     * @param userReputation
//     *     The user_reputation
//     */
//    public void setUserReputation(UserReputation userReputation) {
//        this.userReputation = userReputation;
//    }

    /**
     * 
     * @return
     *     The isCs
     */
    public int getIsCs() {
        return isCs;
    }

    /**
     * 
     * @param isCs
     *     The is_cs
     */
    public void setIsCs(int isCs) {
        this.isCs = isCs;
    }

    /**
     * 
     * @return
     *     The ticketNewRating
     */
    public String getTicketNewRating() {
        return ticketNewRating;
    }

    /**
     * 
     * @param ticketNewRating
     *     The ticket_new_rating
     */
    public void setTicketNewRating(String ticketNewRating) {
        this.ticketNewRating = ticketNewRating;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The ticketDetailMsg
     */
    public Object getTicketDetailMsg() {
        return ticketDetailMsg;
    }

    /**
     * 
     * @param ticketDetailMsg
     *     The ticket_detail_msg
     */
    public void setTicketDetailMsg(Object ticketDetailMsg) {
        this.ticketDetailMsg = ticketDetailMsg;
    }

}
