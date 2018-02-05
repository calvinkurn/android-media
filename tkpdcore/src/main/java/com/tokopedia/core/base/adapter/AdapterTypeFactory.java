package com.tokopedia.core.base.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.ErrorNetworkModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

/**
 * @author kulomady on 1/24/17.
 */

/**
 * Use BaseAdapter (visitable pattern) from tkpd abstraction
 */
@Deprecated
public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    int type(ErrorNetworkModel errorNetworkModel);

    AbstractViewHolder createViewHolder(View parent, int type);

    int type(RetryModel retryModel);
}