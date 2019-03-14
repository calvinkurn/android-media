package com.tokopedia.withdraw.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
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
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity;
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.WithdrawRouter;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.adapter.BankAdapter;
import com.tokopedia.withdraw.view.decoration.SpaceItemDecoration;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPresenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.abstraction.common.utils.GraphqlHelper.streamToString;

public class WithdrawFragment extends BaseDaggerFragment implements WithdrawContract.View {

    private static final int BANK_INTENT = 34275;
    private static final int CONFIRM_PASSWORD_INTENT = 5964;
    private static final int BANK_SETTING_INTENT = 1324;
    private static final int DEFAULT_MIN_FOR_SELECTED_BANK = 10000;
    private static final int DEFAULT_SALDO_MIN = 50000;
    private static final long SHOW_CASE_DELAY = 500;

    private int SELLER_STATE = 2;
    private int BUYER_STATE = 1;
    private TkpdHintTextInputLayout wrapperTotalWithdrawal;
    RecyclerView bankRecyclerView;
    private View withdrawButton;
    private View withdrawAll;
    private BankAdapter bankAdapter;
    private Snackbar snackBarInfo;
    private Snackbar snackBarError;
    private EditText totalWithdrawal;
    private View loadingLayout;

    private static final String BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int";
    private static final String BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int";
    private List<BankAccount> listBank;
    private List<String> bankWithMinimumWithdrawal;

    private TextView withdrawBuyerSaldoTV;
    private TextView withdrawSellerSaldoTV;

    private float buyerSaldoBalance;
    private float sellerSaldoBalance;
    private int currentState = 0;
    private boolean isSeller = false;
    private boolean sellerWithdrawal;
    private TextView saldoTitleTV;
    private CardView saldoTypeCV;
    private TextView saldoValueTV;
    private TextView saldoWithdrawHintTV;


    @Override
    protected String getScreenName() {
        return null;
    }

    @Inject
    WithdrawPresenter presenter;

    @Inject
    WithdrawAnalytics analytics;


    @Inject
    UserSession userSession;

    @Override
    protected void initInjector() {
        WithdrawComponent withdrawComponent = DaggerWithdrawComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        withdrawComponent.inject(this);

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
        CloseableBottomSheetDialog saldoWithdrawInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        saldoWithdrawInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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

        BottomSheetDialog confirmPassword = new BottomSheetDialog(getActivity());
        View confirmPasswordView = getLayoutInflater().inflate(R.layout.layout_confirm_password, null);
        confirmPassword.setContentView(confirmPasswordView);

        bankRecyclerView = view.findViewById(R.id.recycler_view_bank);
        withdrawButton = view.findViewById(R.id.withdraw_button);
        withdrawAll = view.findViewById(R.id.withdrawal_all_tv);
        totalWithdrawal = view.findViewById(R.id.total_withdrawal);
        loadingLayout = view.findViewById(R.id.loading_layout);

        withdrawBuyerSaldoTV = view.findViewById(R.id.withdraw_refund_saldo);
        withdrawSellerSaldoTV = view.findViewById(R.id.withdraw_seller_saldo);

        saldoTitleTV = view.findViewById(R.id.saldo_title);
        saldoTypeCV = view.findViewById(R.id.saldo_type_card_view);
        saldoValueTV = view.findViewById(R.id.total_saldo_value);
        saldoWithdrawHintTV = view.findViewById(R.id.saldo_withdraw_hint);

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

        if (getArguments() != null) {
            buyerSaldoBalance = getArguments().getFloat(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT);
            sellerSaldoBalance = getArguments().getFloat(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT);
        }

        saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(buyerSaldoBalance, false));

        saldoTypeCV.setVisibility(View.VISIBLE);
        new Handler().postDelayed(this::startShowCase, SHOW_CASE_DELAY);

