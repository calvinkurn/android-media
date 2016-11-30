package com.tokopedia.discovery.fragment.browseparent;

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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.CatalogModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.discovery.interfaces.FetchNetwork;
import com.tokopedia.discovery.presenter.DiscoveryActivityPresenter;
import com.tokopedia.discovery.presenter.browseparent.Catalog;
import com.tokopedia.discovery.presenter.browseparent.CatalogImpl;
import com.tokopedia.discovery.view.CatalogView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Erry on 6/30/2016.
 */
public class CatalogFragment extends BaseFragment<Catalog> implements CatalogView, FetchNetwork {
    public static final int IDFRAGMENT = 123_348;
    public static final String INDEX = "FRAGMENT_INDEX";

    private static final int LANDSCAPE_COLUMN_MAIN = 3;
    private static final int PORTRAIT_COLUMN_MAIN = 2;

    private static final int PORTRAIT_COLUMN_HEADER = 2;
    private static final int PORTRAIT_COLUMN_FOOTER = 2;
    private static final int PORTRAIT_COLUMN = 1;

    @Bind(R2.id.list_catalog)
    RecyclerView list_catalog;

    private List<RecyclerViewItem> browseCatalogModelList = new ArrayList<>();
    private BrowseCatalogAdapter browseCatalogAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private BrowseProductRouter.GridType gridType;
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

    private void changeLayoutType(BrowseProductRouter.GridType gridType) {
        this.gridType = gridType;
        switch (gridType) {
            case GRID_1:
                spanCount = 1;
                linearLayoutManager = new LinearLayoutManager(getActivity());
                browseCatalogAdapter.setGridView(gridType);
                list_catalog.setLayoutManager(linearLayoutManager);
                break;
            case GRID_2:
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                browseCatalogAdapter.setGridView(gridType);
                list_catalog.setLayoutManager(gridLayoutManager);
                break;
            case GRID_3:
                spanCount = 1;
                gridLayoutManager.setSpanCount(spanCount);
                browseCatalogAdapter.setGridView(gridType);
                list_catalog.setLayoutManager(gridLayoutManager);
                break;
        }
    }

    public static CatalogFragment newInstance(int index) {

        Bundle args = new Bundle();
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
        browseCatalogAdapter.setIsLoading(false);
        browseCatalogAdapter.setIsErrorState(true);
        browseCatalogAdapter.setOnRetryListenerRV(new BaseRecyclerViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
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
        if (getActivity() != null && getActivity() instanceof DiscoveryActivityPresenter) {
            DiscoveryActivityPresenter discoveryActivityPresenter = (DiscoveryActivityPresenter) getActivity();
            presenter.callNetwork(discoveryActivityPresenter);
        }
    }

    @Override
    public void setupRecyclerView() {
        list_catalog.setLayoutManager(gridLayoutManager);
        list_catalog.setAdapter(browseCatalogAdapter);
        list_catalog.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void initAdapter() {
        browseCatalogAdapter = new BrowseCatalogAdapter(getActivity().getApplicationContext(), browseCatalogModelList);
        browseCatalogAdapter.setIsLoading(true);
        spanCount = calcColumnSize(getResources().getConfiguration().orientation);
        linearLayoutManager = new LinearLayoutManager(getActivity());
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

                headerColumnSize = PORTRAIT_COLUMN_HEADER;
                regularColumnSize = PORTRAIT_COLUMN;
                footerColumnSize = PORTRAIT_COLUMN_FOOTER;

                // set the value of footer, regular and header
                if (position == browseCatalogAdapter.getData().size()) {
                    return spanCount;
                } else if (position == 0) {
                    return regularColumnSize;
                } else {
                    // regular one column
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public void notifyChangeData(List<CatalogModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        browseCatalogAdapter.addAll(false, new ArrayList<RecyclerViewItem>(model));
        browseCatalogAdapter.setPagingHandlerModel(pagingHandlerModel);
        browseCatalogAdapter.setGridView(((BrowseProductActivity) getActivity()).getGridType());
        if (browseCatalogAdapter.checkHasNext()) {
            browseCatalogAdapter.setIsLoading(true);
        } else {
            browseCatalogAdapter.setIsLoading(false);
        }
        browseCatalogAdapter.notifyDataSetChanged();

        browseCatalogAdapter.incrementPage();
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
    public int getPage(String TAG) {
        return 0;
    }

    @Override
    public int getDataSize() {
        return browseCatalogAdapter.getData() != null ? browseCatalogAdapter.getData().size() : -1;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab) {
        if (filterAtrribute.getSort() != null) {
            filterAtrribute.setSelected(filterAtrribute.getSort().get(0).getName());
        }
        ((BrowseProductActivity) getActivity()).setFilterAttribute(filterAtrribute, activeTab);
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
