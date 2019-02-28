package com.tokopedia.feedplus.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.feedplus.domain.model.InspirationItemDomain;
import com.tokopedia.feedplus.domain.model.feed.DataFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FavoriteCtaDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.model.feed.KolCtaDomain;
import com.tokopedia.feedplus.domain.model.feed.KolPostDomain;
import com.tokopedia.feedplus.domain.model.feed.KolRecommendationDomain;
import com.tokopedia.feedplus.domain.model.feed.KolRecommendationItemDomain;
import com.tokopedia.feedplus.domain.model.feed.ProductFeedDomain;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.feedplus.domain.model.officialstore.BadgeDomain;
import com.tokopedia.feedplus.domain.model.officialstore.LabelDomain;
import com.tokopedia.feedplus.domain.model.officialstore.OfficialStoreDomain;
import com.tokopedia.feedplus.domain.model.officialstore.OfficialStoreProductDomain;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.FavoriteCtaViewModel;
import com.tokopedia.feedplus.view.viewmodel.LabelsViewModel;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ContentProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendItemViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationItemViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ProductCardHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.topads.sdk.domain.model.Data;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


/**
 * @author by nisie on 5/29/17.
 */

public class GetFirstPageFeedsSubscriber extends Subscriber<FeedResult> {

    private static final String FREE_RETURN = "Free Return";
    protected final FeedPlus.View viewListener;
    private static final String TYPE_OS_BRANDS = "official_store_brand";
    private static final String TYPE_OS_CAMPAIGN = "official_store_campaign";
    private static final String TYPE_KOL_CTA = "kol_cta";
    private static final String TYPE_NEW_PRODUCT = "new_product";
    private static final String TYPE_INSPIRATION = "inspirasi";
    private static final String TYPE_TOPADS = "topads";
    private static final String TYPE_KOL = "kolpost";
    private static final String TYPE_KOL_FOLLOWED = "followedkolpost";
    private static final String TYPE_KOL_RECOMMENDATION = "kolrecommendation";
    private static final String TYPE_FAVORITE_CTA = "favorite_cta";
    private static final String TYPE_BANNER = "banner";
    private static final String TYPE_POLLING = "polling";
    private static final String SHOP_ID_BRACKETS = "{shop_id}";
    private static final int TOPADS_MAX_SIZE = 6;
    private static final int TOPADS_MAX_SIZE_SMALL = 3;

    private static final String KOLTYPE_IMAGE = "image";
    private static final String KOLTYPE_VIDEO = "video";
    private static final String KOLTYPE_YOUTUBE = "youtube";

    private final int page;

    public static final String FEED_ENHANCE_ANALYTIC = "FEED_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private final LocalCacheHandler cache;
    private final FeedAnalytics analytics;

    public GetFirstPageFeedsSubscriber(FeedPlus.View viewListener, int page, FeedAnalytics analytics) {
        this.viewListener = viewListener;
        this.page = page;
        this.cache = new LocalCacheHandler(viewListener.getActivity(), FEED_ENHANCE_ANALYTIC);
        this.analytics = analytics;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }

