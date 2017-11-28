package com.tokopedia.tkpd.beranda.presentation.view.adapter.factory;

import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeAdapterFactory implements HomeTypeFactory {

    @Override
    public int type(BannerViewModel bannerViewModel) {
        return 0;
    }

    @Override
    public int type(TickerViewModel tickerViewModel) {
        return 0;
    }

    @Override
    public int type(TopPicksViewModel topPicksViewModel) {
        return 0;
    }

    @Override
    public int type(BrandsViewModel brandsViewModel) {
        return 0;
    }

    @Override
    public int type(DigitalsViewModel digitalsViewModel) {
        return 0;
    }

    @Override
    public int type(CategorySectionViewModel categorySectionViewModel) {
        return 0;
    }

    @Override
    public int type(CategoryItemViewModel categoryItemViewModel) {
        return 0;
    }

    @Override
    public AbstractViewHolder createViewHolder(ViewGroup view, int viewType) {
        return null;
    }
}
