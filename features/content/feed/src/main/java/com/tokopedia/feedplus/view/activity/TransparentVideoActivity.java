package com.tokopedia.feedplus.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tokopedia.feedplus.R;



/**
 * @author by nisie on 5/22/17.
 */

public class TransparentVideoActivity extends AppCompatActivity {

    private static final String PARAM_VIDEO_URL = "PARAM_VIDEO_URL";
    private static final String PARAM_TEXT = "PARAM_TEXT";
    private static final String ARGS_POSITION = "ARGS_POSITION";

    private ProgressDialog progressDialog;
    VideoView videoPlayer;
    TextView subtitle;
    int position;

    public static Intent getIntent(Activity activity, String videoUrl, String text) {
        Intent intent = new Intent(activity, TransparentVideoActivity.class);
        intent.putExtra(PARAM_VIDEO_URL, videoUrl);
        intent.putExtra(PARAM_TEXT, text);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_feed_video);
        videoPlayer = (VideoView) findViewById(R.id.video_player);
        subtitle = (TextView) findViewById(R.id.subtitle);

        setVideo();

        if (subtitle != null)
            subtitle.setText(getIntent().getExtras().getString(PARAM_TEXT, ""));

    }

    private void setVideo() {
        MediaController vidControl = new MediaController(this);

        showLoadingProgress();
        vidControl.setAnchorView(videoPlayer);
        videoPlayer.setMediaController(vidControl);
        videoPlayer.setVideoURI(Uri.parse(getIntent()
                .getExtras().getString(PARAM_VIDEO_URL, "")));
        videoPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Toast.makeText(TransparentVideoActivity.this,
                                getString(R.string.error_unknown),
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Toast.makeText(TransparentVideoActivity.this,
                                getString(R.string.default_request_error_internal_server),
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    default:
                        Toast.makeText(TransparentVideoActivity.this,
                                R.string.default_request_error_timeout,
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                }
            }
        });
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                videoPlayer.seekTo(position);
                if (position == 0) {
                    videoPlayer.start();
                } else {
                    videoPlayer.pause();
                }
            }
        });

    }

    private void showLoadingProgress() {
        progressDialog = new ProgressDialog(TransparentVideoActivity.this);
        progressDialog.setMessage("Buffering...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(ARGS_POSITION, videoPlayer.getCurrentPosition());
        videoPlayer.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(ARGS_POSITION);
        videoPlayer.seekTo(position);
    }
}
