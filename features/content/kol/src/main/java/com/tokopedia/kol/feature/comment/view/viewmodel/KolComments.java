package com.tokopedia.kol.feature.comment.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    private final String lastcursor;
    private final boolean hasNextPage;
    private ArrayList<KolCommentViewModel> listComments;
    private KolCommentHeaderViewModel headerViewModel;

    public KolComments(String lastcursor, boolean hasNextPage,
                       ArrayList<KolCommentViewModel> listComments,
                       KolCommentHeaderViewModel headerViewModel) {
        this.lastcursor = lastcursor;
        this.hasNextPage = hasNextPage;
        this.listComments = listComments;
        this.headerViewModel = headerViewModel;
    }

    public String getLastcursor() {
        return lastcursor;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public ArrayList<KolCommentViewModel> getListComments() {
        return listComments;
    }

    public KolCommentHeaderViewModel getHeaderViewModel() {
        return headerViewModel;
    }
}
