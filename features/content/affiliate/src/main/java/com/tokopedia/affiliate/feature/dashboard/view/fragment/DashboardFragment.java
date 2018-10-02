package com.tokopedia.affiliate.feature.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent;
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.DashboardAdapter;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

//import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent;

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

    private ProgressBar progressBar;
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
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_dashboard, container, false);
        rvHistory = (RecyclerView) view.findViewById(R.id.rv_history);
        cvRecommendation = (CardView) view.findViewById(R.id.item_recommendation_count);
        tvRecommendationCount = (TextView) view.findViewById(R.id.tv_recommendation_count);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.GONE);
        cvRecommendation.setVisibility(View.GONE);

        initView();
        initViewListener();
    }

    @Override
    public void onRefresh() {
        loadFirstData(true);
    }

    @Override
    public void showLoading() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initView() {
        initDefaultValue();
        swipeToRefresh.setOnRefreshListener(this);
        adapter = new DashboardAdapter(new DashboardItemTypeFactoryImpl(this), new ArrayList());
        layoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(adapter);
        rvHistory.addOnScrollListener(onScrollListener());
        loadFirstData(false);
//        testData();
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

        });
    }

    private RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
//                if (isCanLoadMore
//                        && !TextUtils.isEmpty(cursor)
//                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT) {
//                    isCanLoadMore = false;
//                    swipeToRefresh.setRefreshing(true);
//                    adapter.addElement(new LoadingModel());
//                    presenter.loadMoreDashboardItem(cursor);
//                }
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
    public void onSuccessGetDashboardItem(DashboardHeaderViewModel header, List<DashboardItemViewModel> itemList, String cursor) {
        adapter.clearAllElements();
        if (swipeToRefresh.isRefreshing()) swipeToRefresh.setRefreshing(false);
        adapter.addElement(header);
        if (itemList.size() == 0) {
            EmptyDashboardViewModel emptyDashboardViewModel = new EmptyDashboardViewModel();
            adapter.addElement(emptyDashboardViewModel);
            adapter.notifyDataSetChanged();
        } else {
            adapter.addElement(itemList);
            adapter.notifyDataSetChanged();
        }

        if (TextUtils.isEmpty(cursor) || cursor.equals("1")) {
            isCanLoadMore = false;
            this.cursor = "";
        } else {
            isCanLoadMore = true;
            this.cursor = cursor;
        }
    }

    @Override
    public void onErrorGetDashboardItem(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getView().getRootView(),
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
        if (TextUtils.isEmpty(cursor)) {
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void testData() {
        DashboardHeaderViewModel headerModel = new DashboardHeaderViewModel("Rp. 123.456", "12x Dilihat", "10 Di klik", "2 Dibeli");
        List<DashboardItemViewModel> itemList = new ArrayList<>();
        itemList.add(new DashboardItemViewModel(
                "123",
                "https://www.bbcgoodfood.com/sites/default/files/styles/recipe/public/recipe/recipe-image/2016/05/nasi-goreng.jpg?itok=f6_VrVGC",
                "Nasi Goreng",
                "Rp 12.000",
                "12x diklik",
                "1 Dibeli",
                true));
        onSuccessGetDashboardItem(headerModel, itemList,"");
    }
}
