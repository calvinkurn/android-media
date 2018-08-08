package com.tokopedia.topads.sdk.view.adapter.factory;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.LoadingViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public interface TopAdsTypeFactory {

    int type(ClientViewModel viewModel);

    int type(TopAdsViewModel viewModel);

    int type(LoadingViewModel viewModel);

    RecyclerView.ViewHolder createViewHolder(ViewGroup view, int viewType);
}
