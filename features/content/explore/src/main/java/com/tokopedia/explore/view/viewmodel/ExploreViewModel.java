package com.tokopedia.explore.view.viewmodel;

import java.util.List;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreViewModel {
    private List<ExploreImageViewModel> exploreImageViewModelList;
    private List<ExploreCategoryViewModel> tagViewModelList;

    public ExploreViewModel(List<ExploreImageViewModel> exploreImageViewModelList,
                            List<ExploreCategoryViewModel> tagViewModelList) {
        this.exploreImageViewModelList = exploreImageViewModelList;
        this.tagViewModelList = tagViewModelList;
    }

    public List<ExploreImageViewModel> getExploreImageViewModelList() {
        return exploreImageViewModelList;
    }

    public void setExploreImageViewModelList(List<ExploreImageViewModel> exploreImageViewModelList) {
        this.exploreImageViewModelList = exploreImageViewModelList;
    }

    public List<ExploreCategoryViewModel> getTagViewModelList() {
        return tagViewModelList;
    }

    public void setTagViewModelList(List<ExploreCategoryViewModel> tagViewModelList) {
        this.tagViewModelList = tagViewModelList;
    }
}
