package com.tokopedia.explore.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kotlin.model.ImpressHolder;

/**
 * @author by milhamj on 24/07/18.
 */

public class ExploreImageViewModel implements Visitable<ExploreImageTypeFactory> {

    private String imageUrl;
    private KolPostViewModel kolPostViewModel;
    private ImpressHolder impressHolder = new ImpressHolder();

    public ExploreImageViewModel(String imageUrl, KolPostViewModel kolPostViewModel) {
        this.imageUrl = imageUrl;
        this.kolPostViewModel = kolPostViewModel;
    }

    @Override
    public int type(ExploreImageTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public KolPostViewModel getKolPostViewModel() {
        return kolPostViewModel;
    }

    public void setKolPostViewModel(KolPostViewModel kolPostViewModel) {
        this.kolPostViewModel = kolPostViewModel;
    }

    public ImpressHolder getImpressHolder() {
        return impressHolder;
    }
}