        bankWithMinimumWithdrawal = Arrays.asList("bca", "bri", "mandiri", "bni", "bank central asia", "bank negara indonesia",
                "bank rakyat indonesia");

        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.dp_8)
                , MethodChecker.getDrawable(getActivity(), R.drawable.divider));
        bankRecyclerView.addItemDecoration(itemDecoration);

        withdrawButton.setOnClickListener(v -> {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            float balance;
            if (currentState == SELLER_STATE) {
                balance = sellerSaldoBalance;
            } else {
                balance = buyerSaldoBalance;
            }
            presenter.doWithdraw(
                    String.valueOf((int) balance),
                    totalWithdrawal.getText().toString(),
                    bankAdapter.getSelectedBank()
            );
        });

        withdrawBuyerSaldoTV.setOnClickListener(v -> {
            if (currentState != BUYER_STATE) {

                if (buyerSaldoBalance == 0) {
                    NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.refund_saldo_inactive));
                } else if (buyerSaldoBalance < DEFAULT_MIN_FOR_SELECTED_BANK) {
                    NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.refund_saldo_less_min));
                } else {
                    totalWithdrawal.setText("");
                    currentState = BUYER_STATE;
                    sellerWithdrawal = false;
                    enableBuyerSaldoView();
                }
            }
        });

        withdrawSellerSaldoTV.setOnClickListener(v -> {
            if (currentState != SELLER_STATE) {

                if (sellerSaldoBalance == 0) {
                    NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.seller_saldo_inactive));
                } else if (sellerSaldoBalance < DEFAULT_MIN_FOR_SELECTED_BANK) {
                    NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.seller_saldo_less_min));
                } else {
                    totalWithdrawal.setText("");
                    currentState = SELLER_STATE;
                    sellerWithdrawal = true;
                    enableSellerSaldoView();
                }

            }
        });

        if (buyerSaldoBalance == 0 || buyerSaldoBalance < DEFAULT_MIN_FOR_SELECTED_BANK) {
            currentState = SELLER_STATE;
            sellerWithdrawal = true;
            enableSellerSaldoView();
        } else {
            currentState = BUYER_STATE;
            sellerWithdrawal = false;
            enableBuyerSaldoView();
        }

        withdrawAll.setOnClickListener(v -> {
            analytics.eventClickWithdrawalAll();
            if (currentState == SELLER_STATE) {
                totalWithdrawal.setText(String.valueOf((long) sellerSaldoBalance));
            } else {
                totalWithdrawal.setText(String.valueOf((long) buyerSaldoBalance));
            }

        });

        CurrencyTextWatcher currencyTextWatcher = new CurrencyTextWatcher(totalWithdrawal, CurrencyEnum.RPwithSpace);
        currencyTextWatcher.setDefaultValue("");

        if (currencyTextWatcher != null) {
            totalWithdrawal.removeTextChangedListener(currencyTextWatcher);
        }


        totalWithdrawal.addTextChangedListener(currencyTextWatcher);
        totalWithdrawal.setText("");
        checkMinimumWithdrawal(0);
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

        Observable<String> nominalObservable = EventsWatcher.text(totalWithdrawal);

        Observable<Boolean> nominalMapper = nominalObservable.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String text) {
                int withdrawal = (int) StringUtils.convertToNumeric(text, false);
                int min = checkSelectedBankMinimumWithdrawal();
                float deposit;
                if (currentState == SELLER_STATE) {
                    deposit = sellerSaldoBalance;
                } else {
                    deposit = buyerSaldoBalance;
                }
                return (withdrawal > 0) && (withdrawal >= min) && (withdrawal <= deposit);
            }
        });

        Observable<Boolean> allField = nominalMapper.map(isValidNominal -> isValidNominal && isBankSelected());

        allField.subscribe(PropertiesEventsWatcher.enabledFrom(withdrawButton));
        allField.subscribe(aBoolean -> canProceed((TextView) withdrawButton, aBoolean));

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

    private void startShowCase() {
        if (!ShowCasePreference.hasShown(getContext(), WithdrawFragment.class.getName())) {
            ShowCasePreference.setShown(getContext(), WithdrawFragment.class.getName(), true);
            createShowCaseDialog().show(getActivity(),
                    WithdrawFragment.class.getName(),
                    getShowCaseObjectListForBuyerSaldo()
            );
        }
    }

    private ArrayList<ShowCaseObject> getShowCaseObjectListForBuyerSaldo() {
        ArrayList<ShowCaseObject> showCaseObjects = new ArrayList<>();

        showCaseObjects.add(new ShowCaseObject(
                saldoTypeCV,
                getString(R.string.show_case_title),
                getString(R.string.show_case_desc),
                ShowCaseContentPosition.BOTTOM,
                Color.WHITE));

        return showCaseObjects;
    }

    @SuppressLint("PrivateResource")
    public ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.label_next)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }

    private void enableBuyerSaldoView() {
        withdrawBuyerSaldoTV.setTextColor(getResources().getColor(R.color.white));
        withdrawBuyerSaldoTV.setBackground(getResources().getDrawable(R.drawable.bg_green_filled_radius_16));

        withdrawSellerSaldoTV.setTextColor(getResources().getColor(R.color.grey_300));
        withdrawSellerSaldoTV.setBackground(getResources().getDrawable(R.drawable.bg_grey_border_radius_16));

        saldoTitleTV.setText(getString(R.string.saldo_refund));
        saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(buyerSaldoBalance, false));
    }

    private void enableSellerSaldoView() {
        withdrawBuyerSaldoTV.setTextColor(getResources().getColor(R.color.grey_300));
        withdrawBuyerSaldoTV.setBackground(getResources().getDrawable(R.drawable.bg_grey_border_radius_16));

        withdrawSellerSaldoTV.setTextColor(getResources().getColor(R.color.white));
        withdrawSellerSaldoTV.setBackground(getResources().getDrawable(R.drawable.bg_green_filled_radius_16));

        saldoTitleTV.setText(getString(R.string.saldo_seller));
        saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(sellerSaldoBalance, false));
    }

    private void checkDepositIsSufficient(int withdrawal) {
        float deposit;
        if (currentState == SELLER_STATE) {
            deposit = sellerSaldoBalance;
        } else {
            deposit = buyerSaldoBalance;
        }

        if (withdrawal > deposit) {
            totalWithdrawal.getBackground().mutate().
                    setColorFilter(getResources().getColor(R.color.hint_red), PorterDuff.Mode.SRC_ATOP);

            if (currentState == SELLER_STATE) {
                saldoWithdrawHintTV.setText(getString(R.string.saldo_exceeding_total_value_seller));
            } else {
                saldoWithdrawHintTV.setText(getString(R.string.saldo_exceeding_total_value_buyer));
            }

            saldoWithdrawHintTV.setTextColor(getResources().getColor(R.color.hint_red));
        } else {
            totalWithdrawal.getBackground().mutate().
                    setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            String minAmount = CurrencyFormatUtil.convertPriceValueToIdrFormat(checkSelectedBankMinimumWithdrawal(), false);
            saldoWithdrawHintTV.setText(String.format(getString(R.string.saldo_withdraw_hint), minAmount));
            saldoWithdrawHintTV.setTextColor(getResources().getColor(R.color.grey_500));
        }

    }

    private boolean checkMinimumWithdrawal(int withdrawal) {
        int min = checkSelectedBankMinimumWithdrawal();
        String minAmount = CurrencyFormatUtil.convertPriceValueToIdrFormat(min, false);
        saldoWithdrawHintTV.setText(String.format(getString(R.string.saldo_withdraw_hint), minAmount));

        if (withdrawal == 0) {
            showErrorWithdrawal(null);
            return false;
        }

        if (withdrawal < min) {
            totalWithdrawal.getBackground().mutate().
                    setColorFilter(getResources().getColor(R.color.hint_red), PorterDuff.Mode.SRC_ATOP);
            saldoWithdrawHintTV.setTextColor(getResources().getColor(R.color.hint_red));
            return false;
        } else {
            return true;
        }

    }

    private int checkSelectedBankMinimumWithdrawal() {
        BankAccount selectedBank = bankAdapter.getSelectedBank();
        if (selectedBank == null) {
            return DEFAULT_MIN_FOR_SELECTED_BANK;
        }
        String selectedBankName = selectedBank.getBankName().toLowerCase();
        if (inBankGroup(selectedBankName)) {
            return DEFAULT_MIN_FOR_SELECTED_BANK;
        } else {
            return DEFAULT_SALDO_MIN;
        }
    }

    private boolean inBankGroup(String selectedBankName) {
        for (String s : bankWithMinimumWithdrawal) {
            if (selectedBankName.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBankSelected() {
        if (bankAdapter == null) {
            return false;
        }
        BankAccount bankAccountViewModel = bankAdapter.getSelectedBank();
        return bankAccountViewModel != null
                && (!TextUtils.isEmpty(bankAccountViewModel.getBankName()));
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
    public void onSuccessGetWithdrawForm(List<BankAccount> bankAccount) {
        bankAdapter.setList(bankAccount);
        bankAdapter.setDefault();
        if (!userSession.isMsisdnVerified()) {
            showMustVerify();
        }
        checkMinimumWithdrawal(0);
    }

    private void showMustVerify() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.alert_not_verified_yet_title))
                .setMessage(getActivity().getString(R.string.alert_not_verified_yet_body))
                .setPositiveButton(getActivity().getString(R.string.alert_not_verified_yet_positive), (dialog, which) -> {
                    if (getActivity() != null) {
                        Intent intent = ((WithdrawRouter) getActivity().getApplicationContext())
                                .getProfileSettingIntent(getActivity());
                        startActivity(intent);
                        getActivity().finish();
                    }

                })
                .setNegativeButton(getActivity().getString(R.string.alert_not_verified_yet_negative), (dialog, which) -> {
                    if (getActivity() != null) {
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
        if (getActivity() != null) {
            return getActivity().getString(id);
        } else {
            return "";
        }
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
        bundle.putBoolean(WithdrawPasswordActivity.BUNDLE_IS_SELLER_WITHDRAWAL, sellerWithdrawal);
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
        /*Intent intent = new Intent(getActivity(), SettingBankActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, BANK_SETTING_INTENT);*/
    }

    @Override
    public String loadRawString(int resId) {
        InputStream rawResource = getResources().openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
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
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setBankAccountId(parcelable.getAccountId());
                    bankAccount.setBankAccountName(parcelable.getAccountName());
                    bankAccount.setBankAccountNumber(parcelable.getAccountNumber());
                    bankAccount.setBankId(parcelable.getBankId());
                    bankAccount.setBankName(parcelable.getBankName());

                    bankAdapter.addItem(bankAccount);
                    bankAdapter.changeItemSelected(listBank.size() - 2);
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

                    if (getActivity() != null) {
                        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                .setTitle(getActivity().getString(R.string.alert_success_withdraw_title))
                                .setMessage(getActivity().getString(R.string.alert_success_withdraw_body))
                                .setPositiveButton(getActivity().getString(R.string.alert_success_withdraw_positive), (dialog1, which) -> {
                                    dialog1.dismiss();
                                    if (getActivity() != null) {
                                        getActivity().setResult(Activity.RESULT_OK);
                                        getActivity().finish();
                                    }

                                })
                                .setCancelable(false)
                                .create();
                        dialog.show();
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.tkpd_main_green));
                    }

                }
                break;
            default:
                break;
        }
    }
}
