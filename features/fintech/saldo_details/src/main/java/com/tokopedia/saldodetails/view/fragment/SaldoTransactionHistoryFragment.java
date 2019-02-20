package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private RelativeLayout startDateLayout;
    private RelativeLayout endDateLayout;
    private View tabSeparator;
    private HeightWrappingViewPager depositHistoryViewPager;
    private TabLayout depositHistoryTabLayout;

    private TextView startDateTV;
    private TextView endDateTV;

    @Inject
    SaldoHistoryPresenter saldoHistoryPresenter;
    @Inject
    UserSession userSession;

    private SaldoDatePickerUtil datePicker;
    protected Bundle savedState;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getInitialdata();
    }

    private void getInitialdata() {
        setActionsEnabled(false);
        saldoHistoryPresenter.setFirstDateParameter();
        saldoHistoryPresenter.getSummaryDeposit();
    }

    private void initViews(View view) {
        startDateLayout = view.findViewById(R.id.start_date_layout);
        endDateLayout = view.findViewById(R.id.end_date_layout);
        startDateTV = view.findViewById(R.id.start_date_tv);
        endDateTV = view.findViewById(R.id.end_date_tv);


        depositHistoryViewPager = view.findViewById(R.id.transaction_history_view_pager);
        depositHistoryTabLayout = view.findViewById(R.id.transaction_history_tab_layout);
        tabSeparator = view.findViewById(R.id.transaction_history_tab_view_separator);
    }

    private void initialVar() {

        isSeller = userSession.hasShop() || userSession.isAffiliate();
        if (isSeller) {
            loadMultipleTabItem();
        } else {
            loadOneTabItem();
        }

        saldoHistoryPresenter.setSeller(isSeller);
        SaldoHistoryPagerAdapter saldoHistoryPagerAdapter = new SaldoHistoryPagerAdapter(getChildFragmentManager());
        saldoHistoryPagerAdapter.setItems(saldoTabItems);
        depositHistoryViewPager.setOffscreenPageLimit(2);
        depositHistoryViewPager.setAdapter(saldoHistoryPagerAdapter);
        depositHistoryTabLayout.setupWithViewPager(depositHistoryViewPager);

        datePicker = new SaldoDatePickerUtil(getActivity());
    }

    private void loadOneTabItem() {
        saldoTabItems.clear();
        singleTabItem = new SaldoHistoryTabItem();
        singleTabItem.setTitle("");
        singleTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_BUYER, saldoHistoryPresenter));
        saldoTabItems.add(singleTabItem);
        depositHistoryTabLayout.setVisibility(View.GONE);
        tabSeparator.setVisibility(View.GONE);
    }

    private void loadMultipleTabItem() {

        saldoTabItems.clear();

        allSaldoHistoryTabItem = new SaldoHistoryTabItem();
        allSaldoHistoryTabItem.setTitle("Semua");
        allSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_ALL, saldoHistoryPresenter));

        saldoTabItems.add(allSaldoHistoryTabItem);

        buyerSaldoHistoryTabItem = new SaldoHistoryTabItem();
        buyerSaldoHistoryTabItem.setTitle("Refund");
        buyerSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_BUYER, saldoHistoryPresenter));

        saldoTabItems.add(buyerSaldoHistoryTabItem);

        sellerSaldoHistoryTabItem = new SaldoHistoryTabItem();
        sellerSaldoHistoryTabItem.setTitle("Penghasilan");
        sellerSaldoHistoryTabItem.setFragment(SaldoHistoryListFragment.createInstance(FOR_SELLER, saldoHistoryPresenter));

        saldoTabItems.add(sellerSaldoHistoryTabItem);

        depositHistoryTabLayout.setVisibility(View.VISIBLE);
        tabSeparator.setVisibility(View.VISIBLE);

    }

    private void initListeners() {

        depositHistoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (activePosition != position) {
                    activePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (activePosition != position) {
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
        return new EmptyModel();
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

    @Override
    public SaldoDepositAdapter getSingleTabAdapter() {
        if (singleTabItem != null) {
            return ((SaldoHistoryListFragment) singleTabItem.getFragment()).getAdapter();
        } else {
            return createNewAdapter();
        }
    }

    @Override
    public SaldoHistoryTabItem getSingleHistoryTabItem() {
        return singleTabItem;
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
    public SaldoHistoryTabItem getAllSaldoHistoryTabItem() {
        return allSaldoHistoryTabItem;
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
    public SaldoHistoryTabItem getBuyerSaldoHistoryTabItem() {
        return buyerSaldoHistoryTabItem;
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

    @Override
    public SaldoHistoryTabItem getSellerSaldoHistoryTabItem() {
        return sellerSaldoHistoryTabItem;
    }

    private SaldoDepositAdapter createNewAdapter() {
        return new SaldoDepositAdapter(new SaldoDetailTransactionFactory());
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

        return new SaldoDepositAdapter(new SaldoDetailTransactionFactory());
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
