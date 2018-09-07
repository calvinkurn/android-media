package com.tokopedia.feedplus.view.adapter.typefactory.recentview;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.view.viewmodel.recentview.RecentViewDetailProductViewModel;

/**
 * @author by nisie on 7/4/17.
 */

public interface RecentViewTypeFactory {

    int type(RecentViewDetailProductViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
