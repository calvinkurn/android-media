package com.tokopedia.withdraw.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.di.DaggerDepositWithdrawComponent;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.adapter.BankAdapter;
import com.tokopedia.withdraw.view.decoration.SpaceItemDecoration;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPresenter;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WithdrawFragment extends BaseDaggerFragment implements WithdrawContract.View{

    private TextView wrapperTotalWithdrawal;
    private CloseableBottomSheetDialog infoDialog;
    RecyclerView bankRecyclerView;
    private View withdrawButton;
    private View withdrawAll;
    private TextView withdrawError;
    private BankAdapter bankAdapter;
    private Snackbar snackBarError;
    private EditText totalBalance;
    private EditText totalWithdrawal;
    private View loadingLayout;
    private CurrencyTextWatcher currencyTextWatcher;

    public static final String BUNDLE_TOTAL_BALANCE = "total_balance";
    public static final String BUNDLE_TOTAL_BALANCE_INT = "total_balance_int";
    private static final String DEFAULT_TOTAL_BALANCE = "Rp.0,-";
    private View info;
    private List<BankAccountViewModel> listBank;


    @Override
    protected String getScreenName() {
        return null;
    }
    
    @Inject
    WithdrawPresenter presenter;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        DaggerDepositWithdrawComponent.builder().withdrawComponent(withdrawComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new WithdrawFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        
        wrapperTotalWithdrawal = view.findViewById(R.id.wrapper_total_withdrawal);

        infoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        infoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        View infoDialogView = getLayoutInflater().inflate(R.layout.layout_withdrawal_info, null);
        infoDialog.setContentView(infoDialogView, getActivity().getString(R.string.withdrawal_info));
        infoDialogView.setOnClickListener(null);

        bankRecyclerView = view.findViewById(R.id.recycler_view_bank);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        withdrawAll = view.findViewById(R.id.withdraw_all);
        totalBalance = view.findViewById(R.id.total_balance);
        totalWithdrawal = view.findViewById(R.id.total_withdrawal);
        withdrawError = view.findViewById(R.id.total_withdrawal_error);
        loadingLayout = view.findViewById(R.id.loading_layout);
        info = view.findViewById(R.id.info_container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.dp_8));
        bankRecyclerView.addItemDecoration(itemDecoration);

        totalBalance.setText(getArguments().getString(BUNDLE_TOTAL_BALANCE, DEFAULT_TOTAL_BALANCE));

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
//                presenter.onConfirmClicked(bankDialogadapter.hasSelectedItem());
            }
        });

        withdrawAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalWithdrawal.setText(getArguments().getString(BUNDLE_TOTAL_BALANCE_INT, DEFAULT_TOTAL_BALANCE));
            }
        });

        currencyTextWatcher = new CurrencyTextWatcher(totalWithdrawal, CurrencyEnum.RPwithSpace);
        currencyTextWatcher.setDefaultValue("");

        if (currencyTextWatcher != null) {
            totalWithdrawal.removeTextChangedListener(currencyTextWatcher);
        }

        totalWithdrawal.addTextChangedListener(currencyTextWatcher);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.show();
            }
        });

//        snackBarError = ToasterError.make(view.findViewById(android.R.id.content),
//                "", BaseToaster.LENGTH_LONG)
//                .setAction(getActivity().getString(R.string.title_close), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        snackBarError.dismiss();
//                    }
//                });

        presenter.getWithdrawForm();

        listBank = new ArrayList<>();
        bankAdapter = BankAdapter.createAdapter(getActivity(), listBank);
        bankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bankRecyclerView.setAdapter(bankAdapter);
        bankAdapter.setList(listBank);

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessGetWithdrawForm(List<BankAccountViewModel> bankAccount) {
        bankAdapter.setList(bankAccount);
    }

    @Override
    public void showError(String throwable) {

    }
}
