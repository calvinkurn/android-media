package com.tokopedia.kol.feature.comment.view.viewmodel;

import java.util.List;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    private final String lastcursor;
    private final boolean hasNextPage;
    private List<KolCommentViewModel> listComments;
    private KolCommentHeaderViewModel headerViewModel;

    public KolComments(String lastcursor, boolean hasNextPage,
                       List<KolCommentViewModel> listComments,
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

    public List<KolCommentViewModel> getListComments() {
        return listComments;
    }

    public KolCommentHeaderViewModel getHeaderViewModel() {
        return headerViewModel;
    }
}
