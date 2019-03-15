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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.ovo.R;
import com.tokopedia.ovo.model.BarcodeResponseData;
import com.tokopedia.ovo.model.ImeiConfirmResponse;
import com.tokopedia.ovo.model.Wallet;
import com.tokopedia.ovo.presenter.PaymentQrSummaryContract;
import com.tokopedia.ovo.presenter.PaymentQrSummaryPresenterImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PaymentQRSummaryFragment extends BaseDaggerFragment implements PaymentQrSummaryContract.View {
    private static final String QR_DATA = "QR_DATA";
    private static final String IMEI = "IMEI";
    private static final String QR_RESPONSE = "QR_RESPONSE";
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int REQUEST_CODE = 0;
    public static final String PENDING_STATUS = "pending";
    public static final String SUCCESS_STATUS = "success";
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
    int transactionId;
    private TextView pointCash;
    private Wallet wallet;

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
        SaveInstanceCacheManager cacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        if (savedInstanceState == null)
            cacheManager = new SaveInstanceCacheManager(getActivity(), id);

        JsonObject response = cacheManager.get(QR_RESPONSE, new TypeToken<JsonObject>() {
        }.getType());
        if (response != null) {
            responseData = new GsonBuilder().create().fromJson(response, BarcodeResponseData.class);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_qr_summary_fragment, container, false);
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
        if (responseData.getGoalQRInquiry() != null) {
            shopName.setText(responseData.getGoalQRInquiry().getMerchant().getName());
            shopDetail.setText(responseData.getGoalQRInquiry().getMerchant().getDescription());
            if (responseData.getGoalQRInquiry().getAmount() != 0) {
                enableInputField(false);
                inputAmount.setText(String.valueOf(responseData.getGoalQRInquiry().getAmount()));
            } else {
                enableInputField(true);
            }
            inputAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            });
            inputAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    inputError.setTextColor(getResources().getColor(R.color.grey_error));
                    inputError.setText(getString(R.string.min_input_hint));
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
                            ovoPoints.setText(String.format(getString(R.string.ovo_cash_point_amnt),
                                    wallet.getPointBalance()));
                            long balanceOvoCash = amountInLong - Utils.convertToCurrencyLongFromString(wallet.getPointBalance());
                            ovoCash.setText(String.format(getString(R.string.ovo_cash_point_amnt),
                                    String.valueOf(Utils.convertToCurrencyStringWithoutRp(balanceOvoCash))));
                        } else {
                            ovoCash.setText(String.format(getString(R.string.ovo_cash_point_amnt), String.valueOf(0)));
                            ovoPoints.setText(formattedString);
                        }
                    }
                }
            });
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
        return view;
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
        pointCash.setText(String.format(getString(R.string.cash_point), walletData.getCashBalance(),
                walletData.getPointBalance()));
        bayarBtn.setOnClickListener(view1 -> {
            hideKeyboard(getView(), getActivity());
            if (TextUtils.isEmpty(inputAmount.getText()) || (inputAmount.getText() != null
                    && Utils.convertToCurrencyLongFromString(inputAmount.getText().toString()) < 1000)) {
                inputError.setText(getString(R.string.min_input_hint));
                inputError.setTextColor(getResources().getColor(R.color.error_color));
            } else if (!TextUtils.isEmpty(inputAmount.getText()) && Utils.convertToCurrencyLongFromString(
                    inputAmount.getText().toString()) > 10000000) {
                inputError.setText(getString(R.string.max_input_hint));
                inputError.setTextColor(getResources().getColor(R.color.error_color));
            } else if (inputAmount.getText() != null && Utils.convertToCurrencyLongFromString(
                    inputAmount.getText().toString()) > walletData.getRawBalance()) {
                inputError.setText(getString(R.string.balance_exceed_error));
                inputError.setTextColor(getResources().getColor(R.color.error_color));
            } else {
                bayarLayout.setAlpha(0.19f);
                progressBar.setVisibility(View.VISIBLE);
                bayarBtn.setClickable(false);
                transferId = responseData.getGoalQRInquiry().getTransferId();
                presenter.confirmQrPayment(imeiNumber,
                        responseData.getGoalQRInquiry().getTransferId(),
                        Utils.convertToCurrencyLongFromString(inputAmount.getText().toString()),
                        responseData.getGoalQRInquiry().getFee(),
                        responseData.getGoalQRInquiry().getShowUsePointToggle());
            }
        });
    }

    @Override
    public void goToUrl(ImeiConfirmResponse response) {
        if (!TextUtils.isEmpty(response.getStatus())) {
            setProgressButton();
            if (response.getStatus().equalsIgnoreCase(PENDING_STATUS)) {
                try {
                    startActivity(OvoWebViewActivity.createInstance(getActivity(), URLDecoder.decode(response.getPinUrl(),"UTF-8")));
                    //((OvoPayWithQrRouter)getActivity().getApplication()).openTokopointWebview(getActivity(),URLDecoder.decode(response.getPinUrl(),"UTF-8"),"Verifikasi PIN OVO");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else if (response.getStatus().equalsIgnoreCase(SUCCESS_STATUS)) {
                startActivityForResult(QrOvoPayTxDetailActivity.createInstance(
                        getActivity(), transferId, response.getTransactionId(), SUCCESS), REQUEST_CODE);
            } else {
                startActivityForResult(QrOvoPayTxDetailActivity.createInstance(
                        getActivity(), transferId, response.getTransactionId(), FAIL), REQUEST_CODE);
            }
        }
    }

    @Override
    public void showError(String message) {
        setProgressButton();
        Snackbar.make(getView(), getString(R.string.error_message), Snackbar.LENGTH_SHORT).show();
        enableInputField(false);
    }

    public void setProgressButton() {
        bayarLayout.setAlpha(1f);
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
}
