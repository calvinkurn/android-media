
package com.tokopedia.tkpd.contactus.model.contactuscategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ContactUsCategory {

    @SerializedName("default_position")
    @Expose
    private java.util.List<DefaultPosition> defaultPosition = new ArrayList<DefaultPosition>();
    @SerializedName("list")
    @Expose
    private java.util.List<TicketCategory> list = new ArrayList<TicketCategory>();

    /**
     * 
     * @return
     *     The defaultPosition
     */
    public java.util.List<DefaultPosition> getDefaultPosition() {
        return defaultPosition;
    }

    /**
     * 
     * @param defaultPosition
     *     The default_position
     */
    public void setDefaultPosition(java.util.List<DefaultPosition> defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<TicketCategory> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<TicketCategory> list) {
        this.list = list;
    }

}
