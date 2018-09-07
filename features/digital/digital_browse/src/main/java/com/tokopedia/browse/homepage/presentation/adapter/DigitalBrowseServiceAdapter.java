package com.tokopedia.browse.homepage.presentation.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;

import java.util.List;

/**
 * @author by furqan on 07/09/18.
 */

public class DigitalBrowseServiceAdapter extends BaseAdapter<DigitalBrowseServiceAdapterTypeFactory> {

    public DigitalBrowseServiceAdapter(DigitalBrowseServiceAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public boolean isLoadingObject(int position) {
        return (visitables.get(position) instanceof LoadingModel);
    }
}
