
package com.tokopedia.kol.feature.comment.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetKolCommentData {

    @SerializedName("get_user_post_comment")
    @Expose
    private GetUserPostComment getUserPostComment;

    public GetUserPostComment getGetUserPostComment() {
        return getUserPostComment;
    }

    public void setGetUserPostComment(GetUserPostComment getUserPostComment) {
        this.getUserPostComment = getUserPostComment;
    }

}
