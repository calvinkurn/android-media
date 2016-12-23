package com.tokopedia.discovery.fragment.browseparent;

import android.content.Context;
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
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.discovery.presenter.browseparent.Shop;
import com.tokopedia.discovery.presenter.browseparent.ShopImpl;
import com.tokopedia.discovery.view.ShopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Erry on 6/30/2016.
 * modified by m.normansyah
 */
public class ShopFragment extends BaseFragment<Shop> implements ShopView, FetchNetwork {
    public static final int IDFRAGMENT = 1903_909;
    public static final String INDEX = "FRAGMENT_INDEX";
    @BindView(R2.id.list_shop)
    RecyclerView list_shop;

    List<RecyclerViewItem> browseShopModelList = new ArrayList<>();
    private BrowseShopAdapter browseShopAdapter;
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
        browseShopAdapter.setIsLoading(false);
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
    public void setupRecyclerView() {
        list_shop.setLayoutManager(gridLayoutManager);
        list_shop.setAdapter(browseShopAdapter);
        list_shop.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            }
        });
    }

    @Override
    public void initAdapter() {
        browseShopAdapter = new BrowseShopAdapter(getActivity().getApplicationContext(), browseShopModelList);
        browseShopAdapter.setIsLoading(true);
        gridLayoutManager = new GridLayoutManager(getActivity(),
                ProductFeedHelper.calcColumnSize(getResources().getConfiguration().orientation));

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

                headerColumnSize = ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                regularColumnSize = ProductFeedHelper.PORTRAIT_COLUMN;
                footerColumnSize = ProductFeedHelper.PORTRAIT_COLUMN_FOOTER;

                // set the value of footer, regular and header
                if (position == browseShopAdapter.getData().size()) {
                    // productFeedPresenter.getData().size()
                    // header column
                    return footerColumnSize;
                } else if (position == 0) {
//                    return headerColumnSize;
                    return regularColumnSize;
                } else if (browseShopAdapter.isTopAds(position)) {
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
    public void setLoading(boolean isLoading) {
        browseShopAdapter.setIsLoading(isLoading);
    }

    @Override
    public void onCallProductServiceLoadMore(List<ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        browseShopAdapter.addAll(true, new ArrayList<RecyclerViewItem>(model));
        browseShopAdapter.setPagingHandlerModel(pagingHandlerModel);
        if (browseShopAdapter.checkHasNext()) {
            browseShopAdapter.setIsLoading(true);
        } else {
            browseShopAdapter.setIsLoading(false);
        }
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
        return browseShopAdapter.getData() != null ? browseShopAdapter.getData().size() : -1;
    }

    @Override
    public void onCallNetwork() {
        Log.d(TAG, "onCallNetwork");
        if (getActivity() != null && getActivity() instanceof DiscoveryActivityPresenter) {
            DiscoveryActivityPresenter discoveryActivityPresenter = (DiscoveryActivityPresenter) getActivity();
            presenter.callNetwork(discoveryActivityPresenter);
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
        ((BrowseProductActivity) getActivity()).setFilterAttribute(filterAtrribute, activeTab);
    }
}
