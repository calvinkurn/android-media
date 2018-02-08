package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.deeplink.Constants;

import io.card.payment.CardIOActivity;

/**
 * Created by okasurya on 8/14/17.
 */

public class ScanCreditCardActivity extends BasePresenterActivity {

    public static final int REQUEST_CARD_SCANNER = 7001;
    public static final String CHECKOUT_DATA = "checkout_data";

    private LinearLayout cardScanner;

    @DeepLink(Constants.Applinks.PAYMENT_SCAN_CC)
    public static Intent getIntentFromDeeplink(Context context, Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon().build();
        Bundle data = new Bundle();
        data.putString(CHECKOUT_DATA, bundle.getString(TkpdCoreRouter.EXTRAS));

        return new Intent(context, ScanCreditCardActivity.class)
                .setData(uri)
                .putExtras(bundle)
                .putExtras(data);
    }


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
        return R.layout.activity_scan_cc;
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
                intent.putExtras(getIntent().getExtras());
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
