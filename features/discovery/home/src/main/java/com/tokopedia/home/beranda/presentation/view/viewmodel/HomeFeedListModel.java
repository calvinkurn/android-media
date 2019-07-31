package com.tokopedia.home.beranda.presentation.view.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;

import java.util.List;

public class HomeFeedListModel {
    private List<Visitable<HomeFeedTypeFactory>> homeFeedViewModels;
    private boolean hasNextPage;

    public HomeFeedListModel(List<Visitable<HomeFeedTypeFactory>> homeFeedViewModels, boolean hasNextPage) {
        this.homeFeedViewModels = homeFeedViewModels;
        this.hasNextPage = hasNextPage;
    }

    public List<Visitable<HomeFeedTypeFactory>> getHomeFeedViewModels() {
        return homeFeedViewModels;
    }

    public void setHomeFeedViewModels(List<Visitable<HomeFeedTypeFactory>> homeFeedViewModels) {
        this.homeFeedViewModels = homeFeedViewModels;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
