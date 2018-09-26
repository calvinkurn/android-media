package com.tokopedia.tokocash.ovoactivation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.tokocash.ApplinkConstant;

/**
 * Created by nabillasabbaha on 20/09/18.
 */
public class IntroOvoActivity extends BaseOvoActivationActivity {

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_INTRODUCTION_OVO)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return IntroOvoActivity.newInstance(context);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, IntroOvoActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return IntroOvoFragment.newInstance();
    }
}
