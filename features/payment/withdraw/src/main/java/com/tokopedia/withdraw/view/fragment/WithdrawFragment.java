package com.tokopedia.withdraw.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.EventsWatcher;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.PropertiesEventsWatcher;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity;
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.WithdrawRouter;
import com.tokopedia.withdraw.di.DaggerDepositWithdrawComponent;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.adapter.BankAdapter;
import com.tokopedia.withdraw.view.decoration.SpaceItemDecoration;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPresenter;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class WithdrawFragment extends BaseDaggerFragment implements WithdrawContract.View {

    private static final int BANK_INTENT = 34275;
    private static final int CONFIRM_PASSWORD_INTENT = 5964;
    private static final int BANK_SETTING_INTENT = 1324;
    private TkpdHintTextInputLayout wrapperTotalWithdrawal;
    private CloseableBottomSheetDialog infoDialog;
    RecyclerView bankRecyclerView;
    private View withdrawButton;
    private View withdrawAll;
    private BankAdapter bankAdapter;
    private Snackbar snackBarInfo;
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
    private BottomSheetDialog confirmPassword;
    private Observable<String> nominalObservable;
    private List<String> bankWithMinimumWithdrawal;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Inject
    WithdrawPresenter presenter;

    @Inject
    WithdrawAnalytics analytics;

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
        View view = inflater.inflate(R.layout.fragment_withdraw_layout, container, false);

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

        confirmPassword = new BottomSheetDialog(getActivity());
        View confirmPasswordView = getLayoutInflater().inflate(R.layout.layout_confirm_password, null);
        confirmPassword.setContentView(confirmPasswordView);

        bankRecyclerView = view.findViewById(R.id.recycler_view_bank);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        withdrawAll = view.findViewById(R.id.withdraw_all);
        totalBalance = view.findViewById(R.id.total_balance);
        totalWithdrawal = view.findViewById(R.id.total_withdrawal);
        loadingLayout = view.findViewById(R.id.loading_layout);
        info = view.findViewById(R.id.info_container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listBank = new ArrayList<>();
        bankAdapter = BankAdapter.createAdapter(this, listBank, analytics);
        bankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bankRecyclerView.setAdapter(bankAdapter);
        bankAdapter.setList(listBank);


        bankWithMinimumWithdrawal = Arrays.asList("bca", "bri", "mandiri", "bni", "bank central asia", "bank negara indonesia",
                "bank rakyat indonesia");

        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.dp_8)
                , MethodChecker.getDrawable(getActivity(), R.drawable.divider));
        bankRecyclerView.addItemDecoration(itemDecoration);

        totalBalance.setText(getArguments().getString(BUNDLE_TOTAL_BALANCE, DEFAULT_TOTAL_BALANCE));

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.doWithdraw(
                        totalBalance.getText().toString(),
                        totalWithdrawal.getText().toString(),
                        bankAdapter.getSelectedBank()
                );
            }
        });

        withdrawAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventClickWithdrawalAll();
                totalWithdrawal.setText(getArguments().getString(BUNDLE_TOTAL_BALANCE_INT, DEFAULT_TOTAL_BALANCE));
            }
        });

        currencyTextWatcher = new CurrencyTextWatcher(totalWithdrawal, CurrencyEnum.RPwithSpace);
        currencyTextWatcher.setDefaultValue("");

        if (currencyTextWatcher != null) {
            totalWithdrawal.removeTextChangedListener(currencyTextWatcher);
        }


        totalWithdrawal.addTextChangedListener(currencyTextWatcher);
        totalWithdrawal.setText("");

        totalWithdrawal.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int withdrawal = (int) StringUtils.convertToNumeric(s.toString(), false);
                checkMinimumWithdrawal(withdrawal);
                checkDepositIsSufficient(withdrawal);
                if (withdrawal == 0) {
                    totalWithdrawal.setSelection(totalWithdrawal.length());
                }
            }
        });

        nominalObservable = EventsWatcher.text(totalWithdrawal);

        Observable<Boolean> nominalMapper = nominalObservable.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String text) {
                int withdrawal = (int) StringUtils.convertToNumeric(text, false);
                int min = checkSelectedBankMinimumWithdrawal();
                int deposit = (int) StringUtils.convertToNumeric(totalBalance.getText().toString(), false);
                return (withdrawal > 0) && (withdrawal >= min) && (withdrawal <= deposit);
            }
        });

        Observable<Boolean> allField = nominalMapper.map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean isValidNominal) {
                 return isValidNominal && isBankSelected();
            }
        });

        allField.subscribe(PropertiesEventsWatcher.enabledFrom(withdrawButton));
        allField.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                canProceed((TextView) withdrawButton, aBoolean);
            }
        });


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.show();
                analytics.eventClickInformasiPenarikanSaldo();
            }
        });

        snackBarError = ToasterError.make(getActivity().findViewById(android.R.id.content),
                "", BaseToaster.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.title_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        analytics.eventClickCloseErrorMessage();
                        snackBarError.dismiss();
                    }
                });

        snackBarInfo = ToasterNormal.make(getActivity().findViewById(android.R.id.content),
                "", BaseToaster.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.title_close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBarError.dismiss();
                    }
                });

        presenter.getWithdrawForm();

    }

    private void checkDepositIsSufficient(int withdrawal) {
        int deposit = (int) StringUtils.convertToNumeric(totalBalance.getText().toString(), false);
        if(withdrawal > deposit){
            showErrorWithdrawal(getStringResource(R.string.error_withdraw_exceed_balance));
        }
    }

    private boolean checkMinimumWithdrawal(int withdrawal) {
        if(withdrawal == 0){
            showErrorWithdrawal(null);
            return false;
        }
        int min = checkSelectedBankMinimumWithdrawal();
        if (withdrawal < min) {
            showErrorWithdrawal(getActivity().getString(R.string.minimal_withdrawal, String.valueOf(min)));
            return false;
        } else {
            showErrorWithdrawal(null);
            return true;
        }
    }

    private int checkSelectedBankMinimumWithdrawal() {
        BankAccountViewModel selectedBank = bankAdapter.getSelectedBank();
        if(selectedBank == null){
            return 0;
        }
        String selectedBankName = selectedBank.getBankName().toLowerCase();
        if (inBankGroup(selectedBankName)) {
            return 10000;
        } else {
            return 50000;
        }
    }

    private boolean inBankGroup(String selectedBankName) {
        for (String s : bankWithMinimumWithdrawal) {
            if(selectedBankName.contains(s)){
                return true;
            }
        }
        return false;
    }

    private boolean isBankSelected() {
        if(bankAdapter == null){
            return false;
        }
        BankAccountViewModel bankAccountViewModel = bankAdapter.getSelectedBank();
        return (!TextUtils.isEmpty(bankAccountViewModel.getBankName()));
    }


    public void canProceed(TextView textView, boolean can) {
        if (can) {
            textView.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.medium_green), PorterDuff.Mode.SRC_IN);
            textView.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        } else {
            textView.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.grey_300), PorterDuff.Mode.SRC_IN);
            textView.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        }
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
    public void onSuccessGetWithdrawForm(List<BankAccountViewModel> bankAccount, int defaultBank, boolean verifiedAccount) {
        bankAdapter.setList(bankAccount);
        bankAdapter.setDefault(defaultBank);
        if(!verifiedAccount){
            showMustVerify();
        }
    }

    private void showMustVerify() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.alert_not_verified_yet_title))
                .setMessage(getActivity().getString(R.string.alert_not_verified_yet_body))
                .setPositiveButton(getActivity().getString(R.string.alert_not_verified_yet_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ((WithdrawRouter) getActivity().getApplicationContext())
                                .getProfileSettingIntent(getActivity());
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(getActivity().getString(R.string.alert_not_verified_yet_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setCancelable(false)

                .show();
    }

    @Override
    public void showError(String error) {
        snackBarError.setText(error);
        snackBarError.show();
    }

    @Override
    public String getStringResource(int id) {
        return getActivity().getString(id);
    }

    @Override
    public void showErrorWithdrawal(String stringResource) {
        if (TextUtils.isEmpty(stringResource)) {
            wrapperTotalWithdrawal.setError(null);
            return;
        }

        wrapperTotalWithdrawal.setError(stringResource);
    }

    @Override
    public void resetView() {
        wrapperTotalWithdrawal.setError(null);
    }

    @Override
    public void showConfirmPassword() {
        analytics.eventClickWithdrawal();
        Intent intent = new Intent(getActivity(), WithdrawPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WithdrawPasswordActivity.BUNDLE_WITHDRAW, totalWithdrawal.getText().toString());
        bundle.putParcelable(WithdrawPasswordActivity.BUNDLE_BANK, bankAdapter.getSelectedBank());
        intent.putExtras(bundle);
        startActivityForResult(intent, CONFIRM_PASSWORD_INTENT);
    }

    @Override
    public void goToAddBank() {
        Intent intent = new Intent(getActivity(), AddEditBankActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, BANK_INTENT);
    }

    @Override
    public void goToSettingBank() {
        Intent intent = new Intent(getActivity(), SettingBankActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, BANK_SETTING_INTENT);
    }

    @Override
    public void itemSelected() {
        String withdrawalString = totalWithdrawal.getText().toString();
        int withdrawal = (int) StringUtils.convertToNumeric(withdrawalString, false);
        boolean isValid = checkMinimumWithdrawal(withdrawal);
        canProceed((TextView) withdrawButton, isValid);
        withdrawButton.setEnabled(isValid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BANK_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    BankFormModel parcelable = data.getExtras().getParcelable(AddEditBankActivity.PARAM_DATA);
                    BankAccountViewModel model = new BankAccountViewModel();
                    model.setBankId(Integer.parseInt(parcelable.getBankId()));
                    model.setBankName(parcelable.getBankName());
                    model.setBankAccountId(parcelable.getAccountId());
                    model.setBankAccountName(parcelable.getAccountName());
                    model.setBankAccountNumber(parcelable.getAccountNumber());
                    bankAdapter.addItem(model);
                    bankAdapter.changeItemSelected(listBank.size()-2);
                    itemSelected();
                    snackBarInfo.setText(R.string.success_add_bank);
                    snackBarInfo.show();
                }
                break;
            case BANK_SETTING_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshBankList();
                }
                break;
            case CONFIRM_PASSWORD_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getString(R.string.alert_success_withdraw_title))
                            .setMessage(getActivity().getString(R.string.alert_success_withdraw_body))
                            .setPositiveButton(getActivity().getString(R.string.alert_success_withdraw_positive), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                break;
            default:
                break;
        }
    }
}
