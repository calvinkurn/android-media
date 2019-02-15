package com.tokopedia.kyc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;

public class StartUpgradeToOvoActivity extends BaseSimpleActivity implements
        HasComponent<UpgradeOvoComponent>, ActivityListener {

    private UpgradeOvoComponent upgradeOvoComponent = null;
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
        return UpgradeToOvoFragment.newInstance();
    }

    @Override
    public UpgradeOvoComponent getComponent() {
        if (upgradeOvoComponent == null) {
            initInjector();
        }
        return upgradeOvoComponent;
    }

    private void initInjector() {
        upgradeOvoComponent = DaggerUpgradeOvoComponent.builder().baseAppComponent(
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
}
