package com.tokopedia.challenges.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.customview.CustomMediaController;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;

import javax.inject.Inject;

public class FullScreenVideoActivity extends BaseActivity implements CustomMediaController.ICurrentPos {

    private VideoView videoView;
    private MediaController mediaController;
    String videoUrl;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_video_view);

        videoView = findViewById(R.id.videoView);

        String fullScreen = getIntent().getStringExtra("fullScreenInd");
        if ("y".equals(fullScreen)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }

        videoUrl = getIntent().getStringExtra("videoUrl");
        pos = getIntent().getIntExtra("seekPos", 0);

        startVideoPlay(videoUrl);
    }

    public void startVideoPlay(String videoUrl) {
        videoView.setVideoURI(Uri.parse(videoUrl));

        mediaController = new CustomMediaController(this, videoUrl, pos, true, this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.seekTo(pos);
        videoView.start();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public int getPosition() {
        return videoView.getCurrentPosition();
    }

    @Override
    public void onBackPressed() {
        if (videoView != null)
            ChallegeneSubmissionFragment.VIDEO_POS = getPosition();
        super.onBackPressed();
    }

}
