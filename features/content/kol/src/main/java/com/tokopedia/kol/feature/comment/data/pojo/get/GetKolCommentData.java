
package com.tokopedia.kol.feature.comment.data.pojo.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetKolCommentData {

    @SerializedName("get_user_post_comment")
    @Expose
    private GetUserPostComment getUserPostComment = new GetUserPostComment();

    public GetKolCommentData(GetUserPostComment getUserPostComment) {
        this.getUserPostComment = getUserPostComment;
    }

    public GetUserPostComment getGetUserPostComment() {
        return getUserPostComment;
    }
}
