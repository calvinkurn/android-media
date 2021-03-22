package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.ovo.R;

import java.util.List;

import static com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY;
import static com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OQR_PIN_URL_ENTRY_PATTERN;
import static com.tokopedia.ovo.view.PaymentQRSummaryFragment.LOCAL_CACHE_ID;

public class QrOvoPayTxDetailActivity extends BaseSimpleActivity implements TransactionResultListener {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final String TRANSFER_ID = "transfer_id";
    public static final String TRANSACTION_ID = "transaction_id";
    private static final String CODE = "code";
    public static final String CACHE_ID = "cache_id";
    private int transferId;
    private int transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalCacheHandler localCache = new LocalCacheHandler(getApplicationContext(), LOCAL_CACHE_ID);
        String saveInstanceCacheId = localCache.getString(CACHE_ID);
        SaveInstanceCacheManager saveInstanceCacheManager = new SaveInstanceCacheManager(
                getApplicationContext(), savedInstanceState);
        if (savedInstanceState == null)
            saveInstanceCacheManager = new SaveInstanceCacheManager(getApplicationContext(), saveInstanceCacheId);
        String txnId = saveInstanceCacheManager.get(TRANSACTION_ID, String.class);
        String tfId = saveInstanceCacheManager.get(TRANSFER_ID, String.class);
        Uri uri = getIntent().getData();
        if (txnId != null)
            transactionId = Integer.parseInt(txnId);
        else if(getIntent().getStringExtra(TRANSACTION_ID) != null){
            transactionId = Integer.parseInt(getIntent().getStringExtra(TRANSACTION_ID));
        }
        if (uri != null) {
            List<String> param = UriUtil.destructureUri(OQR_PIN_URL_ENTRY_PATTERN, uri, true);
            transferId = Integer.parseInt(param.get(0));
        } else if (getIntent().getStringExtra(TRANSFER_ID) != null) {
            transferId = Integer.parseInt(getIntent().getStringExtra(TRANSFER_ID));
        } else {
            if (tfId != null)
                transferId = Integer.parseInt(tfId);
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setPadding(getResources().getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16), 0, 0, 0);
    }

    public static Intent createInstance(Context context, int transferId, int transactionId, int code) {
        Intent intent = new Intent(context, QrOvoPayTxDetailActivity.class);
        intent.putExtra(TRANSFER_ID, String.valueOf(transferId));
        intent.putExtra(TRANSACTION_ID, String.valueOf(transactionId));
        intent.putExtra(CODE, code);
        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        if (getIntent().getIntExtra(CODE, SUCCESS) == SUCCESS) {
            updateTitle(getString(com.tokopedia.ovo.R.string.oqr_success_transaction));
            return QrTxSuccessDetailFragment.createInstance(transferId, transactionId);
        } else {
            updateTitle(getString(com.tokopedia.ovo.R.string.oqr_fail_transaction));
            return QrPayTxFailFragment.createInstance(transferId, transactionId);
        }
    }

    public void goToFailFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(com.tokopedia.abstraction.R.id.parent_view, QrPayTxFailFragment.createInstance(transferId, transactionId));
        transaction.commit();
    }

    public void goToSuccessFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(com.tokopedia.abstraction.R.id.parent_view, QrTxSuccessDetailFragment.createInstance(transferId, transactionId));
        transaction.commit();
    }
}
