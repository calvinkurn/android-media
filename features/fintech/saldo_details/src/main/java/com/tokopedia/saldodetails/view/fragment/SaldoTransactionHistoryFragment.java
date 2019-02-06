package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.adapter.SaldoHistoryPagerAdapter;
import com.tokopedia.saldodetails.contract.SaldoHistoryContract;
import com.tokopedia.saldodetails.di.SaldoDetailsComponent;
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance;
import com.tokopedia.saldodetails.presentation.listener.SaldoItemListener;
import com.tokopedia.saldodetails.presenter.SaldoHistoryPresenter;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;
import com.tokopedia.saldodetails.view.ui.HeightWrappingViewPager;
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.IS_SELLER_ENABLED;

public class SaldoTransactionHistoryFragment extends BaseDaggerFragment implements
        SaldoHistoryContract.View, EmptyResultViewHolder.Callback {

    public static final String TRANSACTION_TYPE = "type";
    private Context context;
    public static final String FOR_SELLER = "for_seller";
    public static final String FOR_BUYER = "for_buyer";
    public static final String FOR_ALL = "for_all";

    private boolean buyerOnlyTranxn = false;
    private boolean sellerOnlyTranxn = false;
    private boolean allTranxn = false;

    private RelativeLayout startDateLayout;
    private RelativeLayout endDateLayout;
    private View tabSeparator;
    private HeightWrappingViewPager depositHistoryViewPager;
    private TabLayout depositHistoryTabLayout;

    private LinearLayout dateSelectorLL;

    private TextView startDateTV;
    private TextView endDateTV;

    @Inject
    SaldoHistoryPresenter saldoHistoryPresenter;
    @Inject
    UserSession userSession;

    //    private SaldoDepositAdapter adapter;
    private SaldoDatePickerUtil datePicker;
    //    private LinearLayoutManager linearLayoutManager;
    protected Bundle savedState;

    private SaldoHistoryPagerAdapter saldoHistoryPagerAdapter;
    private boolean isSeller;
    private List<SaldoHistoryTabItem> saldoTabItems = new ArrayList<>();
    private SaldoHistoryTabItem singleTabItem;
    private int activePosition = -1;
    private SaldoHistoryTabItem allSaldoHistoryTabItem;
    private SaldoHistoryTabItem buyerSaldoHistoryTabItem;
    private SaldoHistoryTabItem sellerSaldoHistoryTabItem;

    public static SaldoTransactionHistoryFragment createInstance() {
        SaldoTransactionHistoryFragment saldoDepositFragment = new SaldoTransactionHistoryFragment();
        Bundle bundle = new Bundle();
        saldoDepositFragment.setArguments(bundle);
        return saldoDepositFragment;
    }

    public static SaldoTransactionHistoryFragment createInstance(String type, boolean isSellerEnabled) {
        SaldoTransactionHistoryFragment saldoDepositFragment = new SaldoTransactionHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TRANSACTION_TYPE, type);
        bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled);
        saldoDepositFragment.setArguments(bundle);
        return saldoDepositFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_history, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialVar();
        initListeners();
    }

    private void onFirstTimeLaunched() {
        setActionsEnabled(false);
        saldoHistoryPresenter.setFirstDateParameter();
        saldoHistoryPresenter.getSummaryDeposit();
    }

    /*private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        return savedState != null;
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }*/
        onFirstTimeLaunched();
    }


    private void initViews(View view) {
        startDateLayout = view.findViewById(R.id.start_date_layout);
        endDateLayout = view.findViewById(R.id.end_date_layout);
        startDateTV = view.findViewById(R.id.start_date_tv);
        endDateTV = view.findViewById(R.id.end_date_tv);


        depositHistoryViewPager = view.findViewById(R.id.transaction_history_view_pager);
        depositHistoryTabLayout = view.findViewById(R.id.transaction_history_tab_layout);
        tabSeparator = view.findViewById(R.id.transaction_history_tab_view_separator);
//        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void initialVar() {

        if (getArguments() != null) {
            sellerOnlyTranxn = FOR_SELLER.equalsIgnoreCase(getArguments().getString(TRANSACTION_TYPE));
            buyerOnlyTranxn = FOR_BUYER.equalsIgnoreCase(getArguments().getString(TRANSACTION_TYPE));
            allTranxn = FOR_ALL.equalsIgnoreCase(getArguments().getString(TRANSACTION_TYPE));
        }

        isSeller = !TextUtils.isEmpty(userSession.getShopId());
        if (isSeller) {
            loadMultipleTabItem();
        } else {
            loadOneTabItem();
        }

        saldoHistoryPresenter.setSeller(isSeller);
        saldoHistoryPagerAdapter = new SaldoHistoryPagerAdapter(getChildFragmentManager());
        saldoHistoryPagerAdapter.setItems(saldoTabItems);
        depositHistoryViewPager.setOffscreenPageLimit(2);
        depositHistoryViewPager.setAdapter(saldoHistoryPagerAdapter);
        depositHistoryTabLayout.setupWithViewPager(depositHistoryViewPager);

        datePicker = new SaldoDatePickerUtil(getActivity());
//        adapter = new SaldoDepositAdapter(new SaldoDetailTransactionFactory(this));
//        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    private void loadOneTabItem() {
        saldoTabItems.clear();
        singleTabItem = new SaldoHistoryTabItem();
        singleTabItem.setTitle("");
        singleTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_ALL, isSellerEnabled()));
        saldoTabItems.add(singleTabItem);
        depositHistoryTabLayout.setVisibility(View.GONE);
        tabSeparator.setVisibility(View.GONE);
    }

    private void loadMultipleTabItem() {

        saldoTabItems.clear();

        allSaldoHistoryTabItem = new SaldoHistoryTabItem();
        allSaldoHistoryTabItem.setTitle("Semua");
        allSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_ALL, isSellerEnabled()));

        saldoTabItems.add(allSaldoHistoryTabItem);

        buyerSaldoHistoryTabItem = new SaldoHistoryTabItem();
        buyerSaldoHistoryTabItem.setTitle("Refund");
        buyerSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_BUYER, isSellerEnabled()));

        saldoTabItems.add(buyerSaldoHistoryTabItem);

        sellerSaldoHistoryTabItem = new SaldoHistoryTabItem();
        sellerSaldoHistoryTabItem.setTitle("Penghasilan");
        sellerSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_SELLER, isSellerEnabled()));

        saldoTabItems.add(sellerSaldoHistoryTabItem);

        depositHistoryTabLayout.setVisibility(View.VISIBLE);
        tabSeparator.setVisibility(View.VISIBLE);

    }

    private void initListeners() {

        depositHistoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (activePosition != position) {
                    onRefresh();
                    activePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (activePosition != position) {
                    onRefresh();
                    activePosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startDateLayout.setOnClickListener(onStartDateClicked());
        endDateLayout.setOnClickListener(onEndDateClicked());

    }


    private View.OnClickListener onEndDateClicked() {
        return v -> saldoHistoryPresenter.onEndDateClicked(datePicker);
    }

    private View.OnClickListener onStartDateClicked() {
        return v -> saldoHistoryPresenter.onStartDateClicked(datePicker);
    }


    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        Intent intent = ((SaldoDetailsRouter) getActivity().getApplication())
                .getHomeIntent(context);
        startActivity(intent);
    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {
        if (!isAdded() || startDateTV == null || endDateTV == null) {
            return;
        }
        startDateLayout.setEnabled(isEnabled);
        endDateLayout.setEnabled(isEnabled);
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void showEmptyState() {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), () -> saldoHistoryPresenter.getSummaryDeposit());
        try {
            View retryLoad = getView().findViewById(R.id.main_retry);
//            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRetry() {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), () -> saldoHistoryPresenter.getSummaryDeposit()).showRetrySnackbar();
    }

    @Override
    public void showEmptyState(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error,
                () -> saldoHistoryPresenter.getSummaryDeposit());
        try {
            View retryLoad = getView().findViewById(R.id.main_retry);
//            retryLoad.setTranslationY(topSlideOffBar.getHeight() / 2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRetry(String error) {
        setActionsEnabled(false);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error,
                () -> saldoHistoryPresenter.getSummaryDeposit()).showRetrySnackbar();
    }

    @Override
    public boolean isSellerEnabled() {
        return isSeller;
    }

    @Override
    public void setStartDate(String date) {
        startDateTV.setText(date);
    }

    @Override
    public void setEndDate(String date) {
        endDateTV.setText(date);
    }

    @Override
    public String getStartDate() {
        return startDateTV.getText().toString();
    }

    @Override
    public String getEndDate() {
        return endDateTV.getText().toString();
    }

    @Override
    public Visitable getDefaultEmptyViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.sp_empty_state_icon);
        emptyModel.setTitle(getString(R.string.no_saldo_transactions));
        emptyModel.setButtonTitle(getString(R.string.sp_goto_home));
        emptyModel.setCallback(this);
        return emptyModel;
    }

    @Override
    public void finishLoading() {
        if (getSingleTabAdapter() != null) {
            getSingleTabAdapter().hideLoading();
        }

        if (getAllHistoryAdapter() != null) {
            getAllHistoryAdapter().hideLoading();
        }

        if (getBuyerHistoryAdapter() != null) {
            getBuyerHistoryAdapter().hideLoading();
        }

        if (getSellerHistoryAdapter() != null) {
            getSellerHistoryAdapter().hideLoading();
        }
    }

    @Override
    public void showErrorMessage(String s) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), s);
    }

    @Override
    public void showInvalidDateError(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void removeError() {
        if (getSingleTabAdapter() != null) {
            getSingleTabAdapter().removeErrorNetwork();
        }

        if (getAllHistoryAdapter() != null) {
            getAllHistoryAdapter().removeErrorNetwork();
        }

        if (getBuyerHistoryAdapter() != null) {
            getBuyerHistoryAdapter().removeErrorNetwork();
        }

        if (getSellerHistoryAdapter() != null) {
            getSellerHistoryAdapter().removeErrorNetwork();
        }

    }

    /*@Override
    public void loadData(int page) {

    }

    @Override
    protected SaldoDetailTransactionFactory getAdapterTypeFactory() {
        return new SaldoDetailTransactionFactory(this);
    }

    @Override
    public void onItemClicked(DepositHistoryList depositHistoryList) {

    }*/

    @Override
    public SaldoDepositAdapter getSingleTabAdapter() {
        if (singleTabItem != null) {
            return ((SaldoHistoryListFragment) singleTabItem.getFragment()).getAdapter();
        } else {
            return createNewAdapter();
        }
    }

    @Override
    public SaldoDepositAdapter getAllHistoryAdapter() {
        if (allSaldoHistoryTabItem != null) {
            return ((SaldoHistoryListFragment) allSaldoHistoryTabItem.getFragment()).getAdapter();
        } else {
            return createNewAdapter();
        }
    }


    @Override
    public SaldoDepositAdapter getBuyerHistoryAdapter() {
        if (buyerSaldoHistoryTabItem != null) {
            return ((SaldoHistoryListFragment) buyerSaldoHistoryTabItem.getFragment()).getAdapter();
        } else {
            return createNewAdapter();
        }
    }

    @Override
    public SaldoDepositAdapter getSellerHistoryAdapter() {

        if (sellerSaldoHistoryTabItem != null) {
            return ((SaldoHistoryListFragment) sellerSaldoHistoryTabItem.getFragment()).getAdapter() == null ?
                    createNewAdapter() : ((SaldoHistoryListFragment) sellerSaldoHistoryTabItem.getFragment()).getAdapter();
        } else {
            return createNewAdapter();
        }
    }

    private SaldoDepositAdapter createNewAdapter() {
        return new SaldoDepositAdapter(new SaldoDetailTransactionFactory(new SaldoItemListener() {
            @Override
            public void setTextColor(View view, int colorId) {

            }
        }));
    }

    @Override
    public SaldoDepositAdapter getAdapter() {
        if (!isSellerEnabled()) {
            return ((SaldoHistoryListFragment) singleTabItem.getFragment()).getAdapter();
        } else if (activePosition == 0) {
            return ((SaldoHistoryListFragment) allSaldoHistoryTabItem.getFragment()).getAdapter();
        } else if (activePosition == 1) {
            return ((SaldoHistoryListFragment) buyerSaldoHistoryTabItem.getFragment()).getAdapter();
        } else if (activePosition == 2) {
            return ((SaldoHistoryListFragment) sellerSaldoHistoryTabItem.getFragment()).getAdapter();
        }

        return new SaldoDepositAdapter(new SaldoDetailTransactionFactory(new SaldoItemListener() {
            @Override
            public void setTextColor(View view, int colorId) {

            }
        }));
    }

    @Override
    public void onDestroy() {
        saldoHistoryPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected void initInjector() {
        SaldoDetailsComponent saldoDetailsComponent =
                SaldoDetailsComponentInstance.getComponent(getActivity().getApplication());
        saldoDetailsComponent.inject(this);
        saldoHistoryPresenter.attachView(this);
    }

    public void onRefresh() {
        saldoHistoryPresenter.onRefresh();
    }
}
