package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

import java.util.List;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailViewModel {
    private KolPostViewModel kolPostViewModel;
    private List<KolCommentViewModel> kolCommentViewModels;

    public KolPostDetailViewModel(KolPostViewModel kolPostViewModel, List<KolCommentViewModel>
            kolCommentViewModels) {
        this.kolPostViewModel = kolPostViewModel;
        this.kolCommentViewModels = kolCommentViewModels;
    }

    public KolPostViewModel getKolPostViewModel() {
        return kolPostViewModel;
    }

    public void setKolPostViewModel(KolPostViewModel kolPostViewModel) {
        this.kolPostViewModel = kolPostViewModel;
    }

    public List<KolCommentViewModel> getKolCommentViewModels() {
        return kolCommentViewModels;
    }

    public void setKolCommentViewModels(List<KolCommentViewModel> kolCommentViewModels) {
        this.kolCommentViewModels = kolCommentViewModels;
    }
}
