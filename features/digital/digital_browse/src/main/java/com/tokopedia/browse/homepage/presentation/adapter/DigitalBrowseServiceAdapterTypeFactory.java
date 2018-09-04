package com.tokopedia.browse.homepage.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceShimmeringViewHolder;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseServiceAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory {

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DigitalBrowseServiceShimmeringViewHolder.LAYOUT) {
            return new DigitalBrowseServiceShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return DigitalBrowseServiceShimmeringViewHolder.LAYOUT;
    }

    public int type(DigitalBrowseServiceViewModel viewModel) {
        return 0;
    }
}
