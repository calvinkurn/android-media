package com.tokopedia.tkpd.discovery.fragment.browseparent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.analytics.ScreenTracking;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.discovery.adapter.ProductAdapter;
import com.tokopedia.tkpd.discovery.interfaces.FetchNetwork;
import com.tokopedia.tkpd.discovery.model.BrowseProductActivityModel;
import com.tokopedia.tkpd.discovery.model.BrowseProductModel;
import com.tokopedia.tkpd.discovery.model.HotListBannerModel;
import com.tokopedia.tkpd.discovery.presenter.FragmentDiscoveryPresenter;
import com.tokopedia.tkpd.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.tkpd.discovery.view.FragmentBrowseProductView;
import com.tokopedia.tkpd.home.fragment.FragmentProductFeed;
import com.tokopedia.tkpd.session.base.BaseFragment;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.var.ProductItem;
import com.tokopedia.tkpd.var.RecyclerViewItem;
import com.tokopedia.tkpd.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by noiz354 on 3/24/16.
 */
public class ProductFragment extends BaseFragment<FragmentDiscoveryPresenter>
        implements FetchNetwork, FragmentBrowseProductView {

    public static final String TAG = "BrowseProductFragment";

    @Bind(R2.id.fragmentv2list)
    RecyclerView mRecyclerView;

    private static String ARG_1 = "";
    private static String ARG_2 = "";

    private ProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private BrowseProductActivity.GridType gridType;
    int spanCount = 2;

    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BrowseProductActivity.GridType gridType = (BrowseProductActivity.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            int lastItemPosition = getLastItemPosition();
            changeLayoutType(gridType);
            productAdapter.notifyItemChanged(productAdapter.getItemCount());
            mRecyclerView.scrollToPosition(lastItemPosition);
        }
    };

    private void changeLayoutType(BrowseProductActivity.GridType gridType) {
        this.gridType = gridType;
        switch (gridType){
            case GRID_1: //List
                spanCount = 1;
                productAdapter.setgridView(gridType);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                break;
            case GRID_2: //Grid 2x2
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                productAdapter.setgridView(gridType);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                break;
            case GRID_3: //Grid 1x1
                spanCount = 1;
                gridLayoutManager.setSpanCount(spanCount);
                productAdapter.setgridView(gridType);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                break;
        }
    }

    public static ProductFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(changeGridReceiver, new IntentFilter(BrowseProductActivity.CHANGE_GRID_ACTION_INTENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(changeGridReceiver);
    }

    @Override
    public View onCreateView(View parentView, Bundle savedInstanceState) {
        return parentView;
    }

    @Override
    protected void initPresenter() {
        Log.d(TAG, "initPresenter");
        presenter = new FragmentDiscoveryPresenterImpl(this);
        presenter.setTAG(TAG);
        ScreenTracking.eventDiscoveryScreenAuth();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fragment_browse_product_v2;
    }

    @Override
    public void onCallProductServiceResult2(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        Log.d(TAG, "onCallProductServiceResult2");
        productAdapter.addAll(true, false, new ArrayList<RecyclerViewItem>(model));
        productAdapter.setgridView(((BrowseProductActivity)getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        if(productAdapter.checkHasNext()){
            productAdapter.setIsLoading(true);
        }else{
            productAdapter.setIsLoading(false);
        }
        BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();
        if(browseModel.getHotListBannerModel()!=null){
            HotListBannerModel bannerModel = browseModel.getHotListBannerModel();
            if(bannerModel.query.shop_id.isEmpty()){
                presenter.getTopAds(productAdapter.getTopAddsCounter(), TAG, getActivity(), spanCount);
            }
        } else if (model.size() > 0){
            presenter.getTopAds(productAdapter.getTopAddsCounter(), TAG, getActivity(), spanCount);
        }
        productAdapter.incrementPage();

        UnifyTracking.eventAppsFlyerViewListingSearch(model, browseModel.q);
    }

    @Override
    public BrowseProductModel getDataModel() {
        return ((FragmentDiscoveryPresenterImpl) presenter).getBrowseProductModel();
    }

    @Override
    public int getTopAdsPaging() {
        return productAdapter.getTopAddsCounter();
    }

    @Override
    public void onCallProductServiceLoadMore(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        Log.d(TAG, "onCallProductServiceLoadMore");
        productAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        if(productAdapter.checkHasNext()){
            productAdapter.setIsLoading(true);
        }else{
            productAdapter.setIsLoading(false);
        }
        BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();
        if(browseModel.getHotListBannerModel()!=null){
            HotListBannerModel bannerModel = browseModel.getHotListBannerModel();
            if(bannerModel.query.shop_id.isEmpty()){
                presenter.getTopAds(getPage(ProductFragment.TAG), TAG, getActivity(), spanCount);
            }
        } else {
            presenter.getTopAds(getPage(ProductFragment.TAG), TAG, getActivity(), spanCount);
        }
        productAdapter.incrementPage();
    }

    @Override
    public int getDataSize(String TAG) {
        if(TAG!=null&& ProductFragment.TAG.equals(TAG)){
            return productAdapter.getItemCount();
        }else{
            return 0;
        }
    }

    @Override
    public void setupAdapter() {
        productAdapter = new ProductAdapter(getActivity(), new ArrayList<RecyclerViewItem>());
        spanCount = FragmentProductFeed.calcColumnSize(getResources().getConfiguration().orientation);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    // to determine size of grid columns
    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                // column size default is one
                int headerColumnSize = 1, footerColumnSize = 1, regularColumnSize = 1;

                headerColumnSize = FragmentProductFeed.PORTRAIT_COLUMN_HEADER;
                regularColumnSize = FragmentProductFeed.PORTRAIT_COLUMN;
                footerColumnSize = FragmentProductFeed.PORTRAIT_COLUMN_FOOTER;

                // set the value of footer, regular and header
                if (position == productAdapter.getData().size()) {
                    return spanCount;
                } else if (position == 0 && !productAdapter.isTopAds(position) && !productAdapter.isHotListBanner(position)) {
                    return regularColumnSize;
                } else if (productAdapter.isTopAds(position)) {
                    // top ads span column
                    return spanCount;
                } else if (productAdapter.isHotListBanner(position)){
                    return spanCount;
                } else if (productAdapter.isEmptySearch(position)) {
                    return spanCount;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public void setupRecyclerView() {
        mRecyclerView.setAdapter(productAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1) {
                    presenter.loadMore(getActivity());
                }
                if(gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1 && productAdapter.getPagingHandlerModel().getUriNext().equals("")){
//                    ((BrowseProductActivity) getActivity()).getBottomNavigation().restoreBottomNavigation(true);
                }
            }
        });
        changeLayoutType(((BrowseProductActivity)getActivity()).getGridType());

    }

    @Override
    public void onCallNetwork() {

    }

    @Override
    public int getFragmentId() {
        return FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

        productAdapter.setIsLoading(false);

        Snackbar snackbar = Snackbar.make(parentView, CommonUtils.generateMessageError(getActivity(), (String) data[0]), Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadMore(getActivity());
            }
        };
        if (listener != null) {
            snackbar.setAction(getString(R.string.title_retry), listener);
        }
        snackbar.show();
    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    private int getLastItemPosition(){
        switch (gridType){
            case GRID_1:
                return linearLayoutManager.findFirstVisibleItemPosition();
            case GRID_2:
            case GRID_3:
            default:
                return gridLayoutManager.findFirstVisibleItemPosition();
        }
    }

    @Override
    public boolean isLoading() {

        switch (gridType){
            case GRID_1:
                return productAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
            case GRID_2:
            case GRID_3:
            default:
                    return productAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
        }
    }

    @Override
    public int getStartIndexForQuery(String TAG) {
        return productAdapter.getPagingHandlerModel().getStartIndex();
    }

    /**
     * need to supply correct TAG
     * @param TAG
     * @return -1 means invalid index
     */
    @Override
    public int getPage(String TAG) {
        if(TAG.equals(ProductFragment.TAG))
            return productAdapter.getPage();
        else
            return -1;
    }

    @Override
    public void savePaging(Bundle savedState) {
        if(productAdapter!=null) {
            productAdapter.saveAdapterPaging(savedState);
        }
    }

    @Override
    public void restorePaging(Bundle savedState) {
        if(productAdapter!=null) {
            productAdapter.restoreAdapterPaging(savedState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(productAdapter!=null) {
            productAdapter.saveAdapterPaging(outState);
        }
    }

    @Override
    public void addTopAds(List<ProductItem> passProduct, int page, String tag) {
        if(!tag.equals(ProductFragment.TAG))
            return;
        if(!passProduct.isEmpty()) {
            mRecyclerView.scrollToPosition(productAdapter.addTopAds(passProduct, page));
        }
    }


    @Override
    public void addHotListHeader(ProductAdapter.HotListBannerModel hotListBannerModel) {
        productAdapter.addHotListHeader(hotListBannerModel);
    }
}
