package com.tokopedia.core.shop.model.uploadShopLogoData;

/**
 * Created by Toped10 on 5/26/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Image {

    @SerializedName("message_status")
    @Expose
    String messageStatus;
    @SerializedName("pic_code")
    @Expose
    String picCode;
    @SerializedName("pic_src")
    @Expose
    String picSrc;
    @SerializedName("success")
    @Expose
    String success;

    /**
     *
     * @return
     * The messageStatus
     */
    public String getMessageStatus() {
        return messageStatus;
    }

    /**
     *
     * @param messageStatus
     * The message_status
     */
    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     *
     * @return
     * The picCode
     */
    public String getPicCode() {
        return picCode;
    }

    /**
     *
     * @param picCode
     * The pic_code
     */
    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    /**
     *
     * @return
     * The picSrc
     */
    public String getPicSrc() {
        return picSrc;
    }

    /**
     *
     * @param picSrc
     * The pic_src
     */
    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    /**
     *
     * @return
     * The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

}