package com.tokopedia.posapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.CardDetail;
import com.tokopedia.posapp.view.activity.PaymentProcessingActivity;
import com.tokopedia.posapp.view.presenter.CardDetailPresenter;
import com.tokopedia.posapp.view.viewmodel.card.PaymentViewModel;
import com.tokopedia.posapp.view.widget.CreditCardTextWatcher;

/**
 * Created by okasurya on 8/18/17.
 */

public class CardDetailFragment extends BaseDaggerFragment implements CardDetail.View {
    public static final String PAYMENT_VIEW_MODEL = "PAYMENT_VIEW_MODEL";

    private ImageView cardLogo;
    private EditText textCardNo;
    private EditText inputValidMonth;
    private EditText inputValidYear;
    private EditText inputCvv;
    private Button buttonPay;

    CardDetailPresenter presenter;
    private PaymentViewModel paymentViewModel;

    public static CardDetailFragment createInstance(PaymentViewModel paymentViewModel) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PAYMENT_VIEW_MODEL, paymentViewModel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_detail, container, false);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new CardDetailPresenter();
        presenter.attachView(this);
        if(getArguments() != null) {
            paymentViewModel = getArguments().getParcelable(PAYMENT_VIEW_MODEL);
            setupCardData(paymentViewModel);
        }
    }

    private void initView(View view) {
        cardLogo = view.findViewById(R.id.card_logo);
        textCardNo = view.findViewById(R.id.text_card_no);
        inputValidMonth = view.findViewById(R.id.input_valid_thru_month);
        inputValidYear = view.findViewById(R.id.input_valid_thru_year);
        inputCvv = view.findViewById(R.id.input_cvv);
        buttonPay = view.findViewById(R.id.button_pay);
    }

    private void initListener() {
        textCardNo.addTextChangedListener(new CreditCardTextWatcher());

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pay(getLatestData());
            }
        });
    }

    private PaymentViewModel getLatestData() {
        paymentViewModel.getCreditCard().setCardNumber(textCardNo.getText().toString());
        paymentViewModel.getCreditCard().setExpiryMonth(Integer.parseInt(inputValidMonth.getText().toString()));
        paymentViewModel.getCreditCard().setExpiryYear(Integer.parseInt(inputValidYear.getText().toString()));
        paymentViewModel.getCreditCard().setCvv(inputCvv.getText().toString());
        return paymentViewModel;
    }

    private void setupCardData(PaymentViewModel paymentViewModel) {
        if(paymentViewModel != null && paymentViewModel.getCreditCard() != null) {
            textCardNo.setText(paymentViewModel.getCreditCard().getCardNumber());
            cardLogo.setImageBitmap(paymentViewModel.getCreditCard().getImageBitmap());
            if (paymentViewModel.getCreditCard().isExpiryValid()) {
                inputValidMonth.setText(paymentViewModel.getCreditCard().getFormattedExpiryMonth());
                inputValidYear.setText(paymentViewModel.getCreditCard().getExpiryYear());
            }
        }
    }

    @Override
    public void onGetPayData(String paymentData) {
        startActivity(PaymentProcessingActivity.newInstance(getContext(), paymentData));
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.d("o2o", e.getMessage());
    }
}
