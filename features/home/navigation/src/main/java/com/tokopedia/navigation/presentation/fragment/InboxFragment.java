package com.tokopedia.navigation.presentation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.discovery.common.manager.ProductCardOptionsManager;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel;
import com.tokopedia.discovery.common.utils.ViewUtilsKt;
import com.tokopedia.iconunify.IconUnify;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.analytics.InboxGtmTracker;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation.domain.model.Recommendation;
import com.tokopedia.navigation.domain.model.TopadsHeadlineUiModel;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.adapter.InboxAdapterTypeFactory;
import com.tokopedia.navigation.presentation.adapter.RecomItemDecoration;
import com.tokopedia.navigation.presentation.base.BaseTestableParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.presenter.InboxPresenter;
import com.tokopedia.navigation.presentation.view.InboxAdapterListener;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel;
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter;
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener;
import com.tokopedia.topads.sdk.v2.tdnbanner.listener.TdnBannerResponseListener;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response;
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response;
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener;
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends BaseTestableParentFragment<GlobalNavComponent, InboxPresenter> implements
        InboxView, InboxAdapterListener, RecommendationListener, TdnBannerResponseListener, TopAdsImageViewClickListener {

    public static final int CHAT_MENU = 0;
    public static final int DISCUSSION_MENU = 1;
    public static final int REVIEW_MENU = 2;
    public static final int HELP_MENU = 3;
    public static final int DEFAULT_SPAN_COUNT = 2;
    public static final int SINGLE_SPAN_COUNT = 1;
    public static final int HEADLINE_POS_NOT_TO_BE_ADDED = 11;
    public static final int TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED = 22;
    private static final String PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition";
    private static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";
    private static final int REQUEST_FROM_PDP = 138;
    private static final String className = "com.tokopedia.navigation.presentation.fragment.InboxFragment";
    private static final String PAGE_SOURCE_KEY = "pageSource";
    private static final String PAGE_SOURCE = "review inbox";

    private static final String COMPONENT_NAME_TOP_ADS = "Inbox Recommendation Top Ads";
    private static final int SHIFTING_INDEX = 1;
    private static final int TOP_ADS_BANNER_COUNT = 2;
    private static final int HEADLINE_ADS_BANNER_COUNT = 2;
    private static final String CLICK_TYPE_WISHLIST = "&click_type=wishlist";

    @Inject
    InboxPresenter presenter;

    @Inject
    GlobalNavAnalytics globalNavAnalytics;

    @Inject
    RemoteConfig remoteConfig;

    @Inject
    UserSessionInterface userSessionInterface;

    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter adapter;
    private View emptyLayout;
    private StaggeredGridLayoutManager layoutManager;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private TrackingQueue trackingQueue;
    private List<Visitable> visitables;
    private RemoteConfigInstance remoteConfigInstance;
    private String talkUnreadCount = "";
    private CpmModel headlineData;
    private boolean isAdded;
    private int pageNum = 0;
    private boolean isTopAdsBannerAdded;
    private int headlineExperimentPosition = HEADLINE_POS_NOT_TO_BE_ADDED;
    private int toAdsBannerExperimentPosition = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED;
    private List<TopAdsImageUiModel> topAdsBannerInProductCards;
    private List<Integer> headlineIndexList;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackingQueue = new TrackingQueue(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        TopAdsGtmTracker.getInstance().eventInboxProductView(trackingQueue);
        InboxGtmTracker.getInstance().eventInboxProductView(trackingQueue);
        trackingQueue.sendAll();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FROM_PDP && data != null) {
            boolean wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                    false);
            int position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1);
            if (position < 0 || adapter.getList().size() <= position) return;

            if (adapter.getList().get(position) instanceof Recommendation) {
                Recommendation recommendation = (Recommendation) adapter.getList().get(position);
                recommendation.getRecommendationItem().setWishlist(wishlistStatusFromPdp);
                adapter.notifyItemChanged(position);
            }
        }

        ProductCardOptionsManager
                .handleProductCardOptionsActivityResult(requestCode, resultCode, data, this::handleWishlistAction);
    }

    private void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel) {
        if (productCardOptionsModel.getWishlistResult().isUserLoggedIn()) {
            handleWishlistActionForLoggedInUser(productCardOptionsModel);
        } else {
            RouteManager.route(getContext(), ApplinkConst.LOGIN);
        }
    }

    private void handleWishlistActionForLoggedInUser(ProductCardOptionsModel productCardOptionsModel) {
        if (getContext() != null) {
            if (productCardOptionsModel.getWishlistResult().isSuccess()) {
                handleWishlistActionSuccess(productCardOptionsModel);
            } else {
                handleWishlistActionFailedV2(productCardOptionsModel.getWishlistResult());
            }
        }
    }

    private void handleWishlistActionSuccess(ProductCardOptionsModel productCardOptionsModel) {
        if (getContext() == null) return;

        boolean isAddWishlist = productCardOptionsModel.getWishlistResult().isAddWishlist();

        InboxGtmTracker.getInstance().eventClickRecommendationWishlist(getContext(), isAddWishlist);
        adapter.notifyItemChanged(productCardOptionsModel.getProductPosition(), isAddWishlist);

        if (isAddWishlist) {
            showSuccessAddWishlistV2(productCardOptionsModel.getWishlistResult());
            if (productCardOptionsModel.isTopAds()) onClickTopAdsWishlistV2(productCardOptionsModel);
        } else {
            showSuccessRemoveWishlistV2(productCardOptionsModel.getWishlistResult());
        }
    }

    private void onClickTopAdsWishlistV2(ProductCardOptionsModel productCardOptionsModel) {
        String clickUrl = productCardOptionsModel.getTopAdsClickUrl() + CLICK_TYPE_WISHLIST;
        new TopAdsUrlHitter(getContext()).hitClickUrl(getActivity().getClass().getName(), clickUrl,
                productCardOptionsModel.getProductId(), productCardOptionsModel.getProductName(),
                productCardOptionsModel.getProductImageUrl(), COMPONENT_NAME_TOP_ADS);
    }

    private void showSuccessAddWishlistV2(ProductCardOptionsModel.WishlistResult wishlistResult) {
        if (getActivity() == null) return;
        View view = getActivity().findViewById(android.R.id.content);

        if (view == null) return;

        AddRemoveWishlistV2Handler.INSTANCE.showAddToWishlistV2SuccessToaster(wishlistResult, getActivity(), view);
    }

    private void showSuccessRemoveWishlistV2(ProductCardOptionsModel.WishlistResult wishlistResult) {
        if (getActivity() == null) return;

        View view = getActivity().findViewById(android.R.id.content);
        if (view == null) return;

        AddRemoveWishlistV2Handler.INSTANCE.showRemoveWishlistV2SuccessToaster(wishlistResult, getActivity(), view);
    }

    private void handleWishlistActionFailedV2(ProductCardOptionsModel.WishlistResult wishlistResult) {
        if (getView() == null || getActivity() == null) return;
        View rootView = getView().getRootView();

        String errorMsg;
        if (!wishlistResult.getMessageV2().isEmpty()) {
            errorMsg = wishlistResult.getMessageV2();
        } else {
            if (wishlistResult.isAddWishlist()) errorMsg = getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg);
            else errorMsg = getString(com.tokopedia.wishlist_common.R.string.on_failed_remove_from_wishlist_msg);
        }

        if (!wishlistResult.getCtaTextV2().isEmpty() && !wishlistResult.getCtaActionV2().isEmpty()) {
            String ctaText = wishlistResult.getCtaTextV2();
            AddRemoveWishlistV2Handler.INSTANCE.showWishlistV2ErrorToasterWithCta(errorMsg, ctaText, wishlistResult.getCtaActionV2(), rootView, getActivity());
        } else {
            AddRemoveWishlistV2Handler.INSTANCE.showWishlistV2ErrorToaster(errorMsg, rootView);
        }
    }

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
        this.intiInjector();
        presenter.setView(this);
        presenter.preFetchTopAdsHeadline();

        List<Visitable> dataInbox = getData();
        InboxAdapterTypeFactory typeFactory = new InboxAdapterTypeFactory(userSessionInterface, this, this, this, this);
        adapter = new InboxAdapter(typeFactory, dataInbox);

        emptyLayout = view.findViewById(R.id.empty_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecomItemDecoration(ViewUtilsKt.toDpInt(8f)));
        layoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener();
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setColorSchemeResources(com.tokopedia.unifyprinciples.R.color.Unify_GN500);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getInboxData());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isAdded = false;
                isTopAdsBannerAdded = false;
                pageNum = 0;
                endlessRecyclerViewScrollListener.resetState();
                adapter.clearAllElements();
                adapter.addElement(getData());
                presenter.getInboxData();
                presenter.preFetchTopAdsHeadline();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @NonNull
    private EndlessRecyclerViewScrollListener getEndlessRecyclerViewScrollListener() {
        return new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.getRecomData(page);
            }
        };
    }

    @Override
    public void onItemClickListener(Visitable item, int position) {
        if (item instanceof Inbox) {
            Inbox inbox = (Inbox) item;
            globalNavAnalytics.eventInboxPage(getString(inbox.getTitle()).toLowerCase());
            getCallingIntent(position);
        }
    }

    @Override
    public void onWishlistV2Click(@NonNull RecommendationItem item, boolean isAddWishlist) {
        if (presenter.isLoggedIn()) {
            if (isAddWishlist) {
                presenter.addWishlistV2(item, new WishlistV2ActionListener() {
                    @Override
                    public void onErrorAddWishList(@NonNull Throwable throwable, @NonNull String productId) {
                        String errorMsg = ErrorHandler.getErrorMessage(getContext(), throwable);
                        if (getView() == null) return;
                        View rootView = getView().getRootView();
                        AddRemoveWishlistV2Handler.INSTANCE.showWishlistV2ErrorToaster(errorMsg, rootView);
                    }

                    @Override
                    public void onSuccessAddWishlist(@NonNull AddToWishlistV2Response.Data.WishlistAddV2 result, @NonNull String productId) {
                        if (getActivity() != null) {
                            View view = getActivity().findViewById(android.R.id.content);
                            if (view == null) return;
                            if (getContext() != null) {
                                AddRemoveWishlistV2Handler.INSTANCE.showAddToWishlistV2SuccessToaster(result, getContext(), view);
                            }
                        }
                    }

                    @Override
                    public void onErrorRemoveWishlist(@NonNull Throwable throwable, @NonNull String productId) {
                        String errorMsg = ErrorHandler.getErrorMessage(getContext(), throwable);
                        if (getView() == null) return;
                        View rootView = getView().getRootView();
                        AddRemoveWishlistV2Handler.INSTANCE.showWishlistV2ErrorToaster(errorMsg, rootView);
                    }

                    @Override
                    public void onSuccessRemoveWishlist(@NonNull DeleteWishlistV2Response.Data.WishlistRemoveV2 result, @NonNull String productId) {
                        if (getActivity() != null) {
                            View view = getActivity().findViewById(android.R.id.content);
                            if (view == null) return;
                            if (getContext() != null) {
                                AddRemoveWishlistV2Handler.INSTANCE.showRemoveWishlistV2SuccessToaster(result, getContext(), view);
                            }
                        }
                    }
                });
            } else {
                presenter.removeWishlistV2(item, new WishlistV2ActionListener() {
                    @Override
                    public void onErrorAddWishList(@NonNull Throwable throwable, @NonNull String productId) { }

                    @Override
                    public void onSuccessAddWishlist(@NonNull AddToWishlistV2Response.Data.WishlistAddV2 result, @NonNull String productId) { }

                    @Override
                    public void onErrorRemoveWishlist(@NonNull Throwable throwable, @NonNull String productId) {

                    }

                    @Override
                    public void onSuccessRemoveWishlist(@NonNull DeleteWishlistV2Response.Data.WishlistRemoveV2 result, @NonNull String productId) {

                    }
                });
            }
        } else {
            RouteManager.route(getContext(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onProductClick(@NotNull RecommendationItem item, @org.jetbrains.annotations.Nullable String layoutType, @NotNull int... position) {
        if (item.isTopAds()) {
            onClickTopAds(item);
        } else {
            onClickOrganic(item);
        }
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, String.valueOf(item.getProductId()));
        if (position.length >= 1) intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[0]);
        startActivityForResult(intent, REQUEST_FROM_PDP);
    }

    @Override
    public void onThreeDotsClick(@NotNull RecommendationItem item, @NotNull int... position) {
        if (position.length == 0) return;

        ProductCardOptionsManager
                .showProductCardOptions(this, createProductCardOptionsModel(item, position[0]));
    }

    private ProductCardOptionsModel createProductCardOptionsModel(RecommendationItem recommendationItem, int productPosition) {
        ProductCardOptionsModel productCardOptionsModel = new ProductCardOptionsModel();

        productCardOptionsModel.setHasWishlist(true);
        productCardOptionsModel.setWishlisted(recommendationItem.isWishlist());
        productCardOptionsModel.setProductId(String.valueOf(recommendationItem.getProductId()));
        productCardOptionsModel.setTopAds(recommendationItem.isTopAds());
        productCardOptionsModel.setTopAdsWishlistUrl(recommendationItem.getWishlistUrl());
        productCardOptionsModel.setTopAdsClickUrl(recommendationItem.getClickUrl());
        productCardOptionsModel.setProductName(recommendationItem.getName());
        productCardOptionsModel.setProductImageUrl(recommendationItem.getImageUrl());
        productCardOptionsModel.setProductPosition(productPosition);

        return productCardOptionsModel;
    }

    @Override
    public void onProductImpression(@NotNull RecommendationItem item) {
        if (item.isTopAds()) {
            onImpressionTopAds(item);
        } else {
            onImpressionOrganic(item);
        }
    }

    private void intiInjector() {
        DaggerGlobalNavComponent.factory()
                .create(
                    ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent(),
                    requireContext()
                ).inject(this);
    }

    private List<Visitable> getData() {
        List<Visitable> inboxList = new ArrayList<>();
        inboxList.add(new Inbox(
                IconUnify.CHAT,
                R.string.chat,
                R.string.chat_desc
        ));
        inboxList.add(new Inbox(
                IconUnify.DISCUSSION,
                R.string.diskusi,
                R.string.diskusi_desc
        ));
        inboxList.add(new Inbox(
                IconUnify.STAR,
                R.string.ulasan,
                R.string.ulasan_desc
        ));
        inboxList.add(new Inbox(
                IconUnify.CALL_CENTER,
                R.string.pesan_bantuan,
                R.string.pesan_bantuan_desc
        ));
        inboxList.add(new InboxTopAdsBannerUiModel());
        return inboxList;
    }

    private void getCallingIntent(int position) {
        switch (position) {
            case CHAT_MENU:
                boolean isChatUsingOld = remoteConfig.getBoolean(RemoteConfigKey.TOPCHAT_OLD);
                if (isChatUsingOld) {
                    RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_OLD);
                } else {
                    RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_IDLESS);
                }
                break;
            case DISCUSSION_MENU:
                if (getActivity() != null
                        && getActivity().getApplicationContext() != null) {
                    InboxGtmTracker.getInstance().sendNewPageInboxTalkTracking(getContext(), presenter.getUserId(), talkUnreadCount);
                    RouteManager.route(getActivity(), ApplinkConstInternalGlobal.INBOX_TALK);
                }
                break;
            case REVIEW_MENU:
                RouteManager.route(getActivity(), Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(PAGE_SOURCE_KEY, PAGE_SOURCE).build().toString());
                break;
            case HELP_MENU:
                RouteManager.route(getActivity(), ApplinkConst.INBOX_TICKET);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumed()) presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void loadData() {
        setTitle(getString(R.string.inbox));
    }

    @Override
    public void onStartLoading() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onError(String message) {
        emptyLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), emptyLayout, message, () ->
                presenter.getInboxData());

    }

    @Override
    public void onHideLoading() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRenderNotifInbox(NotificationsModel entity) {
        emptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        talkUnreadCount = entity.getInbox().getTalk();
        adapter.updateValue(entity);
    }

    @Override
    public void hideLoadMoreLoading() {
        adapter.hideLoading();
        endlessRecyclerViewScrollListener.updateStateAfterGetData();
    }

    @Override
    public void showLoadMoreLoading() {
        adapter.showLoading();
    }

    @Override
    public void onTopAdsHeadlineReceived(CpmModel data) {
        this.headlineData = data;
        presenter.getFirstRecomData();

        if (data == null
                || data.getData() == null
                || data.getData().isEmpty()
                || data.getData().get(0) == null
                || data.getData().get(0).getCpm() == null
                || adapter == null) {
            return;
        }
        setHeadlineIndexList(data);
    }

    private void setHeadlineIndexList(CpmModel data) {
        headlineIndexList = new ArrayList<>();
        int size = data.getHeader().getTotalData();
        for (int i = 0; i< size; i++){
            headlineIndexList.add(data.getData().get(i).getCpm().getPosition()+adapter.getList().size());
        }
    }

    @Override
    public void onRenderRecomInbox(List<Visitable> list, RecomTitle title) {
        this.visitables = list;
        adapter.addElement(list);
        int index = 0;
        if (headlineIndexList != null && !headlineIndexList.isEmpty()){
            if (pageNum == 0) {
                headlineExperimentPosition = headlineIndexList.get(0);
            } else if (headlineIndexList.size() == HEADLINE_ADS_BANNER_COUNT && pageNum < HEADLINE_ADS_BANNER_COUNT) {
                headlineExperimentPosition = headlineIndexList.get(1);
                index = 1;
            }
            if ((headlineExperimentPosition != HEADLINE_POS_NOT_TO_BE_ADDED || (headlineIndexList.size() == HEADLINE_ADS_BANNER_COUNT && pageNum < HEADLINE_ADS_BANNER_COUNT))
                    && headlineExperimentPosition <= adapter.getList().size() && (!isAdded || (headlineIndexList.size() == HEADLINE_ADS_BANNER_COUNT && pageNum < HEADLINE_ADS_BANNER_COUNT))) {
                if (isTopAdsBannerAdded) {
                    adapter.addElement(headlineExperimentPosition + SHIFTING_INDEX,
                            new TopadsHeadlineUiModel(headlineData, 0, index));
                } else {
                    adapter.addElement(headlineExperimentPosition,
                            new TopadsHeadlineUiModel(headlineData, 0, index));
                }
                isAdded = true;
            }
        }

        if (toAdsBannerExperimentPosition != TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
                && toAdsBannerExperimentPosition <= adapter.getList().size() && !isTopAdsBannerAdded) {
            if (isAdded) {
                adapter.addElement(toAdsBannerExperimentPosition + SHIFTING_INDEX, new InboxTopAdsBannerUiModel(topAdsBannerInProductCards));
            } else {
                adapter.addElement(toAdsBannerExperimentPosition, new InboxTopAdsBannerUiModel(topAdsBannerInProductCards));
            }
            isTopAdsBannerAdded = true;
        }
        pageNum++;
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.inbox);
    }

    @Override
    public void reInitInjector(GlobalNavComponent component) {
        component.inject(this);
        presenter.setView(this);
    }

    @Override
    public InboxPresenter getPresenter() {
        return null;
    }

    @Override
    public void setPresenter(GlobalNavComponent presenter) {
    }

    @Override
    public int getStartProductPosition() {
        //product start after inbox data (like chat, diskusi, etc) + 1 recom title
        return (getData().size() - 1) + 1;
    }

    private void onImpressionTopAds(RecommendationItem item) {
        new TopAdsUrlHitter(getContext()).hitImpressionUrl(getActivity().getClass().getName(), item.getTrackerImageUrl(), String.valueOf(item.getProductId()), item.getName(), item.getImageUrl(), COMPONENT_NAME_TOP_ADS);
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.getPosition(), item.isTopAds());
    }

    private void onImpressionOrganic(RecommendationItem item) {
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.getPosition(), item.isTopAds());
    }

    private void onClickTopAds(RecommendationItem item) {
        new TopAdsUrlHitter(getContext()).hitClickUrl(getActivity().getClass().getName(), item.getClickUrl(), String.valueOf(item.getProductId()), item.getName(), item.getImageUrl(), COMPONENT_NAME_TOP_ADS);
        InboxGtmTracker.getInstance().eventInboxProductClick(getContext(), item, item.getPosition(), item.isTopAds());
    }

    private void onClickOrganic(RecommendationItem item) {
        InboxGtmTracker.getInstance().eventInboxProductClick(getContext(), item, item.getPosition(), item.isTopAds());
    }



    @Override
    public void onTdnBannerResponse(@NonNull List<List<TopAdsImageUiModel>> categoriesList) {
        if (categoriesList.isEmpty()) return;
        if (categoriesList.size() == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = categoriesList.get(1);
            if (!topAdsBannerInProductCards.isEmpty()){
                toAdsBannerExperimentPosition = topAdsBannerInProductCards.get(0).getPosition() + SHIFTING_INDEX
                        + getStartProductPosition();
            }
        } else if (categoriesList.get(0).size() == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = Collections.singletonList(categoriesList.get(0).get(1));
                toAdsBannerExperimentPosition = topAdsBannerInProductCards.get(0).getPosition() + SHIFTING_INDEX
                        + getStartProductPosition();
        }
        adapter.updateTopAdsBanner(categoriesList.get(0));
    }

    @Override
    public void onError(@NotNull Throwable t) {

    }

    @Override
    public void onTopAdsImageViewClicked(@org.jetbrains.annotations.Nullable String applink) {
        if (applink == null) return;
        RouteManager.route(getContext(), applink);
    }
}