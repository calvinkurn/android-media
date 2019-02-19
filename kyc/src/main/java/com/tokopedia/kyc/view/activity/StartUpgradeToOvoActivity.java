package com.tokopedia.kyc.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.view.fragment.FragmentUpgradeToOvo;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class StartUpgradeToOvoActivity extends BaseSimpleActivity implements
        HasComponent<KYCComponent>, ActivityListener {

    private KYCComponent KYCComponent = null;
    private static final String UPGRADE_OVO_SCREEN = "/upgradeovo";


    @DeepLink(Constants.AppLinks.OVOUPGRADE)
    public static Intent getCallingStartUpgradeToOvo(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, StartUpgradeToOvoActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentUpgradeToOvo.newInstance();
    }

    @Override
    public KYCComponent getComponent() {
        if (KYCComponent == null) {
            initInjector();
        }
        return KYCComponent;
    }

    private void initInjector() {
        KYCComponent = DaggerKYCComponent.builder().baseAppComponent(
                ((BaseMainApplication)getApplicationContext()).getBaseAppComponent()).build();
    }

    @Override
    public String getScreenName() {
        return UPGRADE_OVO_SCREEN;
    }

    @Override
    public void setHeaderTitle(String title) {
        updateTitle(title);
    }

    @Override
    public void addReplaceFragment(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(replace) {
            fragmentTransaction.replace(R.id.parent_view, baseDaggerFragment, tag);
        }
        else {
            fragmentTransaction.add(R.id.parent_view, baseDaggerFragment, tag);
        }
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
