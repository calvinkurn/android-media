package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by milhamj on 02/03/18.
 */

public class EmptyKolPostViewModel implements Visitable<KolPostTypeFactory> {

    public EmptyKolPostViewModel() {
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
