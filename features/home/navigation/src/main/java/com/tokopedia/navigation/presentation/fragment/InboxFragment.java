package com.tokopedia.navigation.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.analytics.InboxGtmTracker;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.Recommendation;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.view.InboxAdapterListener;
import com.tokopedia.navigation.presentation.adapter.InboxAdapterTypeFactory;
import com.tokopedia.navigation.presentation.adapter.RecomItemDecoration;
import com.tokopedia.navigation.presentation.base.BaseTestableParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.InboxPresenter;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.Category;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends BaseTestableParentFragment<GlobalNavComponent, InboxPresenter> implements
        InboxView, InboxAdapterListener, RecommendationListener {

    public static final int CHAT_MENU = 0;
    public static final int DISCUSSION_MENU = 1;
    public static final int REVIEW_MENU = 2;
    public static final int HELP_MENU = 3;
    public static final int DEFAULT_SPAN_COUNT = 2;
    public static final int SINGLE_SPAN_COUNT = 1;
    private static final String PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition";
    private static final String WIHSLIST_STATUS_IS_WISHLIST = "isWishlist";
    private static final int REQUEST_FROM_PDP = 138;

    @Inject
    InboxPresenter presenter;

    @Inject
    GlobalNavAnalytics globalNavAnalytics;

    @Inject
    RemoteConfig remoteConfig;

    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter adapter;
    private View emptyLayout;
    private StaggeredGridLayoutManager layoutManager;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private TrackingQueue trackingQueue;
    private List<Visitable> visitables;

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
            if(adapter.getList().get(position) instanceof  Recommendation){
                Recommendation recommendation = (Recommendation) adapter.getList().get(position);
                recommendation.getRecommendationItem().setWishlist(wishlistStatusFromPdp);
                adapter.notifyItemChanged(position);
            }
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

        List<Visitable> dataInbox = getData();
        InboxAdapterTypeFactory typeFactory = new InboxAdapterTypeFactory(this, this);
        adapter = new InboxAdapter(typeFactory, dataInbox);

        emptyLayout = view.findViewById(R.id.empty_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecomItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_8)));
        layoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener();
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getInboxData());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                endlessRecyclerViewScrollListener.resetState();
                adapter.clearAllElements();
                adapter.addElement(getData());
                presenter.getInboxData();
                presenter.getFirstRecomData();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        presenter.getFirstRecomData();
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
    public void onWishlistClick(@NotNull RecommendationItem item, boolean isAddWishlist, @NotNull Function2<? super Boolean, ? super Throwable, Unit> callback) {
        if(presenter.isLoggedIn()){
            if(isAddWishlist){
                presenter.addWishlist(item, callback);
            } else {
                presenter.removeWishlist(item, callback);
            }
        }else{
            RouteManager.route(getContext(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onProductClick(@NotNull RecommendationItem item, @org.jetbrains.annotations.Nullable String layoutType, @NotNull int... position) {
        if (item.isTopAds()) {
            new ImpresionTask().execute(item.getClickUrl());
            onClickTopAds(item);
        }else {
            onClickOrganic(item);
        }
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalMarketplace.PRODUCT_DETAIL, String.valueOf(item.getProductId()));
        if(position.length >= 1) intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[0]);
        startActivityForResult(intent, REQUEST_FROM_PDP);
    }

    @Override
    public void onProductImpression(@NotNull RecommendationItem item) {
        if(item.isTopAds()){
            new ImpresionTask().execute(item.getTrackerImageUrl());
            onImpressionTopAds(item);
        }else {
            onImpressionOrganic(item);
        }
    }

    private void intiInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    private List<Visitable> getData() {
        List<Visitable> inboxList = new ArrayList<>();
        inboxList.add(new Inbox(R.drawable.ic_topchat, R.string.chat, R.string.chat_desc));
        inboxList.add(new Inbox(R.drawable.ic_tanyajawab, R.string.diskusi, R.string.diskusi_desc));
        inboxList.add(new Inbox(R.drawable.ic_ulasan, R.string.ulasan, R.string.ulasan_desc));
        inboxList.add(new Inbox(R.drawable.ic_pesan_bantuan, R.string.pesan_bantuan, R.string.pesan_bantuan_desc));
        return inboxList;
    }

    private void getCallingIntent(int position) {
        switch (position) {
            case CHAT_MENU:
                boolean isChatUsingOld = remoteConfig.getBoolean(RemoteConfigKey.TOPCHAT_OLD);
                if(isChatUsingOld) {
                    RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_OLD);
                } else {
                    RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_IDLESS);
                }
                break;
            case DISCUSSION_MENU:
                if (getActivity() != null
                        && getActivity().getApplicationContext() != null) {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData("clickInboxChat",
                                        "inbox - talk",
                                        "click on diskusi product",
                                        ""));

                    if (getActivity().getApplication() instanceof GlobalNavRouter) {
                        startActivity(((GlobalNavRouter) getActivity().getApplication())
                                .getInboxTalkCallingIntent(getActivity()));
                    }
                }
                break;
            case REVIEW_MENU:
                RouteManager.route(getActivity(), ApplinkConst.REPUTATION);
                break;
            case HELP_MENU:
                if (getActivity().getApplication() instanceof GlobalNavRouter) {
                    startActivity(((GlobalNavRouter) getActivity().getApplication())
                            .getInboxTicketCallingIntent(getActivity()));
                }
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
    public void onRenderRecomInbox(List<Visitable> list) {
        this.visitables = list;
        adapter.addElement(list);
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
        return (getData().size()-1)+1;
    }

    private void onImpressionTopAds(RecommendationItem item) {
        new ImpresionTask().execute(item.getTrackerImageUrl());
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.getPosition(), item.isTopAds());
    }

    private void onImpressionOrganic(RecommendationItem item) {
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, item.getPosition(), item.isTopAds());
    }

    private void onClickTopAds(RecommendationItem item) {
        new ImpresionTask().execute(item.getClickUrl());
        InboxGtmTracker.getInstance().eventInboxProductClick(getContext(), item, item.getPosition(), item.isTopAds());
    }

    private void onClickOrganic(RecommendationItem item) {
        InboxGtmTracker.getInstance().eventInboxProductClick(getContext(), item, item.getPosition(), item.isTopAds());
    }
}