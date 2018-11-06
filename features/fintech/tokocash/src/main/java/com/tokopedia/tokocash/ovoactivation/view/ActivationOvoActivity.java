package com.tokopedia.tokocash.ovoactivation.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.tokocash.ApplinkConstant;

/**
 * Created by nabillasabbaha on 20/09/18.
 */
public class ActivationOvoActivity extends BaseOvoActivationActivity {

    public static final String REGISTERED_APPLINK = "applink_registered";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String CHANGE_MSISDN_APPLINK = "applink_change_msisdn";

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_ACTIVATION_OVO)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return ActivationOvoActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, ActivationOvoActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return ActivationOvoFragment.newInstance(getIntent().getStringExtra(REGISTERED_APPLINK),
                getIntent().getStringExtra(PHONE_NUMBER), getIntent().getStringExtra(CHANGE_MSISDN_APPLINK));
    }
}
