
package com.tokopedia.contactus.inboxticket.model.inboxticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;

public class InboxTicketItem {

    @SerializedName("ticket_create_time_fmt2")
    @Expose
    private String ticketCreateTimeFmt2;
    @SerializedName("ticket_first_message_name")
    @Expose
    private String ticketFirstMessageName;
    @SerializedName("ticket_update_time_fmt2")
    @Expose
    private String ticketUpdateTimeFmt2;
    @SerializedName("ticket_create_time_fmt")
    @Expose
    private String ticketCreateTimeFmt;
    @SerializedName("ticket_update_time_fmt")
    @Expose
    private String ticketUpdateTimeFmt;
    @SerializedName("ticket_status")
    @Expose
    private String ticketStatus;
    @SerializedName("ticket_read_status")
    @Expose
    private int ticketReadStatus;
    @SerializedName("ticket_update_is_cs")
    @Expose
    private int ticketUpdateIsCs;
    @SerializedName("ticket_inbox_id")
    @Expose
    private String ticketInboxId;
    @SerializedName("ticket_update_by_url")
    @Expose
    private String ticketUpdateByUrl;
    @SerializedName("ticket_category")
    @Expose
    private String ticketCategory;
    @SerializedName("ticket_title")
    @Expose
    private String ticketTitle;
    @SerializedName("ticket_total_message")
    @Expose
    private int ticketTotalMessage;
    @SerializedName("ticket_show_more")
    @Expose
    private String ticketShowMore;
    @SerializedName("ticket_respond_status")
    @Expose
    private String ticketRespondStatus;
    @SerializedName("ticket_is_replied")
    @Expose
    private String ticketIsReplied;
    @SerializedName("ticket_url_detail")
    @Expose
    private String ticketUrlDetail;
    @SerializedName("ticket_user_involve")
    @Expose
    private java.util.List<TicketUserInvolve> ticketUserInvolve = new ArrayList<TicketUserInvolve>();
    @SerializedName("ticket_update_by_id")
    @Expose
    private String ticketUpdateById;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("ticket_update_by_name")
    @Expose
    private String ticketUpdateByName;

    /**
     * @return The ticketCreateTimeFmt2
     */
    public String getTicketCreateTimeFmt2() {
        return ticketCreateTimeFmt2;
    }

    /**
     * @param ticketCreateTimeFmt2 The ticket_create_time_fmt2
     */
    public void setTicketCreateTimeFmt2(String ticketCreateTimeFmt2) {
        this.ticketCreateTimeFmt2 = ticketCreateTimeFmt2;
    }

    /**
     * @return The ticketFirstMessageName
     */
    public String getTicketFirstMessageName() {
        return ticketFirstMessageName;
    }

    /**
     * @param ticketFirstMessageName The ticket_first_message_name
     */
    public void setTicketFirstMessageName(String ticketFirstMessageName) {
        this.ticketFirstMessageName = ticketFirstMessageName;
    }

    /**
     * @return The ticketUpdateTimeFmt2
     */
    public String getTicketUpdateTimeFmt2() {
        return ticketUpdateTimeFmt2;
    }

    /**
     * @param ticketUpdateTimeFmt2 The ticket_update_time_fmt2
     */
    public void setTicketUpdateTimeFmt2(String ticketUpdateTimeFmt2) {
        this.ticketUpdateTimeFmt2 = ticketUpdateTimeFmt2;
    }

    /**
     * @return The ticketCreateTimeFmt
     */
    public String getTicketCreateTimeFmt() {
        return ticketCreateTimeFmt;
    }

    /**
     * @param ticketCreateTimeFmt The ticket_create_time_fmt
     */
    public void setTicketCreateTimeFmt(String ticketCreateTimeFmt) {
        this.ticketCreateTimeFmt = ticketCreateTimeFmt;
    }

    /**
     * @return The ticketUpdateTimeFmt
     */
    public String getTicketUpdateTimeFmt() {
        return ticketUpdateTimeFmt;
    }

    /**
     * @param ticketUpdateTimeFmt The ticket_update_time_fmt
     */
    public void setTicketUpdateTimeFmt(String ticketUpdateTimeFmt) {
        this.ticketUpdateTimeFmt = ticketUpdateTimeFmt;
    }

    /**
     * @return The ticketStatus
     */
    public String getTicketStatus() {
        return ticketStatus;
    }

    /**
     * @param ticketStatus The ticket_status
     */
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    /**
     * @return The ticketReadStatus
     */
    public int getTicketReadStatus() {
        return ticketReadStatus;
    }

