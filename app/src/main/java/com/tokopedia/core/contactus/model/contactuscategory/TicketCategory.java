
package com.tokopedia.core.contactus.model.contactuscategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TicketCategory {

    @SerializedName("ticket_category_child")
    @Expose
    private java.util.List<TicketCategory> ticketCategoryChild = new ArrayList<>();
    @SerializedName("ticket_category_name")
    @Expose
    private String ticketCategoryName;
    @SerializedName("ticket_category_tree_no")
    @Expose
    private int ticketCategoryTreeNo;
    @SerializedName("ticket_category_description")
    @Expose
    private String ticketCategoryDescription;
    @SerializedName("ticket_category_id")
    @Expose
    private int ticketCategoryId;

    /**
     * 
     * @return
     *     The ticketCategoryChild
     */
    public java.util.List<TicketCategory> getTicketCategoryChild() {
        return ticketCategoryChild;
    }

    /**
     * 
     * @param ticketCategoryChild
     *     The ticket_category_child
     */
    public void setTicketCategoryChild(java.util.List<TicketCategory> ticketCategoryChild) {
        this.ticketCategoryChild = ticketCategoryChild;
    }

    /**
     * 
     * @return
     *     The ticketCategoryName
     */
    public String getTicketCategoryName() {
        return ticketCategoryName;
    }

    /**
     * 
     * @param ticketCategoryName
     *     The ticket_category_name
     */
    public void setTicketCategoryName(String ticketCategoryName) {
        this.ticketCategoryName = ticketCategoryName;
    }

    /**
     * 
     * @return
     *     The ticketCategoryTreeNo
     */
    public int getTicketCategoryTreeNo() {
        return ticketCategoryTreeNo;
    }

    /**
     * 
     * @param ticketCategoryTreeNo
     *     The ticket_category_tree_no
     */
    public void setTicketCategoryTreeNo(int ticketCategoryTreeNo) {
        this.ticketCategoryTreeNo = ticketCategoryTreeNo;
    }

    /**
     * 
     * @return
     *     The ticketCategoryDescription
     */
    public String getTicketCategoryDescription() {
        return ticketCategoryDescription;
    }

    /**
     * 
     * @param ticketCategoryDescription
     *     The ticket_category_description
     */
    public void setTicketCategoryDescription(String ticketCategoryDescription) {
        this.ticketCategoryDescription = ticketCategoryDescription;
    }

    /**
     * 
     * @return
     *     The ticketCategoryId
     */
    public int getTicketCategoryId() {
        return ticketCategoryId;
    }

    /**
     * 
     * @param ticketCategoryId
     *     The ticket_category_id
     */
    public void setTicketCategoryId(int ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
    }

}
