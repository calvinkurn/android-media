
package com.tokopedia.contactus.inboxticket.model.replyticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketImageAttachment {

    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("img_src")
    @Expose
    private String imgSrc;

    /**
     * 
     * @return
     *     The imgLink
     */
    public String getImgLink() {
        return imgLink;
    }

    /**
     * 
     * @param imgLink
     *     The img_link
     */
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    /**
     * 
     * @return
     *     The imgSrc
     */
    public String getImgSrc() {
        return imgSrc;
    }

    /**
     * 
     * @param imgSrc
     *     The img_src
     */
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

}
