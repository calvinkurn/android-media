package com.tokopedia.core.base.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;


/**
 * @author kulomady on 1/24/17.
 */

public interface AdapterTypeFactory {

    int type(EmptyModel viewModel);

    int type(LoadingModel viewModel);

    AbstractViewHolder createViewHolder(View parent, int type);
}
