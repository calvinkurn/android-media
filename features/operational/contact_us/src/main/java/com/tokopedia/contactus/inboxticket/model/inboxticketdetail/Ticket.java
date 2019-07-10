
package com.tokopedia.contactus.inboxticket.model.inboxticketdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

    @SerializedName("ticket_first_message_name")
    @Expose
    private String ticketFirstMessageName;
    @SerializedName("ticket_update_time")
    @Expose
    private String ticketUpdateTime;
    @SerializedName("ticket_create_time_fmt")
    @Expose
    private String ticketCreateTimeFmt;
    @SerializedName("ticket_update_time_fmt")
    @Expose
    private String ticketUpdateTimeFmt;
    @SerializedName("ticket_first_message")
    @Expose
    private String ticketFirstMessage;
    @SerializedName("ticket_show_reopen_btn")
    @Expose
    private int ticketShowReopenBtn;
    @SerializedName("ticket_attachment")
    @Expose
    private List<TicketImageAttachment> ticketAttachment = new ArrayList<TicketImageAttachment>();
    @SerializedName("ticket_status")
    @Expose
    private int ticketStatus;
    @SerializedName("ticket_read_status")
    @Expose
    private int ticketReadStatus;
    @SerializedName("ticket_user_label_id")
    @Expose
    private String ticketUserLabelId;
    @SerializedName("ticket_update_is_cs")
    @Expose
    private int ticketUpdateIsCs;
    @SerializedName("ticket_inbox_id")
    @Expose
    private String ticketInboxId;
    @SerializedName("ticket_user_label")
    @Expose
    private String ticketUserLabel;
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
    @SerializedName("ticket_respond_status")
    @Expose
    private int ticketRespondStatus;
    @SerializedName("ticket_is_replied")
    @Expose
    private int ticketIsReplied;
    @SerializedName("ticket_first_message_image")
    @Expose
    private String ticketFirstMessageImage;
    @SerializedName("ticket_create_time")
    @Expose
    private String ticketCreateTime;
    @SerializedName("ticket_url_detail")
    @Expose
    private String ticketUrlDetail;
    @SerializedName("ticket_update_by_id")
    @Expose
    private String ticketUpdateById;
    @SerializedName("ticket_invoice_ref_num")
    @Expose
    private String ticketInvoiceRefNum;
    @SerializedName("ticket_update_by_name")
    @Expose
    private String ticketUpdateByName;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;

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
     * @return The ticketUpdateTime
     */
    public String getTicketUpdateTime() {
        return ticketUpdateTime;
    }

    /**
     * @param ticketUpdateTime The ticket_update_time
     */
    public void setTicketUpdateTime(String ticketUpdateTime) {
        this.ticketUpdateTime = ticketUpdateTime;
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
     * @return The ticketFirstMessage
     */
    public String getTicketFirstMessage() {
        return ticketFirstMessage == null ? "" : MethodChecker.fromHtml(ticketFirstMessage).toString();
    }

    /**
     * @param ticketFirstMessage The ticket_first_message
     */
    public void setTicketFirstMessage(String ticketFirstMessage) {
        this.ticketFirstMessage = ticketFirstMessage;
    }

    /**
     * @return The ticketShowReopenBtn
     */
    public boolean getTicketShowReopenBtn() {
        return ticketShowReopenBtn == 1;
    }

    /**
     * @param ticketShowReopenBtn The ticket_show_reopen_btn
     */
    public void setTicketShowReopenBtn(int ticketShowReopenBtn) {
        this.ticketShowReopenBtn = ticketShowReopenBtn;
    }

    /**
     * @return The ticketAttachment
     */
    public List<TicketImageAttachment> getTicketAttachment() {
        return ticketAttachment;
    }

    /**
     * @param ticketAttachment The ticket_attachment
     */
    public void setTicketAttachment(List<TicketImageAttachment> ticketAttachment) {
        this.ticketAttachment = ticketAttachment;
    }

    /**
     * @return The ticketStatus
     */
    public int getTicketStatus() {
        return ticketStatus;
    }

    /**
     * @param ticketStatus The ticket_status
     */
    public void setTicketStatus(int ticketStatus) {
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
     * @return The ticketUserLabelId
     */
    public String getTicketUserLabelId() {
        return ticketUserLabelId;
    }

    /**
     * @param ticketUserLabelId The ticket_user_label_id
     */
    public void setTicketUserLabelId(String ticketUserLabelId) {
        this.ticketUserLabelId = ticketUserLabelId;
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
     * @return The ticketUserLabel
     */
    public String getTicketUserLabel() {
        return ticketUserLabel;
    }

    /**
     * @param ticketUserLabel The ticket_user_label
     */
    public void setTicketUserLabel(String ticketUserLabel) {
        this.ticketUserLabel = ticketUserLabel;
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
        return ticketTitle;
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
     * @return The ticketRespondStatus
     */
    public int getTicketRespondStatus() {
        return ticketRespondStatus;
    }

    /**
     * @param ticketRespondStatus The ticket_respond_status
     */
    public void setTicketRespondStatus(int ticketRespondStatus) {
        this.ticketRespondStatus = ticketRespondStatus;
    }

    /**
     * @return The ticketIsReplied
     */
    public boolean getTicketIsReplied() {
        return ticketIsReplied == 1;
    }

    /**
     * @param ticketIsReplied The ticket_is_replied
     */
    public void setTicketIsReplied(int ticketIsReplied) {
        this.ticketIsReplied = ticketIsReplied;
    }

    /**
     * @return The ticketFirstMessageImage
     */
    public String getTicketFirstMessageImage() {
        return ticketFirstMessageImage;
    }

    /**
     * @param ticketFirstMessageImage The ticket_first_message_image
     */
    public void setTicketFirstMessageImage(String ticketFirstMessageImage) {
        this.ticketFirstMessageImage = ticketFirstMessageImage;
    }

    /**
     * @return The ticketCreateTime
     */
    public String getTicketCreateTime() {
        return ticketCreateTime;
    }

    /**
     * @param ticketCreateTime The ticket_create_time
     */
    public void setTicketCreateTime(String ticketCreateTime) {
        this.ticketCreateTime = ticketCreateTime;
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
     * @return The ticketInvoiceRefNum
     */
    public String getTicketInvoiceRefNum() {
        return ticketInvoiceRefNum;
    }

    /**
     * @param ticketInvoiceRefNum The ticket_invoice_ref_num
     */
    public void setTicketInvoiceRefNum(String ticketInvoiceRefNum) {
        this.ticketInvoiceRefNum = ticketInvoiceRefNum;
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

}
