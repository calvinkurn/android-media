package com.tokopedia.tkpd.campaign.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tkpd.campaign.view.presenter.AudioShakeDetectPresenter;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sandeepgoyal on 23/01/18.
 */

@RuntimePermissions
public class ShakeShakeAudioCampaignActivity extends ShakeDetectCampaignActivity {


    @Inject
    AudioShakeDetectPresenter presenter;

    public static Intent getCapturedAudioCampaignActivity(Context context) {
        Intent i = new Intent(context, ShakeShakeAudioCampaignActivity.class);
        return i;
    }


    @Override
    protected int getLayoutRes() {
        return 0;
    }

    void attachToPresenter() {
        presenter.attachView(this);
    }

    protected void shakeDetect() {
        ShakeShakeAudioCampaignActivityPermissionsDispatcher.isRequiredPermissionAvailableWithCheck(this);
    }

    @Override
    protected void initInjector() {
        campaignComponent = DaggerCampaignComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        campaignComponent.inject(this);
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void isRequiredPermissionAvailable() {
        presenter.onShakeDetect();

    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestRequirePermissionDenied() {
        Toast.makeText(this, "Please provide required permissions", Toast.LENGTH_LONG).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestCameraPermissionNeverAsk() {
        Toast.makeText(this, "Please provide required permissions", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShakeShakeAudioCampaignActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"Recording in Progress",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}