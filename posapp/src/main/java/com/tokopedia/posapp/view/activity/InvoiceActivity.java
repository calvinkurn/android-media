package com.tokopedia.posapp.view.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.view.fragment.InvoiceFragment;

/**
 * Created by okasurya on 10/11/17.
 */

public class InvoiceActivity extends BasePresenterActivity {

    public static final String DATA = "data";
    private static final String IS_ERROR = "isError";
    public static final String ERROR_TITLE = "errorTitle";
    public static final String ERROR_MESSAGE = "errorMessage";

    @DeepLink(Constants.Applinks.PAYMENT_INVOICE)
    public static Intent newApplinkIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newTopIntent(Context context, String data) {
        return new Intent(context, InvoiceActivity.class)
                .putExtra(DATA, data)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static Intent newErrorIntent(Context context, String errorTitle, String errorMessage) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IS_ERROR, true);
        intent.putExtra(ERROR_TITLE, errorTitle);
        intent.putExtra(ERROR_MESSAGE, errorMessage);
        return intent;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        if(getIntent().getBooleanExtra(IS_ERROR, false)) {
            getSupportActionBar().setTitle("Error");
        } else {
            getSupportActionBar().setTitle("Invoice");
        }

        InvoiceFragment fragment = InvoiceFragment.newInstance(getIntent().getStringExtra(DATA));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                InvoiceFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getFragmentManager().findFragmentByTag(InvoiceFragment.class.getSimpleName()));
        }

        fragmentTransaction.commit();
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
