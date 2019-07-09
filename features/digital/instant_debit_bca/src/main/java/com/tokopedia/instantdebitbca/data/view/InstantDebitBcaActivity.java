package com.tokopedia.instantdebitbca.data.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.instantdebitbca.data.view.utils.ApplinkConstant;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaActivity extends BaseSimpleActivity implements InstantDebitBcaFragment.ActionListener {

    public static final String CALLBACK_URL = "callbackUrl";

    @DeepLink({ApplinkConstant.INSTANT_DEBIT_BCA_APPLINK})
    public static Intent intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        String callbackUrl = extras.containsKey(CALLBACK_URL) ? extras.getString(CALLBACK_URL) : "";
        return InstantDebitBcaActivity.newInstance(context, callbackUrl);
    }

    public static Intent newInstance(Context context, String callbackUrl) {
        Intent intent = new Intent(context, InstantDebitBcaActivity.class);
        intent.putExtra(CALLBACK_URL, callbackUrl);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return InstantDebitBcaFragment.newInstance(getApplicationContext(), getIntent().getStringExtra(CALLBACK_URL));
    }

    @Override
    public void redirectPage(String applinkUrl) {
        if (RouteManager.isSupportApplink(getApplicationContext(), applinkUrl)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getApplicationContext(), applinkUrl);
            startActivity(intentRegisteredApplink);
        }
        finish();
    }
}
