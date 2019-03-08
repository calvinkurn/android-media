package com.tokopedia.ovo.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PaymentQRSummaryFragment extends BaseDaggerFragment implements PaymentQrSummaryContract.View {
    private static final String QR_DATA = "QR_DATA";
    private static final String IMEI = "IMEI";
    private static final String QR_RESPONSE = "QR_RESPONSE";
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

    public static Fragment createInstance(String qrData,String imei) {
        Bundle bundle = new Bundle();
        bundle.putString(QR_DATA, qrData);
        bundle.putString(IMEI,imei);
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
    public void setWalletBalance(Wallet walletData) {
        ovoPoints.setText(String.format(getString(R.string.ovo_points_amnt), walletData.getPointBalance()));
        ovoCash.setText(String.format(getString(R.string.ovo_cash_amnt), walletData.getCashBalance()));
        bayarBtn.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(inputAmount.getText()) ||
                    (inputAmount.getText() != null && Float.parseFloat(inputAmount.getText().toString()) < 10000f)) {
                inputError.setText(getString(R.string.blank_input_error));
                inputError.setTextColor(getResources().getColor(R.color.error_color));
            } else if (inputAmount.getText() != null &&
                    Integer.parseInt(inputAmount.getText().toString()) > walletData.getRawBalance()) {
                inputError.setText(getString(R.string.balance_exceed_error));
                inputError.setTextColor(getResources().getColor(R.color.error_color));
            } else {
                bayarLayout.setAlpha(0.19f);
                progressBar.setVisibility(View.VISIBLE);
                bayarBtn.setClickable(false);
                presenter.confirmQrPayment(imeiNumber,
                        responseData.getGoalQRInquiry().getTransferId(),
                        Float.parseFloat(inputAmount.getText().toString()),
                        responseData.getGoalQRInquiry().getFee(),
                        responseData.getGoalQRInquiry().getShowUsePointToggle());
            }
        });
    }

    @Override
    public void goToUrl(ImeiConfirmResponse response) {
        bayarLayout.setAlpha(1f);
        progressBar.setVisibility(View.GONE);
        if (response.getStatus() != null) {
            if (response.getStatus().equalsIgnoreCase("pending")) {
                startActivity(OvoWebViewActivity.createInstance(getActivity(), response.getPinUrl()));
            } else if (response.getStatus().equalsIgnoreCase("success")) {
                startActivity(QrOvoPayTxDetailActivity.createInstance(getActivity()));
            } else {
                //startActivity()
            }
        }
    }
}
