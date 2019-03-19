package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.ovo.R;

import static com.tokopedia.ovo.Constants.OVO_THANKS_TRANSACTION_URL;
import static com.tokopedia.ovo.view.PaymentQRSummaryFragment.LOCAL_CACHE_ID;

public class QrOvoPayTxDetailActivity extends BaseSimpleActivity implements TransactionResultListener {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final String TRANSFER_ID = "transfer_id";
    public static final String TRANSACTION_ID = "transaction_id";
    private static final String CODE = "code";
    public static final String CACHE_ID = "cache_id";
    private String cacheId;
    private int transferId;
    private int transactionId;
    private SaveInstanceCacheManager cacheManager;
    LocalCacheHandler cacheHandler;

    @DeepLink({OVO_THANKS_TRANSACTION_URL})
    public static Intent getContactUsIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, QrOvoPayTxDetailActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getIntExtra(TRANSFER_ID, 0) != 0) {
            transferId = getIntent().getIntExtra(TRANSFER_ID, 0);
        } else {
            cacheHandler = new LocalCacheHandler(getApplicationContext(), LOCAL_CACHE_ID);
            cacheId = cacheHandler.getString(CACHE_ID);
            cacheManager = new SaveInstanceCacheManager(this, savedInstanceState);
            if (savedInstanceState == null)
                cacheManager = new SaveInstanceCacheManager(this, cacheId);

            transferId = cacheManager.get(TRANSFER_ID, new TypeToken<Integer>() {
            }.getType());
        }
        if(getIntent().getIntExtra(TRANSACTION_ID, 0) != 0){
            transactionId = getIntent().getIntExtra(TRANSACTION_ID, 0);
        } else {
            transactionId = cacheManager.get(TRANSACTION_ID, new TypeToken<Integer>(){
            }.getType());
        }
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_16), 0 , 0, 0);
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
            return QrTxSuccessDetailFragment.createInstance(transferId, transactionId);
        } else {
            updateTitle(getString(R.string.oqr_fail_transaction));
            return QrPayTxFailFragment.createInstance(transferId, transactionId);
        }
    }

    public void goToFailFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrPayTxFailFragment.createInstance(transferId, transactionId));
        transaction.commit();
    }

    public void goToSuccessFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.parent_view, QrTxSuccessDetailFragment.createInstance(transferId, transactionId));
        transaction.commit();
    }
}
