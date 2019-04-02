
package com.tokopedia.kol.feature.comment.data.pojo.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetUserPostComment {

    @SerializedName("postKol")
    @Expose
    private PostKol postKol = new PostKol();
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<>();
    @SerializedName("lastCursor")
    @Expose
    private String lastCursor = "";
    @SerializedName("error")
    @Expose
    private String error = "";

    public GetUserPostComment() {
    }

    public GetUserPostComment(PostKol postKol, List<Comment> comments, String lastCursor, String
            error) {
        this.postKol = postKol;
        this.comments = comments;
        this.lastCursor = lastCursor;
        this.error = error;
    }

    public PostKol getPostKol() {
        return postKol;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public String getError() {
        return error;
    }
}
