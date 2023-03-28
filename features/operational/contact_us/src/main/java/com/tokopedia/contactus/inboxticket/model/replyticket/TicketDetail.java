
package com.tokopedia.contactus.inboxticket.model.replyticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.session.model.UserReputation;

import java.util.ArrayList;
import java.util.List;

public class TicketDetail {

    @SerializedName("attachment")
    @Expose
    private List<TicketImageAttachment> attachment = new ArrayList<TicketImageAttachment>();
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_label_id")
    @Expose
    private long userLabelId;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("ticket_detail_id")
    @Expose
    private long ticketDetailId;
    @SerializedName("ticket_new_rating")
    @Expose
    private long ticketNewRating;
    @SerializedName("is_cs")
    @Expose
    private long isCs;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("create_time_ago")
    @Expose
    private String createTimeAgo;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("user_pic")
    @Expose
    private String userPic;
    @SerializedName("ticket_new_status")
    @Expose
    private int ticketNewStatus;
    @SerializedName("create_time_fmt")
    @Expose
    private String createTimeFmt;
    @SerializedName("ticket_detail_msg")
    @Expose
    private String ticketDetailMsg;

    /**
     * 
     * @return
     *     The attachment
     */
    public List<TicketImageAttachment> getAttachment() {
        return attachment;
    }

    /**
     * 
     * @param attachment
     *     The attachment
     */
    public void setAttachment(List<TicketImageAttachment> attachment) {
        this.attachment = attachment;
    }

    public TicketDetail withAttachment(List<TicketImageAttachment> attachment) {
        this.attachment = attachment;
        return this;
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

    public TicketDetail withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 
     * @return
     *     The userLabelId
     */
    public long getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(long userLabelId) {
        this.userLabelId = userLabelId;
    }

    public TicketDetail withUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
        return this;
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

    public TicketDetail withUserUrl(String userUrl) {
        this.userUrl = userUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The ticketDetailId
     */
    public long getTicketDetailId() {
        return ticketDetailId;
    }

    /**
     * 
     * @param ticketDetailId
     *     The ticket_detail_id
     */
    public void setTicketDetailId(long ticketDetailId) {
        this.ticketDetailId = ticketDetailId;
    }

    public TicketDetail withTicketDetailId(int ticketDetailId) {
        this.ticketDetailId = ticketDetailId;
        return this;
    }

    /**
     * 
     * @return
     *     The ticketNewRating
     */
    public long getTicketNewRating() {
        return ticketNewRating;
    }

    /**
     * 
     * @param ticketNewRating
     *     The ticket_new_rating
     */
    public void setTicketNewRating(long ticketNewRating) {
        this.ticketNewRating = ticketNewRating;
    }

    public TicketDetail withTicketNewRating(int ticketNewRating) {
        this.ticketNewRating = ticketNewRating;
        return this;
    }

    /**
     * 
     * @return
     *     The isCs
     */
    public long getIsCs() {
        return isCs;
    }

    /**
     * 
     * @param isCs
     *     The is_cs
     */
    public void setIsCs(long isCs) {
        this.isCs = isCs;
    }

    public TicketDetail withIsCs(int isCs) {
        this.isCs = isCs;
        return this;
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

    public TicketDetail withUserLabel(String userLabel) {
        this.userLabel = userLabel;
        return this;
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

    public TicketDetail withCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
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

    public TicketDetail withCreateTimeAgo(String createTimeAgo) {
        this.createTimeAgo = createTimeAgo;
        return this;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public TicketDetail withUserId(int userId) {
        this.userId = userId;
        return this;
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

    public TicketDetail withUserPic(String userPic) {
        this.userPic = userPic;
        return this;
    }

    /**
     * 
     * @return
     *     The ticketNewStatus
     */
    public int getTicketNewStatus() {
        return ticketNewStatus;
    }

    /**
     * 
     * @param ticketNewStatus
     *     The ticket_new_status
     */
    public void setTicketNewStatus(int ticketNewStatus) {
        this.ticketNewStatus = ticketNewStatus;
    }

    public TicketDetail withTicketNewStatus(int ticketNewStatus) {
        this.ticketNewStatus = ticketNewStatus;
        return this;
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

    public TicketDetail withCreateTimeFmt(String createTimeFmt) {
        this.createTimeFmt = createTimeFmt;
        return this;
    }

    /**
     * 
     * @return
     *     The ticketDetailMsg
     */
    public String getTicketDetailMsg() {
        return ticketDetailMsg;
    }

    /**
     * 
     * @param ticketDetailMsg
     *     The ticket_detail_msg
     */
    public void setTicketDetailMsg(String ticketDetailMsg) {
        this.ticketDetailMsg = ticketDetailMsg;
    }

    public TicketDetail withTicketDetailMsg(String ticketDetailMsg) {
        this.ticketDetailMsg = ticketDetailMsg;
        return this;
    }

}
