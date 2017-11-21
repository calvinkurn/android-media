package com.tokopedia.tkpd.home.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.WishList;
import com.tokopedia.tkpd.home.adapter.GridLayoutProductAdapter;
import com.tokopedia.tkpd.home.presenter.ProductHistory;
import com.tokopedia.tkpd.home.presenter.ProductHistoryImpl;
import com.tokopedia.tkpd.home.presenter.ProductHistoryView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public class ProductHistoryFragment extends Fragment implements ProductHistoryView {

    public static final String FRAGMENT_TAG = "WishListFragment";

    @BindView(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.main_content)
    LinearLayout mainContent;

    GridLayoutManager layoutManager;
    GridLayoutProductAdapter adapter;
//    ItemDecorator itemDecorator;

    ProductHistory productHistory;
    private Unbinder unbinder;

    public ProductHistoryFragment(){}

    public static final Fragment newInstance(){
        return new ProductHistoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productHistory = new ProductHistoryImpl(this);
        productHistory.fetchSavedsInstance(savedInstanceState);
        productHistory.initDataInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.activity_recyclerview, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        productHistory.initAnalyticsHandler(getActivity());
        prepareView();
        setListener();
        productHistory.subscribe();
        return parentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        productHistory.saveDataBeforeRotate(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        productHistory.unSubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        productHistory.fetchDataFromCache(getActivity());
    }

    @Override
    public void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    productHistory.loadMore(getActivity());
                }
            }
        });
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productHistory.refreshData(getActivity());
            }
        });
    }

    @Override
    public void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        itemDecorator.setRightMostView(TkpdState.RecyclerView.VIEW_PRODUCT_RIGHT);
//        recyclerView.addItemDecoration(itemDecorator);
    }

    @Override
    public void initGridLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), calcColumnSize());
        layoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    @Override
    public void initItemDecoration() {
//        itemDecorator = new ItemDecorator(getActivity(), R.dimen.item_offset);
    }

    @Override
    public void initAdapterWithData(List<RecyclerViewItem> data) {
        adapter = new GridLayoutProductAdapter(getActivity(), data);
    }

    @Override
    public GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                // column size default is one
                int headerColumnSize = 1,
                        footerColumnSize = 1,
                        regularColumnSize = 1;

                // check the orientation to determine landscape or portrait
                switch (getResources().getConfiguration().orientation){
                    case Configuration.ORIENTATION_LANDSCAPE:
                        headerColumnSize = WishList.LANDSCAPE_COLUMN_HEADER;
                        regularColumnSize = WishList.LANDSCAPE_COLUMN;
                        footerColumnSize = WishList.LANDSCAPE_COLUMN_FOOTER;
                        break;
                    case Configuration.ORIENTATION_PORTRAIT:
                        headerColumnSize = WishList.PORTRAIT_COLUMN_HEADER;
                        regularColumnSize = WishList.PORTRAIT_COLUMN;
                        footerColumnSize = WishList.PORTRAIT_COLUMN_FOOTER;
                        break;
                }

                // set the value of footer, regular and header
                if (position == productHistory.getData().size()) {
                    // header column
                    return footerColumnSize;
                } else if (position % 5 == 0 && productHistory.getData().get(position).getType() == TkpdState.RecyclerViewItem.TYPE_LIST) {
                    // top ads span column
                    return headerColumnSize;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public int calcColumnSize() {
        int defaultColumnNumber = 1;
        switch (getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = WishList.PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = WishList.LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    @Override
    public boolean isPullToRefresh() {
        return swipeToRefresh.isRefreshing();
    }

    @Override
    public boolean isLoading() {
        return adapter.getItemViewType(layoutManager.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        Log.d(TAG, ProductHistoryFragment.class.getSimpleName() + (isLoadMore ? " tampilkan" : " hilangkan ") + " load more");
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public void displayPull(boolean isShow) {
        Log.d(TAG, ProductHistoryFragment.class.getSimpleName() + (isShow ? " tampilkan" : " hilangkan ") + " pull to refresh");
        swipeToRefresh.setRefreshing(isShow);
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        Log.d(TAG, ProductHistoryFragment.class.getSimpleName() + (isPullEnabled ? " hidupkan" : " matikan") + " pull to refresh");
        swipeToRefresh.setEnabled(isPullEnabled);
    }

    @Override
    public void loadDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayMainContent(boolean isShow) {
        if(isShow)
            mainContent.setVisibility(View.VISIBLE);
        else
            mainContent.setVisibility(View.GONE);
    }

    @Override
    public void displayLoading(boolean isShow) {
        if(isShow)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }
}
