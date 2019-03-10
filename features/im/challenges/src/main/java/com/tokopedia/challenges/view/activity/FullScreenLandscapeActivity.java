package com.tokopedia.challenges.view.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.customview.CustomMediaController;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;

public class FullScreenLandscapeActivity extends ChallengesBaseActivity implements CustomMediaController.ICurrentPos {

    public static final String VIDEO_URL_PARAM = "videoUrl";
    public static final String SEEK_POS_PARAM = "seekpos";
    public static final String ISPLAYING_PARAM = "isPlaying";
    private VideoView videoView;
    private MediaController mediaController;
    private String videoUrl;
    private int pos;
    private ImageView videoThumbnail;
    private ImageView playIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_video_view);

        videoView = findViewById(R.id.videoView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        videoUrl = getIntent().getStringExtra(VIDEO_URL_PARAM);
        pos = getIntent().getIntExtra(SEEK_POS_PARAM, 0);
        videoThumbnail  = findViewById(R.id.video_thumbnail);
        playIcon = findViewById(R.id.play_icon);
        startVideoPlay(videoUrl);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ChallengeDetailsFragment.VIDEO_POS != -1) {
            if (videoView != null) {
                videoView.seekTo(ChallengeDetailsFragment.VIDEO_POS);
                if (ChallengeDetailsFragment.isVideoPlaying)
                    videoView.start();
            }
        }
    }

    @Override
    public void onPause() {
        if (videoView != null) {
            ChallengeDetailsFragment.VIDEO_POS = getPosition();
            ChallengeDetailsFragment.isVideoPlaying = false;
        }
        super.onPause();
    }

    public void startVideoPlay(String videoUrl) {

        videoView.setVideoURI(Uri.parse(videoUrl));
        videoThumbnail.setVisibility(View.GONE);
        playIcon.setVisibility(View.GONE);
        mediaController = new CustomMediaController(this, videoUrl, pos, true, this, "landscape");
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.seekTo(pos);
        if (getIntent().getBooleanExtra(ISPLAYING_PARAM, false)) {
            videoView.start();
        }
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });
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
    public boolean isVideoPlaying() {
        return videoView.isPlaying();
    }

    @Override
    public void onBackPressed() {
        if (videoView != null) {
            ChallengeDetailsFragment.VIDEO_POS = getPosition();
            ChallengeDetailsFragment.isVideoPlaying = isVideoPlaying();
        }
        super.onBackPressed();
    }

}

