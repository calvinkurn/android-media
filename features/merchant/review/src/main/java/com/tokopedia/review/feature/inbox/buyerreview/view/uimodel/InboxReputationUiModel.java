package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel;


import java.util.List;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationUiModel {
    List<InboxReputationItemUiModel> list;
    boolean hasNextPage;


    public InboxReputationUiModel(List<InboxReputationItemUiModel> list, boolean hasNextPage) {
        this.list = list;
        this.hasNextPage = hasNextPage;
    }

    public List<InboxReputationItemUiModel> getList() {
        return list;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
