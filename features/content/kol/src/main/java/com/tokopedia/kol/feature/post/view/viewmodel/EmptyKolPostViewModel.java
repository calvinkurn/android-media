package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by milhamj on 02/03/18.
 */

public class EmptyKolPostViewModel implements Visitable<KolPostTypeFactory> {

    private boolean isShowTopShadow;

    public EmptyKolPostViewModel() {
    }

    public boolean isShowTopShadow() {
        return isShowTopShadow;
    }

    public void setShowTopShadow(boolean showTopShadow) {
        isShowTopShadow = showTopShadow;
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
