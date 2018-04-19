
package com.tokopedia.kol.feature.comment.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserPostComment {

    @SerializedName("postKol")
    @Expose
    private PostKol postKol;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;
    @SerializedName("lastCursor")
    @Expose
    private String lastCursor;
    @SerializedName("error")
    @Expose
    private Object error;

    public PostKol getPostKol() {
        return postKol;
    }

    public void setPostKol(PostKol postKol) {
        this.postKol = postKol;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
