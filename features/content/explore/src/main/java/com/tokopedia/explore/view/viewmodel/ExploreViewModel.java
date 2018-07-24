package com.tokopedia.explore.view.viewmodel;

import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

import java.util.List;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreViewModel {
    private List<KolPostViewModel> kolPostViewModelList;
    private List<ExploreCategoryViewModel> tagViewModelList;

    public ExploreViewModel(List<KolPostViewModel> kolPostViewModelList,
                            List<ExploreCategoryViewModel> tagViewModelList) {
        this.kolPostViewModelList = kolPostViewModelList;
        this.tagViewModelList = tagViewModelList;
    }

    public List<KolPostViewModel> getKolPostViewModelList() {
        return kolPostViewModelList;
    }

    public void setKolPostViewModelList(List<KolPostViewModel> kolPostViewModelList) {
        this.kolPostViewModelList = kolPostViewModelList;
    }

    public List<ExploreCategoryViewModel> getTagViewModelList() {
        return tagViewModelList;
    }

    public void setTagViewModelList(List<ExploreCategoryViewModel> tagViewModelList) {
        this.tagViewModelList = tagViewModelList;
    }
}
