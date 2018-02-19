package com.tokopedia.tkpd.campaign.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.campaign.configuration.AudioRecorder;

import java.io.IOException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sandeepgoyal on 23/01/18.
 */

@RuntimePermissions
public class CapturedAudioCampaignActivity extends BaseSimpleActivity implements AudioRecorder.RecordCompleteListener{

    public static Intent getCapturedAudioCampaignActivity(Context context) {
        Intent i = new Intent(context, CapturedAudioCampaignActivity.class);
        return i;
    }


    @Override
    protected int getLayoutRes() {
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CapturedAudioCampaignActivityPermissionsDispatcher.isRequiredPermissionAvailableWithCheck(this);
    }


    @NeedsPermission({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void isRequiredPermissionAvailable() {
        try {

                    startRecording();

            Toast.makeText(this,"Recording Start",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        CapturedAudioCampaignActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }



    AudioRecorder recorder;

    private void startRecording() throws IOException {
        recorder = new AudioRecorder("/sdcard/campaign.wav");
        recorder.start(this);
    }


    @Override
    public void onRecordComplete() {
        Toast.makeText(this,"Recording Complete",Toast.LENGTH_LONG).show();
        finish();
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



