package com.tokopedia.kol.feature.comment.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    private final String lastcursor;
    private final boolean hasNextPage;
    private List<KolCommentViewModel> listComments;
    private List<KolCommentNewModel> listNewComments;
    private final KolCommentHeaderViewModel headerViewModel;
    private KolCommentHeaderNewModel headerNewModel;

    public KolComments(String lastcursor, boolean hasNextPage,
                       List<KolCommentViewModel> listComments,
                       ArrayList<KolCommentNewModel> listNewComments, KolCommentHeaderViewModel headerViewModel, KolCommentHeaderNewModel kolCommentHeaderNewModel) {
        this.lastcursor = lastcursor;
        this.hasNextPage = hasNextPage;
        this.listComments = listComments;
        this.listNewComments = listNewComments;
        this.headerViewModel = headerViewModel;
        this.headerNewModel = kolCommentHeaderNewModel;
    }

    public String getLastcursor() {
        return lastcursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public List<KolCommentViewModel> getListComments() {
        return listComments;
    }

    public List<KolCommentNewModel> getListNewComments() {
        return listNewComments;
    }

    public KolCommentHeaderViewModel getHeaderViewModel() {
        return headerViewModel;
    }

    public KolCommentHeaderNewModel getHeaderNewModel() {
        return headerNewModel;
    }
}
