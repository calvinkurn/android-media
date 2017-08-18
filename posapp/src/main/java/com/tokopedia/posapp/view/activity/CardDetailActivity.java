package com.tokopedia.posapp.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.fragment.CardDetailFragment;
import com.tokopedia.posapp.view.viewmodel.card.CreditCardViewModel;
import com.tokopedia.posapp.view.widget.CreditCardTextWatcher;

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
        CreditCardViewModel data = getCardViewModel(
                (CreditCard) getIntent().getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
        );

        CardDetailFragment fragment = CardDetailFragment.createInstance(data);
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
