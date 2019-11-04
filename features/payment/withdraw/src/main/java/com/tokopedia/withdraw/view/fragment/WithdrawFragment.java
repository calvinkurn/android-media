package com.tokopedia.withdraw.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.EventsWatcher;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.PropertiesEventsWatcher;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.cachemanager.PersistentCacheManager;
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
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.webview.TkpdWebView;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.WithdrawAnalytics;
import com.tokopedia.withdraw.constant.WithdrawConstant;
import com.tokopedia.withdraw.di.DaggerWithdrawComponent;
import com.tokopedia.withdraw.di.WithdrawComponent;
import com.tokopedia.withdraw.domain.model.BankAccount;
import com.tokopedia.withdraw.domain.model.premiumAccount.CheckEligible;
import com.tokopedia.withdraw.domain.model.premiumAccount.Data;
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.adapter.BankAdapter;
import com.tokopedia.withdraw.view.decoration.SpaceItemDecoration;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.presenter.WithdrawPresenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.abstraction.common.utils.GraphqlHelper.streamToString;
import static com.tokopedia.withdraw.constant.WithdrawConstant.WEB_REKENING_PREMIUM_URL;

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
    private TextView withdrawButton;
    private View withdrawAll;
    private BankAdapter bankAdapter;
    private Snackbar snackBarInfo;
    private Snackbar snackBarError;
    private EditText totalWithdrawal;
    private View loadingLayout;

    private static final String BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int";
    private static final String BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int";

    private List<BankAccount> listBank;

    private long buyerSaldoBalance;
    private long sellerSaldoBalance;
    private int currentState = 0;
    private boolean isSeller = false;
    private boolean sellerWithdrawal;
    private TextView saldoTitleTV;
    private TextView saldoValueTV;
    private TextView saldoWithdrawHintTV;
    private static final String IS_WITHDRAW_LOCK = "is_lock";
    private static final String MCL_LATE_COUNT = "late_count";
    private static final String FIREBASE_FLAG_STATUS = "is_on";

    private int statusWithDrawLock;
    private int mclLateCount;
    private ConstraintLayout tickerLayout;
    private TextView tvTickerMessage;
    private TextView tvWithDrawInfo;
    private boolean sellerSaldoWithDrawTvStatus = false;
    private ImageView ivDismissTicker;
    private static final int MCL_STATUS_BLOCK1 = 700;
    private static final int MCL_STATUS_BLOCK3 = 999;
    private boolean showMclBlockTickerFirebaseFlag = false;
    private ImageView ivLockButton;
    TabLayout tabLayout;


    private CompositeSubscription subscription;
    private AlertDialog alertDialog;
    private boolean isRegisterForProgram;

    private View premiumAccountView;
    private View mainView;
    private CloseableBottomSheetDialog briProgramBottomSheet;
    private Group editableGroup;
    private Group emptyScreenGroup;
    private CheckEligible checkEligible;
    private final String PARAM_HEADER_GC_TOKEN = "X-User-Token";


    private TextView tvEmptySaldo;

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
        View view = inflater.inflate(R.layout.fragment_withdraw_layout_new, container, false);

        wrapperTotalWithdrawal = view.findViewById(R.id.wrapper_total_withdrawal);
        CloseableBottomSheetDialog saldoWithdrawInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        saldoWithdrawInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

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
        mainView = view.findViewById(R.id.mainView);

        saldoTitleTV = view.findViewById(R.id.saldo_title);
        saldoValueTV = view.findViewById(R.id.total_saldo_value);
        saldoWithdrawHintTV = view.findViewById(R.id.saldo_withdraw_hint);
        tickerLayout = view.findViewById(R.id.layout_ticker);
        tvTickerMessage = view.findViewById(R.id.tv_desc_info);

        tvWithDrawInfo = view.findViewById(R.id.tv_info);
        ivDismissTicker = view.findViewById(R.id.iv_dismiss_ticker);
        ivLockButton = view.findViewById(R.id.ivButtonLeft);
        tabLayout = view.findViewById(R.id.tab_withdraw);
        premiumAccountView = view.findViewById(R.id.layout_premium_account);
        tvEmptySaldo = view.findViewById(R.id.empty_saldo_description);
        emptyScreenGroup = view.findViewById(R.id.emptyGroup);
        editableGroup = view.findViewById(R.id.editable_group);

        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listBank = new ArrayList<>();
        bankAdapter = BankAdapter.createAdapter(this, listBank, analytics);
        bankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        bankRecyclerView.setAdapter(bankAdapter);
        bankAdapter.setList(listBank);

        if (getArguments() != null) {
            showMclBlockTickerFirebaseFlag = getArguments().getBoolean(FIREBASE_FLAG_STATUS);
            buyerSaldoBalance = getArguments().getLong(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT);
            sellerSaldoBalance = getArguments().getLong(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT);
            statusWithDrawLock = getArguments().getInt(IS_WITHDRAW_LOCK);
            mclLateCount = getArguments().getInt(MCL_LATE_COUNT);
        }

        if ((statusWithDrawLock == MCL_STATUS_BLOCK1 || statusWithDrawLock == MCL_STATUS_BLOCK3) && showMclBlockTickerFirebaseFlag) {
            showTicker();
        }

        saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(buyerSaldoBalance, false));

        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.dp_16)
                , MethodChecker.getDrawable(getActivity(), R.drawable.swd_divider));
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
            analytics.eventClickTarikSaldo();
        });


        if (buyerSaldoBalance == 0 || buyerSaldoBalance < DEFAULT_MIN_FOR_SELECTED_BANK) {
            currentState = SELLER_STATE;
            sellerWithdrawal = true;
            tabLayout.getTabAt(1).select();
            bankAdapter.setCurrentTab(1);
            checkForEmptyView(1);
        } else {
            currentState = BUYER_STATE;
            sellerWithdrawal = false;
            tabLayout.getTabAt(0).select();
            bankAdapter.setCurrentTab(0);
            checkForEmptyView(0);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setWithdrawBuyerSaldo();
                    bankAdapter.setCurrentTab(0);
                    checkForEmptyView(0);
                } else {
                    setWithdrawSellerSaldo();
                    bankAdapter.setCurrentTab(1);
                    checkForEmptyView(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        withdrawAll.setOnClickListener(v -> {
            analytics.eventClickWithdrawalAll();
            if (currentState == SELLER_STATE) {
                totalWithdrawal.setText(String.valueOf((long) sellerSaldoBalance));
            } else {
                totalWithdrawal.setText(String.valueOf((long) buyerSaldoBalance));
            }

        });

        CurrencyTextWatcher currencyTextWatcher = new CurrencyTextWatcher(totalWithdrawal, CurrencyEnum.RPwithSpace);
        currencyTextWatcher.setMaxLength(WithdrawConstant.MAX_LENGTH);
        currencyTextWatcher.setDefaultValue("");

        if (currencyTextWatcher != null) {
            totalWithdrawal.removeTextChangedListener(currencyTextWatcher);
        }


        totalWithdrawal.addTextChangedListener(currencyTextWatcher);
        totalWithdrawal.setText("");
        checkWithdrawalAmount(0);
        totalWithdrawal.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int withdrawal = (int) StringUtils.convertToNumeric(s.toString(), false);
                checkWithdrawalAmount(withdrawal);
                if (withdrawal == 0) {
                    totalWithdrawal.setSelection(totalWithdrawal.length());
                }
            }
        });
        addObserverToWithdrawalEditText(totalWithdrawal);

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
                        snackBarInfo.dismiss();
                    }
                });

        presenter.getWithdrawForm();
        presenter.getPremiumAccountData();
        tvWithDrawInfo.setText(createTermsAndConditionSpannable());
        tvWithDrawInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvWithDrawInfo.setVisibility(View.VISIBLE);
    }


    private void checkForEmptyView(int currentTab) {
        if (currentTab == 0 && buyerSaldoBalance == 0) {
            tvEmptySaldo.setText(R.string.swd_refund_empty_msg);
            editableGroup.setVisibility(View.GONE);
            emptyScreenGroup.setVisibility(View.VISIBLE);
        }else if(currentTab == 1 && sellerSaldoBalance == 0){
            tvEmptySaldo.setText(R.string.swd_penghasilan_empty_msg);
            editableGroup.setVisibility(View.GONE);
            emptyScreenGroup.setVisibility(View.VISIBLE);
        } else {
            editableGroup.setVisibility(View.VISIBLE);
            emptyScreenGroup.setVisibility(View.GONE);
        }
    }

    private void setWithdrawBuyerSaldo() {
        if (currentState != BUYER_STATE) {
            if (sellerSaldoWithDrawTvStatus) {
                sellerSaldoWithDrawTvStatus = false;
                withdrawButton.setEnabled(true);
                withdrawButton.setClickable(true);
                ivLockButton.setVisibility(View.GONE);
                tvWithDrawInfo.setVisibility(View.GONE);
            }
            totalWithdrawal.setText("");
            currentState = BUYER_STATE;
            sellerWithdrawal = false;

            saldoTitleTV.setText(getString(R.string.saldo_refund));
            saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(buyerSaldoBalance, false));
        }
    }

    private void setWithdrawSellerSaldo() {
        if (currentState != SELLER_STATE) {
            if ((statusWithDrawLock == MCL_STATUS_BLOCK3 || statusWithDrawLock == MCL_STATUS_BLOCK1) && showMclBlockTickerFirebaseFlag) {
                ivLockButton.setVisibility(View.VISIBLE);
                withdrawButton.setEnabled(false);
                withdrawButton.setClickable(false);
                sellerSaldoWithDrawTvStatus = true;
            }
            totalWithdrawal.setText("");
            sellerWithdrawal = true;
            currentState = SELLER_STATE;
            saldoTitleTV.setText(getString(R.string.saldo_seller));
            saldoValueTV.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(sellerSaldoBalance, false));
        }
    }

    private void addObserverToWithdrawalEditText(EditText editText) {
        Observable<String> nominalObservable = EventsWatcher.text(totalWithdrawal);
        Observable<Boolean> nominalMapper = nominalObservable.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String text) {
                long withdrawal = (long) StringUtils.convertToNumeric(text, false);
                long min = getMinTransferForCurrentBank();
                long max = getMaxTransferForCurrentBank();
                float deposit;
                if (currentState == SELLER_STATE) {
                    deposit = sellerSaldoBalance;
                } else {
                    deposit = buyerSaldoBalance;
                }
                return (withdrawal > 0) && (withdrawal >= min) && (withdrawal <= max) && (withdrawal <= deposit);
            }
        });
        Observable<Boolean> allField = nominalMapper.map(isValidNominal -> isValidNominal && isBankSelected());
        Subscription enableSubscription = allField.subscribe(PropertiesEventsWatcher.enabledFrom(withdrawButton), Throwable::printStackTrace);
        Subscription proceedSubscription = allField.subscribe(aBoolean -> canProceed(aBoolean), Throwable::printStackTrace);
        subscription = new CompositeSubscription();
        subscription.add(enableSubscription);
        subscription.add(proceedSubscription);
    }

    public void showTicker() {
        String tickerMsg = getString(R.string.saldolock_tickerDescription);
        int startIndex = tickerMsg.indexOf("Bayar Sekarang");
        String late = Integer.toString(mclLateCount);
        tickerMsg = String.format(getResources().getString(R.string.saldolock_tickerDescription), late);
        SpannableString ss = new SpannableString(tickerMsg);

        tvTickerMessage.setMovementMethod(LinkMovementMethod.getInstance());
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                RouteManager.route(getContext(), String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW, WithdrawConstant.SALDOLOCK_PAYNOW_URL));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
            }
        }, startIndex - 1, tickerMsg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTickerMessage.setText(ss);
        ivDismissTicker.setOnClickListener(v -> tickerLayout.setVisibility(View.GONE));
        tickerLayout.setVisibility(View.VISIBLE);
    }

    private boolean checkWithdrawalAmount(long withdrawal) {
        BankAccount bankAccount = bankAdapter.getSelectedBank();
        if (bankAccount == null) {
            changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green, "");
            return false;
        } else if (withdrawal == 0) {
            String minStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(bankAccount.getMinAmount(), false);
            changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                    String.format(getString(R.string.min_saldo_withdraw_hint), minStr));
            return false;
        } else if (withdrawal < bankAccount.getMinAmount()) {
            String minStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(bankAccount.getMinAmount(), false);
            changeHintTextColor(R.color.hint_red, R.color.hint_red,
                    String.format(getString(R.string.min_saldo_withdraw_hint), minStr));
            return false;
        } else if (withdrawal > bankAccount.getMaxAmount()) {
            String maxStr = CurrencyFormatUtil
                    .convertPriceValueToIdrFormat(bankAccount.getMaxAmount(), false);
            changeHintTextColor(R.color.hint_red, R.color.hint_red,
                    String.format(getString(R.string.max_saldo_withdraw_hint), maxStr));
            return false;
        } else if (sellerWithdrawal && withdrawal > sellerSaldoBalance) {
            changeHintTextColor(R.color.hint_red, R.color.hint_red, getString(R.string.saldo_exceeding_withdraw_balance));
            return false;
        } else if (!sellerWithdrawal && withdrawal > buyerSaldoBalance) {
            changeHintTextColor(R.color.hint_red, R.color.hint_red, getString(R.string.saldo_exceeding_withdraw_balance));
            return false;
        } else {
            String minStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(bankAccount.getMinAmount(), false);
            changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                    String.format(getString(R.string.min_saldo_withdraw_hint), minStr));
            return true;
        }
    }

    private void changeHintTextColor(@ColorRes int hintColor, @ColorRes int underLineColor, String hintText) {
        totalWithdrawal.getBackground().mutate().
                setColorFilter(getResources().getColor(underLineColor), PorterDuff.Mode.SRC_ATOP);
        saldoWithdrawHintTV.setTextColor(getResources().getColor(hintColor));
        saldoWithdrawHintTV.setText(hintText);
    }

    private long getMaxTransferForCurrentBank() {
        BankAccount selectedBank = bankAdapter.getSelectedBank();
        if (selectedBank == null) {
            return 0;
        }
        return selectedBank.getMaxAmount();
    }

    private long getMinTransferForCurrentBank() {
        BankAccount selectedBank = bankAdapter.getSelectedBank();
        if (selectedBank == null) {
            return 0;
        }
        return selectedBank.getMinAmount();
    }

    private boolean isBankSelected() {
        if (bankAdapter == null) {
            return false;
        }
        BankAccount bankAccount = bankAdapter.getSelectedBank();
        return bankAccount != null
                && (!TextUtils.isEmpty(bankAccount.getBankName()));
    }

    public void canProceed(boolean can) {
        if (can && !sellerSaldoWithDrawTvStatus) {
            withdrawButton.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.medium_green), PorterDuff.Mode.SRC_IN);
            withdrawButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        } else {
            withdrawButton.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.grey_300), PorterDuff.Mode.SRC_IN);
            withdrawButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
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
        checkWithdrawalAmount(0);
    }

    private void showMustVerify() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.alert_not_verified_yet_title))
                .setMessage(getActivity().getString(R.string.alert_not_verified_yet_body))
                .setPositiveButton(getActivity().getString(R.string.alert_not_verified_yet_positive), (dialog, which) -> {
                    if (getActivity() != null) {
                        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.SETTING_PROFILE);
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
        if (checkEligible != null && checkEligible.getData() != null
                && checkEligible.getData().getProgram() != null)
            bundle.putString(WithdrawPasswordActivity.BUNDLE_IS_PROGRAM_NAME, checkEligible.getData().getProgram());
        intent.putExtras(bundle);
        startActivityForResult(intent, CONFIRM_PASSWORD_INTENT);
    }

    @Override
    public void showConfirmationDialog(ValidatePopUpWithdrawal validatePopUpWithdrawal) {
        if (validatePopUpWithdrawal.getData().isNeedShow()) {
            alertDialog = getConfirmationDialog(validatePopUpWithdrawal.getData().getTitle(),
                    validatePopUpWithdrawal.getData().getNote(),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int viewId = v.getId();
                            if (viewId == R.id.continue_btn) {
                                showConfirmPassword();
                                alertDialog.cancel();
                            } else {
                                alertDialog.cancel();
                            }
                            analytics.eventClickContinueBtn();
                        }
                    }).create();
            alertDialog.show();
        } else
            showConfirmPassword();
    }

    @Override
    public void showCheckProgramData(CheckEligible checkEligible) {
        Data data = checkEligible.getData();
        this.checkEligible = checkEligible;
        this.isRegisterForProgram = data.isIsPowerWD();
        boolean isShown = PersistentCacheManager.instance
                .get(WithdrawConstant.KEY_PREMIUM_ACCOUNT_NEW_TAG, boolean.class, false);
        if (data != null && data.isIsPowerMerchant()) {
            if (!isShown) {
                premiumAccountView.findViewById(R.id.tv_baru_tag).setVisibility(View.VISIBLE);
            } else
                premiumAccountView.findViewById(R.id.tv_baru_tag).setVisibility(View.GONE);

            premiumAccountView.setVisibility(View.VISIBLE);
            if (data.isIsPowerWD()) {
                if (data.getWdPoints() == 0) {
                    setProgramStatus(getString(R.string.active_bri_Account, data.getProgram()),
                            getString(R.string.withdrawal_info));
                } else {
                    setProgramStatus(getString(R.string.active_bri_Account_point,
                            data.getProgram(), data.getWdPoints()),
                            getString(R.string.withdraw_help));
                }
            } else {
                if (data.getStatusInt() == 0)
                    setProgramStatus(getString(R.string.withdraw_with_more),
                            getString(R.string.bri_cek));
                else
                    setProgramStatus(getString(R.string.account_in_progress, data.getProgram()),
                            getString(R.string.bri_cek));
            }
            premiumAccountView.findViewById(R.id.tv_briProgramButton)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleProgramWidgetClick();
                        }
                    });

        } else {
            premiumAccountView.setVisibility(View.GONE);
        }
    }

    private void setProgramStatus(String description, String btnText) {
        ((TextView) premiumAccountView.findViewById(R.id.tv_briProgramDescription))
                .setText(Html.fromHtml(description));
        ((TextView) premiumAccountView.findViewById(R.id.tv_briProgramButton))
                .setText(btnText);
    }

    private void handleProgramWidgetClick() {
        briProgramBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.swd_program_tarik_saldo, null, true);
        if (isRegisterForProgram) {
            ((TextView) view.findViewById(R.id.tv_wdProgramTitle))
                    .setText(getString(R.string.swd_program_tarik_saldo));
            ((TextView) view.findViewById(R.id.tv_wdProgramDescription))
                    .setText(getString(R.string.swd_program_tarik_saldo_description));
            ((TextView) view.findViewById(R.id.wdProgramContinue))
                    .setText(getString(R.string.swd_program_tarik_btn));
            analytics.eventClickInfo();
        } else {
            ((TextView) view.findViewById(R.id.tv_wdProgramTitle))
                    .setText(getString(R.string.swd_rekening_premium));
            ((TextView) view.findViewById(R.id.tv_wdProgramDescription))
                    .setText(getString(R.string.swd_rekening_premium_description));
            ((TextView) view.findViewById(R.id.wdProgramContinue))
                    .setText(getString(R.string.swd_rekening_premium_btn));
            analytics.eventClickJoinNow();
        }

        view.findViewById(R.id.wdProgramContinue).setOnClickListener(v -> {
            briProgramBottomSheet.dismiss();
            RouteManager.route(getContext(), String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW, WEB_REKENING_PREMIUM_URL));
            analytics.eventClickGotoDashboard();
        });
        briProgramBottomSheet.setContentView(view);
        briProgramBottomSheet.show();
    }

    public AlertDialog.Builder getConfirmationDialog(String heading, String description, View.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirmation_dialog, null);
        ((TextView) dialogView.findViewById(R.id.heading)).setText(heading);
        ((TextView) dialogView.findViewById(R.id.description)).setText(Html.fromHtml(description));
        dialogView.findViewById(R.id.continue_btn).setOnClickListener(onClickListener);
        dialogView.findViewById(R.id.back_btn).setOnClickListener(onClickListener);
        return dialogBuilder.setView(dialogView);
    }

    @Override
    public void goToAddBank() {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.ADD_BANK);
        startActivityForResult(intent, BANK_INTENT);
    }

    @Override
    public void goToSettingBank() {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.SETTING_BANK);
        startActivityForResult(intent, BANK_SETTING_INTENT);
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
        boolean isValid = checkWithdrawalAmount(withdrawal);
        canProceed(isValid);
        withdrawButton.setEnabled(isValid);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BANK_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.refreshBankList();
                    Toaster.INSTANCE.showNormal(mainView, getString(R.string.swd_bank_added_success), Snackbar.LENGTH_LONG);
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
                else if (resultCode == WithdrawConstant.ResultCode.GOTO_SALDO_DETAIL_PAGE){
                    getActivity().finish();
                }
                else if(resultCode == WithdrawConstant.ResultCode.GOTO_TOKOPEDIA_HOME_PAGE){
                    getActivity().finish();
                    RouteManager.route(getContext(), ApplinkConst.HOME, "");
                }
                break;
            default:
                break;
        }
    }


    private SpannableStringBuilder createTermsAndConditionSpannable() {
        String originalText = getString(R.string.saldo_withdraw_tnc_original);
        String readMoreText = getString(R.string.saldo_withdraw_tnc_clickable);
        SpannableString spannableString = new SpannableString(readMoreText);
        int startIndex = 0;
        int endIndex = spannableString.length();
        int color = this.getResources().getColor(R.color.tkpd_main_green);
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                analytics.eventClickTANDC();
                openTermsAndConditionBottomSheet();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString);
    }

    private void openTermsAndConditionBottomSheet() {
        CloseableBottomSheetDialog bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.swd_layout_withdraw_tnc, null, true);
        TkpdWebView webView = view.findViewById(R.id.swd_tnc_webview);
        ImageView closeBtn = view.findViewById(R.id.close_button);
        Typography titleView = view.findViewById(R.id.title_closeable);
        webView.loadAuthUrl(WithdrawConstant.WEB_TNC_URL, userSession);
        closeBtn.setOnClickListener((v) -> bottomSheet.dismiss());
        titleView.setText(getString(R.string.saldo_withdraw_tnc_title));
        bottomSheet.setCustomContentView(view, getString(R.string.saldo_withdraw_tnc_title), false);
        bottomSheet.show();
    }

}
