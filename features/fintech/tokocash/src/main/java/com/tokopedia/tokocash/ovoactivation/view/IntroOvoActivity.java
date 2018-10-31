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
public class IntroOvoActivity extends BaseOvoActivationActivity implements IntroOvoFragment.OvoFragmentListener {

    public static final String TOKOCASH_ACTIVE = "tokocash_active";

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_INTRODUCTION_OVO)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        boolean tokocashActive = Boolean.parseBoolean(
                extras.getString(ApplinkConstant.WALLET_TOKOCASH_ACTIVE, "false"));
        return IntroOvoActivity.newInstance(context, tokocashActive);
    }

    public static Intent newInstance(Context context, boolean tokocashActive) {
        Intent intentIntroOvo = new Intent(context, IntroOvoActivity.class);
        intentIntroOvo.putExtra(TOKOCASH_ACTIVE, tokocashActive);
        return intentIntroOvo;
    }

    @Override
    protected Fragment getNewFragment() {
        return IntroOvoFragment.newInstance(getIntent().getBooleanExtra(TOKOCASH_ACTIVE, false));
    }

    @Override
    public void setTitleHeader(String titleHeader) {
        updateTitle(titleHeader);
    }
}
