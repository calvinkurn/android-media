package com.tokopedia.posapp.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by okasurya on 8/15/17.
 */
public class CardDetailActivity extends BasePresenterActivity {
    private ImageView cardLogo;
    private TextView textCardNo;
    private TextView textValidThru;
    private EditText inputVcc;
    private Button buttonPay;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_card_detail;
    }

    @Override
    protected void initView() {
        setToolbar();
        setupView();
        setupCardData((CreditCard) getIntent().getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT));
    }

    private void setToolbar() {
        getSupportActionBar().setTitle(R.string.cardscan_title);
    }

    private void setupView() {
        cardLogo = (ImageView) findViewById(R.id.card_logo);
        textCardNo = (TextView) findViewById(R.id.text_card_no);
        textValidThru = (TextView) findViewById(R.id.text_valid_thru);
        inputVcc = (EditText) findViewById(R.id.input_vcc);
        buttonPay = (Button) findViewById(R.id.button_pay);
    }

    private void setupCardData(CreditCard creditCard) {
        if(creditCard != null) {
            textCardNo.setText(creditCard.getFormattedCardNumber());
            cardLogo.setImageBitmap(creditCard.getCardType().imageBitmap(this));
            if (creditCard.isExpiryValid()) {
                textValidThru.setText(creditCard.expiryMonth + "/" + creditCard.expiryYear);
            }
            inputVcc.setEnabled(true);
            inputVcc.requestFocus();
            Log.d("OKA", creditCard.toString());
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
