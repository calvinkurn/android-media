package com.tokopedia.loyalty.view.fragment;

import android.app.Dialog;
import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoListFragmentComponent;
import com.tokopedia.loyalty.di.component.PromoListFragmentComponent;
import com.tokopedia.loyalty.di.module.PromoListFragmentModule;
import com.tokopedia.loyalty.view.activity.PromoDetailActivity;
import com.tokopedia.loyalty.view.adapter.PromoListAdapter;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.data.PromoSubMenuData;
import com.tokopedia.loyalty.view.listener.RecyclerViewScrollListener;
import com.tokopedia.loyalty.view.presenter.IPromoListPresenter;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;
import com.tokopedia.loyalty.view.view.IPromoListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoListFragment extends BaseDaggerFragment implements IPromoListView,
        PromoListAdapter.ActionListener, RefreshHandler.OnRefreshHandlerListener {
    private static final String ARG_EXTRA_PROMO_MENU_DATA = "ARG_EXTRA_PROMO_MENU_DATA";
    private static final String ARG_EXTRA_AUTO_SELECT_FILTER_CATEGORY_ID = "ARG_EXTRA_AUTO_SELECT_FILTER_CATEGORY_ID";

    private static final String EXTRA_STATE_PROMO_MENU_DATA = "EXTRA_STATE_PROMO_MENU_DATA";
    private static final String EXTRA_STATE_FILTER_SELECTED = "EXTRA_STATE_FILTER_SELECTED";

    private static final String FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_PROMO_LIST = "mp_promo_list";

    private static final int PROMO_DETAIL_REQUEST_CODE = 0;

    private static final String TYPE_FILTER_ALL = "all";
    QuickSingleFilterView quickSingleFilterView;
    RecyclerView rvPromoList;
    View containerList;

    @Inject
    IPromoListPresenter dPresenter;
    @Inject
    CompositeSubscription compositeSubscription;
    @Inject
    PromoTrackingUtil promoTrackingUtil;
    @Inject
    PerformanceMonitoring performanceMonitoring;

    private RefreshHandler refreshHandler;

    private OnFragmentInteractionListener actionListener;

    private PromoMenuData promoMenuData;
    private PromoListAdapter adapter;
    private BottomSheetView bottomSheetViewInfoPromoCode;
    private boolean isLoadMore;
    private String filterSelected = "";
    private RecyclerViewScrollListener recyclerViewScrollListener;


    private String autoSelectedCategoryId;

    @Override
    protected void initInjector() {
        PromoListFragmentComponent promoListComponent = DaggerPromoListFragmentComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivityContext().getApplicationContext()).getBaseAppComponent())
                .promoListFragmentModule(new PromoListFragmentModule(this))
                .build();
        promoListComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        performanceMonitoring.startTrace(FIREBASE_PERFORMANCE_MONITORING_TRACE_MP_PROMO_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_list, container, false);
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        rvPromoList = view.findViewById(R.id.rv_promo_list);
        containerList = view.findViewById(R.id.container_list);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialVar();
    }

    @Override
    public void renderPromoDataList(List<PromoData> promoDataList, boolean firstTimeLoad) {
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        View errorView = containerList.findViewById(R.id.main_retry);
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (firstTimeLoad) {
            adapter.addAllItems(promoDataList);
        } else {
            adapter.addAllItemsLoadMore(promoDataList);
        }
    }

    @Override
    public void renderNextPage(boolean hasNextPage) {
        this.isLoadMore = hasNextPage;
        adapter.setHasNextPage(hasNextPage);
    }

    @Override
    public void renderErrorGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderEmptyResultGetPromoDataList() {
        handleErrorEmptyState(getString(R.string.message_error_data_empty_get_promo_list));
    }

    @Override
    public void renderErrorHttpGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorLoadNextPage(String message, int actualPage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dPresenter.processGetPromoListLoadMore(filterSelected, promoMenuData.getTitle());
            }
        }).showRetrySnackbar();
    }

    @Override
    public void stopPerformanceMonitoring() {
        performanceMonitoring.stopTrace();
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDataListt(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(EXTRA_STATE_PROMO_MENU_DATA, promoMenuData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            promoMenuData = savedInstanceState.getParcelable(EXTRA_STATE_PROMO_MENU_DATA);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    protected void setupArguments(Bundle arguments) {
        this.promoMenuData = arguments.getParcelable(ARG_EXTRA_PROMO_MENU_DATA);
        this.autoSelectedCategoryId = arguments.getString(ARG_EXTRA_AUTO_SELECT_FILTER_CATEGORY_ID, "0");
    }

    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        adapter = new PromoListAdapter(new ArrayList<PromoData>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPromoList.setLayoutManager(layoutManager);
        recyclerViewScrollListener = new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isLoadMore) {
                    dPresenter.processGetPromoListLoadMore(filterSelected, promoMenuData.getTitle());
                    isLoadMore = false;
                }
            }
        };
        rvPromoList.addOnScrollListener(recyclerViewScrollListener);
        rvPromoList.setAdapter(adapter);
    }

    protected void initialVar() {
        int indexAutoSelectCategoryFilter = 0;

        final List<QuickFilterItem> quickFilterItemList = setQuickFilterItems(promoMenuData.getPromoSubMenuDataList());

        for (int i = 0; i < quickFilterItemList.size(); i++) {
            QuickFilterItem item = quickFilterItemList.get(i);
            if (autoSelectedCategoryId.equalsIgnoreCase(item.getType())) {
                indexAutoSelectCategoryFilter = i;
            }
        }

        quickSingleFilterView.renderFilter(quickFilterItemList);
        quickSingleFilterView.setDefaultItem(quickFilterItemList.get(indexAutoSelectCategoryFilter));
        quickSingleFilterView.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                String subCategoryName = getSubCategoryNameById(typeFilter);
                promoTrackingUtil.eventPromoListClickSubCategory(getActivity(), subCategoryName);

                actionListener.onChangeFilter(typeFilter);

                filterSelected = typeFilter.equals(TYPE_FILTER_ALL) ?
                        promoMenuData.getAllSubCategoryId() :
                        typeFilter;

                refreshHandler.startRefresh();
            }

            private String getSubCategoryNameById(String typeFilter) {
                for (QuickFilterItem item : quickFilterItemList) {
                    if (item.getType().equalsIgnoreCase(typeFilter)) return item.getName();
                }
                return "";
            }
        });

        quickSingleFilterView.actionSelect(indexAutoSelectCategoryFilter);
    }

    private List<QuickFilterItem> setQuickFilterItems(List<PromoSubMenuData> promoSubMenuDataList) {
        List<QuickFilterItem> quickFilterItemList = new ArrayList<>();

        for (int i = 0; i < promoSubMenuDataList.size(); i++) {
            QuickFilterItem quickFilterItem = new QuickFilterItem();
            quickFilterItem.setName(promoSubMenuDataList.get(i).getTitle());
            quickFilterItem.setType(promoSubMenuDataList.get(i).getId());
            quickFilterItem.setSelected(promoSubMenuDataList.get(i).isSelected());
            quickFilterItem.setColorBorder(R.color.tkpd_main_green);
            quickFilterItemList.add(quickFilterItem);
        }

        return quickFilterItemList;
    }


    public static Fragment newInstance(OnFragmentInteractionListener promoListActionListener,
                                       PromoMenuData promoMenuData, String autoSelectCategoryId) {
        PromoListFragment fragment = new PromoListFragment();
        fragment.setPromoListActionListener(promoListActionListener);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_PROMO_MENU_DATA, promoMenuData);
        bundle.putString(ARG_EXTRA_AUTO_SELECT_FILTER_CATEGORY_ID, autoSelectCategoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setPromoListActionListener(OnFragmentInteractionListener promoListActionListener) {
        this.actionListener = promoListActionListener;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemPromoCodeCopyClipboardClicked(String promoCode, String promoName) {
        promoTrackingUtil.eventPromoListClickCopyToClipboardPromoCode(getActivity(), promoName);
        ClipboardManager clipboard = (ClipboardManager)
                getActivityContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                "CLIP_DATA_LABEL_VOUCHER_PROMO", promoCode
        );
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        showToastMessage("Kode Voucher telah tersalin");
    }

    @Override
    public void onItemPromoClicked(PromoData promoData, int position) {
        Intent intent = PromoDetailActivity.getCallingIntent(getActivity(), promoData, position,
                dPresenter.getPage());
        startActivityForResult(intent, PROMO_DETAIL_REQUEST_CODE);

        dPresenter.sendClickItemPromoListTrackingData(promoData, position, promoMenuData.getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onItemPromoCodeTooltipClicked() {
        promoTrackingUtil.eventPromoTooltipClickOpenTooltip(getActivity());
        if (bottomSheetViewInfoPromoCode == null) {
            bottomSheetViewInfoPromoCode = new BottomSheetView(getActivity());
            bottomSheetViewInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle("Kode Promo")
                    .setBody("Masukan Kode Promo di halaman pembayaran")
                    .setImg(R.drawable.ic_promo)
                    .build());

            bottomSheetViewInfoPromoCode.setBtnCloseOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promoTrackingUtil.eventPromoTooltipClickCloseTooltip(getActivity());
                    bottomSheetViewInfoPromoCode.dismiss();
                }
            });
        }

        bottomSheetViewInfoPromoCode.show();
    }

    private void handleErrorEmptyState(String message) {
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        adapter.clearDataList();
        NetworkErrorHelper.showEmptyState(
                getActivity(), containerList, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    @Override
    public void onRefresh(View view) {
        recyclerViewScrollListener.resetState();
        dPresenter.setPage(1);
        dPresenter.processGetPromoList(filterSelected, promoMenuData.getTitle());
    }

    public interface OnFragmentInteractionListener {

        void onChangeFilter(String categoryId);

    }
}
