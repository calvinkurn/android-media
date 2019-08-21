package com.tokopedia.tkpd.campaign.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.tkpd.campaign.di.DaggerCampaignComponent;
import com.tokopedia.tkpd.campaign.view.presenter.AudioShakeDetectPresenter;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by sandeepgoyal on 23/01/18.
 */

public class ShakeShakeAudioCampaignActivity extends ShakeDetectCampaignActivity {

    @Inject
    AudioShakeDetectPresenter presenter;

    private PermissionCheckerHelper permissionCheckerHelper;

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
        permissionCheckerHelper = new PermissionCheckerHelper();
        permissionCheckerHelper.checkPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        requestRequirePermissionDenied();
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {
                        requestCameraPermissionNeverAsk();
                    }

                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(ShakeShakeAudioCampaignActivity.this, "Start Recording", Toast.LENGTH_LONG).show();
                        presenter.onShakeDetect();
                    }
                },
                ""
        );
    }

    @Override
    protected void initInjector() {
        campaignComponent = DaggerCampaignComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        campaignComponent.inject(this);
    }

    void requestRequirePermissionDenied() {
        Toast.makeText(this, "Please provide required permissions", Toast.LENGTH_LONG).show();
        finish();
    }

    void requestCameraPermissionNeverAsk() {
        Toast.makeText(this, "Please provide required permissions", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Recording in Progress", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}