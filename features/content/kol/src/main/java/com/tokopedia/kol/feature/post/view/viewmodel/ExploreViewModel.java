package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by milhamj on 18/05/18.
 */

public class ExploreViewModel implements Visitable<KolPostTypeFactory> {

    private String kolName;

    public ExploreViewModel() {
    }

    public String getKolName() {
        return kolName;
    }

    public void setKolName(String kolName) {
        this.kolName = kolName;
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
