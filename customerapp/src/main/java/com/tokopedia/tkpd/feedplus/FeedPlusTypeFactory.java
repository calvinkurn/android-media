package com.tokopedia.tkpd.feedplus;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ProductCardViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
