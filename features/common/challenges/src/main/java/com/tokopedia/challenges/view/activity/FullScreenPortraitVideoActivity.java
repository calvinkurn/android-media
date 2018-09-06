package com.tokopedia.challenges.view.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.customview.CustomMediaController;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;

public class FullScreenPortraitVideoActivity extends BaseActivity implements CustomMediaController.ICurrentPos {

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

        String fullScreen = getIntent().getStringExtra("fullScreenInd");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        videoUrl = getIntent().getStringExtra("videoUrl");
        pos = getIntent().getIntExtra("seekPos", 0);
        videoThumbnail = findViewById(R.id.video_thumbnail);
        playIcon = findViewById(R.id.play_icon);
        startVideoPlay(videoUrl);
    }

    public void startVideoPlay(String videoUrl) {
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoThumbnail.setVisibility(View.GONE);
        playIcon.setVisibility(View.GONE);
        mediaController = new CustomMediaController(this, videoUrl, pos, true, this, "portrait");
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.seekTo(pos);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("dhgsudghs", "" + mediaPlayer.getDuration()+"  "+videoView.getDuration());
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
    public void onBackPressed() {
        if (videoView != null)
            ChallegeneSubmissionFragment.VIDEO_POS = getPosition();
        super.onBackPressed();
    }

}
