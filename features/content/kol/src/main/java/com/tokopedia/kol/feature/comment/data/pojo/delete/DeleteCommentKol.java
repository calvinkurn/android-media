package com.tokopedia.kol.feature.comment.data.pojo.delete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class DeleteCommentKol {
    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("data")
    @Expose
    private DeleteCommentKolData data;

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

    public DeleteCommentKolData getData() {
        return data;
    }

    public void setData(DeleteCommentKolData data) {
        this.data = data;
    }
}
