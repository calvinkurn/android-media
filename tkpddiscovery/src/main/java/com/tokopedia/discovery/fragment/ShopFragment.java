package com.tokopedia.discovery.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.browseparent.Shop;
import com.tokopedia.discovery.presenter.browseparent.ShopImpl;
import com.tokopedia.discovery.view.ShopView;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Erry on 6/30/2016.
 * modified by m.normansyah
 */
public class ShopFragment extends BaseFragment<Shop> implements ShopView, FetchNetwork,
        TopAdsItemClickListener {
    public static final int IDFRAGMENT = 1903_909;
    public static final String INDEX = "FRAGMENT_INDEX";
    @BindView(R2.id.list_shop)
    RecyclerView list_shop;

    List<RecyclerViewItem> browseShopModelList = new ArrayList<>();
    private BrowseShopAdapter browseShopAdapter;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private GridLayoutManager gridLayoutManager;
    private static final String TAG = ShopFragment.class.getSimpleName();

    public static ShopFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        presenter = new ShopImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse_shop;
    }

    @Override
    public int getFragmentId() {
        return IDFRAGMENT;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        browseShopAdapter.setIsErrorState(true);
        browseShopAdapter.setOnRetryListenerRV(new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.loadMore(getActivity());
            }
        });
        if (!browseShopAdapter.isEmpty()) {
            Snackbar snackbar = Snackbar.make(parentView, CommonUtils.generateMessageError(getActivity(), getContext().getString(R.string.title_retry)), Snackbar.LENGTH_INDEFINITE);

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
    public String getScreenName() {
        return null;
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

    @Override
    public void onProductItemClicked(Product product) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
                product.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(com.tokopedia.topads.sdk.domain.model.Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(Data data) {

    }

    @Override
    public void setupRecyclerView() {
        if (list_shop.getAdapter() != null) {
            return;
        }
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .setEndpoint(Endpoint.SHOP)
                .build();

        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), browseShopAdapter);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
//        topAdsRecyclerAdapter.setTopAdsParams(populatedNetworkParams());
        topAdsRecyclerAdapter.setConfig(config);
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                presenter.loadMore(getActivity());
            }
        });
        list_shop.setLayoutManager(gridLayoutManager);
        list_shop.setAdapter(browseShopAdapter);
    }

    private TopAdsParams populatedNetworkParams() {
        NetworkParam.Product networkParam = ((BrowseProductActivity) getActivity()).getProductParam();
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, networkParam.source);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, networkParam.sc);
        if (networkParam.q != null) {
            params.getParam().put(TopAdsParams.KEY_QUERY, networkParam.q);
        }
        if (networkParam.extraFilter != null) {
            params.getParam().putAll(networkParam.extraFilter);
        }
        return params;
    }

    @Override
    public void initAdapter() {
        if (browseShopAdapter != null) {
            return;
        }
        browseShopAdapter = new BrowseShopAdapter(getActivity().getApplicationContext(), browseShopModelList);
        gridLayoutManager = new GridLayoutManager(getActivity(),
                ProductFeedHelper.calcColumnSize(getResources().getConfiguration().orientation));
    }

    // to determine size of grid columns
    GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)
                        || browseShopAdapter.isEmptySearch(position)) {
                    return 2;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (browseShopAdapter != null) browseShopAdapter.setIsLoading(isLoading);
    }

    @Override
    public void onCallProductServiceLoadMore(List<ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        topAdsRecyclerAdapter.hideLoading();
        browseShopAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        browseShopAdapter.setPagingHandlerModel(pagingHandlerModel);
        browseShopAdapter.incrementPage();
    }

    @Override
    public boolean isLoading() {
        return browseShopAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public int getStartIndexForQuery(String TAG) {
        if (browseShopAdapter != null && browseShopAdapter.getPagingHandlerModel() != null) {
            return browseShopAdapter.getPagingHandlerModel().getStartIndex();
        } else {
            return 0;
        }
    }

    @Override
    public int getPage(String TAG) {
        return 0;
    }

    @Override
    public int getDataSize() {
        if (browseShopAdapter != null){
            if (browseShopAdapter.getData() != null) {
                return browseShopAdapter.getData().size();
            } else {
                return -1;
            }
        }
        return -1;
    }

    @Override
    public void onCallNetwork() {
        Log.d(TAG, "onCallNetwork");
        if (getActivity() != null && getActivity() instanceof BrowseView) {
            BrowseView browseView = (BrowseView) getActivity();
            presenter.callNetwork(browseView);
        }
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
}
