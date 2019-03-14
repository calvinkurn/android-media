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

public class QrOvoPayTxDetailActivity extends BaseSimpleActivity implements TransactionResultListener {
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;

    @DeepLink("tokopedia://ovoqrthanks/{transfer_id}")
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
        intent.putExtra("transfer_id", transferId);
        intent.putExtra("transaction_id", transactionId);
        intent.putExtra("code", code);
        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        if (getIntent().getIntExtra("code", -1) == SUCCESS) {
            updateTitle(getString(R.string.success_transaction));
            return QrTxSuccessDetailFragment.createInstance(
                    getIntent().getIntExtra("transfer_id", -1),
                    getIntent().getIntExtra("transaction_id", -1));
        } else {
            updateTitle(getString(R.string.fail_transaction));
            return QrPayTxFailFragment.createInstance(
                    getIntent().getIntExtra("transfer_id", -1),
                    getIntent().getIntExtra("transaction_id", -1));
        }
    }

    public void goToFailFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrPayTxFailFragment.createInstance(
                getIntent().getIntExtra("transfer_id", -1),
                getIntent().getIntExtra("transaction_id", -1)));
        transaction.commit();
    }

    public void goToSuccessFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrTxSuccessDetailFragment.createInstance(
                getIntent().getIntExtra("transfer_id", -1),
                getIntent().getIntExtra("transaction_id", -1)));
        transaction.commit();
    }
}
