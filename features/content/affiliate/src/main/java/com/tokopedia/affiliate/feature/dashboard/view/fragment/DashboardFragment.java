package com.tokopedia.affiliate.feature.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.DashboardAdapter;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;
import com.tokopedia.affiliate.feature.explore.view.activity.ExploreActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardFragment
        extends BaseDaggerFragment
        implements DashboardContract.View,
        SwipeToRefresh.OnRefreshListener {

    private static final int TEXT_TYPE_PROFILE_SEEN = 1;
    private static final int TEXT_TYPE_PRODUCT_CLICKED = 2;
    private static final int TEXT_TYPE_PRODUCT_BOUGHT = 3;
    private static final int TEXT_RECOMMENDATION_LEFT = 4;
    private static final int ITEM_COUNT = 5;

    private TextView tvRecommendationCount;
    private RecyclerView rvHistory;
    private LinearLayoutManager layoutManager;
    private CardView cvRecommendation;
    private DashboardAdapter adapter;
    private SwipeToRefresh swipeToRefresh;
    private boolean isCanLoadMore = false;
    private String cursor = "";

    @Inject
    DashboardPresenter presenter;

    @Inject
    UserSession userSession;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

    public static DashboardFragment getInstance(Bundle bundle) {
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerAffiliateComponent affiliateComponent = (DaggerAffiliateComponent) DaggerAffiliateComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this);
    }

    @Override
    protected String getScreenName() {
        return AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE;
    }

    @Override
    public void onStart() {
        super.onStart();
        affiliateAnalytics.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_dashboard, container, false);
        rvHistory = view.findViewById(R.id.rv_history);
        cvRecommendation = view.findViewById(R.id.item_recommendation_count);
        tvRecommendationCount = view.findViewById(R.id.tv_recommendation_count);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cvRecommendation.setVisibility(View.GONE);
        initView();
        initViewListener();
        if (!userSession.isLoggedIn()) getActivity().finish();
        else presenter.checkAffiliate();
    }

    @Override
    public void onRefresh() {
        loadFirstData(true);
    }

    @Override
    public void showLoading() {
        adapter.addElement(new LoadingModel());
    }

    @Override
    public void hideLoading() {
        adapter.hideLoading();
    }

    private void initView() {
        initDefaultValue();
        swipeToRefresh.setOnRefreshListener(this);
        adapter = new DashboardAdapter(new DashboardItemTypeFactoryImpl(this), new ArrayList());
        layoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(adapter);
        rvHistory.addOnScrollListener(onScrollListener());
    }

    private void loadFirstData(boolean isPullToRefresh) {
        isCanLoadMore = true;
        presenter.loadDashboardItem(isPullToRefresh);
    }

    private void initDefaultValue() {
        tvRecommendationCount.setText(countTextBuilder(TEXT_RECOMMENDATION_LEFT, 0));
    }

    private void initViewListener() {
        cvRecommendation.setOnClickListener(view -> {
            Intent intent = ExploreActivity.getInstance(getActivity());
            startActivity(intent);
        });
    }

    private RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
                if (isCanLoadMore
                        && !TextUtils.isEmpty(cursor)
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT) {
                    isCanLoadMore = false;
                    adapter.addElement(new LoadingMoreModel());
                    presenter.loadMoreDashboardItem(cursor);
                }
            }
        };
    }

    private String countTextBuilder(int textType, int count) {
        String defaultText = getResources().getString(
                textType == TEXT_TYPE_PROFILE_SEEN ?
                        R.string.title_profil_dilihat :
                        (textType == TEXT_TYPE_PRODUCT_CLICKED ?
                                R.string.title_klik_produk :
                                R.string.title_produk_dibeli));
        return String.valueOf(count) + " " + defaultText;
    }

    @Override
    public void onSuccessGetDashboardItem(DashboardHeaderViewModel header,
                                          List<DashboardItemViewModel> itemList,
                                          String cursor,
                                          DashboardFloatingButtonViewModel floatingModel) {
        adapter.clearAllElements();
        if (swipeToRefresh.isRefreshing()) swipeToRefresh.setRefreshing(false);
        adapter.addElement(header);

        if (itemList.size() == 0) {
            EmptyDashboardViewModel emptyDashboardViewModel = new EmptyDashboardViewModel(floatingModel.getCount());
            adapter.addElement(emptyDashboardViewModel);
            adapter.notifyDataSetChanged();
        } else {
            adapter.addElement(itemList);
            adapter.notifyDataSetChanged();
            int paddingBottom = convertDpToPixel(getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_8));
            if (floatingModel.getCount() != 0) {
                cvRecommendation.setVisibility(View.VISIBLE);
                tvRecommendationCount.setText(MethodChecker.fromHtml(floatingModel.getText()));
                paddingBottom = convertDpToPixel(getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_36));
            }
            rvHistory.setPadding(
                    rvHistory.getPaddingLeft(),
                    rvHistory.getPaddingTop(),
                    rvHistory.getPaddingRight(),
                    paddingBottom);
        }

        if (TextUtils.isEmpty(cursor) || cursor.equals("1")) {
            isCanLoadMore = false;
            this.cursor = "";
        } else {
            isCanLoadMore = true;
            this.cursor = cursor;
        }
    }

    public int convertDpToPixel(int dp){
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int px = dp * ((int)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onErrorGetDashboardItem(String error) {
        adapter.clearAllElements();
        cvRecommendation.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(),
                getView(),
                error,
                () -> {
                    presenter.loadDashboardItem(false);
                }
                );
    }

    @Override
    public void onSuccessLoadMoreDashboardItem(List<DashboardItemViewModel> itemList, String cursor) {
        adapter.hideLoading();
        adapter.addElement(itemList);
        adapter.notifyDataSetChanged();
        if (TextUtils.isEmpty(cursor) || itemList.size() == 0) {
            isCanLoadMore = false;
            this.cursor = "";
        } else {
            isCanLoadMore = true;
            this.cursor = cursor;
        }

    }

    @Override
    public void onErrorLoadMoreDashboardItem(String error) {
        NetworkErrorHelper.createSnackbarWithAction(
                getActivity(),
                error,
                () -> {
                    presenter.loadMoreDashboardItem(cursor);
        });
    }

    @Override
    public void onSuccessCheckAffiliate(boolean isAffiliate) {
        if (isAffiliate) loadFirstData(false);
        else getActivity().finish();
    }

    @Override
    public void onErrorCheckAffiliate(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getView(),
                error,
                () -> {
                    presenter.checkAffiliate();
                }
        );
    }

    @Override
    public void goToAffiliateExplore() {
        RouteManager.route(getContext(), ApplinkConst.AFFILIATE_EXPLORE);
    }

    @Override
    public void goToDeposit() {
        RouteManager.route(getContext(), ApplinkConst.DEPOSIT);
        affiliateAnalytics.onAfterClickSaldo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
