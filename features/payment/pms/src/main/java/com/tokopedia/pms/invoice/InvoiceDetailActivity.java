package com.tokopedia.pms.invoice;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.common.Constant;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class InvoiceDetailActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String invoiceUrl){
        Intent intent = new Intent(context, InvoiceDetailActivity.class);
        intent.putExtra(Constant.INVOICE_URL_EXTRA, invoiceUrl);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return InvoiceDetailFragment.createInstance(getIntent().getStringExtra(Constant.INVOICE_URL_EXTRA));
    }
}
