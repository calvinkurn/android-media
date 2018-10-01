package com.tokopedia.tokocash.ovoactivation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.tokocash.ApplinkConstant;
import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 20/09/18.
 */
public class IntroOvoActivity extends BaseOvoActivationActivity {

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_INTRODUCTION_OVO)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        return IntroOvoActivity.newInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_header_ovo));
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, IntroOvoActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return IntroOvoFragment.newInstance();
    }
}
