
package com.tokopedia.tkpd.manage.people.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Notification {

    private static final String PARAM_FLAG_ADMIN_MESSAGE = "flag_admin_message";
    private static final String PARAM_FLAG_MESSAGE = "flag_message";
    private static final String PARAM_FLAG_NEWSLETTER = "flag_newsletter";
    private static final String PARAM_FLAG_REVIEW = "flag_review";
    private static final String PARAM_FLAG_TALK_PRODUCT = "flag_talk_product";


    @SerializedName("flag_talk_product")
    @Expose
    private int flagTalkProduct;
    @SerializedName("flag_admin_message")
    @Expose
    private int flagAdminMessage;
    @SerializedName("flag_message")
    @Expose
    private int flagMessage;
    @SerializedName("flag_review")
    @Expose
    private int flagReview;
    @SerializedName("flag_newsletter")
    @Expose
    private int flagNewsletter;

    /**
     * 
     * @return
     *     The flagTalkProduct
     */
    public boolean isTalkProductChecked() {
        return flagTalkProduct == 1;
    }

    /**
     * 
     * @param flagTalkProduct
     *     The flag_talk_product
     */
    public void setFlagTalkProduct(int flagTalkProduct) {
        this.flagTalkProduct = flagTalkProduct;
    }

    /**
     * 
     * @return
     *     The flagAdminMessage
     */
    public boolean isAdminMessageChecked() {
        return flagAdminMessage == 1;
    }

    /**
     * 
     * @param flagAdminMessage
     *     The flag_admin_message
     */
    public void setFlagAdminMessage(int flagAdminMessage) {
        this.flagAdminMessage = flagAdminMessage;
    }

    /**
     * 
     * @return
     *     The flagMessage
     */
    public boolean isMessageChecked() {
        return flagMessage == 1;
    }

    /**
     * 
     * @param flagMessage
     *     The flag_message
     */
    public void setFlagMessage(int flagMessage) {
        this.flagMessage = flagMessage;
    }

    /**
     * 
     * @return
     *     The flagReview
     */
    public boolean isReviewChecked() {
        return flagReview == 1;
    }

    /**
     * 
     * @param flagReview
     *     The flag_review
     */
    public void setFlagReview(int flagReview) {
        this.flagReview = flagReview;
    }

    /**
     * 
     * @return
     *     The flagNewsletter
     */
    public boolean isNewsLetterChecked() {
        return flagNewsletter == 1;
    }

    /**
     * 
     * @param flagNewsletter
     *     The flag_newsletter
     */
    public void setFlagNewsletter(int flagNewsletter) {
        this.flagNewsletter = flagNewsletter;
    }

    public String getFlagTalkProduct() {
        return String.valueOf(flagTalkProduct);
    }

    public String getFlagAdminMessage() {
        return String.valueOf(flagAdminMessage);
    }

    public String getFlagMessage() {
        return String.valueOf(flagMessage);
    }

    public String getFlagReview() {
        return String.valueOf(flagReview);
    }

    public String getFlagNewsletter() {
        return String.valueOf(flagNewsletter);
    }

    public Map<String, String> getParamEditNotification() {
        HashMap<String,String> param = new HashMap<>();
        param.put(PARAM_FLAG_ADMIN_MESSAGE, getFlagAdminMessage());
        param.put(PARAM_FLAG_TALK_PRODUCT, getFlagTalkProduct());
        param.put(PARAM_FLAG_MESSAGE, getFlagMessage());
        param.put(PARAM_FLAG_NEWSLETTER, getFlagNewsletter());
        param.put(PARAM_FLAG_REVIEW, getFlagReview());
        return param;
    }
}
