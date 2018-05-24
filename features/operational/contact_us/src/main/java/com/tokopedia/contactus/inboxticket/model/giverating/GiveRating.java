
package com.tokopedia.contactus.inboxticket.model.giverating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiveRating {

    @SerializedName("ticket_detail_id")
    @Expose
    private String ticketDetailId;
    @SerializedName("ticket_detail")
    @Expose
    private TicketDetail ticketDetail;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

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

}
