package com.tokopedia.tkpd.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.analytics.ScreenTracking;
import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.customwidget.SwipeToRefresh;
import com.tokopedia.tkpd.home.adapter.DataFeedAdapter;
import com.tokopedia.tkpd.home.util.DefaultRetryListener;
import com.tokopedia.tkpd.home.util.ItemDecorator;
import com.tokopedia.tkpd.home.presenter.ProductFeed;
import com.tokopedia.tkpd.home.presenter.ProductFeed2Impl;
import com.tokopedia.tkpd.home.presenter.ProductFeedView;
import com.tokopedia.tkpd.instoped.InstagramAuth;
import com.tokopedia.tkpd.myproduct.ProductActivity;
import com.tokopedia.tkpd.util.RetryHandler;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.ProductItem;
import com.tokopedia.tkpd.var.RecyclerViewItem;
import com.tokopedia.tkpd.var.TkpdState;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.tkpd.home.presenter.ProductFeed.messageTag;

/**
 * Created by m.normansyah on 13/11/2015.
 * modified by m.normansyah on 27/11/2015
 */
public class FragmentProductFeed extends Fragment implements ProductFeedView, DefaultRetryListener.OnClickRetry{

    @Bind(R.id.index_main_recycler_view)
    RecyclerView indexRecyclerView;

    @Bind(R.id.include_loading)
    ProgressBar progressBar;

    @Bind(R.id.main_content)
    LinearLayout mainContent;

    @Bind(R.id.swipe_refresh_layout)
    SwipeToRefresh swipeRefreshLayout;

    @Bind(R.id.fab_speed_dial)
    FabSpeedDial fabAddProduct;

    RetryHandler retryHandlerFull;

    private ProductFeed productFeedPresenter;
    private ItemDecorator itemDecoration;
    private GridLayoutManager gridLayoutManager;
    private DataFeedAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productFeedPresenter = new ProductFeed2Impl(this);
        productFeedPresenter.fetchSavedsInstance(savedInstanceState);
        productFeedPresenter.initProductFeedInstances(getActivity());
        adapter.restoreAdapterPaging(savedInstanceState);
        //gw comment dulu soalnya karena kita udah ada cache lokal - rico -
        //adapter.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_index_main_v2, container, false);
        ButterKnife.bind(this, parentView);
        productFeedPresenter.initAnalyticsHandler(getActivity());

        //[START] display loading and hide main content
        displayLoading(true);
        displayMainContent(false);
        displayPull(true);
        //[START] display loading and hide main content

        retryHandlerFull = new RetryHandler(getActivity(), parentView);
        retryHandlerFull.setRetryView();
        retryHandlerFull.setOnRetryListener(new DefaultRetryListener(DefaultRetryListener.RETRY_FULL, this));

        prepareView();
        setListener();

        productFeedPresenter.getRecentProductFromCache();

        fabAddProduct.setListenerFabClick(new ListenerFabClick() {
            @Override
            public void onFabClick() {
                if (!fabAddProduct.isShown()) {
                    fabAddProduct.setVisibility(View.VISIBLE);
                }
            }
        });

