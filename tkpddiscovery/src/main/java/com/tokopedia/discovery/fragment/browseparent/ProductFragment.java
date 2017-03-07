package com.tokopedia.discovery.fragment.browseparent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.network.entity.categoriesHades.Category;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.adapter.IntermediaryCategoryAdapter;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenter;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.discovery.view.FragmentBrowseProductView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID;

/**
 * Created by noiz354 on 3/24/16.
 */
public class ProductFragment extends BaseFragment<FragmentDiscoveryPresenter>
        implements FetchNetwork, FragmentBrowseProductView, DefaultCategoryAdapter.CategoryListener, IntermediaryCategoryAdapter.CategoryListener {

    public static final String TAG = "BrowseProductFragment";
    public static final String INDEX = "FRAGMENT_INDEX";
    // this value for main colum recyclerview
    private static final int LANDSCAPE_COLUMN_MAIN = 3;
    private static final int PORTRAIT_COLUMN_MAIN = 2;

    private static final int PORTRAIT_COLUMN_HEADER = 2;
    private static final int PORTRAIT_COLUMN_FOOTER = 2;
    private static final int PORTRAIT_COLUMN = 1;

    @BindView(R2.id.fragmentv2list)
    RecyclerView mRecyclerView;

    private static String ARG_1 = "";
    private static String ARG_2 = "";

    private ProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private BrowseProductRouter.GridType gridType;
    int spanCount = 2;
    private boolean isHasCategoryHeader = false;

    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BrowseProductRouter.GridType gridType = (BrowseProductRouter.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            int lastItemPosition = getLastItemPosition();
            changeLayoutType(gridType);
            productAdapter.notifyItemChanged(productAdapter.getItemCount());
            mRecyclerView.scrollToPosition(lastItemPosition);
        }
    };

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
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

    public static ProductFragment newInstance(int index) {

        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getScreenName() {
        return null;
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
        TrackingUtils.eventLocaSearched(browseModel.q);
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
        if (model.isEmpty()) {
            productAdapter.setSearchNotFound();
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
        spanCount = calcColumnSize(getResources().getConfiguration().orientation);
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

                headerColumnSize = PORTRAIT_COLUMN_HEADER;
                regularColumnSize = PORTRAIT_COLUMN;
                footerColumnSize = PORTRAIT_COLUMN_FOOTER;

                // set the value of footer, regular and header
                if (position == productAdapter.getData().size()) {
                    return spanCount;
                } else if (position == 0 && !productAdapter.isTopAds(position) && !productAdapter.isHotListBanner(position)
                        && !productAdapter.isCategoryHeader(position)) {
                    return regularColumnSize;
                } else if (productAdapter.isTopAds(position)) {
                    // top ads span column
                    return spanCount;
                } else if (productAdapter.isHotListBanner(position)){
                    return spanCount;
                } else if (productAdapter.isCategoryHeader(position)){
                    return spanCount;
                }else if (productAdapter.isEmptySearch(position)) {
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
                if(gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1 && productAdapter.getPagingHandlerModel().getUriNext().isEmpty()){
                    ((BrowseProductActivity) getActivity()).showBottomBar();
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
        return VALUES_PRODUCT_FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

        productAdapter.setIsLoading(false);

        Snackbar snackbar = Snackbar.make(parentView, CommonUtils.generateMessageError(getActivity(), (String) data[0]), Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
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

    @Override
    public void addCategoryHeader(Category category) {
        isHasCategoryHeader = true;
        if (category.getIsInterMediary() !=null && category.getIsInterMediary()) {
            productAdapter.addCategoryIntermediaryHeader(new ProductAdapter.CategoryHeaderIntermediaryModel(category,getActivity(),getCategoryWidth(),this));
        } else {
            productAdapter.addCategoryHeader(new ProductAdapter.CategoryHeaderModel(category,getActivity(),getCategoryWidth(),this));
        }

        productAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    private int calcColumnSize(int orientation) {
        int defaultColumnNumber = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                defaultColumnNumber = PORTRAIT_COLUMN_MAIN;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                defaultColumnNumber = LANDSCAPE_COLUMN_MAIN;
                break;
        }
        return defaultColumnNumber;
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (int) (width / 2);
    }

    @Override
    public void onCategoryClick(Child child) {
        ((BrowseProductActivity) getActivity()).renderNewCategoryLevel(child);
        Log.d(TAG, "onCategoryClick: ");
    }

    @Override
    public void onCategoryIntermediaryClick(Child child) {
        ((BrowseProductActivity) getActivity()).renderNewCategoryLevel(child);
        Log.d(TAG, "onCategoryClick: ");
    }
}
