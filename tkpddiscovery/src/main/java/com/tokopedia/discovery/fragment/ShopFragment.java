package com.tokopedia.discovery.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.browseparent.BrowseShopAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.presenter.BrowseView;
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

    public static final String SHOP_STATUS_IS_FAVORITED = "shopIsFavorited";
    public static final String FAVORITE_STATUS_UPDATED = "favoriteStatusUpdated";

    public static final int IDFRAGMENT = 1903_909;
    public static final String INDEX = "FRAGMENT_INDEX";
    public static final int GOTO_SHOP_DETAIL = 125;

    @BindView(R2.id.list_shop)
    RecyclerView list_shop;
    @BindView(R2.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    List<RecyclerViewItem> browseShopModelList = new ArrayList<>();
    private BrowseShopAdapter browseShopAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private static final String TAG = ShopFragment.class.getSimpleName();
    private BrowseProductRouter.GridType gridType;
    private int spanCount = 2;

    private BroadcastReceiver changeGridReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BrowseProductRouter.GridType gridType = (BrowseProductRouter.GridType) intent.getSerializableExtra(BrowseProductActivity.GRID_TYPE_EXTRA);
            int lastItemPosition = getLastItemPosition();
            changeLayoutType(gridType);
            browseShopAdapter.notifyItemChanged(browseShopAdapter.getItemCount());
            list_shop.scrollToPosition(lastItemPosition);
        }
    };

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
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
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
        if (list_shop.getAdapter() != null) {
            return;
        }
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
        changeLayoutType(((BrowseProductActivity) getActivity()).getGridType());
    }

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
        this.gridType = gridType;
        switch (gridType) {
            case GRID_1:
                spanCount = 1;
                linearLayoutManager = new LinearLayoutManager(getActivity());
                browseShopAdapter.setViewType(gridType);
                list_shop.setLayoutManager(linearLayoutManager);
                break;
            case GRID_2:
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                browseShopAdapter.setViewType(gridType);
                list_shop.setLayoutManager(gridLayoutManager);
                break;
            case GRID_3:
                spanCount = 1;
                gridLayoutManager.setSpanCount(spanCount);
                browseShopAdapter.setViewType(gridType);
                list_shop.setLayoutManager(gridLayoutManager);
                break;
        }
        list_shop.setAdapter(browseShopAdapter);
    }

    @Override
    public void initAdapter() {
        if (browseShopAdapter != null) {
            return;
        }
        browseShopAdapter = new BrowseShopAdapter(getActivity().getApplicationContext(),
                browseShopModelList, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        spanCount = ProductFeedHelper.calcColumnSize(getResources().getConfiguration().orientation);
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);

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
                    return spanCount;
                } else if (position == 0) {
//                    return headerColumnSize;
                    return regularColumnSize;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }


    @Override
    public void setEmptyState() {

    }

    @Override
    public void setLoading(boolean isLoading) {
        if (browseShopAdapter != null) browseShopAdapter.setIsLoading(isLoading);
    }

    @Override
    public void setShopData(List<ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
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
        switch (gridType) {
            case GRID_1:
                return browseShopAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
            case GRID_2:
            case GRID_3:
            default:
                return browseShopAdapter.getItemViewType(gridLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
        }
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

    @Override
    public void startShopInfoActivity(String shopId) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopId);
        getActivity().startActivity(intent);
    }

    @Override
    public void showErrorMessage(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void showToggleFavoriteSuccess(String shopName, boolean favorited) {
        String message;
        if (favorited) {
            message = getResources().getString(R.string.add_favorite_success_message)
                    .replace("$1", shopName);
        } else {
            message = getResources().getString(R.string.remove_favorite_success_message)
                    .replace("$1", shopName);
        }
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == GOTO_SHOP_DETAIL && resultCode == Activity.RESULT_CANCELED) {
            boolean isFavorited = data.getBooleanExtra(SHOP_STATUS_IS_FAVORITED, false);
            boolean isUpdated = data.getBooleanExtra(FAVORITE_STATUS_UPDATED, false);
            int position = browseShopAdapter.getLastItemClickedPosition();
            if (browseShopAdapter != null && position != -1 && isUpdated) {
                browseShopAdapter.updateShopIsFavorited(isFavorited, position);
            }
        }
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
}
