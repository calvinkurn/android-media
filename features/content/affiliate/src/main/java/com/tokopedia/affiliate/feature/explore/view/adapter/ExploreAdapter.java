package com.tokopedia.affiliate.feature.explore.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory;

import java.util.List;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreAdapter extends BaseAdapter<ExploreTypeFactory> {

    public ExploreAdapter(ExploreTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public List<Visitable> getData() {
        return visitables;
    }
}
