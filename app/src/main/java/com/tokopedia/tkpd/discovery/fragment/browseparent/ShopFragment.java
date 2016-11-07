package com.tokopedia.tkpd.discovery.fragment.browseparent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.discovery.interfaces.FetchNetwork;
import com.tokopedia.tkpd.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.tkpd.home.fragment.FragmentProductFeed;
import com.tokopedia.tkpd.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.tkpd.discovery.presenter.browseparent.Shop;
import com.tokopedia.tkpd.discovery.presenter.browseparent.ShopImpl;
import com.tokopedia.tkpd.discovery.view.ShopView;
import com.tokopedia.tkpd.session.base.BaseFragment;
import com.tokopedia.tkpd.util.PagingHandler;
import com.tokopedia.tkpd.var.RecyclerViewItem;
import com.tokopedia.tkpd.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Erry on 6/30/2016.
 * modified by m.normansyah
 */
public class ShopFragment extends BaseFragment<Shop> implements ShopView, FetchNetwork {
    public static final int IDFRAGMENT = 1903_909;

    @Bind(R2.id.list_shop)
    RecyclerView list_shop;

    List<RecyclerViewItem> browseShopModelList = new ArrayList<>();
    private BrowseShopAdapter browseShopAdapter;
    private GridLayoutManager gridLayoutManager;
    private static final String TAG = ShopFragment.class.getSimpleName();

    public static ShopFragment newInstance() {

        Bundle args = new Bundle();
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
        gridLayoutManager = new GridLayoutManager(getActivity(), FragmentProductFeed.calcColumnSize(getResources().getConfiguration().orientation));
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

                headerColumnSize = FragmentProductFeed.PORTRAIT_COLUMN_HEADER;
                regularColumnSize = FragmentProductFeed.PORTRAIT_COLUMN;
                footerColumnSize = FragmentProductFeed.PORTRAIT_COLUMN_FOOTER;

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
    public void onCallProductServiceLoadMore(List<BrowseShopAdapter.ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
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
}
