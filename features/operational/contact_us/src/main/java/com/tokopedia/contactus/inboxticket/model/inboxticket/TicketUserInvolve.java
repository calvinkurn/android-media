
package com.tokopedia.contactus.inboxticket.model.inboxticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketUserInvolve {

    @SerializedName("full_name")
    @Expose
    private String fullName;

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
