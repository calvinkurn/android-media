package com.tokopedia.browse.homepage.presentation.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;

import java.util.List;

/**
 * @author by furqan on 05/09/18.
 */

public class DigitalBrowseMarketplaceAdapter extends BaseAdapter<DigitalBrowseMarketplaceAdapterTypeFactory> {

    public DigitalBrowseMarketplaceAdapter(DigitalBrowseMarketplaceAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public boolean isLoadingObject(int position) {
        return (visitables.get(position) instanceof LoadingModel);
    }
}
