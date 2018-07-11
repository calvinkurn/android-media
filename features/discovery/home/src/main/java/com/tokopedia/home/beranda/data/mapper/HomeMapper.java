package com.tokopedia.home.beranda.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;


/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<Response<GraphqlResponse<HomeData>>, List<Visitable>> {
    @Override
    public List<Visitable> call(Response<GraphqlResponse<HomeData>> response) {
        if (response.isSuccessful()) {
            List<Visitable> list = new ArrayList<>();

            HomeData homeData = response.body().getData();

            if (homeData.getTicker() != null
                    && homeData.getTicker().getTickers() != null
                    && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            if (homeData.getSlides() != null
                    && homeData.getSlides().getSlides() != null
                    && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getUseCaseIcon() != null
                    && !homeData.getDynamicHomeIcon().getUseCaseIcon().isEmpty()) {
                list.add(mappingUseCaseIcon(homeData.getDynamicHomeIcon().getUseCaseIcon()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getDynamicIcon() != null
                    && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
                list.add(mappingDynamicIcon(homeData.getDynamicHomeIcon().getDynamicIcon()));
            }

            if (homeData.getDynamicHomeChannel() != null
                    && homeData.getDynamicHomeChannel().getChannels() != null
                    && !homeData.getDynamicHomeChannel().getChannels().isEmpty()) {
                int position = 1;
                for (DynamicHomeChannel.Channels channel : homeData.getDynamicHomeChannel().getChannels()) {
                    if (channel.getLayout() != null) {
                        if(!homeData.isCache()) {
                            position++;
                            if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT)) {
                                channel.setHomeAttribution(String.format("%d - sprintSaleProduct - $1 - $2", position));
                                HomePageTracking.eventEnhancedImpressionSprintSaleHomePage(
                                        channel.getEnhanceImpressionSprintSaleHomePage(position)

                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL)) {
                                channel.setHomeAttribution(String.format("%d - sprintSaleBanner - $1", position));
                                HomePageTracking.eventEnhancedImpressionSprintSaleHomePage(
                                        channel.getEnhanceImpressionSprintSaleCarouselHomePage(position)
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_6_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%d - lego banner", position));
                                channel.setHomeAttribution(String.format("%d - legoBanner - $1 - $2", position));
                                HomePageTracking.eventEnhancedImpressionDynamicChannelHomePage(
                                        channel.getEnhanceImpressionLegoBannerHomePage(position)
                                );
                            } else {
                                channel.setPromoName(String.format("/ - p%d - %s", position, channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%d - curatedListBanner - %s - $1 - $2", position, channel.getHeader().getName()));
                                HomePageTracking.eventEnhancedImpressionDynamicChannelHomePage(
                                        channel.getEnhanceImpressionDynamicChannelHomePage(position)
                                );
                            }
                        }
                        if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET)) {
                            list.add(new DigitalsViewModel(MainApplication.getAppContext().getString(R.string.digital_widget_title), 0));
                        } else {
                            list.add(mappingDynamicChannel(channel));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(list.size(),channel);
                        }
                    }
                }
            }

            return list;
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private Visitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private Visitable mappingBanner(List<BannerSlidesModel> slides) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(slides);
        return viewModel;
    }

    private Visitable mappingUseCaseIcon(List<DynamicHomeIcon.UseCaseIcon> iconList) {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        for (DynamicHomeIcon.UseCaseIcon icon : iconList) {
            viewModel.addSection(new LayoutSections(LayoutSections.ICON_USE_CASE, icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private Visitable mappingDynamicIcon(List<DynamicHomeIcon.DynamicIcon> iconList) {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        for (DynamicHomeIcon.DynamicIcon icon : iconList) {
            viewModel.addSection(new LayoutSections(LayoutSections.ICON_DYNAMIC_CASE, icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private Visitable mappingDynamicChannel(DynamicHomeChannel.Channels channel) {
        DynamicChannelViewModel viewModel = new DynamicChannelViewModel();
        viewModel.setChannel(channel);
        return viewModel;
    }
}
