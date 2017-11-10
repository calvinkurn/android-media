package com.tokopedia.discovery.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.CatalogModel;
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
import com.tokopedia.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.browseparent.Catalog;
import com.tokopedia.discovery.presenter.browseparent.CatalogImpl;
import com.tokopedia.discovery.view.CatalogView;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Erry on 6/30/2016.
 */
public class CatalogFragment extends BaseFragment<Catalog> implements CatalogView, FetchNetwork,
        TopAdsItemClickListener, TopAdsListener {
    public static final int IDFRAGMENT = 123_348;
    public static final String INDEX = "FRAGMENT_INDEX";

    private static final int LANDSCAPE_COLUMN_MAIN = 3;
    private static final int PORTRAIT_COLUMN_MAIN = 2;

    @BindView(R2.id.list_catalog)
    RecyclerView list_catalog;

    private List<RecyclerViewItem> browseCatalogModelList = new ArrayList<>();
    private BrowseCatalogAdapter browseCatalogAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private BrowseProductRouter.GridType gridType;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private int spanCount = 2;


    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BrowseProductRouter.GridType gridType = (BrowseProductRouter.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            changeLayoutType(gridType);
            int lastItemPosition = getLastItemPosition();
            browseCatalogAdapter.notifyItemChanged(browseCatalogAdapter.getItemCount());
            list_catalog.scrollToPosition(lastItemPosition);
        }
    };

    @Override
    public String getScreenName() {
        return null;
    }

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
        this.gridType = gridType;
        switch (gridType) {
            case GRID_1:
                spanCount = 1;
                browseCatalogAdapter.setGridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(linearLayoutManager);
                break;
            case GRID_2:
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                browseCatalogAdapter.setGridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(gridLayoutManager);
                break;
            case GRID_3:
                spanCount = 1;
                gridLayoutManager.setSpanCount(spanCount);
                browseCatalogAdapter.setGridView(gridType);
                topAdsRecyclerAdapter.setLayoutManager(gridLayoutManager);
                break;
        }
    }

    public static CatalogFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        CatalogFragment fragment = new CatalogFragment();
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
    protected void initPresenter() {
        presenter = new CatalogImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_catalog;
    }

    @Override
    public int getFragmentId() {
        return IDFRAGMENT;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.loadMore(getActivity());
            }
        });
        if (!browseCatalogAdapter.isEmpty()) {

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
    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {
        onCallNetwork();
    }

    @Override
    public void onMessageError(int type, Object... data) {
        onCallNetwork();
    }

    @Override
    public void onCallNetwork() {
        if (getActivity() != null && getActivity() instanceof BrowseView) {
            BrowseView browseView = (BrowseView) getActivity();
            presenter.callNetwork(browseView);
        }
    }

    @Override
    public void onProductItemClicked(Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_url());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
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
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void setupRecyclerView() {
        if (list_catalog.getAdapter() != null) {
            return;
        }

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.PRODUCT)
                .topAdsParams(populatedNetworkParams())
                .build();

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), browseCatalogAdapter);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setConfig(config);
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                if (browseCatalogAdapter.getPagingHandlerModel() != null &&
                        !TextUtils.isEmpty(browseCatalogAdapter.getPagingHandlerModel().getUriNext())) {
                    presenter.loadMore(getActivity());
                } else {
                    topAdsRecyclerAdapter.shouldLoadAds(false);
                    topAdsRecyclerAdapter.hideLoading();
                    topAdsRecyclerAdapter.unsetEndlessScrollListener();
                    if (getActivity() instanceof  BrowseProductActivity) {
                        ((BrowseProductActivity) getActivity()).showBottomBar();
                    }
                }
            }
        });
        list_catalog.setAdapter(topAdsRecyclerAdapter);
        changeLayoutType(((BrowseProductActivity) getActivity()).getGridType());
    }

    private TopAdsParams populatedNetworkParams() {
        NetworkParam.Product networkParam = ((BrowseProductActivity) getActivity()).getProductParam();
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, networkParam.source);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, networkParam.sc);
        if (networkParam.keyword != null) {
            params.getParam().put(TopAdsParams.KEY_QUERY, networkParam.keyword);
        }
        if (networkParam.extraFilter != null) {
            params.getParam().putAll(networkParam.extraFilter);
        }
        return params;
    }

    @Override
    public void initAdapter() {
        if (browseCatalogAdapter != null) {
            return;
        }
        browseCatalogAdapter = new BrowseCatalogAdapter(getActivity().getApplicationContext(), browseCatalogModelList);
        spanCount = calcColumnSize(getResources().getConfiguration().orientation);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
    }

    // to determine size of grid columns
    GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)
                        || browseCatalogAdapter.isEmptySearch(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void notifyChangeData(List<CatalogModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        topAdsRecyclerAdapter.shouldLoadAds(model.size() > 0);
        browseCatalogAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        browseCatalogAdapter.setPagingHandlerModel(pagingHandlerModel);
        browseCatalogAdapter.setGridView(((BrowseProductActivity) getActivity()).getGridType());
        browseCatalogAdapter.incrementPage();
    }

    @Override
    public void displayEmptyResult() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        browseCatalogAdapter.setSearchNotFound();
        browseCatalogAdapter.notifyItemInserted(1);
        setLoading(false);
        ((BrowseProductActivity) getActivity()).showLoading(false);
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
                return browseCatalogAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
            case GRID_2:
            case GRID_3:
            default:
                return browseCatalogAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
        }
    }

    @Override
    public int getStartIndexForQuery(String TAG) {
        if (browseCatalogAdapter != null && browseCatalogAdapter.getPagingHandlerModel() != null) {
            return browseCatalogAdapter.getPagingHandlerModel().getStartIndex();
        } else {
            return 0;
        }
    }

    @Override
    public BrowseCatalogModel getDataModel() {
        Log.d(TAG, "presenter " + presenter);
        return ((CatalogImpl) presenter).getCatalogModel();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if(isLoading){
            topAdsRecyclerAdapter.showLoading();
        } else {
            topAdsRecyclerAdapter.hideLoading();
        }
    }

    @Override
    public void onTopAdsLoaded() {
        setLoading(false);
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        setLoading(false);
    }

    @Override
    public int getPage(String TAG) {
        return 0;
    }

    @Override
    public int getDataSize() {
        if (browseCatalogAdapter == null) return -1;
        return browseCatalogAdapter.getData() != null ? browseCatalogAdapter.getData().size() : -1;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void setDynamicFilterAtrribute(DataValue filterAtrribute, int activeTab) {
        if (filterAtrribute.getSort() != null) {
            filterAtrribute.setSelected(filterAtrribute.getSort().get(0).getName());
        }
        ((BrowseView) getActivity()).setFilterAttribute(filterAtrribute, activeTab);
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

}
