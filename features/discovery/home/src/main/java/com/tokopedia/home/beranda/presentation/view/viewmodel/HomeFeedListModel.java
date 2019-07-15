package com.tokopedia.home.beranda.presentation.view.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

public class HomeFeedListModel {
    private List<Visitable> homeFeedViewModels;
    private boolean hasNextPage;

    public HomeFeedListModel(List<Visitable> homeFeedViewModels, boolean hasNextPage) {
        this.homeFeedViewModels = homeFeedViewModels;
        this.hasNextPage = hasNextPage;
    }

    public List<Visitable> getHomeFeedViewModels() {
        return homeFeedViewModels;
    }

    public void setHomeFeedViewModels(List<Visitable> homeFeedViewModels) {
        this.homeFeedViewModels = homeFeedViewModels;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
