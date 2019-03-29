package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory;

/**
 * @author by yfsx on 24/09/18.
 */
public class EmptyExploreViewModel implements Visitable<ExploreTypeFactory> {

    public EmptyExploreViewModel() {

    }


    @Override
    public int type(ExploreTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
