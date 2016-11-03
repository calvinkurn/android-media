
package com.tokopedia.tkpd.msisdn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MSISDN {

    @SerializedName("show_dialog")
    @Expose
    private int showDialog;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;

    /**
     * 
     * @return
     *     The showDialog
     */
    public int getShowDialog() {
        return showDialog;
    }

    /**
     * 
     * @param showDialog
     *     The show_dialog
     */
    public void setShowDialog(int showDialog) {
        this.showDialog = showDialog;
    }

    /**
     * 
     * @return
     *     The userPhone
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * 
     * @param userPhone
     *     The user_phone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * 
     * @return
     *     The isVerified
     */
    public String getIsVerified() {
        return isVerified;
    }

    /**
     * 
     * @param isVerified
     *     The is_verified
     */
    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public boolean isVerified() {
        return isVerified.equals("1");
    }
}
