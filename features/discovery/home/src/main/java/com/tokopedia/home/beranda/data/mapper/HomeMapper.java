package com.tokopedia.home.beranda.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.domain.model.Spotlight;
import com.tokopedia.home.beranda.domain.model.SpotlightItem;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightItemViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.UseCaseIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsDynamicChannelModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.util.ServerTimeOffsetUtil;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.home.util.ErrorMessageUtils.getErrorMessage;


/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<Response<GraphqlResponse<HomeData>>, List<Visitable>> {
    private final Context context;

    public HomeMapper(Context context) {
        this.context = context;
    }

    @Override
    public List<Visitable> call(Response<GraphqlResponse<HomeData>> response) {
        if (response.isSuccessful()) {
            List<Visitable> list = new ArrayList<>();

            HomeData homeData = response.body().getData();

            if (homeData.getSlides() != null
                    && homeData.getSlides().getSlides() != null
                    && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides()));
            }

            if (homeData.getTicker() != null
                    && homeData.getTicker().getTickers() != null
                    && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getDynamicIcon() != null
                    && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
                list.add(mappingDynamicIcon(homeData.getDynamicHomeIcon().getDynamicIcon()));
                if(!homeData.isCache()) {
                    HomePageTracking.eventEnhancedImpressionDynamicIconHomePage(context,
                            homeData.getDynamicHomeIcon().getEnhanceImpressionDynamicIconHomePage());
                }
            }

            if (homeData.getDynamicHomeChannel() != null
                    && homeData.getDynamicHomeChannel().getChannels() != null
                    && !homeData.getDynamicHomeChannel().getChannels().isEmpty()) {
                int position = 1;
                List<Object> legoAndCuratedAndSprintSaleBannerList = new ArrayList<>();

                for (DynamicHomeChannel.Channels channel : homeData.getDynamicHomeChannel().getChannels()) {
                    if (channel.getLayout() != null) {
                        if(!homeData.isCache()) {
                            position++;
                            if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT)) {
                                channel.setHomeAttribution(String.format("%s - sprintSaleProduct - $1 - $2", String.valueOf(position)));
                                HomePageTracking.eventEnhancedImpressionSprintSaleHomePage(
                                        context,
                                        channel.getEnhanceImpressionSprintSaleHomePage(position)
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL)) {
                                channel.setHomeAttribution(String.format("%s - sprintSaleBanner - $1", String.valueOf(position)));
                                legoAndCuratedAndSprintSaleBannerList.addAll(
                                        channel.convertProductEnhanceSprintSaleCarouselDataLayerForCombination()
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_6_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - lego banner - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - legoBanner - $1 - $2", String.valueOf(position)));
                                legoAndCuratedAndSprintSaleBannerList.addAll(
                                        channel.convertPromoEnhanceLegoBannerDataLayerForCombination()
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - lego banner 3 image - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - legoBanner3Image - $1 - $2", String.valueOf(position)));
                                legoAndCuratedAndSprintSaleBannerList.addAll(
                                        channel.convertPromoEnhanceLegoBannerDataLayerForCombination()
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO)) {
                                channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - sprintSaleProduct - %s - $1 - $2", String.valueOf(position), channel.getHeader().getName()));
                                HomePageTracking.eventEnhancedImpressionDynamicChannelHomePage(context,
                                        channel.getEnhanceImpressionDynamicSprintLegoHomePage(position)
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT)) {
                                homeData.getSpotlight().setPromoName(String.format("/ - p%s - spotlight banner", String.valueOf(position)));
                                homeData.getSpotlight().setHomeAttribution(String.format("%s - spotlightBanner - $1 - $2", String.valueOf(position)));
                                HomePageTracking.eventEnhancedImpressionDynamicChannelHomePage(context,
                                        homeData.getSpotlight().getEnhanceImpressionSpotlightHomePage(position)
                                );
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_HERO)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_ORGANIC)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_TOPADS)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_3_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - curatedListBanner - %s - $1 - $2", String.valueOf(position), channel.getHeader().getName()));
                                legoAndCuratedAndSprintSaleBannerList.addAll(
                                        channel.convertPromoEnhanceDynamicChannelDataLayerForCombination()
                                );
                            }
                        }

                        switch (channel.getLayout()) {
                            case DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET:
                                list.add(new DigitalsViewModel(context.getString(R.string.digital_widget_title), 0));
                                break;
                            case DynamicHomeChannel.Channels.LAYOUT_TOPADS:
                                list.add(mappingDynamicTopAds(channel));
                                break;
                            case DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT:
                                list.add(mappingSpotlight(homeData.getSpotlight()));
                                break;
                            case DynamicHomeChannel.Channels.LAYOUT_3_IMAGE:
                            case DynamicHomeChannel.Channels.LAYOUT_ORGANIC:
                            case DynamicHomeChannel.Channels.LAYOUT_HERO:
                            case DynamicHomeChannel.Channels.LAYOUT_6_IMAGE:
                            case DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE:
                            case DynamicHomeChannel.Channels.LAYOUT_SPRINT:
                            case DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL:
                            case DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO:
                                list.add(mappingDynamicChannel(channel));
                                HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                        list.size(),channel);
                                break;
                        }
                    }
                }

                if (!legoAndCuratedAndSprintSaleBannerList.isEmpty()){
                    HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(context, legoAndCuratedAndSprintSaleBannerList);
                }
            }

            return list;
        } else {
            String messageError = getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new RuntimeException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private Visitable mappingDynamicTopAds(DynamicHomeChannel.Channels channel) {
        TopAdsDynamicChannelModel visitable = new TopAdsDynamicChannelModel();
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < channel.getGrids().length; i++) {
            DynamicHomeChannel.Grid grid = channel.getGrids()[i];
            ProductDynamicChannelViewModel model = new ProductDynamicChannelViewModel();
            model.setProductId(grid.getId());
            model.setProductPrice(grid.getPrice());
            model.setProductName(grid.getName());
            model.setProductCashback(grid.getCashback());
            ProductImage productImage = new ProductImage();
            productImage.setM_url(grid.getImpression());
            productImage.setM_ecs(grid.getImageUrl());
            model.setProductImage(productImage);
            model.setApplink(grid.getApplink());
            model.setProductClickUrl(grid.getProductClickUrl());
            items.add(model);
        }
        visitable.setTitle(channel.getHeader().getName());
        visitable.setItems(items);
        return visitable;
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
        UseCaseIconSectionViewModel viewModel = new UseCaseIconSectionViewModel();
        for (DynamicHomeIcon.UseCaseIcon icon : iconList) {
            viewModel.addItem(new HomeIconItem(icon.getId(), icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private Visitable mappingDynamicIcon(List<DynamicHomeIcon.DynamicIcon> iconList) {
        DynamicIconSectionViewModel viewModelDynamicIcon = new DynamicIconSectionViewModel();
        for (DynamicHomeIcon.DynamicIcon icon : iconList) {
            viewModelDynamicIcon.addItem(new HomeIconItem(icon.getId(), icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModelDynamicIcon;
    }

    private Visitable mappingDynamicChannel(DynamicHomeChannel.Channels channel) {
        DynamicChannelViewModel viewModel = new DynamicChannelViewModel();
        viewModel.setChannel(channel);

        viewModel.setServerTimeOffset(
                ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                        channel.getHeader().getServerTimeUnix()
                )
        );
        return viewModel;
    }

    private Visitable mappingSpotlight(Spotlight spotlight) {
        List<SpotlightItemViewModel> spotlightItems = new ArrayList<>();

        for (SpotlightItem spotlightItem : spotlight.getSpotlights()) {
            spotlightItems.add(new SpotlightItemViewModel(
                    spotlightItem.getId(),
                    spotlightItem.getTitle(),
                    spotlightItem.getDescription(),
                    spotlightItem.getBackgroundImageUrl(),
                    spotlightItem.getTagName(),
                    spotlightItem.getTagNameHexcolor(),
                    spotlightItem.getTagHexcolor(),
                    spotlightItem.getCtaText(),
                    spotlightItem.getCtaTextHexcolor(),
                    spotlightItem.getUrl(),
                    spotlightItem.getApplink(),
                    spotlight.getPromoName()
                    ));
        }

        return new SpotlightViewModel(spotlightItems);
    }
}
