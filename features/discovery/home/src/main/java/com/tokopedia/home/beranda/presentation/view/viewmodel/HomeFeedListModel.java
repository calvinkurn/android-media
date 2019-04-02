package com.tokopedia.home.beranda.presentation.view.viewmodel;

import java.util.List;

public class HomeFeedListModel {
    private List<HomeFeedViewModel> homeFeedViewModels;
    private boolean hasNextPage;

    public List<HomeFeedViewModel> getHomeFeedViewModels() {
        return homeFeedViewModels;
    }

    public void setHomeFeedViewModels(List<HomeFeedViewModel> homeFeedViewModels) {
        this.homeFeedViewModels = homeFeedViewModels;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
