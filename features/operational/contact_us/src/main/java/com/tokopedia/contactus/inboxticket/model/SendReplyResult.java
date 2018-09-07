
package com.tokopedia.contactus.inboxticket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendReplyResult {

    @SerializedName("post_key")
    @Expose
    private String postKey;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    /**
     * 
     * @return
     *     The postKey
     */
    public String getPostKey() {
        return postKey;
    }

    /**
     * 
     * @param postKey
     *     The post_key
     */
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    /**
     * 
     * @return
     *     The isSuccess
     */
    public int getIsSuccess() {
        return isSuccess;
    }

    /**
     * 
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

}
