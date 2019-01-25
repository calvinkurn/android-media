
package com.tokopedia.core.shop.model.uploadShopLogoData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Upload {

    @SerializedName("message_status")
    @Expose
    String messageStatus;
    @SerializedName("src")
    @Expose
    String src;
    @SerializedName("success")
    @Expose
    String success;

    /**
     *
     * @return
     *     The messageStatus
     */
    public String getMessageStatus() {
        return messageStatus;
    }

    /**
     *
     * @param messageStatus
     *     The message_status
     */
    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     *
     * @return
     *     The src
     */
    public String getSrc() {
        return src;
    }

    /**
     *
     * @param src
     *     The src
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     *
     * @return
     *     The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     *     The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

}
