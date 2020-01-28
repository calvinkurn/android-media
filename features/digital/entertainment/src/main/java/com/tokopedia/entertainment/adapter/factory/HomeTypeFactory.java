package com.tokopedia.entertainment.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.entertainment.adapter.HomeViewHolder;
import com.tokopedia.entertainment.adapter.viewmodel.BannerViewModel;
import com.tokopedia.entertainment.adapter.viewmodel.CategoryViewModel;
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel;
import com.tokopedia.entertainment.adapter.viewmodel.EventGridViewModel;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface HomeTypeFactory {

    int type(BannerViewModel viewModel);

    int type(CategoryViewModel viewModel);

    int type(EventCarouselViewModel viewModel);

    int type(EventGridViewModel viewModel);

    HomeViewHolder createViewHolder(ViewGroup view, int viewType);

}
