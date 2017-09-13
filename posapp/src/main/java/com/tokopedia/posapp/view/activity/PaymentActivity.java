package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.posapp.view.fragment.PaymentFragment;

/**
 * Created by okasurya on 9/12/17.
 */

public class PaymentActivity extends BasePresenterActivity {
    @DeepLink(Constants.Applinks.PAYMENT_CHECKOUT)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
        return new Intent(context, PaymentActivity.class)
                .setData(uri)
                .putExtras(extras);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        PaymentFragment fragment = PaymentFragment.newInstance(PosReactConst.Page.PAYMENT);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                PaymentFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            PaymentFragment.class.getSimpleName()));
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
