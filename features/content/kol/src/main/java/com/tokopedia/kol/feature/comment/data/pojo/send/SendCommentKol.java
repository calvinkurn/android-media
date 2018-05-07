package com.tokopedia.kol.feature.comment.data.pojo.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class SendCommentKol {
    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private SendCommentKolData data;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SendCommentKolData getData() {
        return data;
    }

    public void setData(SendCommentKolData data) {
        this.data = data;
    }
}
