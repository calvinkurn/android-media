package com.tokopedia.feedplus.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.firebase.perf.metrics.Trace;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.FeedTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.feedplus.view.activity.BlogWebViewActivity;
import com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.feedplus.view.activity.RecentViewActivity;
import com.tokopedia.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.AddFeedViewHolder;
import com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.feedplus.view.util.NpaLinearLayoutManager;
import com.tokopedia.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.profile.view.activity.TopProfileActivity;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.IS_LIKE_TRUE;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_IS_LIKED;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_COMMENTS;
import static com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.PARAM_TOTAL_LIKES;
import static com.tokopedia.profile.view.activity.TopProfileActivity.IS_FOLLOWING_TRUE;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View,
        FeedPlus.View.Toppicks,
        FeedPlus.View.Kol,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener,
        KolPostListener.View.ViewHolder {

    private static final int OPEN_DETAIL = 54;
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_KOL_PROFILE_FROM_RECOMMENDATION = 83;
    private static final int DEFAULT_VALUE = -1;

    private static final String ARGS_ROW_NUMBER = "row_number";
    private static final String ARGS_ITEM_ROW_NUMBER = "item_row_number";

    private static final String FIRST_CURSOR = "FIRST_CURSOR";
    public static final String KEY_EXPLORE_NATIVE_ENABLE = "mainapp_explore_native_enable";
    public static final String KEY_EXPLORE_URL = "mainapp_explore_url";
    public static final String DEFAULT_EXPLORE_URL = "tokopedia://webview?url=https%3A%2F%2Fm.tokopedia.com%2Fcontent%2Fexplore%3Fwebview%3Dtrue";
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    RelativeLayout mainContent;
    View newFeed;
    Trace trace;
    private ShareBottomDialog shareBottomDialog;
    private TkpdProgressDialog progressDialog;
    private RemoteConfig remoteConfig;
    private AbstractionRouter abstractionRouter;

    @Inject
    FeedPlusPresenter presenter;

    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private CallbackManager callbackManager;
    private TopAdsInfoBottomSheet infoBottomSheet;
    private static final String TOPADS_ITEM = "4,1";
    private static final String TAG = FeedPlusFragment.class.getSimpleName();
    private String firstCursor = "";
    private int loginIdInt;

    boolean hasLoadedOnce = false;

    @Override
    protected String getScreenName() {
        return AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_FEED;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerFeedPlusComponent daggerFeedPlusComponent =
                (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerFeedPlusComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trace = TrackingUtils.startTrace("feed_trace");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getString(FIRST_CURSOR) != null)
            firstCursor = savedInstanceState.getString(FIRST_CURSOR, "");
        initVar();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FIRST_CURSOR, firstCursor);
    }

    private void initVar() {
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this);
        adapter = new FeedPlusAdapter(typeFactory);
        adapter.setOnLoadListener(new FeedPlusAdapter.OnLoadListener() {
            @Override
            public void onLoad(int totalCount) {
                int size = adapter.getlist().size();
                int lastIndex = size - 1;
                if (!(adapter.getlist().get(0) instanceof EmptyModel)
                        && !(adapter.getlist().get(lastIndex) instanceof RetryModel)
                        && !(adapter.getlist().get(lastIndex) instanceof AddFeedViewHolder)
                        )
                    presenter.fetchNextPage();
            }
        });
        layoutManager = new NpaLinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        String loginIdString = SessionHandler.getLoginID(getActivity());
        loginIdInt = loginIdString.isEmpty() ? 0 : Integer.valueOf(loginIdString);

        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplication();
        } else {
            throw new IllegalStateException("Application must implement " +
                    AbstractionRouter.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        mainContent = (RelativeLayout) parentView.findViewById(R.id.main);
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
        newFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop();
                showRefresh();
                onRefresh();
            }
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
                        Visitable item = null;
                        if (itemIsFullScreen()) {
                            position = layoutManager.findLastVisibleItemPosition();
                        } else if (layoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findFirstCompletelyVisibleItemPosition();
                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() != -1) {
                            position = layoutManager.findLastCompletelyVisibleItemPosition();
                        }

                        item = adapter.getlist().get(position);

                        if (position != 0 && item != null && !isTopads(item)) {
                            trackImpression(item, position);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(FeedPlusFragment.TAG, e.toString());
                }

            }

        });
    }

    private void trackImpression(Visitable item, int position) {
        if (isInspirationItem(item)) {
            UnifyTracking.eventR3(AppEventTracking.Action.IMPRESSION,
                    FeedTrackingEventLabel.Impression.FEED_RECOMMENDATION);
        } else if (isPromoItem(item)) {
            UnifyTracking.eventFeedClick(AppEventTracking.Action.IMPRESSION,
                    FeedTrackingEventLabel.Impression.FEED_PROMOTION);
        }
    }

    private boolean isPromoItem(Visitable item) {
        return item instanceof PromoCardViewModel;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
    }

    @Override
    public void onRefresh() {
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
    public void showLoadingProgress() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);
        progressDialog.showDialog();
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null && getActivity() != null)
            progressDialog.dismiss();
    }

    @Override
    public void setFirstCursor(String firstCursor) {
        this.firstCursor = firstCursor;
    }

    @Override
    public void onShareButtonClicked(String shareUrl,
                                     String title,
                                     String imgUrl,
                                     String contentMessage,
                                     String pageRowNumber) {

        ShareData shareData = ShareData.Builder.aShareData()
                .setName(title)
                .setTextContent(title)
                .setDescription(contentMessage)
                .setImgUri(imgUrl)
                .setUri(shareUrl)
                .setType(ShareData.FEED_TYPE)
                .build();

        if (shareBottomDialog == null) {
            shareBottomDialog = new ShareBottomDialog(
                    FeedPlusFragment.this,
                    callbackManager);
        }
        shareBottomDialog.setShareModel(shareData);

        shareBottomDialog.show();

    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(productId)
                            .setProductImage(imageSourceSingle)
                            .setProductName(name)
                            .setProductPrice(price)

                            .build()
            );
        }
    }

    @Override
    public void onGoToProductDetail(int rowNumber, int page, String productId, String
            imageSourceSingle, String name, String price) {
        goToProductDetail(productId, imageSourceSingle, name, price);
        UnifyTracking.eventFeedViewProduct(productId,
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
        FeedTracking.trackEventClickProductUploadEnhanced(
                name,
                productId,
                priceInt,
                productUrl,
                positionFeedCard,
                itemPosition,
                SessionHandler.getLoginID(getContext()),
                eventLabel
        );
        goToProductDetail(productId, imageSourceSingle, name, price);
    }

    @Override
    public void onGoToProductDetailFromRecentView(String productId, String imgUri,
                                                  String name, String price) {
        goToProductDetail(productId, imgUri, name, price);
        UnifyTracking.eventFeedViewProduct(productId, FeedTrackingEventLabel.View.VIEW_RECENT,
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
        FeedTracking.trackEventClickInspirationEnhanced(
                name,
                productId,
                priceInt,
                productUrl,
                positionFeedCard,
                itemPosition,
                source,
                SessionHandler.getLoginID(getContext()),
                eventLabel
        );

        goToProductDetail(productId, imageSource, name, price);
        UnifyTracking.eventR3Product(productId, AppEventTracking.Action.CLICK,
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.FEED_RECOMMENDATION_PDP);
    }

    @Override
    public void onGoToFeedDetail(int page, int rowNumber, String feedId) {
        Intent intent = FeedPlusDetailActivity.getIntent(
                getActivity(),
                feedId,
                getFeedAnalyticsHeader(page, rowNumber));
        startActivityForResult(intent, OPEN_DETAIL);
        UnifyTracking.eventFeedView(
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.View.FEED_PRODUCT_LIST);

    }

    @Override
    public void onGoToShopDetail(int page, int rowNumber, Integer shopId, String url) {
        Intent intent = ((FeedModuleRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), String.valueOf(shopId));
        startActivity(intent);
        UnifyTracking.eventFeedViewShop(String.valueOf(shopId), getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.View.FEED_SHOP);
    }

    @SuppressLint("Range")
    @Override
    public void onCopyClicked(int page, int rowNumber, String id, String code, String name) {
        ClipboardHandler.CopyToClipboard(getActivity(), code);
        SnackbarManager.make(getActivity(), getResources().getString(R.string.copy_promo_success),
                Snackbar.LENGTH_SHORT).show();
        UnifyTracking.eventFeedClickPromo(id, AppEventTracking.Action.COPY_CODE,
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.PROMO_COPY + name);

    }

    @Override
    public void onGoToBlogWebView(String url) {
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), url);
        startActivity(intent);
    }

    @Override
    public void onOpenVideo(String videoUrl, String subtitle) {
        Intent intent = TransparentVideoActivity.getIntent(getActivity(), videoUrl, subtitle);
        startActivity(intent);
    }

    @Override
    public void onGoToBuyProduct(ProductFeedViewModel productFeedViewModel) {

        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setProductId(String.valueOf(productFeedViewModel.getProductId()))
                .setPrice(productFeedViewModel.getPrice())
                .setImageUri(productFeedViewModel.getImageSource())
                .build();

        Intent intent = TransactionAddToCartRouter
                .createInstanceAddToCartActivity(getActivity(), pass);
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
        SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateFavorite(int adapterPosition) {
        Data data = ((FeedTopAdsViewModel) adapter.getlist().get(adapterPosition)).getList().get(0);
        boolean currentStatus = data.isFavorit();
        data.setFavorit(!currentStatus);
        adapter.notifyItemChanged(adapterPosition);
    }

    @Override
    public void onViewMorePromoClicked(int page, int rowNumber) {
        goToAllPromo();
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.Click.PROMO_MORE);

    }

    private void goToAllPromo() {
        Intent intent = new Intent(getContext(), BannerWebView.class);
        intent.putExtra(BannerWebView.EXTRA_TITLE, getContext().getString(R.string.title_activity_promo));
        intent.putExtra(BannerWebView.EXTRA_URL,
                TkpdBaseURL.URL_PROMO + TkpdBaseURL.FLAG_APP
        );
        startActivity(intent);
    }


    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed) {
        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        adapter.setEndlessScrollListener();
    }

    @Override
    public void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeed) {
        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        adapter.unsetEndlessScrollListener();
    }

    @Override
    public void onShowEmptyWithRecentView(ArrayList<Visitable> listFeed) {
        adapter.unsetEndlessScrollListener();
        adapter.showEmpty();
        adapter.addList(listFeed);
        adapter.notifyDataSetChanged();
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
    public void onGoToPromoPageFromHeader(int page, int rowNumber) {
        goToAllPromo();
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.PROMO_PAGE_HEADER);

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
    public void onErrorGetFeedFirstPage(String errorMessage) {
        finishLoading();
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainContent, errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.refreshPage();
                        }
                    });
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }

    }

    @Override
    public void onSearchShopButtonClicked() {
        Intent intent = HomeRouter.getHomeActivity(getActivity());
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_FAVORITE);
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
    public void onShowAddFeedMore() {

    }

    @Override
    public void shouldLoadTopAds(boolean loadTopAds) {
        adapter.unsetEndlessScrollListener();
    }

    @Override
    public void hideTopAdsAdapterLoading() {
        adapter.removeLoading();
    }

    @Override
    public int getColor(int color) {
        return MethodChecker.getColor(getActivity(), color);
    }

    @Override
    public void onSeeAllRecentView() {
        Intent intent = RecentViewActivity.getCallingIntent(getActivity());
        getActivity().startActivity(intent);
    }


    @Override
    public void onSeePromo(int page, int rowNumber, String id, String link, String name) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), link);
        UnifyTracking.eventFeedClickPromo(id,
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.PROMO_SPECIFIC + name);
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_DETAIL:
                if (resultCode == Activity.RESULT_OK)
                    showSnackbar(data.getStringExtra("message"));
                break;
            case OPEN_KOL_COMMENT:
                if (resultCode == Activity.RESULT_OK
                        && data.hasExtra(KolCommentActivity.ARGS_POSITION)
                        && data.hasExtra(KolCommentFragment.ARGS_TOTAL_COMMENT)) {

                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, DEFAULT_VALUE),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0)
                    );
                }
                break;
            case OPEN_KOL_PROFILE:
                if (resultCode == Activity.RESULT_OK
                        && data.hasExtra(ARGS_ROW_NUMBER)
                        && data.hasExtra(TopProfileActivity.EXTRA_IS_FOLLOWING)
                        && data.hasExtra(PARAM_IS_LIKED)
                        && data.hasExtra(PARAM_TOTAL_LIKES)
                        && data.hasExtra(PARAM_TOTAL_COMMENTS)) {

                    onSuccessFollowUnfollowFromProfile(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(TopProfileActivity.EXTRA_IS_FOLLOWING, DEFAULT_VALUE)
                    );

                    updatePostState(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_IS_LIKED, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_LIKES, DEFAULT_VALUE),
                            data.getIntExtra(PARAM_TOTAL_COMMENTS, DEFAULT_VALUE)
                    );
                }
                break;
            case OPEN_KOL_PROFILE_FROM_RECOMMENDATION:
                if (resultCode == Activity.RESULT_OK
                        && data.hasExtra(ARGS_ROW_NUMBER)
                        && data.hasExtra(ARGS_ITEM_ROW_NUMBER)
                        && data.hasExtra(TopProfileActivity.EXTRA_IS_FOLLOWING)) {

                    onSuccessFollowUnfollowFromProfileRecommendation(
                            data.getIntExtra(ARGS_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(ARGS_ITEM_ROW_NUMBER, DEFAULT_VALUE),
                            data.getIntExtra(TopProfileActivity.EXTRA_IS_FOLLOWING, DEFAULT_VALUE)
                    );
                }
            default:
                break;
        }
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
                product.getId());
        getActivity().startActivity(intent);
        UnifyTracking.eventFeedClickProduct(product.getId(),
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

        TrackingUtils.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt));
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((FeedModuleRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        startActivity(intent);
        UnifyTracking.eventFeedClickShop(shop.getId(), FeedTrackingEventLabel.Click.TOP_ADS_SHOP);
  
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

        TrackingUtils.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(listTopAds, loginIdInt));
    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        presenter.favoriteShop(dataShop, position);
        UnifyTracking.eventFeedClickShop(dataShop.getShop().getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE);

    }

    public void scrollToTop() {
        if (recyclerView != null) recyclerView.scrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstCursor == null)
            firstCursor = "";
        if (getUserVisibleHint() && presenter != null) {
            loadData(getUserVisibleHint());
        }
    }

    private void loadData(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded()
                && getActivity()!= null && presenter != null) {

            if (!hasLoadedOnce) {
                presenter.fetchFirstPage();
                if (trace != null)
                    trace.stop();
                hasLoadedOnce = true;
            }

            presenter.checkNewFeed(firstCursor);
            ScreenTracking.screen(getScreenName());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (firstCursor == null)
            firstCursor = "";
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
    public void updateFavoriteFromEmpty(String shopId) {
        onRefresh();
        UnifyTracking.eventFeedClickShop(shopId, FeedTrackingEventLabel.Click.
                TOP_ADS_FAVORITE);

    }

    @Override
    public void onEmptyOfficialStoreClicked() {
        openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
    }

    @Override
    public void onBrandClicked(int page, int rowNumber, OfficialStoreViewModel officialStoreViewModel) {
        UnifyTracking.eventFeedClickShop(
                String.valueOf(officialStoreViewModel.getShopId()),
                getFeedAnalyticsHeader(page, rowNumber) +
                        FeedTrackingEventLabel.Click
                                .OFFICIAL_STORE_BRAND +
                        officialStoreViewModel.getShopName());

        Intent intent = ((FeedModuleRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), String.valueOf(officialStoreViewModel.getShopId()));
        startActivity(intent);
    }

    private String getFeedAnalyticsHeader(int page, int rowNumber) {
        return page + "." + rowNumber + " ";
    }

    @Override
    public void onSeeAllOfficialStoresFromCampaign(int page, int rowNumber, String redirectUrl) {
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) +
                        FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN_SEE_ALL);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), redirectUrl);
    }

    @Override
    public void onGoToCampaign(int page, int rowNumber, String redirectUrl, String title) {
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber)
                        + FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN + title);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), redirectUrl);

    }

    @Override
    public void onSeeAllOfficialStoresFromBrands(int page, int rowNumber) {
        UnifyTracking.eventOfficialStoreBrandSeeAll(
                getFeedAnalyticsHeader(page, rowNumber));
        openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
    }

    @Override
    public void onGoToProductDetailFromCampaign(int page, int rowNumber, String productId, String imageSourceSingle, String name, String price) {
        UnifyTracking.eventFeedClickProduct(
                productId,
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.Click
                        .OFFICIAL_STORE_CAMPAIGN_PDP);
        goToProductDetail(productId, imageSourceSingle, name, price);

    }

    @Override
    public void onGoToShopDetailFromCampaign(int page, int rowNumber, String shopUrl) {
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) + FeedTrackingEventLabel.Click
                        .OFFICIAL_STORE_CAMPAIGN_SHOP);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), shopUrl);
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }

    @Override
    public void onContentProductLinkClicked(String url) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), url);
    }

    @Override
    public void onToppicksClicked(int page, int rowNumber, String name, String url, int itemPosition) {
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) +
                        FeedTrackingEventLabel.Click.TOPPICKS + name);
        switch ((DeepLinkChecker.getDeepLinkType(url))) {
            case DeepLinkChecker.BROWSE:
                DeepLinkChecker.openBrowse(url, getActivity());
                break;
            case DeepLinkChecker.HOT:
                DeepLinkChecker.openHot(url, getActivity());
                break;
            case DeepLinkChecker.CATALOG:
                DeepLinkChecker.openCatalog(url, getActivity());
                break;
            default:
                if (!TextUtils.isEmpty(url)) {
                    ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity()
                            , url);
                }
        }
    }

    @Override
    public void onSeeAllToppicks(int page, int rowNumber) {
        startActivity(TopPicksWebView.newInstance(getActivity(), TkpdBaseURL.URL_TOPPICKS));
        UnifyTracking.eventFeedClick(
                getFeedAnalyticsHeader(page, rowNumber) +
                        FeedTrackingEventLabel.Click.TOPPICKS_SEE_ALL);
    }

    @Override
    public void onGoToKolProfileFromRecommendation(int position, int itemPosition, String userId) {
        Intent profileIntent = TopProfileActivity.newInstance(getContext(), userId)
                .putExtra(ARGS_ROW_NUMBER, position)
                .putExtra(ARGS_ITEM_ROW_NUMBER, itemPosition);

        startActivityForResult(profileIntent, OPEN_KOL_PROFILE_FROM_RECOMMENDATION);
    }

    @Override
    public void onGoToKolProfile(int page, int rowNumber, String userId, int postId) {
        Intent profileIntent = TopProfileActivity.newInstanceFromFeed(getContext(), userId, postId)
                .putExtra(ARGS_ROW_NUMBER, rowNumber);
        startActivityForResult(profileIntent, OPEN_KOL_PROFILE);
    }

    @Override
    public void onOpenKolTooltip(int page, int rowNumber, String url) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), url);
    }

    @Override
    public void onFollowKolClicked(int page, int rowNumber, int id) {
        presenter.followKol(id, rowNumber, this);
    }

    @Override
    public void onUnfollowKolClicked(int page, int rowNumber, int id) {
        presenter.unfollowKol(id, rowNumber, this);

    }

    @Override
    public void onLikeKolClicked(int page, int rowNumber, int id) {
        presenter.likeKol(id, rowNumber, this);
    }

    @Override
    public void onUnlikeKolClicked(int page, int rowNumber, int id) {
        presenter.unlikeKol(id, rowNumber, this);

    }

    @Override
    public void onGoToKolComment(int page, int rowNumber, KolPostViewModel model) {
        if (getActivity().getApplication() instanceof FeedModuleRouter) {
            FeedModuleRouter router = ((FeedModuleRouter) getActivity().getApplication());
            Intent intent = router.getKolCommentActivity(getContext(), model.getId(), rowNumber);
            startActivityForResult(intent, OPEN_KOL_COMMENT);
        }
    }

    @Override
    public void onGoToListKolRecommendation(int page, int rowNumber, String url) {
        if (remoteConfig != null && !remoteConfig.getBoolean(KEY_EXPLORE_NATIVE_ENABLE, false)) {
            url = remoteConfig.getString(KEY_EXPLORE_URL, DEFAULT_EXPLORE_URL);
        }
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), url);
    }

    @Override
    public void onErrorFollowKol(String errorMessage, final int id, final int status, final int rowNumber) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (status == FollowKolPostUseCase.PARAM_UNFOLLOW)
                    presenter.unfollowKol(id, rowNumber, FeedPlusFragment.this);
                else
                    presenter.followKol(id, rowNumber, FeedPlusFragment.this);

            }
        }).showRetrySnackbar();
    }

    @Override
    public void onSuccessFollowUnfollowKol(int rowNumber) {
        if (adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);
            kolPostViewModel.setFollowed(!(kolPostViewModel.isFollowed()));
            kolPostViewModel.setTemporarilyFollowed(!(kolPostViewModel.isTemporarilyFollowed()));
            adapter.notifyItemChanged(rowNumber);
        }
    }

    @Override
    public void onErrorLikeDislikeKolPost(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    @Override
    public void onSuccessLikeDislikeKolPost(int rowNumber) {
        if (adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);
            kolPostViewModel.setLiked(!(kolPostViewModel.isLiked()));
            if (kolPostViewModel.isLiked()) {
                kolPostViewModel.setTotalLike(((KolPostViewModel)
                        adapter.getlist().get(rowNumber)).getTotalLike() + 1);
            } else {
                kolPostViewModel.setTotalLike(((KolPostViewModel)
                        adapter.getlist().get(rowNumber)).getTotalLike() - 1);
            }
            adapter.notifyItemChanged(rowNumber);
        }
    }

    @Override
    public void onFollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position) {
        presenter.followKolFromRecommendation(id, rowNumber, position, this);
    }

    @Override
    public void onUnfollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position) {
        presenter.unfollowKolFromRecommendation(id, rowNumber, position, this);

    }

    @Override
    public void onSuccessFollowKolFromRecommendation(int rowNumber, int position) {
    }

    @Override
    public void onSuccessUnfollowKolFromRecommendation(int rowNumber, int position) {
    }

    private void onSuccessAddDeleteKolComment(int rowNumber, int totalNewComment) {
        if (adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);
            kolPostViewModel.setTotalComment((
                    (KolPostViewModel)
                            adapter.getlist().get(rowNumber)).getTotalComment() +
                    totalNewComment);
            adapter.notifyItemChanged(rowNumber);
        }
    }

    private void onSuccessFollowUnfollowFromProfile(int rowNumber, int isFollowing) {
        if (rowNumber != DEFAULT_VALUE && adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);

            if (isFollowing != DEFAULT_VALUE) {
                kolViewModel.setFollowed(isFollowing == IS_FOLLOWING_TRUE);
                kolViewModel.setTemporarilyFollowed(isFollowing == IS_FOLLOWING_TRUE);
            }
            adapter.notifyItemChanged(rowNumber);
        }
    }

    private void updatePostState(int rowNumber, int isLiked, int totalLike, int totalComment) {
        if (rowNumber != DEFAULT_VALUE && adapter.getlist().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolViewModel = (KolPostViewModel) adapter.getlist().get(rowNumber);

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
                && adapter.getlist().get(rowNumber) instanceof KolRecommendationViewModel) {
            KolRecommendationViewModel recommendationViewModel =
                    (KolRecommendationViewModel) adapter.getlist().get(rowNumber);

            if (isFollowing != DEFAULT_VALUE) {
                recommendationViewModel.getListRecommend()
                        .get(itemRowNumber)
                        .setFollowed(isFollowing == IS_FOLLOWING_TRUE);
            }

            adapter.notifyItemChanged(rowNumber);
        }
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
        Intent intent = ((FeedModuleRouter) getActivity().getApplication()).getLoginIntent(getContext());
        startActivity(intent);
    }

    @Override
    public int getAdapterListSize() {
        return adapter.getItemCount();
    }

    @Override
    public UserSession getUserSession() {
        return abstractionRouter.getSession();
    }

    @Override
    public AbstractionRouter getAbstractionRouter() {
        return abstractionRouter;
    }
}
