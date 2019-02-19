package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.common.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.common.utils.MenuTintUtils;
import com.tokopedia.seller.common.utils.NetworkStatus;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.mapper.SearchProductEOFMapper;
import com.tokopedia.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.utils.TopAdsNetworkErrorHelper;
import com.tokopedia.topads.dashboard.view.TopAdsSearchProductView;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterProductPromoActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAddProductListAdapter;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.topads.dashboard.view.listener.AdapterSelectionListener;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddProductListPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author normansyahputa on 2/13/17.
 */
public class TopAdsAddProductListFragment extends BasePresenterFragment
        implements AdapterSelectionListener<TopAdsProductViewModel>, SearchView.OnQueryTextListener, TopAdsSearchProductView,
        DefaultErrorSubscriber.ErrorNetworkListener {

    public static final String TAG = "TAAddPrductListFragment";
    public static final int FILTER_REQ_CODE = 100;
    int RESULT_CODE = 9912;

    private RecyclerView topAdsAddProductList;
    private TopAdsAddProductListAdapter topAdsProductListAdapter;
    private SwipeToRefresh swipeToRefresh;
    private RefreshHandler refreshHandler;
    private TopAdsNetworkErrorHelper gmNetworkErrorHelper;
    private View rootView;
    private Button buttonNext;
    private TextView counterProduct;
    private View containerCounterProduct;

    private TopAdsAddProductListPresenter topAdsAddProductListPresenter;
    private RecyclerView.OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;
    private HashSet<TopAdsProductViewModel> selections = new HashSet<>();

    private boolean isFirstTime = true;
    private boolean isEndOfFile = true;
    private int maxNumberChoosen;
    private boolean isExistingGroup;
    private boolean isHideEtalase;

    public static Fragment newInstance(int maxNumberSelection, ArrayList<TopAdsProductViewModel> selectionParcel, boolean isExistingGroup, boolean isHideEtalase) {
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, maxNumberSelection);
        bundle.putBoolean(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, isExistingGroup);
        bundle.putBoolean(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, isHideEtalase);
        bundle.putParcelableArrayList(TopAdsExtraConstant.EXTRA_SELECTIONS, selectionParcel);

        Fragment fragment = new TopAdsAddProductListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        inject();
        gmNetworkErrorHelper = new TopAdsNetworkErrorHelper(null, rootView);
        setupRecyclerView();
        fetchData();
    }


    private void fetchData() {
        if (topAdsAddProductListPresenter.getNetworkStatus()
                == NetworkStatus.ONACTIVITYFORRESULT) {
            refreshHandler.setRefreshing(true);
            topAdsAddProductListPresenter.searchProduct();
        } else {
            topAdsAddProductListPresenter.setNetworkStatus(
                    NetworkStatus.PULLTOREFRESH);
            if (topAdsAddProductListPresenter.isFirstTime()) {
                topAdsProductListAdapter.showLoadingFull(true);
                swipeToRefresh.setEnabled(false);
                topAdsAddProductListPresenter.searchProduct();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gmNetworkErrorHelper.onPause();
        topAdsAddProductList.removeOnScrollListener(onScrollListener);
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void initialPresenter() {
        topAdsAddProductListPresenter = new TopAdsAddProductListPresenter();
        topAdsAddProductListPresenter.attachView(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        ArrayList<TopAdsProductViewModel> selectionParcel = arguments.getParcelableArrayList(
                TopAdsExtraConstant.EXTRA_SELECTIONS);

        if (selectionParcel != null) {
            for (TopAdsProductViewModel topAdsProductViewModel : selectionParcel) {
                selections.add(topAdsProductViewModel);
            }
        }

        isExistingGroup = arguments.getBoolean(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        isHideEtalase = arguments.getBoolean(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        maxNumberChoosen = arguments.getInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_product_list;
    }

    @Override
    protected void initView(View view) {
        this.rootView = view;
        setHasOptionsMenu(true);
        counterProduct = (TextView) view.findViewById(R.id.counter_product);
        containerCounterProduct = view.findViewById(R.id.container_counter_selection);
        buttonNext = (Button) view.findViewById(R.id.top_ads_btn_next_);
        topAdsAddProductList = (RecyclerView) view.findViewById(R.id.top_ads_add_product_recycler_view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.top_ads_add_product_refresh);
        refreshHandler = new RefreshHandler(swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                dismissSnackbar();

                if (topAdsProductListAdapter.getDataSize() > 0) {

                } else {
                    topAdsProductListAdapter.clear();
                    topAdsProductListAdapter.notifyDataSetChanged();

                    topAdsProductListAdapter.showLoadingFull(true);
                }

                topAdsAddProductListPresenter.resetPage();
                topAdsAddProductListPresenter.setNetworkStatus(
                        NetworkStatus.PULLTOREFRESH);
                searchProductNetworkCall();
            }
        });
        updateSelectedProductCount();
    }

    @Override
    protected void setViewListener() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnSelections();
            }
        });
    }

    private void returnSelections() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, new ArrayList<>(selections));
        getActivity().setResult(RESULT_CODE, intent);
        getActivity().finish();
    }

    @Override
    protected void initialVar() {
        topAdsProductListAdapter = new TopAdsAddProductListAdapter();
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(topAdsProductListAdapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_promo_not_found_title_empty_text));
        topAdsProductListAdapter.setEmptyView(emptyGroupAdsDataBinder);
        RetryDataBinder topAdsRetryDataBinder = new TopAdsRetryDataBinder(topAdsProductListAdapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                dismissSnackbar();

                if (topAdsProductListAdapter.getDataSize() > 0) {

                } else {
                    topAdsProductListAdapter.clear();
                    topAdsProductListAdapter.notifyDataSetChanged();

                    topAdsProductListAdapter.showLoadingFull(true);
                    swipeToRefresh.setEnabled(false);
                }

                topAdsAddProductListPresenter.resetPage();
                topAdsAddProductListPresenter.setNetworkStatus(
                        NetworkStatus.RETRYNETWORKCALL);
                searchProductNetworkCall();
            }
        });
        topAdsProductListAdapter.setRetryView(topAdsRetryDataBinder);
    }

    private void setupRecyclerView() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isEndOfFile) {
                    return;
                }

                if (topAdsAddProductListPresenter.isHitNetwork()) {
                    return;
                }

                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                int totalItem = Integer.MAX_VALUE;
                if (lastItemPosition == visibleItem
                        && topAdsProductListAdapter.getDataSize() < totalItem) {
                    topAdsAddProductListPresenter.incrementPage();
                    topAdsAddProductListPresenter.setNetworkStatus(
                            NetworkStatus.LOADMORE);
                    loadMoreNetworkCall();
                    topAdsProductListAdapter.showLoading(true);
                }
            }
        };
        topAdsProductListAdapter.setImageHandler(new ImageHandler(getActivity()));
        topAdsProductListAdapter.setListener(this);
        if (isFirstTime) {
            layoutManager = new LinearLayoutManager(getActivity());
            this.topAdsAddProductList.setLayoutManager(layoutManager);
            this.topAdsAddProductList.setAdapter(topAdsProductListAdapter);
        }
        isFirstTime = false;
        this.topAdsAddProductList.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void setActionVar() {

    }

    protected void loadMoreNetworkCall() {
        topAdsAddProductListPresenter.loadMore();
    }

    protected void searchProductNetworkCall() {
        topAdsAddProductListPresenter.searchProduct();
    }

    private void inject() {
        //[START] This is for dependent component
        TopAdsManagementService topAdsSearchProductService = new TopAdsManagementService(new SessionHandler(getActivity()));
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        SearchProductEOFMapper searchProductMapper = new SearchProductEOFMapper();
        CloudTopAdsSearchProductDataSource cloudTopAdsSeachProductDataSource = new CloudTopAdsSearchProductDataSource(
                getActivity(),
                topAdsSearchProductService,
                searchProductMapper
        );
        TopAdsSearchProductRepositoryImpl topAdsSeachProductRepository = new TopAdsSearchProductRepositoryImpl(getActivity(), cloudTopAdsSeachProductDataSource);
        TopAdsProductListUseCase topAdsProductListUseCase = new TopAdsProductListUseCase(
                new JobExecutor(), new UIThread(), topAdsSeachProductRepository
        );

        topAdsAddProductListPresenter.setSessionHandler(sessionHandler);
        topAdsAddProductListPresenter.setTopAdsProductListUseCase(topAdsProductListUseCase);
        topAdsAddProductListPresenter.setErrorNetworkListener(this);
        //[END] This is for dependent component
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {
        if (selections.size() == maxNumberChoosen) {
            return;
        }

        addSelection(data);
        topAdsProductListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        removeSelection(data);
        topAdsProductListAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        if (selections == null)
            return false;
        
        return selections.contains(data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isAdded())
            return;

        inflater.inflate(R.menu.menu_top_ads_product_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_product));
        SearchView.SearchAutoComplete mSearchSrcTextView =
                (SearchView.SearchAutoComplete)
                        searchView.findViewById(com.tokopedia.core.R.id.search_src_text);
        mSearchSrcTextView.setTextColor(getResources().getColor(com.tokopedia.core.R.color.black_70));
        mSearchSrcTextView.setHintTextColor(
                getResources().getColor(com.tokopedia.core.R.color.black_70)
        );

        updateOptionMenuColor(menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateOptionMenuColor(Menu menu) {
        MenuTintUtils.tintAllIcons(menu, com.tokopedia.seller.R.color.black_70);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_filter) {
            if (topAdsAddProductListPresenter != null) {
                TopAdsFilterProductPromoActivity.start(
                        this,
                        getActivity(),
                        FILTER_REQ_CODE,
                        topAdsAddProductListPresenter.getSelectedFilterStatus(),
                        topAdsAddProductListPresenter.getSelectedFilterEtalaseId(),
                        isExistingGroup);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (topAdsAddProductListPresenter != null) {
                    topAdsAddProductListPresenter.putSelectedFilterStatus(
                            data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS_PROMO, 0));
                    topAdsAddProductListPresenter.putSelectedEtalaseId(
                            data.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, 0));

                    topAdsAddProductListPresenter.setNetworkStatus(NetworkStatus.ONACTIVITYFORRESULT);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        fetchDataWithQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        fetchDataWithQuery(newText);
        return true;
    }

    public void fetchDataWithQuery(String newText) {
        if (newText != null && newText.isEmpty()) {
            topAdsAddProductListPresenter.setQuery(null);
        } else {
            topAdsAddProductListPresenter.setQuery(newText);
        }

        topAdsAddProductListPresenter.resetPage();

        dismissSnackbar();

        topAdsProductListAdapter.clear();
        topAdsProductListAdapter.notifyDataSetChanged();
        topAdsProductListAdapter.showLoadingFull(true);

        topAdsAddProductListPresenter.setNetworkStatus(
                NetworkStatus.SEARCHVIEW);
        searchProductNetworkCall();
    }

    @Override
    public void loadData(List<ItemType> datas) {
        renderDatas(datas);
    }

    private void renderDatas(List<ItemType> datas) {
        if (refreshHandler.isRefreshing()) {
            refreshHandler.finishRefresh();
        }

        topAdsProductListAdapter.showLoadingFull(false);
        topAdsProductListAdapter.showEmpty(false);
        topAdsProductListAdapter.showRetry(false);

        switch (topAdsAddProductListPresenter.getNetworkStatus()) {
            case LOADMORE:
                break;
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                topAdsProductListAdapter.clear();
                break;
            case RETRYNETWORKCALL:
                if (topAdsProductListAdapter.getDataSize() <= 0) {
                    topAdsProductListAdapter.clear();
                }
                break;
        }
        topAdsProductListAdapter.addAllWithoutNotify(datas);
        boolean isEmpty = topAdsProductListAdapter.getDataSize() <= 0;
        if (isEmpty) {
            if (!isEndOfFile) {
                topAdsProductListAdapter.showLoading(true);
            } else {
                topAdsProductListAdapter.showEmptyFull(true);
            }
        } else {
            if (!isEndOfFile) {
                topAdsProductListAdapter.showLoading(true);
            } else {
                topAdsProductListAdapter.showLoading(false);
            }
            topAdsProductListAdapter.notifyDataSetChanged();
        }
        swipeToRefresh.setEnabled(true);
    }

    @Override
    public void loadMore(List<ItemType> datas) {
        renderDatas(datas);
    }

    @Override
    public void dismissSnackbar() {
        gmNetworkErrorHelper.dismissSnackbar();
    }

    @Override
    public void showMessageError(final String errorMessage) {
        // disable pull to refresh + hide
        refreshHandler.setRefreshing(false);
        topAdsProductListAdapter.showLoading(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && rootView != null) {

                    switch (topAdsAddProductListPresenter.getNetworkStatus()) {
                        case RETRYNETWORKCALL:
                        case ONACTIVITYFORRESULT:
                        case PULLTOREFRESH:
                        case SEARCHVIEW:
                            topAdsProductListAdapter.clear();
                            topAdsProductListAdapter.showRetryFull(true);
                            break;
                        default:
                            gmNetworkErrorHelper.showSnackbar(errorMessage,
                                    getString(R.string.toast_try_again), new NetworkErrorHelper.RetryClickedListener() {
                                        @Override
                                        public void onRetryClicked() {
                                            Toast.makeText(
                                                    TopAdsAddProductListFragment.this.getActivity(),
                                                    errorMessage,
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            dismissSnackbar();

                                            refreshHandler.setRefreshing(true);

                                            topAdsAddProductListPresenter.setNetworkStatus(
                                                    NetworkStatus.RETRYNETWORKCALL);
                                            loadMoreNetworkCall();
                                        }
                                    }, getActivity());
                            break;
                    }
                }
            }
        }, 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsAddProductListPresenter.detachView();
    }

    @Override
    public boolean isExistingGroup() {
        return isExistingGroup;
    }

    @Override
    public void setLoadMoreFlag(boolean eof) {
        isEndOfFile = eof;
    }

    @Override
    public void resetEmptyViewHolder() {
        topAdsProductListAdapter.resetEmptyShown();
    }

    @Override
    public void showBottom() {
        // do nothing
    }

    public void removeSelection(TopAdsProductViewModel data) {
        selections.remove(data);
        updateSelectedProductCount();
    }

    public void addSelection(TopAdsProductViewModel data) {
        selections.add(data);
        updateSelectedProductCount();
    }

    private void updateSelectedProductCount() {
        int totalSelection = selections!=null? selections.size(): 0;
        if (totalSelection > 0) {
            containerCounterProduct.setVisibility(View.VISIBLE);
            if(totalSelection > 9) {
                counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product, totalSelection)));
            }else{
                counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product_zero, totalSelection)));
            }
            enableNextButton();
        } else {
            containerCounterProduct.setVisibility(View.GONE);
            counterProduct.setText(MethodChecker.fromHtml(getString(R.string.top_ads_label_total_selected_product_zero, totalSelection)));
            disableNextButton();
        }
    }

    public void disableNextButton() {
        buttonNext.setEnabled(false);
        buttonNext.setTextColor(ContextCompat.getColor(getActivity(), R.color.font_black_disabled_38));
    }

    public void enableNextButton() {
        buttonNext.setEnabled(true);
        buttonNext.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
    }
}
