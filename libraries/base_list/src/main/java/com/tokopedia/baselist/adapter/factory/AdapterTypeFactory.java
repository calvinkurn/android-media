package com.tokopedia.baselist.adapter.factory;

import android.view.View;

import com.tokopedia.baselist.adapter.model.EmptyModel;
import com.tokopedia.baselist.adapter.model.EmptyResultViewModel;
import com.tokopedia.baselist.adapter.model.ErrorNetworkModel;
import com.tokopedia.baselist.adapter.model.LoadingModel;
import com.tokopedia.baselist.adapter.model.LoadingMoreModel;
import com.tokopedia.baselist.adapter.viewholders.AbstractViewHolder;


/**
 * @author kulomady on 1/24/17.
 */

public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    int type(LoadingMoreModel viewModel);

    int type(ErrorNetworkModel errorNetworkModel);

    AbstractViewHolder createViewHolder(View parent, int type);

    int type(EmptyResultViewModel viewModel);
}