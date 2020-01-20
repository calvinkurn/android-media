
package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModeList {

    @SerializedName("mode_code")
    @Expose
    private int modeCode;
    @SerializedName("mode_text")
    @Expose
    private String modeText;
    @SerializedName("otp_list_text")
    @Expose
    private String otpListText;
    @SerializedName("after_otp_list_text")
    @Expose
    private String afterOtpListText;
    @SerializedName("after_otp_list_text_html")
    @Expose
    private String newAfterOtpListText;
    @SerializedName("otp_list_img_url")
    @Expose
    private String otpListImgUrl;
    @SerializedName("using_pop_up")
    @Expose
    private boolean usingPopUp;
    @SerializedName("pop_up_header")
    @Expose
    private String popUpHeader;
    @SerializedName("pop_up_body")
    @Expose
    private String popUpBody;
    @SerializedName("number_otp_digit")
    @Expose
    private Integer numberOtpDigit;

    public int getModeCode() {
        return modeCode;
    }

    public String getModeText() {
        return modeText;
    }

    public String getOtpListText() {
        return otpListText;
    }

    public String getAfterOtpListText() {
        return afterOtpListText;
    }

    public String getOtpListImgUrl() {
        return otpListImgUrl;
    }

    public String getNewAfterOtpListText() {
        return newAfterOtpListText;
    }

    public boolean isUsingPopUp() {
        return usingPopUp;
    }

    public String getPopUpHeader() {
        return popUpHeader;
    }

    public String getPopUpBody() {
        return popUpBody;
    }

    public Integer getNumberOtpDigit() {
        return numberOtpDigit;
    }
}
