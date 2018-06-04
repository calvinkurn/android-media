package com.tokopedia.kol.feature.comment.data.pojo.delete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 19/04/18.
 */

public class DeleteCommentKolGraphql {
    @SerializedName("delete_comment_kol")
    @Expose
    private DeleteCommentKol deleteCommentKol;

    public DeleteCommentKol getDeleteCommentKol() {
        return deleteCommentKol;
    }

    public void setDeleteCommentKol(DeleteCommentKol deleteCommentKol) {
        this.deleteCommentKol = deleteCommentKol;
    }
}
