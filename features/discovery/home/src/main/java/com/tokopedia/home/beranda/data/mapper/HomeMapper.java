package com.tokopedia.home.beranda.data.mapper;

import android.content.Context;

import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.HomeFlag;
import com.tokopedia.home.beranda.domain.model.Spotlight;
import com.tokopedia.home.beranda.domain.model.SpotlightItem;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TopAdsDynamicChannelModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.home.util.ServerTimeOffsetUtil;
import com.tokopedia.stickylogin.internal.StickyLoginConstant;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Func1;


/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<HomeData, HomeViewModel> {
    private final Context context;
    private final HomeVisitableFactory homeVisitableFactory;

    public HomeMapper(Context context,
                      HomeVisitableFactory homeVisitableFactory) {
        this.context = context;
        this.homeVisitableFactory = homeVisitableFactory;
    }

    @Override
    public HomeViewModel call(HomeData homeData) {
        List list = new ArrayList<>();


        list.add(mappingBanner(homeData.getBanner(), homeData.isCache()));

        if (homeData.getTicker() != null
                && homeData.getTicker().getTickers() != null
                && !homeData.getTicker().getTickers().isEmpty()
                && !HomeFragment.HIDE_TICKER) {
            HomeVisitable ticker = mappingTicker(homeData.getTicker().getTickers());
            if (ticker != null) {
                list.add(ticker);
            }
        }

        if (homeData.getHomeFlag() != null) {
            if (mappingOvoTokpoint(homeData.getHomeFlag().getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS), homeData.isCache()) != null) {
                list.add(mappingOvoTokpoint(homeData.getHomeFlag().getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS), homeData.isCache()));
            }
        }

        list.add(new GeolocationPromptViewModel());

        if (homeData.getDynamicHomeIcon() != null
                && homeData.getDynamicHomeIcon().getDynamicIcon() != null
                && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
            list.add(mappingDynamicIcon(
                    homeData.getDynamicHomeIcon().getDynamicIcon(),
                    homeData.isCache(),
                    homeData.getHomeFlag().getFlag(HomeFlag.TYPE.DYNAMIC_ICON_WRAP)
            ));
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
                            channel.setPosition(position);
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL)) {
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_6_IMAGE)) {
                            channel.setPromoName(String.format("/ - p%s - lego banner - %s", String.valueOf(position), channel.getHeader().getName()));
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE)) {
                            channel.setPromoName(String.format("/ - p%s - lego banner 3 image - %s", String.valueOf(position), channel.getHeader().getName()));
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO)
                                || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_ORGANIC)) {
                            channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                            channel.setPosition(position);
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT)) {
                            homeData.getSpotlight().setPromoName(String.format("/ - p%s - spotlight banner", String.valueOf(position)));
                            homeData.getSpotlight().setChannelId(channel.getId());
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET)
                                || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_HERO)
                                || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_TOPADS)
                                || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_3_IMAGE)) {
                            channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC)
                                || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL)) {
                            channel.setPosition(position);
                        } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_REVIEW)){
                            channel.setPosition(position);
                        }
                    }

                    switch (channel.getLayout()) {
                        case DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET:
                            list.add(mappingDigitalWidget(
                                    channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                                    homeData.isCache()
                                    ));
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_TOPADS:
                            list.add(mappingDynamicTopAds(channel, homeData.isCache()));
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT:
                            list.add(mappingSpotlight(homeData.getSpotlight(), homeData.isCache()));
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET :
                            if (!homeData.isCache()) {
                                list.add(
                                        new BusinessUnitViewModel(context.getString(R.string.digital_widget_title), position)
                                );
                            }
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_3_IMAGE:
                        case DynamicHomeChannel.Channels.LAYOUT_HERO:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                                    true,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_6_IMAGE:
                        case DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination(),
                                    true,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_SPRINT:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    null,
                                    false,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertProductEnhanceSprintSaleCarouselDataLayerForCombination(),
                                    true,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO:
                        case DynamicHomeChannel.Channels.LAYOUT_ORGANIC:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.getEnhanceImpressionDynamicSprintLegoHomePage(),
                                    null,
                                    false,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC:
                        case DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.getEnhanceImpressionProductChannelMix(),
                                    null,
                                    false,
                                    homeData.isCache()));
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size(),channel);

                            HomePageTracking.eventEnhanceImpressionBanner(context, channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF:
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.getEnhanceImpressionProductChannelMix(),
                                    null,
                                    false,
                                    homeData.isCache()
                            ));
                            HomePageTracking.eventEnhanceImpressionBannerGif(context, channel);
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_REVIEW:
                            if (!homeData.isCache()) {
                                list.add(mappingToReviewViewModel(channel));
                            }
                            break;
                        case DynamicHomeChannel.Channels.LAYOUT_PLAY_BANNER:
                            if (!homeData.isCache()) {
                                HomeVisitable playBanner = mappingPlayChannel(channel, new HashMap<>(), homeData.isCache());
                                if (!list.contains(playBanner)) list.add(playBanner);
                            }
                            break;
                    }
                }
            }
        }

        return new HomeViewModel(homeData.getHomeFlag(), list, homeData.isCache());
    }

    private HomeVisitable mappingToReviewViewModel(DynamicHomeChannel.Channels channel) {
        ReviewViewModel reviewViewModel = new ReviewViewModel();
        return reviewViewModel;
    }

    private HomeVisitable mappingDigitalWidget(List<Object> trackingDataForCombination, boolean isCache) {
        DigitalsViewModel digitalsViewModel = new DigitalsViewModel(context.getString(R.string.digital_widget_title), 0);
        if (!isCache) {
            digitalsViewModel.setTrackingCombined(true);
            digitalsViewModel.setTrackingDataForCombination(trackingDataForCombination);
        }
        return digitalsViewModel;
    }

    private HomeVisitable mappingDynamicTopAds(DynamicHomeChannel.Channels channel, boolean isCache) {
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
        if (!isCache) {
            visitable.setTrackingDataForCombination(channel.convertPromoEnhanceDynamicChannelDataLayerForCombination());
            visitable.setTrackingCombined(true);
        }
        return visitable;
    }

    private HomeVisitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        ArrayList<Ticker.Tickers> tmpTickers = new ArrayList<>();
        for (Ticker.Tickers tmpTicker: tickers) {
            if (!tmpTicker.getLayout().equals(StickyLoginConstant.LAYOUT_FLOATING)) {
                tmpTickers.add(tmpTicker);
            }
        }

        if (tmpTickers.size() <= 0) {
            return null;
        }

        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tmpTickers);
        return viewModel;
    }

    private HomeVisitable mappingBanner(BannerDataModel slides, boolean isCache) {
        return homeVisitableFactory.createBannerVisitable(
                slides, isCache);
    }

    private HomeVisitable mappingOvoTokpoint(boolean hasTokopoints, boolean isCache) {
        return homeVisitableFactory.createOvoTokopointVisitable(
                hasTokopoints, isCache);
    }

    private HomeVisitable mappingDynamicIcon(List<DynamicHomeIcon.DynamicIcon> iconList,
                                                boolean isCache,
                                             boolean dynamicIconWrap) {
        DynamicIconSectionViewModel viewModelDynamicIcon = new DynamicIconSectionViewModel();
        viewModelDynamicIcon.setDynamicIconWrap(dynamicIconWrap);
        for (DynamicHomeIcon.DynamicIcon icon : iconList) {
            viewModelDynamicIcon.addItem(new HomeIconItem(icon.getId(), icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl(), icon.getBu_identifier()));
        }
        if (!isCache) {
            viewModelDynamicIcon.setTrackingData(
                    HomePageTracking.getEnhanceImpressionDynamicIconHomePage(viewModelDynamicIcon.getItemList())
            );
            viewModelDynamicIcon.setTrackingCombined(false);
        }
        return viewModelDynamicIcon;
    }

    private HomeVisitable mappingDynamicChannel(DynamicHomeChannel.Channels channel,
                                                   Map<String, Object> trackingData,
                                                   List<Object> trackingDataForCombination,
                                                   boolean isCombined,
                                                   boolean isCache) {
        DynamicChannelViewModel viewModel = new DynamicChannelViewModel();
        viewModel.setChannel(channel);
        if (!isCache) {
            viewModel.setTrackingData(trackingData);
            viewModel.setTrackingDataForCombination(trackingDataForCombination);
            viewModel.setTrackingCombined(isCombined);
        }

        viewModel.setServerTimeOffset(
                ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                        channel.getHeader().getServerTimeUnix()
                )
        );
        return viewModel;
    }

    private HomeVisitable mappingSpotlight(Spotlight spotlight, boolean isCache) {
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
                    spotlight.getPromoName(),
                    spotlight.getChannelId()
                    ));
        }

        SpotlightViewModel viewModel = new SpotlightViewModel(spotlightItems, spotlight.getChannelId());
        if (!isCache) {
            viewModel.setTrackingData(spotlight.getEnhanceImpressionSpotlightHomePage());
            viewModel.setTrackingCombined(false);
        }
        return viewModel;
    }

    private HomeVisitable mappingPlayChannel(DynamicHomeChannel.Channels channel,
                                             Map<String, Object> trackingData,
                                             boolean isCache) {
        PlayCardViewModel playCardViewModel = new PlayCardViewModel();
        if (!isCache) {
            playCardViewModel.setChannel(channel);
            playCardViewModel.setTrackingData(trackingData);
        }
        return playCardViewModel;
    }
}
