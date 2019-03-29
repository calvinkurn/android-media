
package com.tokopedia.contactus.inboxticket.model.inboxticketdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

public class TicketReplyDatum {

    @SerializedName("ticket_detail_id")
    @Expose
    private String ticketDetailId;
    @SerializedName("ticket_detail_create_time_fmt")
    @Expose
    private String ticketDetailCreateTimeFmt;
    @SerializedName("ticket_detail_user_name")
    @Expose
    private String ticketDetailUserName;
    @SerializedName("ticket_detail_new_rating")
    @Expose
    private String ticketDetailNewRating;
    @SerializedName("ticket_detail_is_cs")
    @Expose
    private String ticketDetailIsCs;
    @SerializedName("ticket_detail_user_url")
    @Expose
    private String ticketDetailUserUrl;
    @SerializedName("ticket_detail_create_time")
    @Expose
    private String ticketDetailCreateTime;
    @SerializedName("ticket_detail_user_label_id")
    @Expose
    private String ticketDetailUserLabelId;
    @SerializedName("ticket_detail_user_label")
    @Expose
    private String ticketDetailUserLabel;
    @SerializedName("ticket_detail_user_image")
    @Expose
    private String ticketDetailUserImage;
    @SerializedName("ticket_detail_attachment")
    @Expose
    private List<TicketImageAttachment> ticketDetailAttachment = new ArrayList<TicketImageAttachment>();
    @SerializedName("ticket_detail_user_id")
    @Expose
    private String ticketDetailUserId;
    @SerializedName("ticket_detail_new_status")
    @Expose
    private String ticketDetailNewStatus;
    @SerializedName("ticket_detail_message")
    @Expose
    private String ticketDetailMessage;

    /**
     * @return The ticketDetailId
     */
    public String getTicketDetailId() {
        return ticketDetailId;
    }

    /**
     * @param ticketDetailId The ticket_detail_id
     */
    public void setTicketDetailId(String ticketDetailId) {
        this.ticketDetailId = ticketDetailId;
    }

    /**
     * @return The ticketDetailCreateTimeFmt
     */
    public String getTicketDetailCreateTimeFmt() {
        return ticketDetailCreateTimeFmt;
    }

    /**
     * @param ticketDetailCreateTimeFmt The ticket_detail_create_time_fmt
     */
    public void setTicketDetailCreateTimeFmt(String ticketDetailCreateTimeFmt) {
        this.ticketDetailCreateTimeFmt = ticketDetailCreateTimeFmt;
    }

    /**
     * @return The ticketDetailUserName
     */
    public String getTicketDetailUserName() {
        return ticketDetailUserName;
    }

    /**
     * @param ticketDetailUserName The ticket_detail_user_name
     */
    public void setTicketDetailUserName(String ticketDetailUserName) {
        this.ticketDetailUserName = ticketDetailUserName;
    }

    /**
     * @return The ticketDetailNewRating
     */
    public String getTicketDetailNewRating() {
        return ticketDetailNewRating;
    }

    /**
     * @param ticketDetailNewRating The ticket_detail_new_rating
     */
    public void setTicketDetailNewRating(String ticketDetailNewRating) {
        this.ticketDetailNewRating = ticketDetailNewRating;
    }

    /**
     * @return The ticketDetailIsCs
     */
    public String getTicketDetailIsCs() {
        return ticketDetailIsCs;
    }

    /**
     * @param ticketDetailIsCs The ticket_detail_is_cs
     */
    public void setTicketDetailIsCs(String ticketDetailIsCs) {
        this.ticketDetailIsCs = ticketDetailIsCs;
    }

    /**
     * @return The ticketDetailUserUrl
     */
    public String getTicketDetailUserUrl() {
        return ticketDetailUserUrl;
    }

    /**
     * @param ticketDetailUserUrl The ticket_detail_user_url
     */
    public void setTicketDetailUserUrl(String ticketDetailUserUrl) {
        this.ticketDetailUserUrl = ticketDetailUserUrl;
    }

    /**
     * @return The ticketDetailCreateTime
     */
    public String getTicketDetailCreateTime() {
        return ticketDetailCreateTime;
    }

    /**
     * @param ticketDetailCreateTime The ticket_detail_create_time
     */
    public void setTicketDetailCreateTime(String ticketDetailCreateTime) {
        this.ticketDetailCreateTime = ticketDetailCreateTime;
    }

    /**
     * @return The ticketDetailUserLabelId
     */
    public String getTicketDetailUserLabelId() {
        return ticketDetailUserLabelId;
    }

    /**
     * @param ticketDetailUserLabelId The ticket_detail_user_label_id
     */
    public void setTicketDetailUserLabelId(String ticketDetailUserLabelId) {
        this.ticketDetailUserLabelId = ticketDetailUserLabelId;
    }

    /**
     * @return The ticketDetailUserLabel
     */
    public String getTicketDetailUserLabel() {
        return ticketDetailUserLabel;
    }

    /**
     * @param ticketDetailUserLabel The ticket_detail_user_label
     */
    public void setTicketDetailUserLabel(String ticketDetailUserLabel) {
        this.ticketDetailUserLabel = ticketDetailUserLabel;
    }

    /**
     * @return The ticketDetailUserImage
     */
    public String getTicketDetailUserImage() {
        return ticketDetailUserImage;
    }

    /**
     * @param ticketDetailUserImage The ticket_detail_user_image
     */
    public void setTicketDetailUserImage(String ticketDetailUserImage) {
        this.ticketDetailUserImage = ticketDetailUserImage;
    }

    /**
     * @return The ticketDetailAttachment
     */
    public List<TicketImageAttachment> getTicketDetailAttachment() {
        return ticketDetailAttachment;
    }

    /**
     * @param ticketDetailAttachment The ticket_detail_attachment
     */
    public void setTicketDetailAttachment(List<TicketImageAttachment> ticketDetailAttachment) {
        this.ticketDetailAttachment = ticketDetailAttachment;
    }

    /**
     * @return The ticketDetailUserId
     */
    public String getTicketDetailUserId() {
        return ticketDetailUserId;
    }

    /**
     * @param ticketDetailUserId The ticket_detail_user_id
     */
    public void setTicketDetailUserId(String ticketDetailUserId) {
        this.ticketDetailUserId = ticketDetailUserId;
    }

    /**
     * @return The ticketDetailNewStatus
     */
    public String getTicketDetailNewStatus() {
        return ticketDetailNewStatus;
    }

    /**
     * @param ticketDetailNewStatus The ticket_detail_new_status
     */
    public void setTicketDetailNewStatus(String ticketDetailNewStatus) {
        this.ticketDetailNewStatus = ticketDetailNewStatus;
    }

    /**
     * @return The ticketDetailMessage
     */
    public String getTicketDetailMessage() {
        return ticketDetailMessage!= null && ticketDetailMessage.equals("0") ? "" : MethodChecker.fromHtml(ticketDetailMessage).toString();
    }

    /**
     * @param ticketDetailMessage The ticket_detail_message
     */
    public void setTicketDetailMessage(String ticketDetailMessage) {
        this.ticketDetailMessage = ticketDetailMessage;
    }

}
