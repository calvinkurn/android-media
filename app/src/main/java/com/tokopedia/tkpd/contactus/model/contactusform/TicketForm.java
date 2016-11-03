
package com.tokopedia.tkpd.contactus.model.contactusform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TicketForm {

    @SerializedName("list")
    @Expose
    private java.util.List<TicketFormData> list = new ArrayList<TicketFormData>();

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<TicketFormData> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<TicketFormData> list) {
        this.list = list;
    }

}
