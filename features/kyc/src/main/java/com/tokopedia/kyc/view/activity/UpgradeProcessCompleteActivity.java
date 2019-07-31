package com.tokopedia.kyc.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.R;
import com.tokopedia.kyc.di.DaggerKYCComponent;
import com.tokopedia.kyc.di.KYCComponent;
import com.tokopedia.kyc.model.ConfirmRequestDataContainer;
import com.tokopedia.kyc.view.fragment.FragmentFollowupCustomerCare;
import com.tokopedia.kyc.view.fragment.FragmentVerificationFailure;
import com.tokopedia.kyc.view.fragment.FragmentVerificationSuccess;
import com.tokopedia.kyc.view.interfaces.ActivityListener;

public class UpgradeProcessCompleteActivity extends BaseSimpleActivity implements
        HasComponent<KYCComponent>, ActivityListener {

    private String status;
    private String message;
    private KYCComponent KYCComponent = null;

    @DeepLink(Constants.AppLinks.OVOUPGRADE_STATUS)
    public static Intent getCallingUpgradeProcessComplete(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, UpgradeProcessCompleteActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected Fragment getNewFragment() {
        showHideActionbar(false);
        if(getIntent().getExtras() != null) {
            status = getIntent().getExtras().getString(Constants.Keys.STATUS);
            message = getIntent().getExtras().getString(Constants.Keys.MESSAGE);
        }
        BaseDaggerFragment baseDaggerFragment = null;
        if(TextUtils.isEmpty(status)){
            finish();
        }
        else if(status.equalsIgnoreCase(Constants.Status.INPROGRESS)){
            baseDaggerFragment = FragmentFollowupCustomerCare.newInstance();
        }
        else if(status.equalsIgnoreCase(Constants.Status.SUCCESSFUL)){
            baseDaggerFragment = FragmentVerificationSuccess.newInstance();
        }
        else if(status.equalsIgnoreCase(Constants.Status.FAILED)){
            baseDaggerFragment = FragmentVerificationFailure.newInstance();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Keys.MESSAGE, message);
        baseDaggerFragment.setArguments(bundle);
        return baseDaggerFragment;
    }

    @Override
    public KYCComponent getComponent() {
        if (KYCComponent == null) {
            initInjector();
        }
        return KYCComponent;
    }

    @Override
    public void setHeaderTitle(String title) {
        updateTitle(title);
    }

    @Override
    public void addReplaceFragment(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag) {

    }

    @Override
    public void addReplaceFragmentWithCustAnim(BaseDaggerFragment baseDaggerFragment, boolean replace, String tag, int entryAnimId, int exitAnimId) {

    }

    @Override
    public void showHideActionbar(boolean show){
        if(show) getSupportActionBar().show();
        else getSupportActionBar().hide();
    }

    private void initInjector() {
        KYCComponent = DaggerKYCComponent.builder().baseAppComponent(
                ((BaseMainApplication)getApplicationContext()).getBaseAppComponent()).build();
    }

    @Override
    public ConfirmRequestDataContainer getDataContatainer() {
        return new ConfirmRequestDataContainer();
    }

    @Override
    public boolean isRetryValid() {
        return false;
    }

    @Override
    protected void inflateFragment() {
        Fragment newFragment = getNewFragment();
        if (newFragment == null) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(com.tokopedia.abstraction.R.id.parent_view, newFragment, getTagFragment())
                .setCustomAnimations(R.anim.enter_from_bottom_to_top, R.anim.exit_from_top_to_bottom)
                .commit();
    }

}
