package com.tokopedia.feedplus.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like;
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel;
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.api.FeedUrl;
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.AddFeedViewHolder;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.di.FeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollOptionViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.common.util.PostMenuListener;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity;
import com.tokopedia.profile.view.activity.ProfileActivity;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.feedplus.view.FeedPlusConstant.KEY_FEED;
import static com.tokopedia.feedplus.view.FeedPlusConstant.KEY_FEED_FIRSTPAGE_LAST_CURSOR;
import static com.tokopedia.kol.common.util.PostMenuUtilKt.createBottomMenu;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.IS_LIKE_TRUE;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_IS_LIKED;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_COMMENTS;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_LIKES;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View,
        FeedPlus.View.Kol,
        FeedPlus.View.Polling,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener,
        KolPostListener.View.ViewHolder,
        BannerAdapter.BannerItemListener,
        RecommendationCardAdapter.RecommendationCardListener,
        TopadsShopViewHolder.TopadsShopListener,
        CardTitleView.CardTitleListener,
        DynamicPostViewHolder.DynamicPostListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener {

    private static final int OPEN_DETAIL = 54;
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_KOL_PROFILE_FROM_RECOMMENDATION = 83;
    private static final int OPEN_CONTENT_REPORT = 1310;
    private static final int CREATE_POST = 888;
    private static final int DEFAULT_VALUE = -1;
    public static final int REQUEST_LOGIN = 345;

    private static final String TAG = FeedPlusFragment.class.getSimpleName();
    private static final String ARGS_ROW_NUMBER = "row_number";
    private static final String ARGS_ITEM_ROW_NUMBER = "item_row_number";
    private static final String FIRST_CURSOR = "FIRST_CURSOR";
    private static final String YOUTUBE_URL = "{youtube_url}";
    private static final String FEED_TRACE = "mp_feed";
    public static final String BROADCAST_FEED = "BROADCAST_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED";

    private RecyclerView recyclerView;
    private SwipeToRefresh swipeToRefresh;
    private View mainContent;
    private View newFeed;
    private AbstractionRouter abstractionRouter;
    private FeedModuleRouter feedModuleRouter;
    private BroadcastReceiver newFeedReceiver;

    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private PerformanceMonitoring performanceMonitoring;
    private TopAdsInfoBottomSheet infoBottomSheet;
    private int loginIdInt;
    private boolean isLoadedOnce;

    @Inject
    FeedPlusPresenter presenter;

    @Inject
    FeedAnalytics analytics;

    @Inject
    UserSessionInterface userSession;

    public static FeedPlusFragment newInstance() {
        FeedPlusFragment fragment = new FeedPlusFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return FeedTrackingEventLabel.SCREEN_UNIFY_HOME_FEED;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplication() != null) {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(getActivity()
                            .getApplication()))
                    .build()
                    .inject(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getActivity() != null) GraphqlClient.init(getActivity());
        performanceMonitoring = PerformanceMonitoring.start(FEED_TRACE);
        super.onCreate(savedInstanceState);
        initVar();
        setRetainInstance(true);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initVar() {
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this, analytics);
        adapter = new FeedPlusAdapter(typeFactory);
        adapter.setOnLoadListener(totalCount -> {
            int size = adapter.getlist().size();
            int lastIndex = size - 1;
            if (!(adapter.getlist().get(0) instanceof EmptyModel)
                    && !(adapter.getlist().get(lastIndex) instanceof RetryModel)
                    && !(adapter.getlist().get(lastIndex) instanceof AddFeedViewHolder)
                    )
                presenter.fetchNextPage();
        });
        layoutManager = new NpaLinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);

        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getActivity().getApplicationContext() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must implement " +
                    AbstractionRouter.class.getSimpleName());
        }

        if (getActivity().getApplication() instanceof FeedModuleRouter) {
            feedModuleRouter = (FeedModuleRouter) getActivity().getApplication();
        } else {
            throw new IllegalStateException("Application must implement " +
                    FeedModuleRouter.class.getSimpleName());
        }

        String loginIdString = getUserSession().getUserId();
        loginIdInt = TextUtils.isEmpty(loginIdString) ? 0 : Integer.valueOf(loginIdString);

        newFeedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null && intent.getAction().equals(BROADCAST_FEED)) {
                    boolean isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false);
                    if (isHaveNewFeed) {
                        onShowNewFeed("");
                    }
                }
            }
        };
        registerNewFeedReceiver();
    }

    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void reInitInjector(FeedPlusComponent component) {
        component.inject(this);
        presenter.attachView(this);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public FeedPlusPresenter getPresenter() {
        return presenter;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void resetToFirstTime() {
        isLoadedOnce = false;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setPresenter(FeedPlusPresenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        recyclerView = parentView.findViewById(R.id.recycler_view);
        swipeToRefresh = parentView.findViewById(R.id.swipe_refresh_layout);
        mainContent = parentView.findViewById(R.id.main);
        newFeed = parentView.findViewById(R.id.layout_new_feed);

        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void prepareView() {
        adapter.setItemTreshold(2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeToRefresh.setOnRefreshListener(this);
        infoBottomSheet = TopAdsInfoBottomSheet.newInstance(getActivity());
        newFeed.setOnClickListener(v -> {
            scrollToTop();
            showRefresh();
            onRefresh();
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE
                            && layoutManager != null) {
                        int position = 0;
                        Visitable item;
                        if (itemIsFullScreen()) {
                            position = layoutManager.findLastVisibleItemPosition();
                        } else if (layoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findFirstCompletelyVisibleItemPosition();
                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findLastCompletelyVisibleItemPosition();
                        }

                        item = adapter.getlist().get(position);

                        if (position != 0 && item != null && !isTopads(item)) {
                            trackImpression(item);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(FeedPlusFragment.TAG, e.toString());
                }

            }

        });
    }

    private void trackImpression(Visitable item) {
        if (isInspirationItem(item)) {
            analytics.trackImpressionFeedRecommendation();
        }
    }

    private boolean isInspirationItem(Visitable item) {
        return item instanceof InspirationViewModel;
    }

    private boolean isTopads(Visitable item) {
        return item instanceof FeedTopAdsViewModel;
    }

    private boolean itemIsFullScreen() {
        return layoutManager.findLastVisibleItemPosition() -
                layoutManager.findFirstVisibleItemPosition() == 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRefresh() {
        triggerClearNewFeedNotification();
        adapter.clearData();
        newFeed.setVisibility(View.GONE);
        presenter.refreshPage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();

        if (layoutManager != null)
            layoutManager = null;
    }

    @Override
    public void setLastCursorOnFirstPage(String lastCursor) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            LocalCacheHandler cache = new LocalCacheHandler(
                    getActivity().getApplicationContext(),
                    KEY_FEED
            );
            cache.putString(KEY_FEED_FIRSTPAGE_LAST_CURSOR, lastCursor);
            cache.applyEditor();
        }
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name,
                                   String price) {
        feedModuleRouter.goToProductDetail(getContext(), productId, imageSourceSingle, name, price);
    }

    @Override
    public void onGoToProductDetail(int rowNumber, int page, String productId, String
            imageSourceSingle, String name, String price) {
        goToProductDetail(productId, imageSourceSingle, name, price);
        analytics.eventFeedViewProduct(
                getScreenName(),
                productId,
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.View.FEED_PDP);
    }

    @Override
    public void onGoToProductDetailFromProductUpload(int rowNumber,
                                                     int positionFeedCard,
                                                     int page,
                                                     int itemPosition,
                                                     String productId,
                                                     String imageSourceSingle,
                                                     String name,
                                                     String price,
                                                     String priceInt,
                                                     String productUrl,
                                                     String eventLabel) {

        analytics.trackEventClickProductUploadEnhanced(
                name,
                productId,
                priceInt,
                productUrl,
                positionFeedCard,
                itemPosition,
                getUserSession().getUserId(),
                eventLabel
        );
        goToProductDetail(productId, imageSourceSingle, name, price);
    }

    @Override
    public void onGoToProductDetailFromRecentView(String productId, String imgUri,
                                                  String name, String price) {
        goToProductDetail(productId, imgUri, name, price);
        analytics.eventFeedViewProduct(getScreenName(),
                productId,
                FeedTrackingEventLabel.View.VIEW_RECENT,
                FeedTrackingEventLabel.View.FEED_PDP);
    }

    @Override
    public void onGoToProductDetailFromInspiration(int page,
                                                   int rowNumber,
                                                   String productId,
                                                   String imageSource,
                                                   String name,
                                                   String price,
                                                   String priceInt,
                                                   String productUrl,
                                                   String source,
                                                   int positionFeedCard,
                                                   int itemPosition,
                                                   String eventLabel) {
        analytics.trackEventClickInspirationEnhanced(
                name,
                productId,
                priceInt,
                productUrl,
                positionFeedCard,
                itemPosition,
                source,
                getUserSession().getUserId(),
                eventLabel
        );

        goToProductDetail(productId, imageSource, name, price);
        analytics.eventR3Product(productId, getFeedAnalyticsHeader(page, rowNumber)
                + FeedTrackingEventLabel.Click.FEED_RECOMMENDATION_PDP);
    }

    @Override
    public void onGoToFeedDetail(int page, int rowNumber, String feedId) {
        Intent intent = FeedPlusDetailActivity.getIntent(
                getActivity(),
                feedId,
                getFeedAnalyticsHeader(page, rowNumber));
        startActivityForResult(intent, OPEN_DETAIL);
        analytics.eventFeedView(
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.View
                        .FEED_PRODUCT_LIST);

    }

    @Override
    public void onGoToShopDetail(int page, int rowNumber, Integer shopId, String url) {
        Intent intent = feedModuleRouter.getShopPageIntent(getActivity(), String.valueOf(shopId));
        startActivity(intent);
        analytics.eventFeedViewShop(getScreenName(),
                String.valueOf(shopId),
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.View.FEED_SHOP);
    }

    @Override
    public void onOpenVideo(String videoUrl, String subtitle) {
        Intent intent = TransparentVideoActivity.getIntent(getActivity(), videoUrl, subtitle);
        startActivity(intent);
    }

    @Override
    public void onInfoClicked() {
        infoBottomSheet.show();
    }

    @Override
    public void onFavoritedClicked(int adapterPosition) {
        adapter.getItemViewType(adapterPosition);
    }

    @SuppressLint("Range")
    @Override
    public void showSnackbar(String s) {
        NetworkErrorHelper.showSnackbar(getActivity(), s);
    }

    @Override
    public void updateFavorite(int adapterPosition) {
        if (adapter.getlist().get(adapterPosition) instanceof FeedTopAdsViewModel
                && ((FeedTopAdsViewModel) adapter.getlist().get(adapterPosition)).getList().size()
                > 0) {

            Data data = ((FeedTopAdsViewModel) adapter.getlist().get(adapterPosition))
                    .getList()
                    .get(0);
            boolean currentStatus = data.isFavorit();
            data.setFavorit(!currentStatus);
            adapter.notifyItemChanged(adapterPosition);
        }

        if (adapter.getlist().get(adapterPosition) instanceof TopadsShopViewModel) {
            Data data = ((FeedTopAdsViewModel) adapter.getlist().get(adapterPosition))
                    .getList()
                    .get(0);
        }
    }

    @Override
    public void updateFavoriteFromEmpty(String shopId) {
        onRefresh();
        analytics.eventFeedClickShop(getScreenName(),
                shopId, FeedTrackingEventLabel.Click.
                        TOP_ADS_FAVORITE);

    }

    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed) {
        trackFeedImpression(listFeed);

        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        adapter.setEndlessScrollListener();
    }

    @Override
    public void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeed) {
        trackFeedImpression(listFeed);

        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        adapter.unsetEndlessScrollListener();
    }

    @Override
    public void onShowEmpty() {
        adapter.unsetEndlessScrollListener();
        adapter.showEmpty();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void clearData() {
        adapter.clearData();
    }

    @Override
    public void unsetEndlessScroll() {
        adapter.unsetEndlessScrollListener();
    }

    @Override
    public void onShowNewFeed(String totalData) {
        newFeed.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideNewFeed() {
        newFeed.setVisibility(View.GONE);
    }

    @Override
    public void finishLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void showInterestPick() {
        if (getContext() != null && feedModuleRouter.isEnableInterestPick()) {
            RouteManager.route(getContext(), ApplinkConst.INTEREST_PICK);
        }
    }

    @Override
    public void onErrorGetFeedFirstPage(String errorMessage) {
        finishLoading();
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainContent, errorMessage,
                    () -> presenter.refreshPage());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }

    }

    @Override
    public void onSearchShopButtonClicked() {
        int INIT_STATE_FRAGMENT_FAVORITE = 2;
        String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
        Intent intent = feedModuleRouter.getHomeIntent(getContext());
        intent.putExtra(EXTRA_INIT_FRAGMENT, INIT_STATE_FRAGMENT_FAVORITE);
        startActivity(intent);
    }

    @Override
    public void showRefresh() {
        if (!swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void updateCursor(String currentCursor) {
        presenter.setCursor(currentCursor);
    }


    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> listFeed) {
        trackFeedImpression(listFeed);

        adapter.removeEmpty();
        int posStart = adapter.getItemCount();
        adapter.addList(listFeed);
        adapter.notifyItemRangeInserted(posStart, listFeed.size());
    }

    @Override
    public void onShowRetryGetFeed() {
        adapter.showRetry();
    }

    @Override
    public void hideAdapterLoading() {
        adapter.removeLoading();
    }

    @Override
    public int getColor(int color) {
        return MethodChecker.getColor(getActivity(), color);
    }

    @Override
    public void onRetryClicked() {
        adapter.removeRetry();
        adapter.showLoading();
        adapter.setEndlessScrollListener();
        presenter.fetchNextPage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case OPEN_DETAIL:
                if (resultCode == Activity.RESULT_OK)
                    showSnackbar(data.getStringExtra("message"));
                break;
            case OPEN_KOL_COMMENT:
                if (resultCode == Activity.RESULT_OK) {
                    String serverErrorMsg =
                            data.getStringExtra(KolCommentFragment.ARGS_SERVER_ERROR_MSG);
                    if (!TextUtils.isEmpty(serverErrorMsg)) {
                        ToasterError
                                .make(getView(), serverErrorMsg, BaseToaster.LENGTH_LONG)
                                .setAction(R.string.cta_refresh_feed, v -> onRefresh()).show();
                    } else {
                        onSuccessAddDeleteKolComment(
                                data.getIntExtra(KolCommentActivity.ARGS_POSITION, DEFAULT_VALUE),
                                data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0)
                        );
                    }
                }
                break;
            case OPEN_KOL_PROFILE:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessFollowUnfollowFromProfile(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(ProfileActivity.PARAM_IS_FOLLOWING, DEFAULT_VALUE)
                    );

                    updatePostState(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_IS_LIKED, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_LIKES, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_COMMENTS, DEFAULT_VALUE)
                    );
                }
                break;
            case CREATE_POST:
                break;
            case OPEN_KOL_PROFILE_FROM_RECOMMENDATION:
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessFollowUnfollowFromProfileRecommendation(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(ARGS_ITEM_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(ProfileActivity.PARAM_IS_FOLLOWING,
                                    DEFAULT_VALUE)
                    );
                }
                break;
            case OPEN_CONTENT_REPORT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra(ContentReportActivity.RESULT_SUCCESS, false)) {
                        onSuccessReportContent();
                    } else {
                        onErrorReportContent(
                                data.getStringExtra(ContentReportActivity.RESULT_ERROR_MSG)
                        );
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        goToProductDetail(product.getId(),
                product.getImage().getS_ecs(),
                product.getName(),
                product.getPriceFormat()
        );

        analytics.eventFeedClickProduct(getScreenName(),
                product.getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_PRODUCT);

        List<FeedEnhancedTracking.Promotion> listTopAds = new ArrayList<>();

        listTopAds.add(new FeedEnhancedTracking.Promotion(
                Integer.valueOf(product.getAdId()),
                FeedEnhancedTracking.Promotion
                        .createContentNameTopadsProduct(),
                (TextUtils.isEmpty(product.getAdRefKey()) ?
                        FeedEnhancedTracking.Promotion.TRACKING_NONE :
                        product.getAdRefKey()),
                position,
                String.valueOf(product.getCategory()),
                Integer.valueOf(product.getId()),
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY));

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt));
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = feedModuleRouter.getShopPageIntent(getActivity(), shop.getId());
        startActivity(intent);
        analytics.eventFeedClickShop(getScreenName(),
                shop.getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_SHOP);

        List<FeedEnhancedTracking.Promotion> listTopAds = new ArrayList<>();

        listTopAds.add(new FeedEnhancedTracking.Promotion(
                Integer.valueOf(shop.getAdId()),
                FeedEnhancedTracking.Promotion
                        .createContentNameTopadsShop(),
                shop.getAdRefKey(),
                position,
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY,
                Integer.valueOf(shop.getAdId()),
                FeedEnhancedTracking.Promotion.TRACKING_EMPTY
        ));

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt));
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        presenter.favoriteShop(dataShop, position);
        analytics.eventFeedClickShop(getScreenName(),
                dataShop.getShop().getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE);

    }

    public void scrollToTop() {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
        triggerClearNewFeedNotification();
    }

    private void triggerClearNewFeedNotification() {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Intent intent = new Intent(BROADCAST_FEED);
            intent.putExtra(PARAM_BROADCAST_NEW_FEED_CLICKED, true);
            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerNewFeedReceiver();
        if (getUserVisibleHint() && presenter != null) {
            loadData(getUserVisibleHint());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterNewFeedReceiver();
    }

    private void registerNewFeedReceiver() {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_FEED);

            LocalBroadcastManager
                    .getInstance(getActivity().getApplicationContext())
                    .registerReceiver(newFeedReceiver, intentFilter);
        }
    }

    private void unRegisterNewFeedReceiver() {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            LocalBroadcastManager
                    .getInstance(getActivity().getApplicationContext())
                    .unregisterReceiver(newFeedReceiver);
        }
    }

    private void loadData(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded()
                && getActivity() != null && presenter != null) {
            if (!isLoadedOnce) {
                presenter.fetchFirstPage();

                isLoadedOnce = !isLoadedOnce;
            }

            analytics.trackScreen(getActivity(), getScreenName());
        }
    }

    @Override
    public void stopTracePerformanceMon() {
        performanceMonitoring.stopTrace();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadData(isVisibleToUser);

    }

    @Override
    public boolean hasFeed() {
        return adapter.getlist() != null
                && !adapter.getlist().isEmpty()
                && adapter.getlist().size() > 1
                && !(adapter.getlist().get(0) instanceof EmptyModel);
    }

    @Override
    public void onBrandClicked(int page, int rowNumber, OfficialStoreViewModel
            officialStoreViewModel) {
        analytics.eventFeedClickShop(
                getScreenName(),
                String.valueOf(officialStoreViewModel.getShopId()),
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.OFFICIAL_STORE_BRAND
                        + officialStoreViewModel.getShopName()
        );

        Intent intent = feedModuleRouter.getShopPageIntent(getActivity(), String.valueOf
                (officialStoreViewModel.getShopId()));
        startActivity(intent);
    }

    private String getFeedAnalyticsHeader(int page, int rowNumber) {
        return page + "." + rowNumber + " ";
    }

    @Override
    public void onSeeAllOfficialStoresFromCampaign(int page, int rowNumber, String redirectUrl) {
        analytics.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) +
                        FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN_SEE_ALL);
        feedModuleRouter.actionAppLink(getActivity(), redirectUrl);
    }

    @Override
    public void onGoToCampaign(int page, int rowNumber, String redirectUrl, String title) {
        analytics.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN + title);
        feedModuleRouter.actionAppLink(getActivity(), redirectUrl);

    }

    @Override
    public void onSeeAllOfficialStoresFromBrands(int page, int rowNumber) {
        analytics.eventOfficialStoreBrandSeeAll(
                getFeedAnalyticsHeader(page, rowNumber));
        openWebViewBrandsURL(FeedUrl.URL_WEBVIEW_OS);
    }

    @Override
    public void onGoToProductDetailFromCampaign(int page, int rowNumber, String productId, String
            imageSourceSingle, String name, String price) {
        analytics.eventFeedClickProduct(
                getScreenName(),
                productId,
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.Click
                        .OFFICIAL_STORE_CAMPAIGN_PDP);
        goToProductDetail(productId, imageSourceSingle, name, price);

    }

    @Override
    public void onGoToShopDetailFromCampaign(int page, int rowNumber, String shopUrl) {
        analytics.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.Click
                        .OFFICIAL_STORE_CAMPAIGN_SHOP);
        feedModuleRouter.actionAppLink(getActivity(), shopUrl);

    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(feedModuleRouter.getBrandsWebViewIntent(getActivity(), url));
        }
    }

    @Override
    public void onContentProductLinkClicked(String url) {
        if (!TextUtils.isEmpty(url)) {
            feedModuleRouter.openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onGoToKolProfileFromRecommendation(int position, int itemPosition, String userId) {
        if (getContext() != null) {
            Intent profileIntent = ProfileActivity.Companion.createIntent(getContext(), userId)
                    .putExtra(ARGS_ROW_NUMBER, position)
                    .putExtra(ARGS_ITEM_ROW_NUMBER, itemPosition);

            startActivityForResult(profileIntent, OPEN_KOL_PROFILE_FROM_RECOMMENDATION);
        }
    }

    @Override
    public void onGoToKolProfile(int rowNumber, String userId, int postId) {
        if (getContext() != null) {
            Intent profileIntent = ProfileActivity.Companion
                    .createIntentFromFeed(getContext(), userId, postId)
                    .putExtra(ARGS_ROW_NUMBER, rowNumber);
            startActivityForResult(profileIntent, OPEN_KOL_PROFILE);
        }
    }

    @Override
    public void onGoToKolProfileUsingApplink(int rowNumber, String applink) {
        feedModuleRouter.openRedirectUrl(getActivity(), applink);
    }

    @Override
    public void onOpenKolTooltip(int rowNumber, String uniqueTrackingId, String url) {
        feedModuleRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public void trackContentClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void trackTooltipClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void onFollowKolClicked(int rowNumber, int id) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.followKol(id, rowNumber, this);
        } else {
            startActivity(feedModuleRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber, this);
        } else {
            startActivity(feedModuleRouter.getLoginIntent(getActivity()));
        }

    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
        } else {
            startActivity(feedModuleRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnlikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                   String activityType) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.unlikeKol(id, rowNumber, this);
        } else {
            startActivity(feedModuleRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        Intent intent = KolCommentActivity.getCallingIntentFromFeed(getContext(), id, rowNumber);
        startActivityForResult(intent, OPEN_KOL_COMMENT);
    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id) {
        onLikeKolClicked(rowNumber, id, false, "");
    }

    @Override
    public void onUnlikeKolClicked(int adapterPosition, int id) {
        onUnlikeKolClicked(adapterPosition, id, false, "");
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        onGoToKolComment(rowNumber, id, false, "");
    }

    @Override
    public void onEditClicked(boolean hasMultipleContent, String activityId,
                              String activityType) {

    }

    @Override
    public void onMenuClicked(int rowNumber, BaseKolViewModel element) {
        if (getContext() != null) {
            Menus menus = createBottomMenu(getContext(), element,
                    new PostMenuListener() {
                        @Override
                        public void onDeleteClicked() {

                        }

                        @Override
                        public void onReportClick() {
                            goToContentReport(element.getContentId());
                        }
                    }
            );
            menus.show();
        }
    }

    @Override
    public void onGoToLink(String link) {
        if (!TextUtils.isEmpty(link)) {
            feedModuleRouter.openRedirectUrl(getActivity(), link);
        }
    }

    @Override
    public void trackEEPoll(PollOptionViewModel element, String trackingPromoCode, int rowNumber,
                            PollViewModel pollViewModel) {
        UserSessionInterface userSession = getUserSession();
        int loginId = Integer.valueOf(
                !TextUtils.isEmpty(userSession.getUserId()) ? userSession.getUserId() : "0"
        );

        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
        list.add(new FeedEnhancedTracking.Promotion(
                Integer.valueOf(element.getOptionId()),
                FeedEnhancedTracking.Promotion.createContentNameVote(),
                element.getOption(),
                rowNumber,
                pollViewModel.getReview(),
                Integer.valueOf(pollViewModel.getPollId()),
                trackingPromoCode
        ));

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(
                        list,
                        loginId
                )
        );
    }

    @Override
    public void onVoteOptionClicked(int rowNumber, String pollId, String optionId) {
        presenter.sendVote(rowNumber, pollId, optionId);
    }

    @Override
    public void onGoToListKolRecommendation(int page, int rowNumber, String url) {
        if (getParentFragment() instanceof FeedPlusContainerFragment) {
            ((FeedPlusContainerFragment) getParentFragment()).goToExplore(true);
        } else {
            feedModuleRouter.openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onErrorFollowKol(String errorMessage, final int id, final int status, final int
            rowNumber) {
        ToasterError.make(getView(), errorMessage, BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_try_again, v -> {
                    if (status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
                        presenter.unfollowKol(id, rowNumber, FeedPlusFragment.this);
                    else
                        presenter.followKol(id, rowNumber, FeedPlusFragment.this);
                })
                .show();
    }

    @Override
    public void onSuccessFollowUnfollowKol(int rowNumber) {
        if (adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);
            kolPostViewModel.setFollowed(!(kolPostViewModel.isFollowed()));
            kolPostViewModel.setTemporarilyFollowed(!(kolPostViewModel.isTemporarilyFollowed()));
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW);
        }

        if (adapter.getlist().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getlist().get(rowNumber);
            model.getHeader().getFollowCta().setFollow(
                    !model.getHeader().getFollowCta().isFollow()
            );
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW);
        }
    }

    @Override
    public void onErrorLikeDislikeKolPost(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    @Override
    public void onSuccessLikeDislikeKolPost(int rowNumber) {
        if (adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof BaseKolViewModel) {
            BaseKolViewModel kolViewModel = (BaseKolViewModel) adapter.getlist().get(rowNumber);
            kolViewModel.setLiked(!(kolViewModel.isLiked()));
            if (kolViewModel.isLiked()) {
                kolViewModel.setTotalLike(((BaseKolViewModel)
                        adapter.getlist().get(rowNumber)).getTotalLike() + 1);
            } else {
                kolViewModel.setTotalLike(((BaseKolViewModel)
                        adapter.getlist().get(rowNumber)).getTotalLike() - 1);
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE);
        }

        if (adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getlist().get(rowNumber);
            Like like = model.getFooter().getLike();
            like.setChecked(!like.isChecked());
            if (like.isChecked()) {
                try {
                    int likeValue = Integer.valueOf(like.getFmt()) + 1;
                    like.setFmt(String.valueOf(likeValue));
                } catch (NumberFormatException ignored) {
                }
                like.setValue(like.getValue() + 1);
            } else {
                try {
                    int likeValue = Integer.valueOf(like.getFmt()) - 1;
                    like.setFmt(String.valueOf(likeValue));
                } catch (NumberFormatException ignored) {
                }
                like.setValue(like.getValue() - 1);
            }
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_LIKE);
        }
    }

    @Override
    public void onFollowKolFromRecommendationClicked(int rowNumber, int id, int position) {
        presenter.followKolFromRecommendation(id, rowNumber, position, this);
    }

    @Override
    public void onUnfollowKolFromRecommendationClicked(int rowNumber, int id, int position) {
        presenter.unfollowKolFromRecommendation(id, rowNumber, position, this);
    }

    @Override
    public void onSuccessFollowKolFromRecommendation(int rowNumber, int position, boolean isFollow) {
        if (adapter.getlist().get(rowNumber) instanceof FeedRecommendationViewModel) {
            FeedRecommendationViewModel model = (FeedRecommendationViewModel) adapter.getlist().get(rowNumber);
            model.getCards().get(position).getCta().setFollow(isFollow);
            adapter.notifyItemChanged(rowNumber, position);
        }
    }

    private void onSuccessAddDeleteKolComment(int rowNumber, int totalNewComment) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof BaseKolViewModel) {
            BaseKolViewModel kolViewModel = (BaseKolViewModel) adapter.getlist().get(rowNumber);
            kolViewModel.setTotalComment((
                    (BaseKolViewModel)
                            adapter.getlist().get(rowNumber)).getTotalComment() +
                    totalNewComment);
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_COMMENT);
        }

        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getlist().get(rowNumber);
            Comment comment = model.getFooter().getComment();
            try {
                int commentValue = Integer.valueOf(comment.getFmt()) + totalNewComment;
                comment.setFmt(String.valueOf(commentValue));
            } catch (NumberFormatException ignored) {
            }
            comment.setValue(comment.getValue() + totalNewComment);
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_COMMENT);
        }
    }

    private void onSuccessFollowUnfollowFromProfile(int rowNumber, int isFollowing) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);

            if (isFollowing != DEFAULT_VALUE) {
                kolViewModel.setFollowed(isFollowing
                        == ProfileActivity.IS_FOLLOWING_TRUE);
                kolViewModel.setTemporarilyFollowed(isFollowing
                        == ProfileActivity.IS_FOLLOWING_TRUE);
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW);
        }
    }

    private void updatePostState(int rowNumber, int isLiked, int totalLike, int totalComment) {
        if (rowNumber != DEFAULT_VALUE
                && adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof BaseKolViewModel) {
            BaseKolViewModel kolViewModel = (BaseKolViewModel) adapter.getlist().get(rowNumber);

            if (isLiked != DEFAULT_VALUE) {
                kolViewModel.setLiked(isLiked == IS_LIKE_TRUE);
            }

            if (totalLike != DEFAULT_VALUE) {
                kolViewModel.setTotalLike(totalLike);
            }

            if (totalComment != DEFAULT_VALUE) {
                kolViewModel.setTotalComment(totalComment);
            }
            adapter.notifyItemChanged(rowNumber);
        }
    }

    private void onSuccessFollowUnfollowFromProfileRecommendation(int rowNumber,
                                                                  int itemRowNumber,
                                                                  int isFollowing) {
        if (rowNumber != DEFAULT_VALUE
                && itemRowNumber != DEFAULT_VALUE
                && adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof KolRecommendationViewModel) {
            KolRecommendationViewModel recommendationViewModel =
                    (KolRecommendationViewModel) adapter.getlist().get(rowNumber);

            if (isFollowing != DEFAULT_VALUE) {
                recommendationViewModel.getListRecommend()
                        .get(itemRowNumber)
                        .setFollowed(isFollowing
                                == ProfileActivity.IS_FOLLOWING_TRUE);
            }

            adapter.notifyItemChanged(rowNumber);
        }
    }

    private void onSuccessReportContent() {
        ToasterNormal
                .make(getView(),
                        getString(R.string.feed_content_reported),
                        BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close, v -> {
                })
                .show();
    }

    private void onErrorReportContent(String errorMsg) {
        ToasterError
                .make(getView(), errorMsg, BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close, v -> {
                })
                .show();
    }

    @Override
    public void onUserNotLogin() {
        finishLoading();
        adapter.clearData();
        adapter.unsetEndlessScrollListener();
        adapter.showUserNotLogin();
    }

    @Override
    public void onGoToLogin() {
        if (getActivity() != null) {
            Intent intent = feedModuleRouter.getLoginIntent(getActivity());
            getActivity().startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    @Override
    public void onSuccessSendVote(int rowNumber, String optionId,
                                  VoteStatisticDomainModel voteStatisticDomainModel) {
        if (adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof PollViewModel) {
            PollViewModel pollViewModel = (PollViewModel) adapter.getlist().get(rowNumber);
            pollViewModel.setVoted(true);
            pollViewModel.setTotalVoter(voteStatisticDomainModel.getTotalParticipants());

            for (int i = 0; i < pollViewModel.getOptionViewModels().size(); i++) {
                PollOptionViewModel pollOptionViewModel
                        = pollViewModel.getOptionViewModels().get(i);

                pollOptionViewModel.setSelected(optionId.equals(pollOptionViewModel.getOptionId()) ?
                        PollOptionViewModel.SELECTED : PollOptionViewModel.UNSELECTED);

                String newPercentage
                        = voteStatisticDomainModel.getListOptions().get(i).getPercentage();
                pollOptionViewModel.setPercentage(newPercentage);
            }

            adapter.notifyItemChanged(rowNumber);
        }

        if (adapter.getlist().size() > rowNumber
                && adapter.getlist().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getlist().get(rowNumber);
            for (BasePostViewModel basePostViewModel : model.getContentList()) {
                if (basePostViewModel instanceof PollContentViewModel) {
                    PollContentViewModel pollContentViewModel = (PollContentViewModel) basePostViewModel;
                    pollContentViewModel.setVoted(true);

                    int totalVoter;
                    try {
                        totalVoter = Integer.valueOf(voteStatisticDomainModel.getTotalParticipants());
                    } catch (NumberFormatException ignored) {
                        totalVoter = 0;
                    }
                    pollContentViewModel.setTotalVoterNumber(totalVoter);

                    for (int i = 0; i < pollContentViewModel.getOptionList().size(); i++) {
                        PollContentOptionViewModel optionViewModel
                                = pollContentViewModel.getOptionList().get(i);

                        optionViewModel.setSelected(optionId.equals(optionViewModel.getOptionId()) ?
                                PollOptionViewModel.SELECTED : PollOptionViewModel.UNSELECTED);

                        int newPercentage = 0;
                        try {
                            newPercentage = Integer.valueOf(
                                    voteStatisticDomainModel.getListOptions().get(i).getPercentage()
                            );
                        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                        }
                        optionViewModel.setPercentage(newPercentage);
                    }
                }
            }

            adapter.notifyItemChanged(rowNumber);
        }


    }

    @Override
    public void onErrorSendVote(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public int getAdapterListSize() {
        return adapter.getItemCount();
    }

    @Override
    public void onWhitelistClicked(String url) {
        if (getActivity() != null) {
            startActivityForResult(CreatePostImagePickerActivity.getInstance(getActivity(), url),
                    CREATE_POST);
        }
    }

    @Override
    public void onSuccessToggleFavoriteShop(int rowNumber, int adapterPosition) {
        if (adapter.getlist().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getlist().get(rowNumber);
            model.getHeader().getFollowCta().setFollow(
                    !model.getHeader().getFollowCta().isFollow()
            );
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW);
        }

        if (adapter.getlist().get(rowNumber) instanceof FeedRecommendationViewModel) {
            FeedRecommendationViewModel model
                    = (FeedRecommendationViewModel) adapter.getlist().get(rowNumber);
            model.getCards().get(adapterPosition).getCta().setFollow(
                    !model.getCards().get(adapterPosition).getCta().isFollow()
            );
            adapter.notifyItemChanged(rowNumber, adapterPosition);
        }

        if (adapter.getlist().get(rowNumber) instanceof TopadsShopViewModel) {
            TopadsShopViewModel model
                    = (TopadsShopViewModel) adapter.getlist().get(rowNumber);
            model.getDataList().get(adapterPosition).setFavorit(
                    !model.getDataList().get(adapterPosition).isFavorit()
            );
            adapter.notifyItemChanged(rowNumber, adapterPosition);
        }
    }

    @Override
    public void onErrorToggleFavoriteShop(String message, int rowNumber, int adapterPosition,
                                          String shopId) {
        ToasterError.make(getView(), message, BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_try_again,
                        v -> presenter.toggleFavoriteShop(rowNumber, adapterPosition, shopId)
                )
                .show();
    }

    @Override
    public UserSessionInterface getUserSession() {
        return userSession;
    }

    @Override
    public void eventTrackingEEGoToProduct(Integer shopIdInt, String feedId, int totalProduct, int
            positionInFeed, String category) {
        String SHOP_ID_BRACKETS = "{shop_id}";

        String loginIdString = getUserSession().getUserId();
        int loginIdInt = loginIdString.isEmpty() ? 0 : Integer.valueOf(loginIdString);

        String shopId = String.valueOf(shopIdInt);
        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
        list.add(new FeedEnhancedTracking.Promotion(
                Integer.valueOf(feedId),
                FeedEnhancedTracking.Promotion.createContentNameProductUpload(totalProduct),
                String.valueOf(totalProduct),
                positionInFeed,
                category,
                shopIdInt,
                ApplinkConst.SHOP.replace(SHOP_ID_BRACKETS, shopId)
        ));

        analytics.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(list, loginIdInt));
    }

    @Override
    public void sendMoEngageOpenFeedEvent() {
        feedModuleRouter.sendMoEngageOpenFeedEvent(!hasFeed());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null && getActivity().isFinishing()) {
            unRegisterNewFeedReceiver();
        }
    }

    @Override
    public AbstractionRouter getAbstractionRouter() {
        return abstractionRouter;
    }

    @Override
    public void onBannerItemClick(int positionInFeed, int adapterPosition,
                                  @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);

        if (adapter.getlist().get(positionInFeed) instanceof BannerViewModel) {
            BannerViewModel model = (BannerViewModel) adapter.getlist().get(positionInFeed);
            trackBannerClick(
                    positionInFeed,
                    adapterPosition,
                    model.getItemViewModels().get(adapterPosition).getTrackingBannerModel(),
                    FeedAnalytics.Element.IMAGE
            );
        }
    }

    @Override
    public void onRecommendationAvatarClick(int positionInFeed, int adapterPosition,
                                            @NotNull String redirectLink) {
        onGoToLink(redirectLink);

        if (adapter.getlist().get(positionInFeed) instanceof FeedRecommendationViewModel) {

            FeedRecommendationViewModel model
                    = (FeedRecommendationViewModel) adapter.getlist().get(positionInFeed);
            trackRecommendationClick(
                    positionInFeed,
                    adapterPosition,
                    model.getCards().get(adapterPosition).getTrackingRecommendationModel(),
                    FeedAnalytics.Element.AVATAR
            );
        }
    }

    @Override
    public void onRecommendationActionClick(int positionInFeed, int adapterPosition,
                                            @NonNull String id, @NonNull String type,
                                            boolean isFollow) {
        if (type.equals(FollowCta.AUTHOR_USER)) {
            int userIdInt = 0;
            try {
                userIdInt = Integer.valueOf(id);
            } catch (NumberFormatException ignored) {
            }

            if (isFollow) {
                onUnfollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition);
            } else {
                onFollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition);
            }

        } else if (type.equals(FollowCta.AUTHOR_SHOP)) {
            presenter.toggleFavoriteShop(positionInFeed, id);
        }

        if (adapter.getlist().get(positionInFeed) instanceof FeedRecommendationViewModel) {

            FeedRecommendationViewModel model
                    = (FeedRecommendationViewModel) adapter.getlist().get(positionInFeed);
            trackRecommendationClick(
                    positionInFeed,
                    adapterPosition,
                    model.getCards().get(adapterPosition).getTrackingRecommendationModel(),
                    FeedAnalytics.Element.FOLLOW
            );
        }
    }

    @Override
    public void onShopItemClicked(int positionInFeed, int adapterPosition, @NotNull Shop shop) {
        Intent intent = feedModuleRouter.getShopPageIntent(getActivity(), shop.getId());
        startActivity(intent);

        if (adapter.getlist().get(positionInFeed) instanceof TopadsShopViewModel) {
            TopadsShopViewModel model = (TopadsShopViewModel) adapter.getlist().get(positionInFeed);

            if (model.getTrackingList().size() > adapterPosition) {
                TrackingRecommendationModel trackingRecommendationModel
                        = model.getTrackingList().get(adapterPosition);

                trackRecommendationClick(
                        positionInFeed,
                        adapterPosition,
                        trackingRecommendationModel,
                        FeedAnalytics.Element.AVATAR
                );
            }
        }
    }

    @Override
    public void onAddFavorite(int positionInFeed, int adapterPosition, @NotNull Data data) {
        presenter.toggleFavoriteShop(positionInFeed, adapterPosition, data.getShop().getId());

        if (adapter.getlist().get(positionInFeed) instanceof TopadsShopViewModel) {
            TopadsShopViewModel model = (TopadsShopViewModel) adapter.getlist().get(positionInFeed);

            if (model.getTrackingList().size() > adapterPosition) {
                TrackingRecommendationModel trackingRecommendationModel
                        = model.getTrackingList().get(adapterPosition);

                trackRecommendationClick(
                        positionInFeed,
                        adapterPosition,
                        trackingRecommendationModel,
                        FeedAnalytics.Element.FOLLOW
                );
            }
        }
    }

    @Override
    public void onActionPopup() {
        onInfoClicked();
    }

    @Override
    public void onActionRedirect(@NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onTitleCtaClick(@NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onAvatarClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.AVATAR,
                    redirectUrl
            );
        }
    }

    @Override
    public void onHeaderActionClick(int positionInFeed, @NotNull String id, @NotNull String type,
                                    boolean isFollow) {
        if (type.equals(FollowCta.AUTHOR_USER)) {
            int userIdInt = 0;
            try {
                userIdInt = Integer.valueOf(id);
            } catch (NumberFormatException ignored) {
            }

            if (isFollow) {
                onUnfollowKolClicked(positionInFeed, userIdInt);
            } else {
                onFollowKolClicked(positionInFeed, userIdInt);
            }

        } else if (type.equals(FollowCta.AUTHOR_SHOP)) {
            presenter.toggleFavoriteShop(positionInFeed, id);
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.FOLLOW,
                    ""
            );
        }
    }

    @Override
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable,
                            boolean editable) {
        if (getContext() != null) {
            Menus menus = new Menus(getContext());
            List<Menus.ItemMenus> menusList = new ArrayList<>();

            if (reportable) {
                menusList.add(
                        new Menus.ItemMenus(
                                getString(R.string.feed_report),
                                -1
                        )
                );
            }

            menus.setItemMenuList(menusList);
            menus.setActionText(getString(R.string.feed_cancel));

            menus.setOnActionClickListener(v -> menus.dismiss());
            menus.setOnItemMenuClickListener((itemMenus, pos) -> {
                if (itemMenus.title.equals(getString(R.string.feed_report))) {
                    goToContentReport(postId);
                }

                menus.dismiss();
            });
            menus.show();
        }
    }

    @Override
    public void onCaptionClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onLikeClick(int positionInFeed, int id, boolean isLiked) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id);
        } else {
            onLikeKolClicked(positionInFeed, id);
        }
    }

    @Override
    public void onCommentClick(int positionInFeed, int id) {
        onGoToKolComment(positionInFeed, id);
    }

    @Override
    public void onShareClick(int positionInFeed, int id, @NotNull String title,
                             @NotNull String description, @NotNull String url,
                             @NotNull String imageUrl) {
        if (getActivity() != null) {
            feedModuleRouter.shareFeed(
                    getActivity(),
                    String.valueOf(id),
                    url,
                    title,
                    imageUrl,
                    description
            );
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.SHARE,
                    url
            );
        }
    }

    @Override
    public void onFooterActionClick(int positionInFeed, @NonNull String redirectUrl) {
        onGoToLink(redirectUrl);

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.TAG,
                    redirectUrl
            );
        }
    }


    @Override
    public void onImageClick(int positionInFeed, int contentPosition,
                             @NotNull String redirectLink) {
        onGoToLink(redirectLink);

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.IMAGE,
                    redirectLink
            );
        }
    }

    @Override
    public void onYoutubeThumbnailClick(int positionInFeed, int contentPosition,
                                        @NotNull String youtubeId) {
        String redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId);

        if (getContext() != null) {
            RouteManager.route(
                    getContext(),
                    redirectUrl
            );
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.VIDEO,
                    redirectUrl
            );
        }
    }

    @Override
    public void onPollOptionClick(int positionInFeed, int contentPosition, int option,
                                  @NotNull String pollId, @NotNull String optionId, boolean isVoted,
                                  @NotNull String redirectLink) {
        if (isVoted) {
            onGoToLink(redirectLink);
        } else {
            onVoteOptionClicked(positionInFeed, pollId, optionId);
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.OPTION + option,
                    redirectLink
            );
        }
    }

    @Override
    public void onGridItemClick(int positionInFeed, int contentPosition,
                                @NotNull String redirectLink) {
        onGoToLink(redirectLink);

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.getTrackingPostModel(),
                    FeedAnalytics.Element.PRODUCT,
                    redirectLink
            );
        }
    }

    private void goToContentReport(int contentId) {
        if (getContext() != null) {
            Intent intent = ContentReportActivity.Companion.createIntent(
                    getContext(),
                    contentId
            );
            startActivityForResult(intent, OPEN_CONTENT_REPORT);
        }
    }

    private void trackFeedImpression(ArrayList<Visitable> listFeed) {
        for (int i = 0; i < listFeed.size(); i++) {
            Visitable visitable = listFeed.get(i);
            int feedPosition = adapter.getlist().size() + i;
            int userId = 0;
            try {
                userId = Integer.valueOf(getUserSession().getUserId());
            } catch (NumberFormatException ignored) {
            }

            if (visitable instanceof DynamicPostViewModel) {
                DynamicPostViewModel postViewModel = (DynamicPostViewModel) visitable;
                TrackingPostModel trackingPostModel = postViewModel.getTrackingPostModel();

                analytics.eventCardPostImpression(
                        trackingPostModel.getTemplateType(),
                        trackingPostModel.getActivityName(),
                        trackingPostModel.getTrackingType(),
                        trackingPostModel.getMediaType(),
                        trackingPostModel.getTagsType(),
                        trackingPostModel.getRedirectUrl(),
                        trackingPostModel.getTotalContent(),
                        trackingPostModel.getPostId(),
                        feedPosition,
                        userId
                );
            } else if (visitable instanceof BannerViewModel) {
                BannerViewModel bannerViewModel = (BannerViewModel) visitable;

                for (BannerItemViewModel item : bannerViewModel.getItemViewModels()) {
                    TrackingBannerModel trackingBannerModel = item.getTrackingBannerModel();
                    analytics.eventBannerImpression(
                            trackingBannerModel.getTemplateType(),
                            trackingBannerModel.getActivityName(),
                            trackingBannerModel.getTrackingType(),
                            trackingBannerModel.getMediaType(),
                            trackingBannerModel.getBannerUrl(),
                            trackingBannerModel.getTotalBanner(),
                            trackingBannerModel.getPostId(),
                            feedPosition,
                            trackingBannerModel.getBannerPosition(),
                            userId
                    );
                }
            } else if (visitable instanceof FeedRecommendationViewModel) {
                FeedRecommendationViewModel recommendationViewModel
                        = (FeedRecommendationViewModel) visitable;

                for (RecommendationCardViewModel card : recommendationViewModel.getCards()) {

                    TrackingRecommendationModel trackingRecommendationModel
                            = card.getTrackingRecommendationModel();
                    analytics.eventRecommendationImpression(
                            trackingRecommendationModel.getTemplateType(),
                            trackingRecommendationModel.getActivityName(),
                            trackingRecommendationModel.getTrackingType(),
                            trackingRecommendationModel.getMediaType(),
                            trackingRecommendationModel.getAuthorName(),
                            trackingRecommendationModel.getAuthorType(),
                            trackingRecommendationModel.getAuthorId(),
                            feedPosition,
                            trackingRecommendationModel.getCardPosition(),
                            userId
                    );
                }
            } else if (visitable instanceof TopadsShopViewModel) {
                TopadsShopViewModel topadsShopViewModel = (TopadsShopViewModel) visitable;

                for (TrackingRecommendationModel trackingRecommendationModel
                        : topadsShopViewModel.getTrackingList()) {

                    analytics.eventRecommendationImpression(
                            trackingRecommendationModel.getTemplateType(),
                            trackingRecommendationModel.getActivityName(),
                            trackingRecommendationModel.getTrackingType(),
                            trackingRecommendationModel.getMediaType(),
                            trackingRecommendationModel.getAuthorName(),
                            trackingRecommendationModel.getAuthorType(),
                            trackingRecommendationModel.getAuthorId(),
                            feedPosition,
                            trackingRecommendationModel.getCardPosition(),
                            userId
                    );
                }
            }
        }
    }

    private void trackCardPostClick(int positionInFeed, TrackingPostModel trackingPostModel,
                                    String element, String redirectUrl) {
        trackCardPostClick(positionInFeed, -1, trackingPostModel, element, redirectUrl);
    }

    private void trackCardPostClick(int positionInFeed, int contentPosition,
                                    TrackingPostModel trackingPostModel, String element,
                                    String redirectUrl) {
        int userId = 0;
        try {
            userId = Integer.valueOf(getUserSession().getUserId());
        } catch (NumberFormatException ignored) {
        }

        analytics.eventCardPostClick(
                trackingPostModel.getTemplateType(),
                trackingPostModel.getActivityName(),
                trackingPostModel.getTrackingType(),
                trackingPostModel.getMediaType(),
                trackingPostModel.getTagsType(),
                redirectUrl,
                element,
                trackingPostModel.getTotalContent(),
                trackingPostModel.getPostId(),
                positionInFeed,
                contentPosition != -1 ? String.valueOf(contentPosition) : "",
                userId
        );
    }

    private void trackRecommendationClick(int positionInFeed, int cardPosition,
                                          TrackingRecommendationModel trackingRecommendationModel,
                                          String element) {
        int userId = 0;
        try {
            userId = Integer.valueOf(getUserSession().getUserId());
        } catch (NumberFormatException ignored) {
        }

        analytics.eventRecommendationClick(
                trackingRecommendationModel.getTemplateType(),
                trackingRecommendationModel.getActivityName(),
                trackingRecommendationModel.getTrackingType(),
                trackingRecommendationModel.getMediaType(),
                trackingRecommendationModel.getAuthorName(),
                trackingRecommendationModel.getAuthorType(),
                element,
                trackingRecommendationModel.getAuthorId(),
                positionInFeed,
                cardPosition,
                userId
        );
    }

    private void trackBannerClick(int positionInFeed, int adapterPosition,
                                  TrackingBannerModel trackingBannerModel,
                                  String element) {
        int userId = 0;
        try {
            userId = Integer.valueOf(getUserSession().getUserId());
        } catch (NumberFormatException ignored) {
        }

        analytics.eventBannerClick(
                trackingBannerModel.getTemplateType(),
                trackingBannerModel.getActivityName(),
                trackingBannerModel.getTrackingType(),
                trackingBannerModel.getMediaType(),
                trackingBannerModel.getBannerUrl(),
                element,
                trackingBannerModel.getTotalBanner(),
                trackingBannerModel.getPostId(),
                positionInFeed,
                adapterPosition,
                userId
        );
    }
}
