package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.SerializedName;

public class LikeUpdateResult {
    @SerializedName("code")
    private String code;

    @SerializedName("message_error")
    private String messageError;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    @SerializedName("is_liked")
    private boolean isLiked;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("feedback")
    private String feedback;

    @SerializedName("product_id")
    private int productId;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return
                "LikeUpdateResponse{" +
                        "code = '" + code + '\'' +
                        ",message_error = '" + messageError + '\'' +
                        ",message = '" + message + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
