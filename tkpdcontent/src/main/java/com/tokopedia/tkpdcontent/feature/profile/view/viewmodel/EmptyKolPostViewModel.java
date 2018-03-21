package com.tokopedia.tkpdcontent.feature.profile.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory.KolPostTypeFactory;

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
