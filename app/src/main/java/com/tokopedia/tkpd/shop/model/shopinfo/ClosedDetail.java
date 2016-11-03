
package com.tokopedia.tkpd.shop.model.shopinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClosedDetail {

    @SerializedName("until")
    @Expose
    private String until;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("reason")
    @Expose
    private String reason;

    /**
     * 
     * @return
     *     The until
     */
    public String getUntil() {
        return until;
    }

    /**
     * 
     * @param until
     *     The until
     */
    public void setUntil(String until) {
        this.until = until;
    }

    /**
     * 
     * @return
     *     The note
     */
    public String getNote() {
        return note;
    }

    /**
     * 
     * @param note
     *     The note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 
     * @return
     *     The reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 
     * @param reason
     *     The reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
