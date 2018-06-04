package com.tokopedia.kol.feature.comment.data.pojo.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class SendCommentKolGraphql {
    @SerializedName("create_comment_kol")
    @Expose
    private SendCommentKol createCommentKol;

    public SendCommentKol getCreateCommentKol() {
        return createCommentKol;
    }

    public void setCreateCommentKol(SendCommentKol createCommentKol) {
        this.createCommentKol = createCommentKol;
    }
}
