
package com.tokopedia.contactus.inboxticket.model.inboxticketdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TicketReply {

    @SerializedName("ticket_reply_data")
    @Expose
    private List<TicketReplyDatum> ticketReplyData = new ArrayList<TicketReplyDatum>();
    @SerializedName("ticket_reply_total_data")
    @Expose
    private int ticketReplyTotalData;
    @SerializedName("ticket_reply_total_page")
    @Expose
    private int ticketReplyTotalPage;

    /**
     * 
     * @return
     *     The ticketReplyData
     */
    public List<TicketReplyDatum> getTicketReplyData() {
        return ticketReplyData;
    }

    /**
     * 
     * @param ticketReplyData
     *     The ticket_reply_data
     */
    public void setTicketReplyData(List<TicketReplyDatum> ticketReplyData) {
        this.ticketReplyData = ticketReplyData;
    }

    /**
     * 
     * @return
     *     The ticketReplyTotalData
     */
    public int getTicketReplyTotalData() {
        return ticketReplyTotalData;
    }

    /**
     * 
     * @param ticketReplyTotalData
     *     The ticket_reply_total_data
     */
    public void setTicketReplyTotalData(int ticketReplyTotalData) {
        this.ticketReplyTotalData = ticketReplyTotalData;
    }

    /**
     * 
     * @return
     *     The ticketReplyTotalPage
     */
    public int getTicketReplyTotalPage() {
        return ticketReplyTotalPage;
    }

    /**
     * 
     * @param ticketReplyTotalPage
     *     The ticket_reply_total_page
     */
    public void setTicketReplyTotalPage(int ticketReplyTotalPage) {
        this.ticketReplyTotalPage = ticketReplyTotalPage;
    }

}
