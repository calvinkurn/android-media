package com.tokopedia.tkpdcontent.feature.profile.domain.model;

import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

import java.util.List;

/**
 * @author by milhamj on 26/02/18.
 */

public class KolProfileModel {
    private List<KolPostViewModel> kolPostViewModels;

    private String lastCursor;

    public KolProfileModel(List<KolPostViewModel> kolPostViewModels, String lastCursor) {
        this.kolPostViewModels = kolPostViewModels;
        this.lastCursor = lastCursor;
    }

    public void setKolPostViewModels(List<KolPostViewModel> kolPostViewModels) {
        this.kolPostViewModels = kolPostViewModels;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public List<KolPostViewModel> getKolPostViewModels() {
        return kolPostViewModels;
    }

    public String getLastCursor() {
        return lastCursor;
    }
}
