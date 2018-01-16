package com.tokopedia.tkpd.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.viewmodel.InspirationViewModel;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeTypeFactory {

    int type(BannerViewModel bannerViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(TopPicksViewModel topPicksViewModel);

    int type(BrandsViewModel brandsViewModel);

    int type(DigitalsViewModel digitalsViewModel);

    int type(CategorySectionViewModel categorySectionViewModel);

    int type(CategoryItemViewModel categoryItemViewModel);

    int type(SellViewModel sellViewModel);

    int type(SaldoViewModel saldoViewModel);

    int type(HeaderViewModel headerViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(InspirationViewModel inspirationViewModel);
}
