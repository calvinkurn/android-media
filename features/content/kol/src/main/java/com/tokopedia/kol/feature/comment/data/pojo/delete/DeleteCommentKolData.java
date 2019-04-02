package com.tokopedia.kol.feature.comment.data.pojo.delete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class DeleteCommentKolData {
    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("success")
    @Expose
    private Integer success;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
