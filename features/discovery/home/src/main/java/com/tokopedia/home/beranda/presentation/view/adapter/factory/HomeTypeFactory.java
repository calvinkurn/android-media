package com.tokopedia.home.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BusinessUnitViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.InspirationHeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsDynamicChannelModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.UseCaseIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeTypeFactory {

    int type(InspirationHeaderViewModel inspirationHeaderViewModel);

    int type(BannerViewModel bannerViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(DigitalsViewModel digitalsViewModel);

    int type(BusinessUnitViewModel businessUnitViewModel);

    int type(UseCaseIconSectionViewModel useCaseIconSectionViewModel);

    int type(DynamicIconSectionViewModel dynamicIconSectionViewModel);

    int type(SellViewModel sellViewModel);

    int type(HeaderViewModel headerViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(TopAdsDynamicChannelModel topAdsDynamicChannelModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(InspirationViewModel inspirationViewModel);

    int type(DynamicChannelViewModel dynamicChannelViewModel);

    int type(SpotlightViewModel spotlightViewModel);
}
