
package com.tokopedia.contactus.inboxticket.model.replyticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyResult {

    @SerializedName("ticket_detail")
    @Expose
    private TicketDetail ticketDetail;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("ticket_detail_id")
    @Expose
    private long ticketDetailId;

    /**
     * 
     * @return
     *     The ticketDetail
     */
    public TicketDetail getTicketDetail() {
        return ticketDetail;
    }

    /**
     * 
     * @param ticketDetail
     *     The ticket_detail
     */
    public void setTicketDetail(TicketDetail ticketDetail) {
        this.ticketDetail = ticketDetail;
    }

    public ReplyResult withTicketDetail(TicketDetail ticketDetail) {
        this.ticketDetail = ticketDetail;
        return this;
    }

    /**
     * 
     * @return
     *     The attachment
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 
     * @param attachment
     *     The attachment
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public ReplyResult withAttachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    /**
     * 
     * @return
     *     The isSuccess
     */
    public int getIsSuccess() {
        return isSuccess;
    }

    /**
     * 
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ReplyResult withIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
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

    public ReplyResult withTicketDetailId(int ticketDetailId) {
        this.ticketDetailId = ticketDetailId;
        return this;
    }

}
