package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.contract.QrPaymentContract;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.presenter.QrPaymentPresenter;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class NominalQrPaymentActivity extends BaseSimpleActivity implements QrPaymentContract.View,
        HasComponent<TokoCashComponent> {

    private static final int MAX_DIGIT_NOMINAL = 10;
    private static final String INFO_QR = "info_qr";
    private static final String IDENTIFIER = "identifier";

    private TextView merchantName;
    private TextView merchantPhone;
    private EditText nominalValue;
    private EditText notesValue;
    private View separatorNominal;
    private TextView tokocashValue;
    private Button payButton;
    private InfoQrTokoCash infoQrTokoCash;
    private TokoCashComponent tokoCashComponent;
    private BalanceTokoCash balanceTokoCash;
    private ProgressBar progressBar;
    @Inject
    QrPaymentPresenter presenter;

    public static Intent newInstance(Context context, String identifier, InfoQrTokoCash infoQrTokoCash) {
        Intent intent = new Intent(context, NominalQrPaymentActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        intent.putExtra(INFO_QR, infoQrTokoCash);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        presenter.attachView(this);

        updateTitle(getString(R.string.title_input_nominal));
        Bundle bundle = getIntent().getExtras();
        infoQrTokoCash = bundle.getParcelable(INFO_QR);

        initView();
        setVar();
        presenter.getBalanceTokoCash();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_nominal_qr;
    }

    private void setVar() {
        merchantName.setText(infoQrTokoCash.getName());
        merchantPhone.setText(infoQrTokoCash.getPhoneNumber());
        merchantPhone.setVisibility(!infoQrTokoCash.getPhoneNumber().equals("") ? View.VISIBLE : View.GONE);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.postQrPayment();
            }
        });
    }

    private int getColorNominal(int color) {
        return ContextCompat.getColor(getApplicationContext(), color);
    }

    private void initView() {
        merchantName = (TextView) findViewById(R.id.merchant_name);
        merchantPhone = (TextView) findViewById(R.id.merchant_phone);
        nominalValue = (EditText) findViewById(R.id.value_nominal);
        notesValue = (EditText) findViewById(R.id.notes_value);
        separatorNominal = (View) findViewById(R.id.separator);
        tokocashValue = (TextView) findViewById(R.id.tokocash_value);
        payButton = (Button) findViewById(R.id.pay_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        tokoCashComponent.inject(this);
    }

    @Override
    public RequestParams getRequestParams() {
        String valueNominal = CurrencyFormatHelper.RemoveNonNumeric(nominalValue.getText().toString());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PostQrPaymentUseCase.IDENTIFIER, getIntent().getStringExtra(IDENTIFIER));
        requestParams.putString(PostQrPaymentUseCase.NOTE, notesValue.getText().toString().equals("") ?
                getString(R.string.default_notes_payment) : notesValue.getText().toString());
        requestParams.putLong(PostQrPaymentUseCase.AMOUNT, Long.parseLong(valueNominal));
        return requestParams;
    }

    @Override
    public void directToSuccessPayment(QrPaymentTokoCash qrPaymentTokoCash) {
        progressBar.setVisibility(View.GONE);
        Intent intent = SuccessPaymentQRActivity.newInstance(getApplicationContext(), qrPaymentTokoCash,
                infoQrTokoCash.getName(), nominalValue.getText().toString(), true);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    @Override
    public void directToFailedPayment() {
        progressBar.setVisibility(View.GONE);
        Intent intent = SuccessPaymentQRActivity.newInstance(getApplicationContext(), new QrPaymentTokoCash(),
                infoQrTokoCash.getName(), nominalValue.getText().toString(), false);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    @Override
    public void renderBalanceTokoCash(final BalanceTokoCash balanceTokoCash) {
        tokocashValue.setVisibility(View.VISIBLE);
        tokocashValue.setText(String.format(getString(R.string.tokocash_balance_payment),
                balanceTokoCash.getBalance()));
        this.balanceTokoCash = balanceTokoCash;

        if (infoQrTokoCash.getAmount() > 0) {
            nominalValue.setEnabled(false);
            nominalValue.setText(CurrencyFormatHelper
                    .ConvertToRupiah(String.valueOf(infoQrTokoCash.getAmount())));
            handleWarningPayment(infoQrTokoCash.getAmount());
        } else {
            nominalValue.setEnabled(true);
        }
        nominalValue.setFilters(
                new InputFilter[]{new InputFilter.LengthFilter(MAX_DIGIT_NOMINAL)});
        nominalValue.addTextChangedListener(new NumberTextWatcher(nominalValue, "0") {
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                handleWarningPayment(number);
            }
        });
    }

    private void handleWarningPayment(double nominal) {
        if (nominal <= 0) {
            separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_grey));
            tokocashValue.setTextColor(getColorNominal(R.color.separator_grey));
            payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_border_black));
            payButton.setTextColor(getColorNominal(R.color.grey_nonactive_text));
            payButton.setEnabled(false);
        } else {
            if (nominal > (double) balanceTokoCash.getRawBalance()) {
                separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_red));
                tokocashValue.setTextColor(getColorNominal(R.color.separator_red));
                payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_grey_border_black));
                payButton.setTextColor(getColorNominal(R.color.grey_nonactive_text));
                payButton.setEnabled(false);
            } else {
                separatorNominal.setBackgroundColor(getColorNominal(R.color.separator_green));
                tokocashValue.setTextColor(getColorNominal(R.color.separator_green));
                payButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_button_rounded));
                payButton.setTextColor(getColorNominal(R.color.white));
                payButton.setEnabled(true);
            }
        }
    }

    @Override
    public void showErrorBalanceTokoCash(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getApplicationContext(), throwable);
        progressBar.setVisibility(View.GONE);
        tokocashValue.setVisibility(View.VISIBLE);
        tokocashValue.setText(getString(R.string.error_message_tokocash));
        NetworkErrorHelper.createSnackbarWithAction(this, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getBalanceTokoCash();
                    }
                }).showRetrySnackbar();
    }

    protected void onDestroy() {
        presenter.onDestroyPresenter();
        super.onDestroy();
    }
}
