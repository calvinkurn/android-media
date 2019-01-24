
package com.tokopedia.core.shop.model.shopData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ClosedDetail {

    @SerializedName("until")
    @Expose
    String until;
    @SerializedName("reason")
    @Expose
    String reason;
    @SerializedName("note")
    @Expose
    String note;

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

}
