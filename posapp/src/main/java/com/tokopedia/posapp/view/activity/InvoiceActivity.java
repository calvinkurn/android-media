package com.tokopedia.posapp.view.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.fragment.InvoiceFragment;

/**
 * Created by okasurya on 10/11/17.
 */

public class InvoiceActivity extends BasePresenterActivity {

    public static final String DATA = "data";

    public static Intent newTopIntent(Context context, String data) {
        return new Intent(context, InvoiceActivity.class)
                .putExtra(DATA, data)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        getSupportActionBar().setTitle("Invoice");

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
