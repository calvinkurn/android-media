package com.tokopedia.abstraction.base.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringGrid;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringList;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;


/**
 * @author kulomady on 1/24/17.
 */

public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    int type(ErrorNetworkModel errorNetworkModel);

    AbstractViewHolder createViewHolder(View parent, int type);

    int type(EmptyResultViewModel viewModel);

    int type(LoadingModelShimmeringGrid loadingModelShimmeringGrid);

    int type(LoadingModelShimmeringList loadingModelShimmeringList);
}