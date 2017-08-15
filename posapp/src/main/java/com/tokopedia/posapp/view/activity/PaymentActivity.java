package com.tokopedia.posapp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by okasurya on 8/14/17.
 */

public class PaymentActivity extends BasePresenterActivity {

    public static final int REQUEST_CARD_SCANNER = 7001;

    private LinearLayout cardScanner;

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
        return R.layout.activity_payment;
    }

    @Override
    protected void initView() {
        setToolbar();
        setupView();
    }

    @Override
    protected void setViewListener() {
        cardScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCardScanner();
            }
        });
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CARD_SCANNER) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                Intent intent = new Intent(this, CardDetailActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        }
    }

    private void setToolbar() {
        getSupportActionBar().setTitle(R.string.payment_title);
    }

    private void setupView() {
        cardScanner = (LinearLayout) findViewById(R.id.card_scanner);
    }

    private void openCardScanner() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);

        startActivityForResult(scanIntent, REQUEST_CARD_SCANNER);
    }
}
