package com.tokopedia.events.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pranaymohapatra on 04/04/18.
 */

public class LikeUpdateResultDomain {
    String message;
    int status;
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
