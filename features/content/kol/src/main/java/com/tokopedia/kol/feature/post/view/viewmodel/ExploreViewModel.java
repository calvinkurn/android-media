package com.tokopedia.kol.feature.post.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;

/**
 * @author by milhamj on 18/05/18.
 */

public class ExploreViewModel implements Visitable<KolPostTypeFactory> {

    public ExploreViewModel() {
    }

    @Override
    public int type(KolPostTypeFactory typeFactory) {
        return 0;
    }
}
