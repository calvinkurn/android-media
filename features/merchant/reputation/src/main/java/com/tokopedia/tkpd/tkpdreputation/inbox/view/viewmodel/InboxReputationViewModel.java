package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;


import java.util.List;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationViewModel {
    List<InboxReputationItemViewModel> list;
    boolean hasNextPage;


    public InboxReputationViewModel(List<InboxReputationItemViewModel> list, boolean hasNextPage) {
        this.list = list;
        this.hasNextPage = hasNextPage;
    }

    public List<InboxReputationItemViewModel> getList() {
        return list;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
