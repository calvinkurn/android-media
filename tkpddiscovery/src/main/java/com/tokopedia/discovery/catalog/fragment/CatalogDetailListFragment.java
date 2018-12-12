package com.tokopedia.discovery.catalog.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customadapter.ColoredFilterAdapter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SlideOffViewHandler;
import com.tokopedia.discovery.catalog.adapter.CatalogDetailAdapter;
import com.tokopedia.discovery.catalog.listener.ICatalogDetailListView;
import com.tokopedia.discovery.catalog.model.CatalogDetailItem;
import com.tokopedia.discovery.catalog.model.CatalogDetailListLocation;
import com.tokopedia.discovery.catalog.model.CatalogListWrapperData;
import com.tokopedia.discovery.catalog.model.SingleItemFilter;
import com.tokopedia.discovery.catalog.presenter.CatalogDetailListPresenter;
import com.tokopedia.discovery.catalog.presenter.ICatalogDetailListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by Alvarisi
 */

public class CatalogDetailListFragment extends BasePresenterFragment<ICatalogDetailListPresenter>
        implements ICatalogDetailListView {
    public static final String CATALOG_ID = "catalog_id";
    private TextView mSorting;
    private TextView mCondition;
    private TextView mLocation;
    private RecyclerView mList;
    private CoordinatorLayout mContainer;
    private LinearLayout mFilterContainer;
    private ProgressBar mLoading;
    private SwipeRefreshLayout mRefresh;

    CatalogDetailAdapter mAdapter;
    LinearLayoutManager layoutManager;
    String catalogId;
    boolean isLoading = false;
    private List<SingleItemFilter> mSortList;
    private List<SingleItemFilter> mConditionList;

    private List<CatalogDetailItem> mCatalogDetailItems;
    private List<CatalogDetailListLocation> mLocationsData;
    private CatalogListWrapperData mWrapperData;
    private SlideOffViewHandler mSlideOffViewHandler;

    private int selectedSortPosition = 0;
    private int selectedLocationPosition = 0;
    private int selectedConditionPosition = 0;

    public CatalogDetailListFragment() {
    }


    public static CatalogDetailListFragment newInstance(String catalogId) {
        CatalogDetailListFragment fragment = new CatalogDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATALOG_ID, catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new CatalogDetailListPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        catalogId = arguments.getString(CATALOG_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_catalog_detail_list;
    }

    @Override
    protected void initView(View view) {
        mSorting = view.findViewById(R.id.sorting);
        mCondition = view.findViewById(R.id.condition);
        mLocation = view.findViewById(R.id.location);
        mList = view.findViewById(R.id.list);
        mContainer = view.findViewById(R.id.container);
        mFilterContainer = view.findViewById(R.id.filter_container);
        mLoading = view.findViewById(R.id.loading);
        mRefresh = view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void setViewListener() {
        mList.setHasFixedSize(true);
        mList.setLayoutManager(
                layoutManager
        );
        mList.addOnScrollListener(OnScrollRecyclerView());
        mList.setAdapter(mAdapter);
        mRefresh.setOnRefreshListener(OnRefreshSwipeView());
        mSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortingDialog();
            }
        });
        mCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConditionDialog();
            }
        });
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationDialog();
            }
        });
        mList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSlideOffViewHandler.MotionEventListener(event,
                        new SlideOffViewHandler.SlideOffMotionEventListener() {
                            @Override
                            public void OnMoveUp() {
                                mSlideOffViewHandler.ToggleSlideOffScreen(mFilterContainer, false, true);
                            }

                            @Override
                            public void OnMoveDown() {
                                mSlideOffViewHandler.ToggleSlideOffScreen(mFilterContainer, false, false);
                            }
                        });
                return false;
            }
        });
    }

    @Override
    protected void initialVar() {
        mCatalogDetailItems = new ArrayList<>();
        mLocationsData = new ArrayList<>();
        mAdapter = CatalogDetailAdapter.createAdapter(getActivity(), mCatalogDetailItems, presenter);

        mWrapperData = new CatalogListWrapperData(catalogId);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSortList = constructSortList();
        mConditionList = constructConditionList();
        mSlideOffViewHandler = new SlideOffViewHandler();
    }

    @Override
    protected void setActionVar() {
        presenter.fetchCatalogDetailListData(this.mWrapperData);
        showProgressLoading();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showProgressLoading() {
        isLoading = true;
        mLoading.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressLoading() {
        isLoading = false;
        if (mLoading != null)
            mLoading.setVisibility(View.GONE);
        if (mContainer != null)
            mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void closeView() {

    }

    @Override
    public void showSortingDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        ColoredFilterAdapter adapter = new ColoredFilterAdapter(getActivity(),
                selectedSortPosition, mSortList);
        alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mWrapperData.setOrderBy(mSortList.get(which).getId());
                refreshData();
                selectedSortPosition = which;
            }
        });
        alertDialog.create().show();
    }

    private void refreshData() {
        showProgressLoading();
        mWrapperData.refreshPage();
        presenter.fetchCatalogDetailListData(mWrapperData);
    }

    @Override
    public void showConditionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        ColoredFilterAdapter adapter = new ColoredFilterAdapter(getActivity(),
                selectedConditionPosition, mConditionList);
        alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mWrapperData.setCondition(mConditionList.get(which).getId());
                selectedConditionPosition = which;
                refreshData();
            }
        });
        alertDialog.create().show();
    }

    @Override
    public void showLocationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        ColoredFilterAdapter adapter = new ColoredFilterAdapter(getActivity(),
                selectedLocationPosition, mLocationsData);
        alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mWrapperData.setLocation(mLocationsData.get(which).getId());
                refreshData();
                selectedLocationPosition = which;
            }
        });
        alertDialog.create().show();
    }

    @Override
    public void renderListLocation(@NonNull List<CatalogDetailListLocation> locationsData) {
        if (mLocationsData == null) {
            return;
        }
        this.mLocationsData.clear();
        this.mLocationsData.add(0, CatalogDetailListLocation.createSelectionInfo(getString(R.string.sort_browse_catalog_all_location)));
        this.mLocationsData.addAll(locationsData);
    }

    @Override
    public void renderListCatalogProduct(@NonNull List<CatalogDetailItem> catalogDetailItems) {
        if (mRefresh == null) {
            return;
        }
        hideProgressLoading();
        mRefresh.setRefreshing(false);
        mCatalogDetailItems.clear();
        if (catalogDetailItems.isEmpty()) {
            mAdapter.showEmpty(true);
        } else {
            mAdapter.showEmpty(false);
        }
        mCatalogDetailItems.addAll(catalogDetailItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderListCatalogProductLoadMore(@NonNull List<CatalogDetailItem> catalogDetailItems) {
        mCatalogDetailItems.addAll(catalogDetailItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderErrorGetCatalogProduct(String message) {
        if (mContainer == null) {
            return;
        }
        hideProgressLoading();
        mContainer.setVisibility(View.GONE);
        mRefresh.setRefreshing(false);
        NetworkErrorHelper.showEmptyState(getActivity(),
                getView(),
                message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshData();
                    }
                });
    }

    @Override
    public void renderErrorGetCatalogProductLoadMore(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.fetchCatalogDetailListDataLoadMore(mWrapperData);
            }
        }).showRetrySnackbar();
    }

    private RecyclerView.OnScrollListener OnScrollRecyclerView() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastPosition()) {
                    int page = mWrapperData.getStart();
                    mWrapperData.setStart(++page);
                    presenter.fetchCatalogDetailListDataLoadMore(mWrapperData);
                }
            }
        };
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshSwipeView() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWrapperData.refreshPage();
                presenter.fetchCatalogDetailListData(mWrapperData);
            }
        };
    }

    private boolean isLastPosition() {
        int lastIndex = layoutManager.findLastCompletelyVisibleItemPosition();
        int size = layoutManager.getItemCount() - 1;
        return lastIndex == size;
    }

    private List<SingleItemFilter> constructSortList() {
        List<SingleItemFilter> sortList = new ArrayList<>();
        sortList.add(new SingleItemFilter(String.valueOf(1), getString(R.string.sort_browse_catalog_product_sold)));
        sortList.add(new SingleItemFilter(String.valueOf(3), getString(R.string.sort_browse_catalog_lowest_price)));
        sortList.add(new SingleItemFilter(String.valueOf(4), getString(R.string.sort_browse_catalog_highest_price)));
        return sortList;
    }

    private List<SingleItemFilter> constructConditionList() {
        List<SingleItemFilter> conditionsList = new ArrayList<>();
        conditionsList.add(new SingleItemFilter("", getString(R.string.sort_browse_catalog_all_condition)));
        conditionsList.add(new SingleItemFilter(String.valueOf(1), getString(R.string.sort_browse_catalog_new_condition)));
        conditionsList.add(new SingleItemFilter(String.valueOf(2), getString(R.string.sort_browse_catalog_used_condition)));
        return conditionsList;
    }
}
