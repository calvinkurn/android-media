package com.tokopedia.home.beranda.data.mapper;

import android.content.Context;
import android.text.TextUtils;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.home.R;
import com.tokopedia.home.analytics.HomePageTracking;
import com.tokopedia.home.beranda.data.model.Promotion;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder;
import com.tokopedia.home.beranda.domain.model.Ticker;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.Spotlight;
import com.tokopedia.home.beranda.domain.model.SpotlightItem;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.*;
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils;
import com.tokopedia.home.util.ServerTimeOffsetUtil;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.home.util.ErrorMessageUtils.getErrorMessage;


/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<Response<GraphqlResponse<HomeData>>, List<TrackedVisitable>> {
    private final Context context;

    public HomeMapper(Context context) {
        this.context = context;
    }

    @Override
    public List<TrackedVisitable> call(Response<GraphqlResponse<HomeData>> response) {
        if (response.isSuccessful()) {
            List<TrackedVisitable> list = new ArrayList<>();

            HomeData homeData = response.body().getData();

            if (homeData.getSlides() != null
                    && homeData.getSlides().getSlides() != null
                    && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides(), homeData.isCache()));
            }

            if (homeData.getTicker() != null
                    && homeData.getTicker().getTickers() != null
                    && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getDynamicIcon() != null
                    && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
                list.add(mappingDynamicIcon(
                        homeData.getDynamicHomeIcon().getDynamicIcon(),
                        homeData.getDynamicHomeIcon().getEnhanceImpressionDynamicIconHomePage(),
                        homeData.isCache()
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
                                channel.setHomeAttribution(String.format("%s - sprintSaleProduct - $1 - $2", String.valueOf(position)));
                                channel.setPosition(position);
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL)) {
                                channel.setHomeAttribution(String.format("%s - sprintSaleBanner - $1", String.valueOf(position)));
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_6_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - lego banner - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - legoBanner - $1 - $2", String.valueOf(position)));
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - lego banner 3 image - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - legoBanner3Image - $1 - $2", String.valueOf(position)));
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_ORGANIC)) {
                                channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - sprintSaleProduct - %s - $1 - $2", String.valueOf(position), channel.getHeader().getName()));
                                channel.setPosition(position);
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT)) {
                                homeData.getSpotlight().setPromoName(String.format("/ - p%s - spotlight banner", String.valueOf(position)));
                                homeData.getSpotlight().setHomeAttribution(String.format("%s - spotlightBanner - $1 - $2", String.valueOf(position)));
                            } else if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_HERO)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_TOPADS)
                                    || channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_3_IMAGE)) {
                                channel.setPromoName(String.format("/ - p%s - %s", String.valueOf(position), channel.getHeader().getName()));
                                channel.setHomeAttribution(String.format("%s - curatedListBanner - %s - $1 - $2", String.valueOf(position), channel.getHeader().getName()));
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
                                        channel.getEnhanceImpressionSprintSaleHomePage(),
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
                        }
                    }
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

    private TrackedVisitable mappingDigitalWidget(List<Object> trackingDataForCombination, boolean isCache) {
        DigitalsViewModel digitalsViewModel = new DigitalsViewModel(context.getString(R.string.digital_widget_title), 0);
        if (!isCache) {
            digitalsViewModel.setTrackingCombined(true);
            digitalsViewModel.setTrackingDataForCombination(trackingDataForCombination);
        }
        return digitalsViewModel;
    }

    private TrackedVisitable mappingDynamicTopAds(DynamicHomeChannel.Channels channel, boolean isCache) {
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

    private TrackedVisitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private TrackedVisitable mappingSearchPlaceholder(SearchPlaceholder placeholder){
        SearchPlaceholderViewModel viewModel = new SearchPlaceholderViewModel();
        viewModel.setSearchPlaceholder(placeholder);
        return viewModel;
    }

    private TrackedVisitable mappingBanner(List<BannerSlidesModel> slides, boolean isCache) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(slides);
        if (!isCache) {
            viewModel.setTrackingData(getBannerTrackingData(slides));
        }
        return viewModel;
    }

    private Map<String, Object> getBannerTrackingData(List<BannerSlidesModel> slides) {
        if (slides != null && slides.size() > 0) {
            List<Promotion> promotionList = new ArrayList<>();
            for (int i = 0, sizei = slides.size(); i < sizei; i++) {
                BannerSlidesModel model = slides.get(i);

                Promotion promotion = new Promotion();
                promotion.setPromotionID(String.valueOf(model.getId()));
                promotion.setPromotionName("/ - p1 - promo");
                promotion.setPromotionAlias(model.getTitle().trim().replaceAll(" ", "_"));
                promotion.setPromotionPosition(i + 1);
                promotion.setRedirectUrl(model.getRedirectUrl());
                promotion.setPromoCode(model.getPromoCode());

                promotionList.add(promotion);
            }
            return HomePageTracking.convertToPromoImpression(promotionList);
        } else {
            return null;
        }
    }

    private TrackedVisitable mappingUseCaseIcon(List<DynamicHomeIcon.UseCaseIcon> iconList) {
        UseCaseIconSectionViewModel viewModel = new UseCaseIconSectionViewModel();
        for (DynamicHomeIcon.UseCaseIcon icon : iconList) {
            viewModel.addItem(new HomeIconItem(icon.getId(), icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private TrackedVisitable mappingDynamicIcon(List<DynamicHomeIcon.DynamicIcon> iconList,
                                                Map<String, Object> trackingData,
                                                boolean isCache) {
        DynamicIconSectionViewModel viewModelDynamicIcon = new DynamicIconSectionViewModel();
        for (DynamicHomeIcon.DynamicIcon icon : iconList) {
            viewModelDynamicIcon.addItem(new HomeIconItem(icon.getId(), icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        if (!isCache) {
            viewModelDynamicIcon.setTrackingData(trackingData);
            viewModelDynamicIcon.setTrackingCombined(false);
        }
        return viewModelDynamicIcon;
    }

    private TrackedVisitable mappingDynamicChannel(DynamicHomeChannel.Channels channel,
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

    private TrackedVisitable mappingSpotlight(Spotlight spotlight, boolean isCache) {
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

        SpotlightViewModel viewModel = new SpotlightViewModel(spotlightItems);
        if (!isCache) {
            viewModel.setTrackingData(spotlight.getEnhanceImpressionSpotlightHomePage());
            viewModel.setTrackingCombined(false);
        }
        return viewModel;
    }
}
