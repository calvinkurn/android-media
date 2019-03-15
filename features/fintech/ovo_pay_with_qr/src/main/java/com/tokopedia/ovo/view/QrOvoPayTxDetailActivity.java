package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.ovo.R;

import static com.tokopedia.ovo.Constants.OVO_THANKS_TRANSACTION_URL;
import static com.tokopedia.ovo.Constants.OVO_THANKS_URL;

public class QrOvoPayTxDetailActivity extends BaseSimpleActivity implements TransactionResultListener {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final String TRANSFER_ID = "transfer_id";
    public static final String TRANSACTION_ID = "transaction_id";
    private static final String CODE = "code";

    @DeepLink({OVO_THANKS_TRANSACTION_URL, OVO_THANKS_URL})
    public static Intent getContactUsIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, QrOvoPayTxDetailActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    public static Intent createInstance(Context context, int transferId, int transactionId, int code) {
        Intent intent = new Intent(context, QrOvoPayTxDetailActivity.class);
        intent.putExtra(TRANSFER_ID, transferId);
        intent.putExtra(TRANSACTION_ID, transactionId);
        intent.putExtra(CODE, code);
        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        if (getIntent().getIntExtra(CODE, -1) == SUCCESS) {
            updateTitle(getString(R.string.oqr_success_transaction));
            return QrTxSuccessDetailFragment.createInstance(
                    getIntent().getIntExtra(TRANSFER_ID, -1),
                    getIntent().getIntExtra(TRANSACTION_ID, -1));
        } else {
            updateTitle(getString(R.string.oqr_fail_transaction));
            return QrPayTxFailFragment.createInstance(
                    getIntent().getIntExtra(TRANSFER_ID, -1),
                    getIntent().getIntExtra(TRANSACTION_ID, -1));
        }
    }

    public void goToFailFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrPayTxFailFragment.createInstance(
                getIntent().getIntExtra(TRANSFER_ID, -1),
                getIntent().getIntExtra(TRANSACTION_ID, -1)));
        transaction.commit();
    }

    public void goToSuccessFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrTxSuccessDetailFragment.createInstance(
                getIntent().getIntExtra(TRANSFER_ID, -1),
                getIntent().getIntExtra(TRANSACTION_ID, -1)));
        transaction.commit();
    }
}
