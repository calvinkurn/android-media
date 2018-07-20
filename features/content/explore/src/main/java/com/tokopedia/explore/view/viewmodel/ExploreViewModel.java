package com.tokopedia.explore.view.viewmodel;

import java.util.List;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreViewModel {
    private List<ExploreImageViewModel> imageViewModelList;
    private List<ExploreTagViewModel> tagViewModelList;

    public ExploreViewModel(List<ExploreImageViewModel> imageViewModelList,
                            List<ExploreTagViewModel> tagViewModelList) {
        this.imageViewModelList = imageViewModelList;
        this.tagViewModelList = tagViewModelList;
    }

    public List<ExploreImageViewModel> getImageViewModelList() {
        return imageViewModelList;
    }

    public void setImageViewModelList(List<ExploreImageViewModel> imageViewModelList) {
        this.imageViewModelList = imageViewModelList;
    }

    public List<ExploreTagViewModel> getTagViewModelList() {
        return tagViewModelList;
    }

    public void setTagViewModelList(List<ExploreTagViewModel> tagViewModelList) {
        this.tagViewModelList = tagViewModelList;
    }
}
