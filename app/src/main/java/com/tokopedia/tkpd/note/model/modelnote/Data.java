
package com.tokopedia.tkpd.note.model.modelnote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("detail")
    @Expose
    private Detail detail;

    /**
     * 
     * @return
     *     The detail
     */
    public Detail getDetail() {
        return detail;
    }

    /**
     * 
     * @param detail
     *     The detail
     */
    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}
