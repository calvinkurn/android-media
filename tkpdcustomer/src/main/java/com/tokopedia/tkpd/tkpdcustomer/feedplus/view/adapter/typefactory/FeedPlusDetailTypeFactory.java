package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.FeedDetailViewModel;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetailTypeFactory {

    int type(FeedDetailViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
