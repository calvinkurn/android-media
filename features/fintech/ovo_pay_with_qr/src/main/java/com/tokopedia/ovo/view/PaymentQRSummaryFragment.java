package com.tokopedia.ovo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.ovo.OvoPayWithQrRouter;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.BarcodeResponseData;
import com.tokopedia.ovo.model.ImeiConfirmResponse;
import com.tokopedia.ovo.model.Wallet;
import com.tokopedia.ovo.presenter.PaymentQrSummaryContract;
import com.tokopedia.ovo.presenter.PaymentQrSummaryPresenterImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.tokopedia.ovo.view.PaymentQRSummaryActivity.IMEI;
import static com.tokopedia.ovo.view.PaymentQRSummaryActivity.QR_DATA;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSACTION_ID;
import static com.tokopedia.ovo.view.QrOvoPayTxDetailActivity.TRANSFER_ID;

public class PaymentQRSummaryFragment extends BaseDaggerFragment implements
        PaymentQrSummaryContract.View, View.OnFocusChangeListener, TextWatcher, View.OnClickListener {
    private static final String QR_RESPONSE = "QR_RESPONSE";
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int REQUEST_CODE = 0;
    public static final String PENDING_STATUS = "pending";
    public static final String SUCCESS_STATUS = "success";
    private static final float BUY_BTN_FADE_VISIBILITY = 0.19f;
    private static final float BUY_BTN_COMPLETE_VISIBILITY = 1f;
    private static final long MIN_AMOUNT = 1000;
    private static final long MAX_AMOUNT = 10000000;
    String id;
    String imeiNumber;
    BarcodeResponseData responseData;
    TextView shopName;
    TextView shopDetail;
    EditText inputAmount;
    TextView inputError;
    LinearLayout switchLayout;
    TextView ovoPoints;
    TextView ovoCash;
    LinearLayout bayarLayout;
    TextView bayarBtn;
    TextView cancelBtn;
    ProgressBar progressBar;
    LinearLayout ovoDetailLayout;
    Switch switchButton;
    PaymentQrSummaryPresenterImpl presenter;
    int transferId;
    private TextView pointCash;
    private Wallet wallet;
    private SaveInstanceCacheManager cacheManager;

    public static Fragment createInstance(String qrData, String imei) {
        Bundle bundle = new Bundle();
        bundle.putString(QR_DATA, qrData);
        bundle.putString(IMEI, imei);
        Fragment fragment = new PaymentQRSummaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString(QR_DATA);
        imeiNumber = getArguments().getString(IMEI);
        presenter = new PaymentQrSummaryPresenterImpl(getActivity());
        presenter.attachView(this);
        presenter.fetchWalletDetails();
        cacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        if (savedInstanceState == null)
            cacheManager = new SaveInstanceCacheManager(getActivity(), id);

        responseData = cacheManager.get(QR_RESPONSE, new TypeToken<BarcodeResponseData>() {
        }.getType());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.oqr_payment_qr_summary_fragment, container, false);
        initViews(view);
        setDataAndListeners();
        return view;
    }

    private void initViews(View view) {
        shopName = view.findViewById(R.id.shop_name);
        shopDetail = view.findViewById(R.id.merchant_name);
        inputAmount = view.findViewById(R.id.input_amount);
        inputError = view.findViewById(R.id.info_error_text);
        switchLayout = view.findViewById(R.id.switch_layout);
        bayarLayout = view.findViewById(R.id.btn_text_layout);
        ovoDetailLayout = view.findViewById(R.id.ovo_detail_layout);
        pointCash = view.findViewById(R.id.ovo_cash_point);
        ovoPoints = view.findViewById(R.id.ovo_points);
        ovoCash = view.findViewById(R.id.ovo_cash);
        switchButton = view.findViewById(R.id.switch_ovo);
        bayarBtn = view.findViewById(R.id.bayar_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        progressBar = view.findViewById(R.id.pay_progress);
    }

    private void setDataAndListeners() {
        if (responseData.getGoalQRInquiry() != null) {
            shopName.setText(responseData.getGoalQRInquiry().getMerchant().getName());
            shopDetail.setText(responseData.getGoalQRInquiry().getMerchant().getDescription());
            if (responseData.getGoalQRInquiry().getAmount() != 0) {
                enableInputField(false);
                inputAmount.setText(String.valueOf(responseData.getGoalQRInquiry().getAmount()));
            } else {
                enableInputField(true);
            }
            inputAmount.setOnFocusChangeListener(this);
            inputAmount.addTextChangedListener(this);
            if (responseData.getGoalQRInquiry().getShowUsePointToggle()) {
                switchLayout.setVisibility(View.VISIBLE);
                switchButton.setOnCheckedChangeListener((compoundButton, b) -> {
                    ovoDetailLayout.setVisibility(b ? View.VISIBLE : View.GONE);
                });
            } else {
                switchLayout.setVisibility(View.GONE);
            }
            cancelBtn.setOnClickListener(view1 -> getActivity().finish());
        }
    }

    private void enableInputField(boolean isEnabled) {
        inputAmount.setFocusable(isEnabled);
        inputAmount.setClickable(isEnabled);
    }

    @Override
    public void onDestroyView() {
        hideKeyboard(getView(), getActivity());
        super.onDestroyView();
    }

    private void hideKeyboard(View v, Activity activity) {
        InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void setWalletBalance(Wallet walletData) {
        wallet = walletData;
        pointCash.setText(String.format(getString(R.string.oqr_cash_point), walletData.getCashBalance(),
                walletData.getPointBalance()));
        bayarBtn.setOnClickListener(this);
    }

    @Override
    public void goToUrl(ImeiConfirmResponse response) {
        if (!TextUtils.isEmpty(response.getStatus())) {
            setProgressButton();
            if (response.getStatus().equalsIgnoreCase(PENDING_STATUS)) {
                try {
                    Intent intent = ((OvoPayWithQrRouter) getActivity().getApplication())
                            .tokopointWebviewIntent(getActivity(), URLDecoder.decode(
                                    response.getPinUrl(), "UTF-8"), getString(R.string.oqr_pin_page_title));
                    intent.putExtra("cache_id",id);
                    intent.putExtra(TRANSACTION_ID, response.getTransactionId());
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (response.getStatus().equalsIgnoreCase(SUCCESS_STATUS)) {
                startActivity(QrOvoPayTxDetailActivity.createInstance(
                        getActivity(), transferId, response.getTransactionId(), SUCCESS));
            } else {
                startActivity(QrOvoPayTxDetailActivity.createInstance(
                        getActivity(), transferId, response.getTransactionId(), FAIL));
            }
            getActivity().finish();
        }
    }

    @Override
    public void showError(String message) {
        setProgressButton();
        Snackbar.make(getView(), getErrorMessage(), Snackbar.LENGTH_SHORT).show();
        enableInputField(false);
    }

    @Override
    public String getErrorMessage() {
        return getString(R.string.oqr_error_message);
    }

    public void setProgressButton() {
        bayarLayout.setAlpha(BUY_BTN_COMPLETE_VISIBILITY);
        progressBar.setVisibility(View.GONE);
        bayarBtn.setClickable(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.getId() == R.id.input_amount && hasFocus) {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputError.setTextColor(getResources().getColor(R.color.oqr_grey_error));
        inputError.setText(getString(R.string.oqr_min_input_hint));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            inputAmount.removeTextChangedListener(this);
            long amountInLong = Utils.convertToCurrencyLongFromString(editable.toString());
            String formattedString = Utils.convertToCurrencyStringWithoutRp(amountInLong);
            inputAmount.setText(formattedString);
            inputAmount.addTextChangedListener(this);
            inputAmount.setSelection(inputAmount.getText().length());

            if (wallet != null && amountInLong <= Utils.convertToCurrencyLongFromString(wallet.getPointBalance())) {
                long balanceOvoCash = amountInLong - Utils.convertToCurrencyLongFromString(wallet.getPointBalance());
                ovoPoints.setText(String.format(getString(R.string.oqr_ovo_cash_point_amnt),
                        wallet.getPointBalance()));
                ovoCash.setText(String.format(getString(R.string.oqr_ovo_cash_point_amnt),
                        String.valueOf(Utils.convertToCurrencyStringWithoutRp(balanceOvoCash))));
            } else {
                ovoCash.setText(String.format(getString(R.string.oqr_ovo_cash_point_amnt), String.valueOf(0)));
                ovoPoints.setText(formattedString);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bayar_btn) {
            hideKeyboard(getView(), getActivity());
            if (TextUtils.isEmpty(inputAmount.getText()) || (inputAmount.getText() != null
                    && Utils.convertToCurrencyLongFromString(inputAmount.getText().toString()) < MIN_AMOUNT)) {
                setErrorMessage(getString(R.string.oqr_min_input_hint));
            } else if (!TextUtils.isEmpty(inputAmount.getText()) && Utils.convertToCurrencyLongFromString(
                    inputAmount.getText().toString()) > MAX_AMOUNT) {
                setErrorMessage(getString(R.string.oqr_max_input_hint));
            } else if (inputAmount.getText() != null && Utils.convertToCurrencyLongFromString(
                    inputAmount.getText().toString()) > wallet.getRawBalance()) {
                setErrorMessage(getString(R.string.oqr_balance_exceed_error));
            } else {
                confirmQrRequest();
            }
        }
    }

    private void confirmQrRequest() {
        bayarLayout.setAlpha(BUY_BTN_FADE_VISIBILITY);
        progressBar.setVisibility(View.VISIBLE);
        bayarBtn.setClickable(false);
        transferId = responseData.getGoalQRInquiry().getTransferId();
        cacheManager.put(TRANSFER_ID, transferId);
        presenter.confirmQrPayment(imeiNumber,
                responseData.getGoalQRInquiry().getTransferId(),
                Utils.convertToCurrencyLongFromString(inputAmount.getText().toString()),
                responseData.getGoalQRInquiry().getFee(),
                responseData.getGoalQRInquiry().getShowUsePointToggle());
    }

    public void setErrorMessage(String message) {
        inputError.setText(message);
        inputError.setTextColor(getResources().getColor(R.color.oqr_error_color));
    }
}
