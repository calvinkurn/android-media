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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Comment;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Like;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.feedcomponent.util.FeedScrollListener;
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerItemViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.RecommendationCardViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel;
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.feedplus.view.adapter.EntryPointAdapter;
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.analytics.ProductEcommerce;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.di.FeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.common.util.PostMenuListener;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity;
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity;
import com.tokopedia.kolcommon.data.pojo.Author;
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

import static com.tokopedia.affiliatecommon.AffiliateCommonConstantKt.DISCOVERY_BY_ME;
import static com.tokopedia.feedplus.FeedPlusConstant.KEY_FEED;
import static com.tokopedia.feedplus.FeedPlusConstant.KEY_FEED_FIRSTPAGE_LAST_CURSOR;
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
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener,
        FeedMultipleImageView.FeedMultipleImageViewListener {

    private static final int OPEN_DETAIL = 54;
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_CONTENT_REPORT = 1310;
    private static final int CREATE_POST = 888;
    private static final int DEFAULT_VALUE = -1;
    public static final int REQUEST_LOGIN = 345;

    private static final String TAG = FeedPlusFragment.class.getSimpleName();
    private static final String ARGS_ROW_NUMBER = "row_number";
    private static final String YOUTUBE_URL = "{youtube_url}";
    private static final String FEED_TRACE = "mp_feed";
    private static final String AFTER_POST = "after_post";
    private static final String TRUE = "true";
    private static final String FEED_DETAIL = "feedcommunicationdetail";
    public static final String BROADCAST_FEED = "BROADCAST_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED = "PARAM_BROADCAST_NEW_FEED";
    public static final String PARAM_BROADCAST_NEW_FEED_CLICKED = "PARAM_BROADCAST_NEW_FEED_CLICKED";

    private RecyclerView recyclerView;
    private SwipeToRefresh swipeToRefresh;
    private View mainContent;
    private View newFeed;
    private FeedModuleRouter feedModuleRouter;
    private BroadcastReceiver newFeedReceiver;
    private FloatingActionButton fabFeed, fabByme, fabShop;
    private TextView fabTextByme, fabTextShop;
    private FrameLayout greyBackground;

    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private PerformanceMonitoring performanceMonitoring;
    private TopAdsInfoBottomSheet infoBottomSheet;
    private CloseableBottomSheetDialog createPostBottomSheet;
    private int loginIdInt;
    private boolean isLoadedOnce;
    private boolean afterPost;
    public boolean isFabExpanded = false;

    @Inject
    FeedPlusPresenter presenter;

    @Inject
    FeedAnalytics analytics;

    @Inject
    PostTagAnalytics postTagAnalytics;

    @Inject
    UserSessionInterface userSession;

    @Inject
    AffiliatePreference affiliatePreference;

    public static FeedPlusFragment newInstance(Bundle bundle) {
        FeedPlusFragment fragment = new FeedPlusFragment();
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
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this, analytics, userSession);
        adapter = new FeedPlusAdapter(typeFactory);
        adapter.setOnLoadListener(totalCount -> {
            int size = adapter.getlist().size();
            int lastIndex = size - 1;
            if (!(adapter.getlist().get(0) instanceof EmptyModel)
                    && !(adapter.getlist().get(lastIndex) instanceof RetryModel))
                presenter.fetchNextPage();
        });
        layoutManager = new NpaLinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);

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
                    hideAllFab(false);
                    boolean isHaveNewFeed = intent.getBooleanExtra(PARAM_BROADCAST_NEW_FEED, false);
                    if (isHaveNewFeed) {
                        onShowNewFeed("");
                    }
                }
            }
        };
        registerNewFeedReceiver();

        if (getArguments() != null) {
            afterPost = TextUtils.equals(getArguments().getString(AFTER_POST, ""), TRUE);
        }
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
        fabFeed = parentView.findViewById(R.id.fab_feed);
        fabShop = parentView.findViewById(R.id.fab_feed_2);
        fabByme = parentView.findViewById(R.id.fab_feed_1);
        fabTextByme = parentView.findViewById(R.id.text_fab_1);
        fabTextShop = parentView.findViewById(R.id.text_fab_2);
        greyBackground = parentView.findViewById(R.id.layout_grey_popup);

        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void hideAllFab(boolean isInitial) {
        if (fabFeed != null) {
            if (isInitial) {
                fabFeed.hide();
            } else {
                fabFeed.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward));
            }
            fabShop.hide();
            fabByme.hide();
            fabTextByme.setVisibility(View.GONE);
            fabTextShop.setVisibility(View.GONE);
            greyBackground.setVisibility(View.GONE);
            isFabExpanded = false;
        }
    }

    private void prepareView() {
        hideAllFab(true);
        if (!userSession.isLoggedIn()) {
            fabFeed.setVisibility(View.VISIBLE);
            fabFeed.setOnClickListener(v -> {
                onGoToLogin();
            });
        }
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
            @SuppressWarnings("unchecked")
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

                        if (item instanceof DynamicPostViewModel) {
                            if (!TextUtils.isEmpty(((DynamicPostViewModel) item).getFooter().getButtonCta().getAppLink())) {
                                adapter.notifyItemChanged(position, DynamicPostViewHolder.PAYLOAD_ANIMATE_FOOTER);
                            }
                        }
                        FeedScrollListener.onFeedScrolled(recyclerView, adapter.getlist());
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(FeedPlusFragment.TAG, e.toString());
                }

            }

        });
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

    @Override
    public void onOpenVideo(String videoUrl, String subtitle) {
        Intent intent = TransparentVideoActivity.getIntent(getActivity(), videoUrl, subtitle);
        startActivity(intent);
    }

    @Override
    public void onInfoClicked() {
        infoBottomSheet.show();
    }

    @SuppressLint("Range")
    @Override
    public void showSnackbar(String s) {
        NetworkErrorHelper.showSnackbar(getActivity(), s);
    }

    @Override
    public void updateFavorite(int adapterPosition) {

    }

    @Override
    public void updateFavoriteFromEmpty(String shopId) {
        onRefresh();
        analytics.eventFeedClickShop(getScreenName(),
                shopId, FeedTrackingEventLabel.Click.
                        TOP_ADS_FAVORITE);

    }

    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed, WhitelistViewModel whitelistViewModel) {
        trackFeedImpression(listFeed);

        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        triggerClearNewFeedNotification();
        if (userSession.isLoggedIn() && whitelistViewModel != null && !whitelistViewModel.getWhitelist().getAuthors().isEmpty()) {
            showFeedFab(whitelistViewModel);
        }
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
    public void setEndlessScroll() {
        adapter.setEndlessScrollListener();
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
        if (getContext() != null) {
            int INIT_STATE_FRAGMENT_FAVORITE = 2;
            String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.HOME);
            intent.putExtra(EXTRA_INIT_FRAGMENT, INIT_STATE_FRAGMENT_FAVORITE);
            startActivity(intent);
        }
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
        goToProductDetail(product.getId());

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
        goToShopPage(shop.getId());
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
        hideAllFab(false);
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

    private void showFeedFab(WhitelistViewModel whitelistViewModel) {
        fabFeed.show();
        isFabExpanded = false;
        if (!whitelistViewModel.getWhitelist().getAuthors().isEmpty() &&
                whitelistViewModel.getWhitelist().getAuthors().size() != 1) {
            fabFeed.setOnClickListener(fabClickListener(whitelistViewModel));
        } else {
            Author author = whitelistViewModel.getWhitelist().getAuthors().get(0);
            fabFeed.setOnClickListener(v -> onGoToLink(author.getLink()));
        }
    }

    private View.OnClickListener fabClickListener(WhitelistViewModel whitelistViewModel) {
        return v -> {
            if (isFabExpanded) {
                hideAllFab(false);
            } else {
                fabFeed.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward));
                greyBackground.setVisibility(View.VISIBLE);
                for (Author author : whitelistViewModel.getWhitelist().getAuthors()) {
                    if (author.getTitle().equalsIgnoreCase(Author.KEY_POST_TOKO)) {
                        fabShop.show();
                        fabTextShop.setVisibility(View.VISIBLE);
                        fabShop.setOnClickListener(v1 -> onGoToLink(author.getLink()));
                    } else {
                        fabByme.show();
                        fabTextByme.setVisibility(View.VISIBLE);
                        fabByme.setOnClickListener(v12 -> goToCreateAffiliate(author.getLink()));
                    }
                }
                greyBackground.setOnClickListener(v3 -> {
                    hideAllFab(false);
                });
                isFabExpanded = true;
            }
        };
    }

    private void goToCreateAffiliate(String link) {
        if (getContext() != null) {
            if (affiliatePreference.isFirstTimeEducation(userSession.getUserId())) {

                Intent intent = RouteManager.getIntent(
                        getContext(),
                        ApplinkConst.DISCOVERY_PAGE.replace("{page_id}", DISCOVERY_BY_ME)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                affiliatePreference.setFirstTimeEducation(userSession.getUserId());

            } else {
                RouteManager.route(getContext(), ApplinkConst.AFFILIATE_CREATE_POST, "-1", "-1");
            }
        }
    }

    private void loadData(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null && presenter != null) {
            if (!isLoadedOnce) {
                presenter.fetchFirstPage();
                isLoadedOnce = !isLoadedOnce;
            }

            if (afterPost) {
                showAfterPostToaster();
                afterPost = false;
            }

            analytics.trackScreen(getScreenName());
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
        if (!isVisibleToUser) {
            hideAllFab(false);
        }

    }

    @Override
    public boolean hasFeed() {
        return adapter.getlist() != null
                && !adapter.getlist().isEmpty()
                && adapter.getlist().size() > 1
                && !(adapter.getlist().get(0) instanceof EmptyModel);
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
            onGoToLogin();
        }
    }

    @Override
    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber, this);
        } else {
            onGoToLogin();
        }

    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.LIKE);
        } else {
            onGoToLogin();
        }
    }

    @Override
    public void onUnlikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                   String activityType) {
        if (getUserSession() != null && getUserSession().isLoggedIn()) {
            presenter.unlikeKol(id, rowNumber, this);
            trackCardPostElementClick(rowNumber, FeedAnalytics.Element.UNLIKE);
        } else {
            onGoToLogin();
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        Intent intent = KolCommentActivity.getCallingIntentFromFeed(getContext(), id, rowNumber);
        startActivityForResult(intent, OPEN_KOL_COMMENT);
        trackCardPostElementClick(rowNumber, FeedAnalytics.Element.COMMENT);
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

                        @Override
                        public void onEditClick() {

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
    public void onVoteOptionClicked(int rowNumber, String pollId, String optionId) {
        presenter.sendVote(rowNumber, pollId, optionId);
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
    public void onGoToLogin() {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
            getActivity().startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    @Override
    public void onSuccessSendVote(int rowNumber, String optionId,
                                  VoteStatisticDomainModel voteStatisticDomainModel) {
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
                                PollContentOptionViewModel.SELECTED
                                : PollContentOptionViewModel.UNSELECTED);

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
    public void onWhitelistClicked(WhitelistViewModel element) {
        analytics.trackClickCreatePost(userSession.getUserId());
        showBottomSheetCreatePost(element);
    }

    private void showBottomSheetCreatePost(WhitelistViewModel element) {
        if (getActivity() != null) {
            if (createPostBottomSheet == null) {
                createPostBottomSheet = CloseableBottomSheetDialog.createInstance(getContext(),
                        () -> {

                        }, () -> {

                        });
                View customView =
                        createCustomCreatePostBottomSheetView(getActivity().getLayoutInflater(), element);
                createPostBottomSheet.setCustomContentView(customView,
                        getString(R.string.create_post_as), true);
            }
            createPostBottomSheet.show();
        }
    }

    private View createCustomCreatePostBottomSheetView(@NonNull LayoutInflater layoutInflater, WhitelistViewModel element) {
        View view = layoutInflater.inflate(R.layout.layout_create_post_bottom_sheet, null);

        if (getActivity() != null) {
            RecyclerView entryPointRecyclerView = view.findViewById(R.id.entry_point_list);
            EntryPointAdapter adapter = new EntryPointAdapter(getActivity(),
                    element.getWhitelist().getAuthors(), applink -> {
                        analytics.trackClickCreatePostAs(applink, userSession.getUserId(),
                                userSession.getShopId());
                        startActivityForResult(
                                RouteManager.getIntent(getContext(), applink),
                                CREATE_POST
                        );
                        createPostBottomSheet.dismiss();
                    });
            entryPointRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            entryPointRecyclerView.setAdapter(adapter);
        }
        return view;
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
    public void onBannerItemClick(int positionInFeed, int adapterPosition,
                                  @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);

        if (adapter.getlist().get(positionInFeed) instanceof BannerViewModel) {
            BannerViewModel model = (BannerViewModel) adapter.getlist().get(positionInFeed);
            trackBannerClick(
                    adapterPosition,
                    model.getItemViewModels().get(adapterPosition).getTrackingBannerModel()
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
            TrackingRecommendationModel tracking
                    = model.getCards().get(adapterPosition).getTrackingRecommendationModel();
            analytics.eventRecommendationClick(
                    tracking.getTemplateType(),
                    tracking.getActivityName(),
                    tracking.getAuthorName(),
                    tracking.getAuthorType(),
                    tracking.getAuthorId(),
                    tracking.getCardPosition(),
                    getUserIdInt()
            );
        }
    }

    @Override
    public void onRecommendationActionClick(int positionInFeed, int adapterPosition,
                                            @NonNull String id, @NonNull String type,
                                            boolean isFollow) {
        if (type.equals(FollowCta.AUTHOR_USER)) {
            int userIdInt = getUserIdInt();

            if (isFollow) {
                onUnfollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition);
            } else {
                onFollowKolFromRecommendationClicked(positionInFeed, userIdInt, adapterPosition);
            }

        } else if (type.equals(FollowCta.AUTHOR_SHOP)) {
            presenter.toggleFavoriteShop(positionInFeed, adapterPosition, id);
        }

        if (adapter.getlist().get(positionInFeed) instanceof FeedRecommendationViewModel) {

            FeedRecommendationViewModel model
                    = (FeedRecommendationViewModel) adapter.getlist().get(positionInFeed);
            trackRecommendationFollowClick(
                    model.getCards().get(adapterPosition).getTrackingRecommendationModel(),
                    isFollow ? FeedAnalytics.Element.UNFOLLOW : FeedAnalytics.Element.FOLLOW
            );
        }
    }

    @Override
    public void onShopItemClicked(int positionInFeed, int adapterPosition, @NotNull Shop shop) {
        goToShopPage(shop.getId());

        if (adapter.getlist().get(positionInFeed) instanceof TopadsShopViewModel) {
            TopadsShopViewModel model = (TopadsShopViewModel) adapter.getlist().get(positionInFeed);
            for (TrackingRecommendationModel tracking : model.getTrackingList()) {
                if (TextUtils.equals(tracking.getAuthorName(), shop.getName())) {
                    analytics.eventTopadsRecommendationClick(
                            tracking.getTemplateType(),
                            tracking.getAdId(),
                            tracking.getAuthorId(),
                            tracking.getCardPosition(),
                            getUserIdInt()
                    );
                    break;
                }
            }
        }
    }

    @Override
    public void onAddFavorite(int positionInFeed, int adapterPosition, @NotNull Data data) {
        presenter.toggleFavoriteShop(positionInFeed, adapterPosition, data.getShop().getId());

        if (adapter.getlist().get(positionInFeed) instanceof TopadsShopViewModel) {
            TopadsShopViewModel model = (TopadsShopViewModel) adapter.getlist().get(positionInFeed);

            for (TrackingRecommendationModel tracking : model.getTrackingList()) {
                if (TextUtils.equals(tracking.getAuthorName(), data.getShop().getName())) {
                    trackRecommendationFollowClick(
                            tracking,
                            FeedAnalytics.Element.FOLLOW
                    );
                    break;
                }
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

        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.AVATAR);
    }

    @Override
    public void onHeaderActionClick(int positionInFeed, @NotNull String id, @NotNull String type,
                                    boolean isFollow) {
        if (userSession.isLoggedIn()) {
            if (type.equals(FollowCta.AUTHOR_USER)) {
                int userIdInt = getUserIdInt();

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
                analytics.eventFollowCardPost(
                        isFollow ? FeedAnalytics.Element.UNFOLLOW : FeedAnalytics.Element.FOLLOW,
                        model.getTrackingPostModel().getActivityName(),
                        String.valueOf(model.getTrackingPostModel().getPostId()),
                        model.getTrackingPostModel().getMediaType()
                );
            }
        } else {
            onGoToLogin();
        }
    }

    @Override
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable,
                            boolean editable) {
        if (getContext() != null) {
            Menus menus = createBottomMenu(getContext(), deletable, reportable, false, new PostMenuListener() {
                @Override
                public void onDeleteClicked() {

                }

                @Override
                public void onReportClick() {
                    if (userSession.isLoggedIn()) {
                        goToContentReport(postId);
                    } else {
                        onGoToLogin();
                    }
                }

                @Override
                public void onEditClick() {

                }
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
            doShare(String.format("%s %s", description, url), title);
        }

        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.SHARE);
    }

    @Override
    public void onFooterActionClick(int positionInFeed, @NonNull String redirectUrl) {
        onGoToLink(redirectUrl);
        trackCardPostElementClick(positionInFeed, FeedAnalytics.Element.TAG);
    }

    @Override
    public void onPostTagItemClick(int positionInFeed, @NotNull String redirectUrl, @NotNull PostTagItem postTagItem, int itemPosition) {
        onGoToLink(redirectUrl);
        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            postTagAnalytics.trackClickPostTagFeed(
                    model.getId(),
                    postTagItem,
                    itemPosition,
                    model.getHeader().getFollowCta().getAuthorType(),
                    model.getTrackingPostModel()
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
            trackCardPostClick(positionInFeed, model.getTrackingPostModel());
        }
    }

    @Override
    public void onAffiliateTrackClicked(@NotNull List<TrackingViewModel> trackList, boolean isClick) {
        for (TrackingViewModel track : trackList) {
            if (isClick) {
                presenter.trackAffiliate(track.getClickURL());
            } else  {
                presenter.trackAffiliate(track.getViewURL());
            }
        }
    }

    @Override
    public void onPostTagItemBuyClicked(@NotNull PostTagItem postTagItem) {
        presenter.addPostTagItemToCart(postTagItem);
    }

    @Override
    public void onYoutubeThumbnailClick(int positionInFeed, int contentPosition,
                                        @NotNull String youtubeId) {
        String redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId);

        if (getContext() != null) {
            RouteManager.route(getContext(), redirectUrl);
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(positionInFeed, model.getTrackingPostModel());
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
            if (model.getContentList().get(contentPosition) instanceof PollContentViewModel) {
                PollContentViewModel poll
                        = (PollContentViewModel) model.getContentList().get(contentPosition);
                PollContentOptionViewModel optionViewModel = poll.getOptionList().get(option);
                analytics.eventVoteClick(
                        model.getTrackingPostModel().getActivityName(),
                        model.getTrackingPostModel().getMediaType(),
                        pollId,
                        optionId,
                        optionViewModel.getOption(),
                        optionViewModel.getImageUrl(),
                        model.getTrackingPostModel().getPostId(),
                        getUserIdInt()
                );
            }
        }
    }

    @Override
    public void onGridItemClick(int positionInFeed, int contentPosition, int productPosition,
                                @NotNull String redirectLink) {
        onGoToLink(redirectLink);

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            if (redirectLink.contains(FEED_DETAIL)) {
                analytics.eventGoToFeedDetail(model.getTrackingPostModel().getPostId());
            } else if (model.getContentList().get(contentPosition) instanceof GridPostViewModel) {
                GridPostViewModel grid
                        = (GridPostViewModel) model.getContentList().get(contentPosition);
                GridItemViewModel item = grid.getItemList().get(productPosition);
                analytics.eventProductGridClick(
                        new ProductEcommerce(item.getId(),
                               item.getText(),
                               item.getPrice(),
                               productPosition),
                        model.getTrackingPostModel().getActivityName(),
                        model.getTrackingPostModel().getPostId(),
                        getUserIdInt()
                );
            }
        }
    }

    @Override
    public void onVideoPlayerClicked(int positionInFeed,
                                     int contentPosition,
                                     @NotNull String postId) {
        if (getActivity() != null) {
            startActivity(VideoDetailActivity.Companion.getInstance(
                    getActivity(),
                    postId));
        }

        if (adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model
                    = (DynamicPostViewModel) adapter.getlist().get(positionInFeed);
            trackCardPostClick(positionInFeed, model.getTrackingPostModel());
        }
    }

    @Override
    public void onAddToCartSuccess() {
        RouteManager.route(getContext(), ApplinkConstInternalMarketplace.CART);
    }

    @Override
    public void onAddToCartFailed(String pdpAppLink) {
        onGoToLink(pdpAppLink);
    }

    private void doShare(String body, String title) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(
                Intent.createChooser(sharingIntent, title)
        );
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

    private void goToProductDetail(String productId) {
        if (getActivity() != null) {
            getActivity().startActivity(getProductIntent(productId));
        }
    }

    private Intent getProductIntent(String productId) {
        if (getContext() != null) {
            return RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId);
        } else {
            return null;
        }
    }

    private void goToShopPage(String shopId) {
        if (getActivity() != null) {
            Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.SHOP, shopId);
            getActivity().startActivity(intent);
        }
    }

    private void trackFeedImpression(ArrayList<Visitable> listFeed) {
        for (int i = 0; i < listFeed.size(); i++) {
            Visitable visitable = listFeed.get(i);
            int feedPosition = adapter.getlist().size() + i;
            int userId = getUserIdInt();

            if (visitable instanceof DynamicPostViewModel) {
                DynamicPostViewModel postViewModel = (DynamicPostViewModel) visitable;
                TrackingPostModel trackingPostModel = postViewModel.getTrackingPostModel();

                if (postViewModel.getContentList().size() > 0) {
                    trackPostContentImpression(
                            postViewModel,
                            trackingPostModel,
                            userId,
                            feedPosition
                    );
                }

                if (postViewModel.getPostTag().getItems().size() != 0) {
                    postTagAnalytics.trackViewPostTagFeed(
                            postViewModel.getId(),
                            postViewModel.getPostTag().getItems(),
                            postViewModel.getHeader().getFollowCta().getAuthorType(),
                            trackingPostModel);
                }
                onAffiliateTrackClicked(postViewModel.getTracking(), false);

            } else if (visitable instanceof BannerViewModel) {
                BannerViewModel bannerViewModel = (BannerViewModel) visitable;
                ArrayList<TrackingBannerModel> trackingBannerModels = new ArrayList<>();
                for (BannerItemViewModel banner : bannerViewModel.getItemViewModels()) {
                    trackingBannerModels.add(banner.getTrackingBannerModel());
                    onAffiliateTrackClicked(banner.getTracking(), false);
                }
                analytics.eventBannerImpression(trackingBannerModels, userId);
            } else if (visitable instanceof FeedRecommendationViewModel) {
                FeedRecommendationViewModel recommendationViewModel
                        = (FeedRecommendationViewModel) visitable;
                ArrayList<TrackingRecommendationModel> trackingList = new ArrayList<>();
                for (RecommendationCardViewModel card : recommendationViewModel.getCards()) {
                    trackingList.add(card.getTrackingRecommendationModel());
                    onAffiliateTrackClicked(card.getTracking(), false);
                }
                analytics.eventRecommendationImpression(
                        trackingList,
                        userId
                );
            } else if (visitable instanceof TopadsShopViewModel) {
                TopadsShopViewModel topadsShopViewModel = (TopadsShopViewModel) visitable;
                analytics.eventTopadsRecommendationImpression(
                        topadsShopViewModel.getTrackingList(),
                        userId
                );
                onAffiliateTrackClicked(topadsShopViewModel.getTracking(), false);
            }
        }
    }

    private void trackPostContentImpression(DynamicPostViewModel postViewModel,
                                            TrackingPostModel trackingPostModel,
                                            int userId, int feedPosition) {
        if (postViewModel.getContentList().isEmpty()) {
            return;
        }

        if (postViewModel.getContentList().get(0) instanceof GridPostViewModel) {
            GridPostViewModel model = (GridPostViewModel) postViewModel.getContentList().get(0);
            ArrayList<ProductEcommerce> productList = new ArrayList<>();
            for (int position = 0; position < model.getItemList().size(); position++) {
                GridItemViewModel item = model.getItemList().get(position);
                    productList.add(new ProductEcommerce(
                            String.valueOf(item.getId()),
                            item.getText(),
                            item.getPrice(),
                            position
                    ));
            }
            analytics.eventProductGridImpression(
                    productList,
                    trackingPostModel.getActivityName(),
                    trackingPostModel.getPostId(),
                    getUserIdInt()
            );
        } else if (postViewModel.getContentList().get(0) instanceof PollContentViewModel) {
            PollContentViewModel model = (PollContentViewModel) postViewModel.getContentList().get(0);
            analytics.eventVoteImpression(
                    trackingPostModel.getActivityName(),
                    trackingPostModel.getMediaType(),
                    model.getPollId(),
                    trackingPostModel.getPostId(),
                    userId
            );
        } else {
            analytics.eventCardPostImpression(
                    trackingPostModel.getTemplateType(),
                    trackingPostModel.getActivityName(),
                    trackingPostModel.getMediaType(),
                    trackingPostModel.getRedirectUrl(),
                    trackingPostModel.getMediaUrl(),
                    trackingPostModel.getAuthorId(),
                    trackingPostModel.getTotalContent(),
                    trackingPostModel.getPostId(),
                    userId,
                    feedPosition
            );
        }
    }

    private void trackCardPostClick(int positionInFeed, TrackingPostModel trackingPostModel) {
        analytics.eventCardPostClick(
                trackingPostModel.getTemplateType(),
                trackingPostModel.getActivityName(),
                trackingPostModel.getMediaType(),
                trackingPostModel.getRedirectUrl(),
                trackingPostModel.getMediaUrl(),
                trackingPostModel.getAuthorId(),
                trackingPostModel.getTotalContent(),
                trackingPostModel.getPostId(),
                getUserIdInt(),
                positionInFeed
        );
    }

    private void trackRecommendationFollowClick(TrackingRecommendationModel trackingRecommendationModel,
                                                String action) {
        analytics.eventFollowRecommendation(
                action,
                trackingRecommendationModel.getAuthorType(),
                String.valueOf(trackingRecommendationModel.getAuthorId())
        );
    }

    private void trackBannerClick(int adapterPosition,
                                  TrackingBannerModel trackingBannerModel) {
        analytics.eventBannerClick(
                trackingBannerModel.getTemplateType(),
                trackingBannerModel.getActivityName(),
                trackingBannerModel.getMediaType(),
                trackingBannerModel.getBannerUrl(),
                trackingBannerModel.getApplink(),
                trackingBannerModel.getTotalBanner(),
                trackingBannerModel.getPostId(),
                adapterPosition,
                getUserIdInt()
        );
    }

    private void trackCardPostElementClick(int positionInFeed, String element) {
        if (adapter.getlist().size() > positionInFeed
                && adapter.getlist().get(positionInFeed) instanceof DynamicPostViewModel) {
            TrackingPostModel trackingPostModel = ((DynamicPostViewModel)
                    adapter.getlist().get(positionInFeed)).getTrackingPostModel();

            analytics.eventCardPostElementClick(
                    element,
                    trackingPostModel.getActivityName(),
                    trackingPostModel.getMediaType(),
                    String.valueOf(trackingPostModel.getPostId())
            );
        }
    }

    private void showAfterPostToaster() {
        if (getContext() != null) {
            Toast.makeText(getContext(), R.string.feed_after_post, Toast.LENGTH_LONG).show();
        }
    }

    private int getUserIdInt() {
        try {
            return Integer.valueOf(getUserSession().getUserId());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

}
