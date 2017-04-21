package com.tokopedia.discovery.fragment;

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
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.network.entity.categoriesHades.Data;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenter;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.discovery.view.FragmentBrowseProductView;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID;

/**
 * Created by noiz354 on 3/24/16.
 */
public class ProductFragment extends BaseFragment<FragmentDiscoveryPresenter>
        implements FetchNetwork, FragmentBrowseProductView, DefaultCategoryAdapter.CategoryListener,
        RevampCategoryAdapter.CategoryListener, ProductAdapter.ScrollListener,
        TopAdsItemClickListener, TopAdsInfoClickListener {

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
    private BrowseProductRouter.GridType gridType = BrowseProductRouter.GridType.GRID_2;
    int spanCount = 2;
    private boolean isHasCategoryHeader = false;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;

    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isViewInitialized()) {
                return;
            }
            BrowseProductRouter.GridType gridType = (BrowseProductRouter.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            int lastItemPosition = getLastItemPosition();
            changeLayoutType(gridType);
            productAdapter.notifyItemChanged(productAdapter.getItemCount());
            mRecyclerView.scrollToPosition(lastItemPosition);
        }
    };

    private boolean isViewInitialized() {
        return productAdapter != null
                && linearLayoutManager != null
                && gridLayoutManager != null
                && mRecyclerView != null;
    }

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
        this.gridType = gridType;
        switch (gridType) {
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
        presenter = new FragmentDiscoveryPresenterImpl(this);
        presenter.setTAG(TAG);
        ScreenTracking.eventDiscoveryScreenAuth();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fragment_browse_product_v2;
    }

    @Override
    public void onCallProductServiceResult2(Long totalProduct, List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        productAdapter.addAll(true, false, new ArrayList<RecyclerViewItem>(model));
        productAdapter.notifyDataSetChanged();
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        if (!productAdapter.checkHasNext()) {
            topAdsRecyclerAdapter.hideLoading();
        }
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();

            if (totalProduct > 0)
                browseModel.setTotalDataCategory(NumberFormat.getNumberInstance(Locale.US)
                        .format(totalProduct.longValue()).replace(',', '.'));
            productAdapter.incrementPage();

            UnifyTracking.eventAppsFlyerViewListingSearch(model, browseModel.q);
            TrackingUtils.eventLocaSearched(browseModel.q);

        }

    }

    @Override
    public BrowseProductModel getDataModel() {
        return ((FragmentDiscoveryPresenterImpl) presenter).getBrowseProductModel();
    }

    @Override
    public void onCallProductServiceLoadMore(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        topAdsRecyclerAdapter.hideLoading();
        productAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        productAdapter.incrementPage();
    }

    @Override
    public void setHotlistData(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        topAdsRecyclerAdapter.hideLoading();
        topAdsRecyclerAdapter.setHasHeader(true);
        productAdapter.addAll(new ArrayList<RecyclerViewItem>(model));
        productAdapter.setgridView(((BrowseProductActivity) getActivity()).getGridType());
        productAdapter.setPagingHandlerModel(pagingHandlerModel);
        productAdapter.incrementPage();
        if (model.isEmpty()) {
            productAdapter.setSearchNotFound();
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public int getDataSize(String TAG) {
        if (TAG != null && ProductFragment.TAG.equals(TAG)) {
            return productAdapter.getItemCount();
        } else {
            return 0;
        }
    }

    @Override
    public void setupAdapter() {
        if (productAdapter != null) {
            return;
        }
        productAdapter = new ProductAdapter(getActivity(), new ArrayList<RecyclerViewItem>());
        spanCount = calcColumnSize(getResources().getConfiguration().orientation);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
//        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    // to determine size of grid columns
    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)
                        || productAdapter.isHotListBanner(position)
                        || productAdapter.isCategoryHeader(position)
                        || productAdapter.isEmptySearch(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void setupRecyclerView() {
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .build();

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), productAdapter);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setAdsInfoClickListener(this);
        topAdsRecyclerAdapter.setTopAdsParams(populatedNetworkParams());
        topAdsRecyclerAdapter.setConfig(config);
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                presenter.loadMore(getActivity());
                if (gridLayoutManager.findLastVisibleItemPosition() == gridLayoutManager.getItemCount() - 1 &&
                        productAdapter.getPagingHandlerModel().getUriNext().isEmpty()) {
                    ((BrowseProductActivity) getActivity()).showBottomBar();
                }
            }
        });
        mRecyclerView.setAdapter(productAdapter);
        changeLayoutType(((BrowseProductActivity) getActivity()).getGridType());
    }

    private TopAdsParams populatedNetworkParams() {
        NetworkParam.Product networkParam = ((BrowseProductActivity) getActivity()).getProductParam();
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, networkParam.source);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, networkParam.sc);
        if (networkParam.q != null) {
            params.getParam().put(TopAdsParams.KEY_QUERY, networkParam.q);
        }
        if (networkParam.h != null) {
            params.getParam().put(TopAdsParams.KEY_HOTLIST_ID, networkParam.h);
        }
        if (networkParam.extraFilter != null) {
            params.getParam().putAll(networkParam.extraFilter);
        }
        return params;
    }

    @Override
    public void onInfoClicked() {

    }

    @Override
    public void onProductItemClicked(Product product) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
                product.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(Shop shop) {

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

    private int getLastItemPosition() {
        switch (gridType) {
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

        switch (gridType) {
            case GRID_1:
                return productAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
            case GRID_2:
            case GRID_3:
            default:
                return productAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
        }
    }

    @Override
    public int getStartIndexForQuery(String TAG) {
        return productAdapter.getPagingHandlerModel().getStartIndex();
    }

    /**
     * need to supply correct TAG
     *
     * @param TAG
     * @return -1 means invalid index
     */
    @Override
    public int getPage(String TAG) {
        if (TAG.equals(ProductFragment.TAG))
            return productAdapter.getPage();
        else
            return -1;
    }

    @Override
    public void savePaging(Bundle savedState) {
        if (productAdapter != null) {
            productAdapter.saveAdapterPaging(savedState);
        }
    }

    @Override
    public void restorePaging(Bundle savedState) {
        if (productAdapter != null) {
            productAdapter.restoreAdapterPaging(savedState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (productAdapter != null) {
            productAdapter.saveAdapterPaging(outState);
        }
    }

    @Override
    public void addHotListHeader(ProductAdapter.HotListBannerModel hotListBannerModel) {
        topAdsRecyclerAdapter.setHasHeader(true);
        productAdapter.addHotListHeader(hotListBannerModel);
    }

    @Override
    public void addCategoryHeader(Data categoryHeader) {
        topAdsRecyclerAdapter.setHasHeader(true);
        isHasCategoryHeader = true;
        if (getActivity() != null && getActivity() instanceof BrowseProductActivity) {
            BrowseProductActivityModel browseModel = ((BrowseProductActivity) getActivity()).getBrowseProductActivityModel();
            if (categoryHeader.getIsRevamp() != null && categoryHeader.getIsRevamp()) {
                productAdapter.addCategoryRevampHeader(
                        new ProductAdapter.CategoryHeaderRevampModel(categoryHeader, getActivity(), getCategoryWidth(),
                                this, browseModel.getTotalDataCategory(), this));
            } else {
                productAdapter.addCategoryHeader(
                        new ProductAdapter.CategoryHeaderModel(categoryHeader, getActivity(), getCategoryWidth(),
                                this, browseModel.getTotalDataCategory(), this));
            }
            backToTop();
        }
        productAdapter.notifyDataSetChanged();
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
        return width / 2;
    }

    @Override
    public void onCategoryClick(Child child) {
        UnifyTracking.eventLevelCategory(((BrowseProductActivity) getActivity()).
                getBrowseProductActivityModel().getParentDepartement(), child.getId());
        ((BrowseProductActivity) getActivity()).renderLowerCategoryLevel(child);
    }

    @Override
    public void onCategoryRevampClick(Child child) {
        UnifyTracking.eventLevelCategory(((BrowseProductActivity) getActivity()).
                getBrowseProductActivityModel().getParentDepartement(), child.getId());
        ((BrowseProductActivity) getActivity()).renderLowerCategoryLevel(child);
    }

    @Override
    public void backToTop() {
        mRecyclerView.scrollToPosition(0);
    }
}
