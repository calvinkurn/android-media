package com.tokopedia.tkpd.campaign.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.configuration.AudioRecorder;
import com.tokopedia.tkpd.campaign.configuration.ShakeDetector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sandeepgoyal on 23/01/18.
 */

public class CapturedAudioCampaignActivity extends BasePresenterActivity implements AudioRecorder.RecordCompleteListener{
    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    public static Intent getCapturedAudioCampaignActivity(Context context) {
        Intent i = new Intent(context, CapturedAudioCampaignActivity.class);
        return i;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        try {
            startRecording();
            Toast.makeText(this,"Recording Start",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void setActionVar() {

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
}



