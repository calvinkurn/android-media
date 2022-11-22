package com.tokopedia.kol.feature.comment.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    private final String lastcursor;
    private final boolean hasNextPage;
    private List<KolCommentNewModel> listNewComments;
    private KolCommentHeaderNewModel headerNewModel;

    public KolComments(String lastcursor, boolean hasNextPage,
                       ArrayList<KolCommentNewModel> listNewComments,
                       KolCommentHeaderNewModel kolCommentHeaderNewModel) {
        this.lastcursor = lastcursor;
        this.hasNextPage = hasNextPage;
        this.listNewComments = listNewComments;
        this.headerNewModel = kolCommentHeaderNewModel;
    }

    public String getLastcursor() {
        return lastcursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public List<KolCommentNewModel> getListNewComments() {
        return listNewComments;
    }

    public KolCommentHeaderNewModel getHeaderNewModel() {
        return headerNewModel;
    }
}