    /**
     * @param ticketReadStatus The ticket_read_status
     */
    public void setTicketReadStatus(int ticketReadStatus) {
        this.ticketReadStatus = ticketReadStatus;
    }

    /**
     * @return The ticketUpdateIsCs
     */
    public int getTicketUpdateIsCs() {
        return ticketUpdateIsCs;
    }

    /**
     * @param ticketUpdateIsCs The ticket_update_is_cs
     */
    public void setTicketUpdateIsCs(int ticketUpdateIsCs) {
        this.ticketUpdateIsCs = ticketUpdateIsCs;
    }

    /**
     * @return The ticketInboxId
     */
    public String getTicketInboxId() {
        return ticketInboxId;
    }

    /**
     * @param ticketInboxId The ticket_inbox_id
     */
    public void setTicketInboxId(String ticketInboxId) {
        this.ticketInboxId = ticketInboxId;
    }

    /**
     * @return The ticketUpdateByUrl
     */
    public String getTicketUpdateByUrl() {
        return ticketUpdateByUrl;
    }

    /**
     * @param ticketUpdateByUrl The ticket_update_by_url
     */
    public void setTicketUpdateByUrl(String ticketUpdateByUrl) {
        this.ticketUpdateByUrl = ticketUpdateByUrl;
    }

    /**
     * @return The ticketCategory
     */
    public String getTicketCategory() {
        return ticketCategory;
    }

    /**
     * @param ticketCategory The ticket_category
     */
    public void setTicketCategory(String ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    /**
     * @return The ticketTitle
     */
    public String getTicketTitle() {
        return MethodChecker.fromHtml(ticketTitle).toString();
    }

    /**
     * @param ticketTitle The ticket_title
     */
    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    /**
     * @return The ticketTotalMessage
     */
    public int getTicketTotalMessage() {
        return ticketTotalMessage;
    }

    /**
     * @param ticketTotalMessage The ticket_total_message
     */
    public void setTicketTotalMessage(int ticketTotalMessage) {
        this.ticketTotalMessage = ticketTotalMessage;
    }

    /**
     * @return The ticketShowMore
     */
    public String getTicketShowMore() {
        return ticketShowMore;
    }

    /**
     * @param ticketShowMore The ticket_show_more
     */
    public void setTicketShowMore(String ticketShowMore) {
        this.ticketShowMore = ticketShowMore;
    }

    /**
     * @return The ticketRespondStatus
     */
    public String getTicketRespondStatus() {
        return ticketRespondStatus;
    }

    /**
     * @param ticketRespondStatus The ticket_respond_status
     */
    public void setTicketRespondStatus(String ticketRespondStatus) {
        this.ticketRespondStatus = ticketRespondStatus;
    }

    /**
     * @return The ticketIsReplied
     */
    public String getTicketIsReplied() {
        return ticketIsReplied;
    }

    /**
     * @param ticketIsReplied The ticket_is_replied
     */
    public void setTicketIsReplied(String ticketIsReplied) {
        this.ticketIsReplied = ticketIsReplied;
    }

    /**
     * @return The ticketUrlDetail
     */
    public String getTicketUrlDetail() {
        return ticketUrlDetail;
    }

    /**
     * @param ticketUrlDetail The ticket_url_detail
     */
    public void setTicketUrlDetail(String ticketUrlDetail) {
        this.ticketUrlDetail = ticketUrlDetail;
    }

    /**
     * @return The ticketUserInvolve
     */
    public java.util.List<TicketUserInvolve> getTicketUserInvolve() {
        return ticketUserInvolve;
    }

    /**
     * @param ticketUserInvolve The ticket_user_involve
     */
    public void setTicketUserInvolve(java.util.List<TicketUserInvolve> ticketUserInvolve) {
        this.ticketUserInvolve = ticketUserInvolve;
    }

    /**
     * @return The ticketUpdateById
     */
    public String getTicketUpdateById() {
        return ticketUpdateById;
    }

    /**
     * @param ticketUpdateById The ticket_update_by_id
     */
    public void setTicketUpdateById(String ticketUpdateById) {
        this.ticketUpdateById = ticketUpdateById;
    }

    /**
     * @return The ticketId
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * @param ticketId The ticket_id
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * @return The ticketUpdateByName
     */
    public String getTicketUpdateByName() {
        return ticketUpdateByName;
    }

    /**
     * @param ticketUpdateByName The ticket_update_by_name
     */
    public void setTicketUpdateByName(String ticketUpdateByName) {
        this.ticketUpdateByName = ticketUpdateByName;
    }

}
