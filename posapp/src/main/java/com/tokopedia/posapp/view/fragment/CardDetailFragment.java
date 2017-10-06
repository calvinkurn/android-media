package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.CardDetail;
import com.tokopedia.posapp.view.viewmodel.card.CreditCardViewModel;
import com.tokopedia.posapp.view.widget.CreditCardTextWatcher;

/**
 * Created by okasurya on 8/18/17.
 */

public class CardDetailFragment extends BaseDaggerFragment implements CardDetail.View {
    public static final String CREDIT_CARD_PASS = "CREDIT_CARD_PASS";

    private ImageView cardLogo;
    private EditText textCardNo;
    private EditText inputValidMonth;
    private EditText inputValidYear;
    private EditText inputVcc;
    private Button buttonPay;

    CardDetail.Presenter presenter;

    public static CardDetailFragment createInstance(CreditCardViewModel data) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CREDIT_CARD_PASS, data);
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
        if(getArguments() != null) {
            setupCardData((CreditCardViewModel) getArguments().getParcelable(CREDIT_CARD_PASS));
        }
    }

    private void initView(View view) {
        cardLogo = view.findViewById(R.id.card_logo);
        textCardNo = view.findViewById(R.id.text_card_no);
        inputValidMonth = view.findViewById(R.id.input_valid_thru_month);
        inputValidYear = view.findViewById(R.id.input_valid_thru_year);
        inputVcc = view.findViewById(R.id.input_vcc);
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

    private CreditCardViewModel getLatestData() {
        return null;
    }

    private void setupCardData(CreditCardViewModel creditCard) {
        if(creditCard != null) {
            textCardNo.setText(creditCard.getCardNumber());
            cardLogo.setImageBitmap(creditCard.getImageBitmap());
            if (creditCard.isExpiryValid()) {
                inputValidMonth.setText(creditCard.getFormattedExpiryMonth());
                inputValidYear.setText(creditCard.getFormattedExpiryYear());
            }
        }
    }
}