        fabAddProduct.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.action_instagram:
                        onAddInstagram();
                        break;
                    case R.id.action_gallery:
                        ProductActivity.moveToImageGalleryCamera(getActivity(), 0, false, 5);
                        break;
                    case R.id.action_camera:
                        onAddGallery();
                        break;
                }
                return false;
            }
        });

        return parentView;
    }

    @Override
    public void setListener() {
        indexRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1) {
                    productFeedPresenter.loadMore();// load data only
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayPull(true);
                getAdapter().setIsLoading(false);// disable load more if user scroll to the very bottom
                productFeedPresenter.refreshData();// fetch both recent product and product feed
            }
        });
        adapter.setOnRetryListenerRV(new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                new DefaultRetryListener(DefaultRetryListener.RETRY_FOOTER, FragmentProductFeed.this).onRetryCliked();
            }
        });
    }

    @OnClick(R.id.fab_speed_dial)
    public void onFabClick(){
        productFeedPresenter.moveToAddProduct(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        productFeedPresenter.subscribe();
        //[START] AN-1173
        // [Product Feed v2] Add product button still shown when user don't have shop
        String shopID = SessionHandler.getShopID(getActivity());
        if(shopID ==null||shopID.equals("0")|| shopID.length()==0){
            fabAddProduct.setVisibility(View.GONE);
        }
        //[END] AN-1173

//        if(productFeedPresenter.isAfterRotation()){
//            productFeedPresenter.setData();
//        }else{
//        productFeedPresenter.getRecentProductFromCache();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
        adapter.saveAdapterPaging(outState);
        productFeedPresenter.saveDataBeforeRotate(outState);
    }

    @Override
    public void displayMainContent(boolean isMain) {
        if(isMain)
            mainContent.setVisibility(View.VISIBLE);
        else
            mainContent.setVisibility(View.GONE);
    }

    @Override
    public void displayRetry(boolean isRetry) {
        Log.d(TAG, FragmentProductFeed.class.getSimpleName() + (isRetry ? " tampilkan" : " hilangkan ") + " isRetry");
        adapter.setIsRetry(isRetry);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        productFeedPresenter.unsubscribe();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // this is called when user view Fragment inside ViewPager
        // if visible to User then send data localytic
        if (isVisibleToUser&&getActivity()!=null) {
            ScreenTracking.screen(this);
            productFeedPresenter.setLocalyticFlow(getActivity(), getActivity().getString(R.string.home_product_feed));
            productFeedPresenter.sendAppsFlyerData(getActivity());
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Deprecated
    @Override
    public void initHolder() {
        throw new RuntimeException("don't use this");
    }

    @Override
    public void initProductFeedRecyclerViewAdapter(List<RecyclerViewItem> data) {
        adapter = new DataFeedAdapter(getActivity(), data);
    }

    @Override
    public void initItemDecoration() {
        itemDecoration = new ItemDecorator(getActivity(), R.dimen.item_offset);
    }

    @Override
    public DataFeedAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Context getMainContext() {
        return getActivity();
    }

    // this value for main colum recyclerview
    public static final int LANDSCAPE_COLUMN_MAIN = 3;
    public static final int PORTRAIT_COLUMN_MAIN = 2;

    // this value for horizontal recyclerview
    public static final int LANDSCAPE_COLUMN_HEADER = 3;
    public static final int LANDSCAPE_COLUMN_FOOTER = 3;
    public static final int LANDSCAPE_COLUMN = 1;

    public static final int PORTRAIT_COLUMN_HEADER = 2;
    public static final int PORTRAIT_COLUMN_FOOTER = 2;
    public static final int PORTRAIT_COLUMN = 1;

    @Override
    public int calcColumnSize() {
        return calcColumnSize(getResources().getConfiguration().orientation);
    }

    public static int calcColumnSize(int orientation){
        int defaultColumnNumber = 1;
        switch (orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    @Override
    public void prepareView() {
        indexRecyclerView.setLayoutManager(gridLayoutManager);
        indexRecyclerView.setHasFixedSize(true);
        indexRecyclerView.setAdapter(adapter);
        //[START] disable item decorator to divide between item
//        itemDecoration.setRightMostView(TkpdState.RecyclerView.VIEW_PRODUCT_RIGHT);
//        indexRecyclerView.addItemDecoration(itemDecoration);
        //[END] disable item decorator to divide between item
    }

    @Override
    public void displayRetryFull() {
        retryHandlerFull.enableRetryView();
    }

    @Override
    public void hideRetryFull() {
        retryHandlerFull.disableRetryView();
    }

    @Override
    public void addAll(List<RecyclerViewItem> items) {
        adapter.updateHistoryAdapter(items.get(0));
        adapter.addAll(true, false, items);
    }

    @Override
    public void addNextPage(List<RecyclerViewItem> items) {
        Log.i(TAG, messageTag+ " addNextPage : "+items.size());
        adapter.addNextPage(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean isAfterRotate() {
        return adapter!=null&&adapter.getData()!=null&&adapter.getData().size()>0;
    }

    @Override
    public List<RecyclerViewItem> getData() {
        return adapter.getData();
    }

    @Override
    public void initGridLayoutManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), calcColumnSize());
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    // to determine size of grid columns
    GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                // column size default is one
                int headerColumnSize = 1,
                        footerColumnSize = 1,
                        regularColumnSize = 1;

                // check the orientation to determine landscape or portrait
//                switch (getResources().getConfiguration().orientation){
//                    case Configuration.ORIENTATION_LANDSCAPE:
//                        headerColumnSize = LANDSCAPE_COLUMN_HEADER;
//                        regularColumnSize = LANDSCAPE_COLUMN;
//                        footerColumnSize = LANDSCAPE_COLUMN_FOOTER;
//                        break;
//                    case Configuration.ORIENTATION_PORTRAIT:
                        headerColumnSize = PORTRAIT_COLUMN_HEADER;
                        regularColumnSize = PORTRAIT_COLUMN;
                        footerColumnSize = PORTRAIT_COLUMN_FOOTER;
//                        break;
//                }

                // set the value of footer, regular and header
                if (position == adapter.getData().size()) {
                    // productFeedPresenter.getData().size()
                    // header column
                    return footerColumnSize;
                } else if (adapter.isHistory(position)) {
                    return headerColumnSize;
                } else if (adapter.isTopAds(position)) {
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
    public boolean isRefresh() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public boolean isLoading() {
        return adapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public void displayLoading(boolean isLoading) {
        if(isLoading)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void finishLoading() {
        //[START] hide loading and show main content
        displayLoading(false);
        displayMainContent(true);
        //[END] hide loading and show main content
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void scrolltoPositon(int index) {
        gridLayoutManager.scrollToPosition(index);
    }

    @Override
    public int getLastPosition() {
        return gridLayoutManager.findFirstVisibleItemPosition();
    }

    @UiThread
    @Override
    public void loadDataChange() {
        getAdapter().notifyDataSetChanged();
        if(getAdapter().getHistoryAdapter()!=null)
            getAdapter().getHistoryAdapter().notifyDataSetChanged();
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {
        Log.d(TAG, FragmentProductFeed.class.getSimpleName() + (isLoadMore ? " tampilkan" : " hilangkan ") + " load more");
        adapter.setIsLoading(isLoadMore);
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {
        Log.d(TAG, FragmentProductFeed.class.getSimpleName() + (isPullEnabled ? " hidupkan" : " matikan") + " pull to refresh");
        swipeRefreshLayout.setEnabled(isPullEnabled);
    }

    @Override
    public void displayPull(boolean isShow) {
        Log.d(TAG, FragmentProductFeed.class.getSimpleName() + (isShow ? " tampilkan" : " hilangkan ") + " pull to refresh");
        swipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void onRetryFull() {
        displayLoading(true);
        displayMainContent(false);
        hideRetryFull();
        productFeedPresenter.getRecentProduct();
    }

    @Override
    public void onRetryFooter() {
        Log.d(TAG, "onRetryFooter");
        productFeedPresenter.getProductFeed();
    }

    @Override
    public void addTopAds(List<ProductItem> product, int page) {
        adapter.addTopAds(product, page);
    }

    @Override
    public int getTopAdsPagging() {
        return adapter.getTopAddsCounter();
    }

    @Override
    public void resetPaging() {
        adapter.resetPaging();
    }

    private void onAddGallery() {
        ProductActivity.moveToImageGalleryCamera(getActivity(), 0, true, -1);
    }

    private void onAddInstagram() {
        Intent moveToProductActivity = new Intent(getActivity(), ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        startActivity(moveToProductActivity);
    }

}
