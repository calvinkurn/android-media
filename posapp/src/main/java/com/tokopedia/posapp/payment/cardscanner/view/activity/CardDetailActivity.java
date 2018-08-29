package com.tokopedia.posapp.payment.cardscanner.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.payment.cardscanner.data.pojo.PaymentDataResponse;
import com.tokopedia.posapp.payment.cardscanner.view.fragment.CardDetailFragment;
import com.tokopedia.posapp.payment.cardscanner.view.viewmodel.CreditCardViewModel;
import com.tokopedia.posapp.payment.cardscanner.view.viewmodel.PaymentViewModel;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by okasurya on 8/15/17.
 */
public class CardDetailActivity extends BasePresenterActivity {

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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        setToolbar();
        setupView();
    }

    private void setToolbar() {
        getSupportActionBar().setTitle(R.string.cardscan_title);
    }

    private void setupView() {
        PaymentViewModel paymentViewModel = getPaymentViewModel(
            getIntent().getStringExtra(ScanCreditCardActivity.CHECKOUT_DATA),
            (CreditCard) getIntent().getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
        );

        CardDetailFragment fragment = CardDetailFragment.createInstance(paymentViewModel);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                CardDetailFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            CardDetailFragment.class.getSimpleName()));
        }

        fragmentTransaction.commit();
    }

    private PaymentViewModel getPaymentViewModel(String paymentResponse, CreditCard creditCard) {
        PaymentDataResponse response = new Gson().fromJson(paymentResponse, PaymentDataResponse.class);

        PaymentViewModel payment = new PaymentViewModel();
        payment.setEmiId(response.getSelectedEmiId());

        if(response.getBankData() != null) {
            payment.setBankId(response.getBankData().getBankId());
            payment.setBankName(response.getBankData().getBankName());
            payment.setValidateBin(response.getBankData().getValidateBin());
            payment.setInstallmentBin(response.getBankData().getInstallmentBin());
            payment.setAllowInstallment(response.getBankData().getAllowInstallment());
        }

        if(response.getCheckoutDataResponse() != null
                && response.getCheckoutDataResponse().getData() != null) {
            payment.setPaymentAmount(response.getCheckoutDataResponse().getData().getPaymentAmount());
            payment.setMerchantCode(response.getCheckoutDataResponse().getData().getMerchantCode());
            payment.setProfileCode(response.getCheckoutDataResponse().getData().getProfileCode());
            payment.setTransactionId(response.getCheckoutDataResponse().getData().getTransactionId());
            payment.setSignature(response.getCheckoutDataResponse().getData().getSignature());
        }

        payment.setCreditCard(getCardViewModel(creditCard));

        return payment;
    }

    private CreditCardViewModel getCardViewModel(CreditCard creditCard) {
        CreditCardViewModel viewModel = new CreditCardViewModel();
        viewModel.setCardNumber(creditCard.cardNumber);
        viewModel.setRedactedCardNumber(creditCard.getRedactedCardNumber());
        viewModel.setFormattedCardNumber(creditCard.getFormattedCardNumber());
        viewModel.setCardholderName(creditCard.cardholderName);
        viewModel.setCvv(creditCard.cvv);
        viewModel.setExpiryMonth(creditCard.expiryMonth);
        viewModel.setExpiryYear(creditCard.expiryYear);
        viewModel.setPostalCode(creditCard.postalCode);
        viewModel.setCardType(creditCard.getCardType().toString());
        viewModel.setCvvLength(creditCard.getCardType().cvvLength());
        viewModel.setImageBitmap(creditCard.getCardType().imageBitmap(this));
        viewModel.setExpiryValid(creditCard.isExpiryValid());
        return viewModel;
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
