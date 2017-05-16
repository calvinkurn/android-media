package com.tokopedia.tkpd.feedplus.view.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ProductCardViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);

}