        viewListener.onErrorGetFeedFirstPage(
                ErrorHandler.getErrorMessage(viewListener.getContext(), e));
    }

    private void clearCacheFeedAnalytic() {
        if (page == 1) {
            LocalCacheHandler.clearCache(viewListener.getActivity(), FEED_ENHANCE_ANALYTIC);
        }
    }

    @Override
    public void onNext(FeedResult feedResult) {

        clearCacheFeedAnalytic();

        if (feedResult.getDataSource() == FeedResult.SOURCE_CLOUD)
            viewListener.clearData();

        FeedDomain feedDomain = feedResult.getFeedDomain();
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        if (feedDomain.getWhitelist() != null && feedDomain.getWhitelist().isWhitelist()) {
            addWhitelistData(listFeedView, feedDomain.getWhitelist());
        }
        if (hasFeed(feedDomain)) {
            addMainData(listFeedView, feedDomain, feedResult);
        } else
            viewListener.onShowEmpty();

        if (hasFeed(feedDomain)) {
            viewListener.updateCursor(getCurrentCursor(feedResult));
            viewListener.setLastCursorOnFirstPage(getLastProductCursor(feedDomain.getListFeed()));
        }

        if (feedResult.getDataSource() == FeedResult.SOURCE_CLOUD) {
            viewListener.finishLoading();
        }

        if (feedDomain.getInterestWhitelist()) {
            viewListener.showInterestPick();
        }

        viewListener.sendMoEngageOpenFeedEvent();
    }

    private String getLastProductCursor(List<DataFeedDomain> productList) {
        try {
            return productList.get(productList.size() - 1).getCursor();
        } catch (Exception e) {
            return "";
        }
    }

    private void addMainData(ArrayList<Visitable> listFeedView,
                             FeedDomain feedDomain, FeedResult feedResult) {
        addFeedData(listFeedView, feedDomain.getListFeed());
        checkCanLoadNext(feedResult, listFeedView);
    }

    private void checkCanLoadNext(FeedResult feedResult, ArrayList<Visitable> listFeedView) {

        if (hasFeed(feedResult.getFeedDomain())
                && !feedResult.isHasNext()
                && feedResult.getDataSource() == FeedResult.SOURCE_CLOUD) {
            viewListener.onSuccessGetFeedFirstPageWithAddFeed(listFeedView);
        } else {
            viewListener.onSuccessGetFeedFirstPage(listFeedView);
        }
    }

    protected ArrayList<Visitable> convertToViewModel(FeedDomain feedDomain) {
        ArrayList<Visitable> listFeedView = new ArrayList<>();
        addFeedData(listFeedView, feedDomain.getListFeed());
        return listFeedView;
    }

    private boolean hasFeed(FeedDomain feedDomain) {
        return feedDomain.getListFeed() != null
                && !feedDomain.getListFeed().isEmpty()
                && feedDomain.getListFeed().get(0) != null
                && feedDomain.getListFeed().get(0).getContent() != null
                && feedDomain.getListFeed().get(0).getContent().getType() != null;
    }

    private void addWhitelistData(ArrayList<Visitable> listFeedView,
                                  WhitelistDomain whitelistDomain) {
        listFeedView.add(new WhitelistViewModel(whitelistDomain));
    }

    private void addFeedData(ArrayList<Visitable> listFeedView,
                             List<DataFeedDomain> listFeedDomain) {
        String loginIdString = viewListener.getUserSession().getUserId();
        int loginIdInt = loginIdString.isEmpty() ? 0 : Integer.valueOf(loginIdString);
        int positionFeedProductCard = cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
        if (listFeedDomain != null)
            for (DataFeedDomain domain : listFeedDomain) {
                int currentPosition = viewListener.getAdapterListSize() + listFeedView.size();
                currentPosition = excludeProgressBarOnCurrentPosition(viewListener, currentPosition);
                switch (domain.getContent().getType() != null ? domain.getContent().getType() : "") {
                    case TYPE_OS_CAMPAIGN:
                        if (domain.getContent().getOfficialStores() != null
                                && !domain.getContent().getOfficialStores().isEmpty()) {
                            OfficialStoreCampaignViewModel campaign =
                                    convertToOfficialStoreCampaign(domain);
                            listFeedView.add(campaign);
                        }
                        break;
                    case TYPE_OS_BRANDS:
                        if (domain.getContent().getOfficialStores() != null
                                && !domain.getContent().getOfficialStores().isEmpty()) {
                            OfficialStoreBrandsViewModel officialStore =
                                    convertToBrandsViewModel(domain);
                            if (!officialStore.getListStore().isEmpty())
                                listFeedView.add(officialStore);
                        }
                        break;
                    case TYPE_NEW_PRODUCT:
                        ActivityCardViewModel model = convertToActivityViewModel(domain);
                        if (model.getListProduct() != null && !model.getListProduct().isEmpty()) {
                            positionFeedProductCard++;

                            String eventLabel = String.format("%s", "product upload");
                            model.setEventLabel(eventLabel);
                            model.setPositionFeedCard(positionFeedProductCard);

                            listFeedView.add(model);

                            analytics.eventImpressionFeedUploadedProduct(
                                    model.getListProductAsObjectDataLayer(
                                            eventLabel, loginIdString, positionFeedProductCard),
                                    eventLabel
                            );
                            cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, positionFeedProductCard);
                            cache.applyEditor();

                            String shopId = String.valueOf(model.getHeader().getShopId());
                            List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                            list.add(new FeedEnhancedTracking.Promotion(
                                    Integer.valueOf(model.getFeedId()),
                                    FeedEnhancedTracking.Promotion.createContentNameProductUpload(
                                            model.getTotalProduct()),
                                    String.valueOf(model.getTotalProduct()),
                                    currentPosition,
                                    FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                                    model.getHeader().getShopId(),
                                    ApplinkConst.SHOP.replace(SHOP_ID_BRACKETS, shopId)
                            ));
                            analytics.eventTrackingEnhancedEcommerce(
                                    FeedEnhancedTracking.getImpressionTracking(list, loginIdInt));
                        }
                        break;
                    case TYPE_INSPIRATION:
                        InspirationViewModel inspirationViewModel = convertToInspirationViewModel(domain);
                        if (inspirationViewModel != null
                                && inspirationViewModel.getListProduct() != null
                                && !inspirationViewModel.getListProduct().isEmpty()) {

                            positionFeedProductCard++;
                            String eventLabel = String.format("%s - %s", "inspirasi", inspirationViewModel.getSource());
                            inspirationViewModel.setEventLabel(eventLabel);
                            inspirationViewModel.setPositionFeedCard(positionFeedProductCard);

                            listFeedView.add(inspirationViewModel);


                            analytics.eventImpressionFeedInspiration(
                                    inspirationViewModel.getListProductAsObjectDataLayer(
                                            eventLabel, loginIdString, positionFeedProductCard),
                                    eventLabel
                            );
                            cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, positionFeedProductCard);
                            cache.applyEditor();
                        }
                        break;
                    case TYPE_TOPADS:
                        if (domain.getContent() != null
                                && domain.getContent().getTopAdsList() != null
                                && !domain.getContent().getTopAdsList().isEmpty()) {
                            FeedTopAdsViewModel feedTopAdsViewModel =
                                    convertToTopadsViewModel(domain);
                            if (!feedTopAdsViewModel.getList().isEmpty()) {
                                listFeedView.add(feedTopAdsViewModel);
                            }

                            List<FeedEnhancedTracking.Promotion> listTopAds = new ArrayList<>();
                            List<Data> listData = feedTopAdsViewModel.getList();

                            for (int i = 0; i < listData.size(); i++) {
                                Data data = listData.get(i);
                                if (data.getProduct() != null
                                        && !TextUtils.isEmpty(data.getProduct().getId())) {
                                    listTopAds.add(new FeedEnhancedTracking.Promotion(
                                            Integer.valueOf(data.getId()),
                                            FeedEnhancedTracking.Promotion
                                                    .createContentNameTopadsProduct(),
                                            (TextUtils.isEmpty(data.getAdRefKey()) ?
                                                    FeedEnhancedTracking.Promotion.TRACKING_NONE :
                                                    data.getAdRefKey()),
                                            currentPosition,
                                            String.valueOf(data.getProduct().getCategory()),
                                            Integer.valueOf(data.getId()),
                                            FeedEnhancedTracking.Promotion.TRACKING_EMPTY
                                    ));
                                } else if (data.getShop() != null
                                        && !TextUtils.isEmpty(data.getShop().getId())) {
                                    listTopAds.add(new FeedEnhancedTracking.Promotion(
                                            Integer.valueOf(data.getId()),
                                            FeedEnhancedTracking.Promotion
                                                    .createContentNameTopadsShop(),
                                            data.getAdRefKey(),
                                            currentPosition,
                                            FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                                            Integer.valueOf(data.getId()),
                                            FeedEnhancedTracking.Promotion.TRACKING_EMPTY
                                    ));
                                }

                            }
                            analytics.eventTrackingEnhancedEcommerce(
                                    FeedEnhancedTracking.
                                            getImpressionTracking(listTopAds, loginIdInt));
                        }
                        break;
                    case TYPE_KOL_FOLLOWED:
                    case TYPE_KOL:
                        if (domain.getContent() != null
                                && domain.getContent().getKolPostDomain() != null) {
                            if (domain.getContent().getKolPostDomain().getType().equals(KOLTYPE_YOUTUBE)) {
                                KolPostYoutubeViewModel kolPostYoutubeViewModel
                                        = convertToKolYoutubeViewModel(domain);
                                listFeedView.add(kolPostYoutubeViewModel);

                                List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                                list.add(new FeedEnhancedTracking.Promotion(
                                        kolPostYoutubeViewModel.getContentId(),
                                        FeedEnhancedTracking.Promotion.createContentNameAnnouncement(
                                                kolPostYoutubeViewModel.getTagsType(),
                                                kolPostYoutubeViewModel.getCardType()),
                                        TextUtils.isEmpty(kolPostYoutubeViewModel.getName()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolPostYoutubeViewModel.getName(),
                                        listFeedView.size(),
                                        TextUtils.isEmpty(kolPostYoutubeViewModel.getLabel()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolPostYoutubeViewModel.getLabel(),
                                        kolPostYoutubeViewModel.getTagsId(),
                                        TextUtils.isEmpty(kolPostYoutubeViewModel.getTagsLink()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolPostYoutubeViewModel.getTagsLink()
                                ));
                                analytics.eventTrackingEnhancedEcommerce(
                                        FeedEnhancedTracking.getImpressionTracking(list, loginIdInt)
                                );
                            } else {
                                KolPostViewModel kolViewModel = convertToKolViewModel(domain);
                                listFeedView.add(kolViewModel);

                                List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                                list.add(new FeedEnhancedTracking.Promotion(
                                        kolViewModel.getContentId(),
                                        FeedEnhancedTracking.Promotion.createContentName(
                                                kolViewModel.getTagsType(),
                                                kolViewModel.getCardType())
                                        ,
                                        TextUtils.isEmpty(kolViewModel.getName()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolViewModel.getName(),
                                        listFeedView.size(),
                                        TextUtils.isEmpty(kolViewModel.getLabel()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolViewModel.getLabel(),
                                        kolViewModel.getTagsId(),
                                        TextUtils.isEmpty(kolViewModel.getTagsLink()) ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                kolViewModel.getTagsLink()
                                ));
                                analytics.eventTrackingEnhancedEcommerce(
                                        FeedEnhancedTracking.getImpressionTracking(list, loginIdInt)
                                );
                            }
                        }
                        break;
                    case TYPE_KOL_RECOMMENDATION:
                        if (domain.getContent() != null
                                && domain.getContent().getKolRecommendations() != null
                                && domain.getContent().getKolRecommendations()
                                .getListRecommendation() != null
                                && !domain.getContent().getKolRecommendations()
                                .getListRecommendation().isEmpty()) {
                            KolRecommendationViewModel kolRecommendationViewModel =
                                    convertToKolRecommendationViewModel(domain.getContent().getKolRecommendations());
                            listFeedView.add(kolRecommendationViewModel);

                            List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                            for (KolRecommendItemViewModel recItem : kolRecommendationViewModel.getListRecommend()) {
                                list.add(new FeedEnhancedTracking.Promotion(
                                        recItem.getId(),
                                        FeedEnhancedTracking.Promotion.createContentNameRecommendation(),
                                        recItem.getName().equals("") ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                recItem.getName(),
                                        listFeedView.size(),
                                        recItem.getLabel().equals("") ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                recItem.getLabel(),
                                        recItem.getId(),
                                        recItem.getUrl().equals("") ?
                                                FeedEnhancedTracking.Promotion.TRACKING_EMPTY :
                                                recItem.getUrl()
                                ));
                            }
                            analytics.eventTrackingEnhancedEcommerce(
                                    FeedEnhancedTracking.getImpressionTracking(list, loginIdInt)
                            );
                        }
                        break;
                    case TYPE_FAVORITE_CTA:
                        if (domain.getContent() != null
                                && domain.getContent().getFavoriteCtaDomain() != null) {
                            FavoriteCtaViewModel favoriteCtaViewModel =
                                    convertToFavoriteCtaViewModel(domain.getContent().getFavoriteCtaDomain());
                            listFeedView.add(favoriteCtaViewModel);
                        }
                        break;
                    case TYPE_KOL_CTA:
                        if (domain.getContent() != null
                                && domain.getContent().getKolCtaDomain() != null) {
                            ContentProductViewModel contentProductViewModel =
                                    convertContentProductViewModel(domain.getContent().getKolCtaDomain());
                            if (contentProductViewModel != null) {
                                listFeedView.add(contentProductViewModel);
                            }
                        }
                    case TYPE_BANNER:
                        if (domain.getContent() != null
                                && domain.getContent().getProductCommunications() != null) {
                            listFeedView.add(domain.getContent().getProductCommunications());

                            List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                            for (ProductCommunicationItemViewModel item :
                                    domain.getContent()
                                            .getProductCommunications()
                                            .getItemViewModels()) {

                                String totalBanner = String.valueOf(
                                        domain.getContent().getProductCommunications()
                                                .getItemViewModels()
                                                .size()
                                );

                                list.add(new FeedEnhancedTracking.Promotion(
                                        item.getActivityId(),
                                        FeedEnhancedTracking.Promotion.createContentNameBanner(),
                                        item.getImageUrl(),
                                        currentPosition,
                                        String.valueOf(totalBanner),
                                        item.getActivityId(),
                                        item.getRedirectUrl()
                                ));
                            }
                            analytics.eventTrackingEnhancedEcommerce(
                                    FeedEnhancedTracking.getImpressionTracking(list, loginIdInt)
                            );
                        }
                        break;
                    case TYPE_POLLING:
                        if (domain.getContent() != null
                                && domain.getContent().getPollViewModel() != null) {
                            PollViewModel pollViewModel = domain.getContent().getPollViewModel();
                            pollViewModel.setPage(page);
                            listFeedView.add(pollViewModel);

                            List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
                            for (PollOptionViewModel option : pollViewModel.getOptionViewModels()) {
                                list.add(new FeedEnhancedTracking.Promotion(
                                        Integer.valueOf(option.getOptionId()),
                                        FeedEnhancedTracking.Promotion.createContentNameVote(),
                                        option.getOption(),
                                        currentPosition,
                                        pollViewModel.getReview(),
                                        Integer.valueOf(pollViewModel.getPollId()),
                                        pollViewModel.getKolProfileUrl()
                                ));
                            }
                            analytics.eventTrackingEnhancedEcommerce(
                                    FeedEnhancedTracking.getImpressionTracking(list, loginIdInt)
                            );
                        }
                        break;
                    default:
                        break;
                }
            }
    }

    private KolRecommendationViewModel convertToKolRecommendationViewModel(KolRecommendationDomain domain) {
        return new KolRecommendationViewModel(
                domain.getExploreLink(),
                domain.getHeaderTitle(),
                domain.getExploreText(),
                convertToListKolRecommend(domain.getListRecommendation())
        );
    }

    private FavoriteCtaViewModel convertToFavoriteCtaViewModel(FavoriteCtaDomain domain) {
        return new FavoriteCtaViewModel(
                domain.getTitle(),
                domain.getSubtitle()
        );
    }

    private ArrayList<KolRecommendItemViewModel> convertToListKolRecommend(List<KolRecommendationItemDomain> kolRecommendations) {
        ArrayList<KolRecommendItemViewModel> list = new ArrayList<>();
        for (KolRecommendationItemDomain recommendationDomain : kolRecommendations) {
            list.add(new KolRecommendItemViewModel(
                    recommendationDomain.getUserId(),
                    recommendationDomain.getUserName(),
                    recommendationDomain.getUserPhoto(),
                    recommendationDomain.getUrl(),
                    recommendationDomain.getInfo(),
                    recommendationDomain.isFollowed()
            ));
        }
        return list;
    }

    private FeedTopAdsViewModel convertToTopadsViewModel(DataFeedDomain domain) {
        Data data = domain.getContent().getTopAdsList().get(0);
        boolean isTopadsShop = data.getProduct() == null || TextUtils.isEmpty(data.getProduct().getId());
        int topadsSize = 0;

        if (isTopadsShop) {
            topadsSize = 1;
        } else if (domain.getContent().getTopAdsList().size() >= TOPADS_MAX_SIZE) {
            topadsSize = TOPADS_MAX_SIZE;
        } else if (domain.getContent().getTopAdsList().size() >= TOPADS_MAX_SIZE_SMALL) {
            topadsSize = TOPADS_MAX_SIZE_SMALL;
        }

        List<Data> topadsDataList = new ArrayList<>();
        for (int i = 0; i < topadsSize; i++) {
            Data topadsData = domain.getContent().getTopAdsList().get(i);
            topadsDataList.add(topadsData);
        }

        return new FeedTopAdsViewModel(topadsDataList);
    }

    private KolPostViewModel convertToKolViewModel(DataFeedDomain domain) {
        KolPostDomain kolPostDomain = domain.getContent().getKolPostDomain();
        List<String> imageList = new ArrayList<>();
        imageList.add(kolPostDomain.getImageUrl());
        KolPostViewModel model = new KolPostViewModel(
                kolPostDomain.getUserId(),
                kolPostDomain.getCardType(),
                kolPostDomain.getHeaderTitle(),
                kolPostDomain.getUserName(),
                kolPostDomain.getUserPhoto(),
                kolPostDomain.getLabel(),
                kolPostDomain.getUserUrl(),
                kolPostDomain.isFollowed(),
                kolPostDomain.getDescription(),
                kolPostDomain.isLiked(),
                kolPostDomain.getLikeCount(),
                kolPostDomain.getCommentCount(),
                page,
                kolPostDomain.getId(),
                TimeConverter.generateTime(viewListener.getContext(), kolPostDomain.getCreateTime
                        ()),
                kolPostDomain.isShowComment(),
                kolPostDomain.isShowLike(),
                imageList,
                kolPostDomain.getItemId(),
                "",
                kolPostDomain.getTagsType(),
                kolPostDomain.getCaption(),
                !TextUtils.isEmpty(kolPostDomain.getContentLink()) ? kolPostDomain.getContentLink()
                        : kolPostDomain.getContentUrl()
        );
        model.setReportable(kolPostDomain.isReportable());

        return model;
    }

    private KolPostYoutubeViewModel convertToKolYoutubeViewModel(DataFeedDomain domain) {
        KolPostDomain kolPostDomain = domain.getContent().getKolPostDomain();
        return new KolPostYoutubeViewModel(
                kolPostDomain.getUserId(),
                kolPostDomain.getCardType(),
                kolPostDomain.getHeaderTitle(),
                kolPostDomain.getUserName(),
                kolPostDomain.getUserPhoto(),
                kolPostDomain.getLabel(),
                kolPostDomain.getUserUrl(),
                kolPostDomain.isFollowed(),
                kolPostDomain.getDescription(),
                kolPostDomain.isLiked(),
                kolPostDomain.getLikeCount(),
                kolPostDomain.getCommentCount(),
                page,
                kolPostDomain.getId(),
                TimeConverter.generateTime(viewListener.getContext(), kolPostDomain
                        .getCreateTime()),
                kolPostDomain.isShowComment(),
                kolPostDomain.isShowLike(),
                kolPostDomain.getYoutubeUrl(),
                kolPostDomain.getItemId(),
                "",
                kolPostDomain.getTagsType(),
                kolPostDomain.getCaption(),
                !TextUtils.isEmpty(kolPostDomain.getContentLink()) ? kolPostDomain.getContentLink()
                        : kolPostDomain.getContentUrl()
        );
    }

    private OfficialStoreCampaignViewModel convertToOfficialStoreCampaign(DataFeedDomain domain) {
        return new OfficialStoreCampaignViewModel(
                domain.getContent().getOfficialStores().get(0).getMobile_img_url(),
                domain.getContent().getOfficialStores().get(0).getRedirect_url_app(),
                domain.getContent().getOfficialStores().get(0).getFeed_hexa_color(),
                domain.getContent().getOfficialStores().get(0).getTitle(),
                convertToOfficialStoreProducts(domain.getContent().getOfficialStores().get(0)),
                page
        );
    }

    private ArrayList<OfficialStoreCampaignProductViewModel>
    convertToOfficialStoreProducts(OfficialStoreDomain domain) {
        ArrayList<OfficialStoreCampaignProductViewModel> listStore = new ArrayList<>();
        if (domain.getProducts() != null)
            for (OfficialStoreProductDomain productDomain : domain.getProducts()) {
                listStore.add(convertToOfficialStoreProduct(productDomain));
            }
        return listStore;
    }

    private OfficialStoreCampaignProductViewModel
    convertToOfficialStoreProduct(OfficialStoreProductDomain productDomain) {
        return new OfficialStoreCampaignProductViewModel(
                productDomain.getData().getId(),
                productDomain.getData().getName(),
                productDomain.getData().getPrice(),
                productDomain.getData().getOriginal_price(),
                productDomain.getData().getDiscount_percentage(),
                productDomain.getData().getImage_url(),
                productDomain.getData().getImage_url_700(),
                productDomain.getData().getUrl_app(),
                productDomain.getData().getShop().getName(),
                productDomain.getBrand_logo(),
                productDomain.getData().getShop().getUrl(),
                convertLabels(productDomain.getData().getLabels()),
                isFreeReturn(productDomain.getData().getBadges()));
    }

    private boolean isFreeReturn(List<BadgeDomain> badges) {
        for (BadgeDomain domain : badges) {
            if (domain.getTitle().equals(FREE_RETURN))
                return true;
        }
        return false;
    }

    private List<LabelsViewModel> convertLabels(List<LabelDomain> labels) {
        List<LabelsViewModel> labelsViewModels = new ArrayList<>();
        for (LabelDomain labelDomain : labels) {
            labelsViewModels.add(new LabelsViewModel(labelDomain.getTitle(),
                    labelDomain.getColor()));
        }
        return labelsViewModels;
    }

    private OfficialStoreBrandsViewModel convertToBrandsViewModel(DataFeedDomain domain) {
        return new OfficialStoreBrandsViewModel(
                convertToListBrands(
                        domain.getContent().getOfficialStores()),
                page
        );
    }

    private ArrayList<OfficialStoreViewModel> convertToListBrands(
            List<OfficialStoreDomain> officialStores) {
        ArrayList<OfficialStoreViewModel> listStore = new ArrayList<>();
        if (officialStores != null)
            for (OfficialStoreDomain officialStoreDomain : officialStores) {
                listStore.add(convertToOfficialStore(officialStoreDomain));
            }
        return listStore;
    }

    private OfficialStoreViewModel convertToOfficialStore(OfficialStoreDomain officialStoreDomain) {
        return new OfficialStoreViewModel(
                officialStoreDomain.getShop_id(),
                officialStoreDomain.getShop_apps_url(),
                officialStoreDomain.getShop_name(),
                officialStoreDomain.getMicrosite_url(),
                officialStoreDomain.getIs_new()
        );
    }

    private InspirationViewModel convertToInspirationViewModel(DataFeedDomain domain) {
        if (domain.getContent() != null
                && !domain.getContent().getInspirationDomains().isEmpty()) {
            return new InspirationViewModel(
                    domain.getContent().getInspirationDomains().get(0).getTitle(),
                    convertToRecommendationListViewModel(domain.getContent().getInspirationDomains().get(0).getListInspirationItem()),
                    domain.getContent().getInspirationDomains().get(0).getSource()
            );
        } else {
            return null;
        }
    }

    private ArrayList<InspirationProductViewModel> convertToRecommendationListViewModel(
            List<InspirationItemDomain> domains) {
        ArrayList<InspirationProductViewModel> listRecommendation = new ArrayList<>();
        if (domains != null && domains.size() == 4)
            for (InspirationItemDomain recommendationDomain : domains) {
                listRecommendation.add(convertToRecommendationViewModel(recommendationDomain));
            }
        return listRecommendation;
    }

    private InspirationProductViewModel convertToRecommendationViewModel(
            InspirationItemDomain recommendationDomain) {
        return new InspirationProductViewModel(recommendationDomain.getId(),
                recommendationDomain.getName(),
                recommendationDomain.getPrice(),
                recommendationDomain.getImageUrl(),
                recommendationDomain.getUrl(),
                page,
                recommendationDomain.getPriceInt());
    }

    protected ActivityCardViewModel convertToActivityViewModel(DataFeedDomain domain) {
        return new ActivityCardViewModel(
                convertToProductCardHeaderViewModel(domain),
                convertToProductListViewModel(domain),
                domain.getSource().getShop().getShareLinkURL(),
                domain.getSource().getShop().getShareLinkDescription(),
                domain.getContent().getStatusActivity(),
                domain.getId(),
                domain.getContent().getTotalProduct(),
                domain.getCursor(),
                page);
    }

    protected ProductCardHeaderViewModel convertToProductCardHeaderViewModel(DataFeedDomain domain) {
        return new ProductCardHeaderViewModel(
                domain.getSource().getShop().getId(),
                domain.getSource().getShop().getUrl(),
                domain.getSource().getShop().getName(),
                domain.getSource().getShop().getAvatar(),
                domain.getSource().getShop().getGold(),
                domain.getCreateTime(),
                domain.getSource().getShop().getOfficial()
        );
    }

    protected ArrayList<ProductFeedViewModel> convertToProductListViewModel(
            DataFeedDomain dataFeedDomain) {
        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
        for (ProductFeedDomain domain : dataFeedDomain.getContent().getProducts()) {
            listProduct.add(
                    new ProductFeedViewModel(
                            domain.getId(),
                            domain.getName(),
                            domain.getPrice(),
                            domain.getImage(),
                            domain.getImageSingle(),
                            domain.getUrl(),
                            dataFeedDomain.getSource().getShop().getName(),
                            dataFeedDomain.getSource().getShop().getAvatar(),
                            domain.getWishlist(),
                            domain.getPriceInt(),
                            page));
        }
        return listProduct;
    }

    private ContentProductViewModel convertContentProductViewModel(KolCtaDomain domain) {
        if (!TextUtils.isEmpty(domain.getImageUrl())
                || !TextUtils.isEmpty(domain.getApplink())
                || !TextUtils.isEmpty(domain.getButtonTitle())
                || !TextUtils.isEmpty(domain.getApplink())
                || !TextUtils.isEmpty(domain.getTextHeader())
                || !TextUtils.isEmpty(domain.getTextDescription())) {
            return new ContentProductViewModel(
                    domain.getImageUrl(),
                    domain.getApplink(),
                    domain.getButtonTitle(),
                    domain.getTextHeader(),
                    domain.getTextDescription()
            );
        }

        return null;
    }

    private int excludeProgressBarOnCurrentPosition(FeedPlus.View viewListener,
                                                    int currentPosition) {
        if (viewListener.getAdapterListSize() > 1) {
            return currentPosition - 1;
        } else {
            return currentPosition;
        }
    }

    protected String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getFeedDomain().getListFeed().size() - 1;
        return feedResult.getFeedDomain().getListFeed().get(lastIndex).getCursor();
    }
}
