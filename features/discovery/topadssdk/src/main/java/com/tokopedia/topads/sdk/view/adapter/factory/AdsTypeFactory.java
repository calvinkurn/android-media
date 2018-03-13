package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface AdsTypeFactory {

    int type(ProductGridViewModel viewModel);

    int type(ProductListViewModel viewModel);

    int type(ShopGridViewModel viewModel);

    int type(ShopListViewModel viewModel);

    int type(ShopFeedViewModel viewModel);

    int type(ProductFeedViewModel viewModel);

    AbstractViewHolder createViewHolder(ViewGroup view, int viewType);

}
