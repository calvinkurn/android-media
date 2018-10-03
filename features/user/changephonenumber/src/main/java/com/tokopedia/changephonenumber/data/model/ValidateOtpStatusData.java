package com.tokopedia.changephonenumber.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by alvinatin on 11/05/18.
 */

public class ValidateOtpStatusData {
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("data")
    @Expose
    private Data data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("is_success")
        @Expose
        private Integer isSuccess;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("otp_type")
        @Expose
        private String otpType;
        @SerializedName("otp_mode")
        @Expose
        private String otpMode;
        @SerializedName("is_validate")
        @Expose
        private Boolean isValidate;
        @SerializedName("msisdn")
        @Expose
        private String msisdn;

        public Integer getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(Integer isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOtpType() {
            return otpType;
        }

        public void setOtpType(String otpType) {
            this.otpType = otpType;
        }

        public String getOtpMode() {
            return otpMode;
        }

        public void setOtpMode(String otpMode) {
            this.otpMode = otpMode;
        }

        public Boolean getValidate() {
            return isValidate;
        }

        public void setValidate(Boolean validate) {
            isValidate = validate;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }
    }

    public class Header{
        @SerializedName("process_time")
        @Expose
        private Double processTime;
        @SerializedName("messages")
        @Expose
        private List<String> messages = null;
        @SerializedName("reason")
        @Expose
        private String reason;
        @SerializedName("error_code")
        @Expose
        private String errorCode;

        public Double getProcessTime() {
            return processTime;
        }

        public void setProcessTime(Double processTime) {
            this.processTime = processTime;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
