package com.tokopedia.digital.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tokopedia.digital.R;

public class DigitalYoutubeActivity extends YouTubeBaseActivity {

    public static final String EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL";

    YouTubePlayerView youTubePlayerView;

    private YouTubePlayer youTubePlayerScreen;
    private String videoUrl;

    public static Intent createInstance(Context context, String videoUrl) {
        Intent intent = new Intent(context, DigitalYoutubeActivity.class);
        intent.putExtra(EXTRA_YOUTUBE_VIDEO_URL, videoUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_youtube);

        youTubePlayerView = findViewById(R.id.youtube_player_main);

        getVideoDatas();
        setSideBarAvailability();
    }

    private void getVideoDatas() {
        videoUrl = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_URL);
    }

    private void setSideBarAvailability() {
        youTubePlayerView.initialize(getApplicationContext().getString(com.tokopedia.core.R.string.GOOGLE_API_KEY),
                onSingleVideoInitializedListener());
    }

    private YouTubePlayer.OnInitializedListener onSingleVideoInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                DigitalYoutubeActivity.this.youTubePlayerScreen = youTubePlayer;
                youTubePlayer.setFullscreen(true);
                youTubePlayer.setShowFullscreenButton(false);
                playYoutubeVideo(youTubePlayer, videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult initializationResult) {

            }
        };
    }

    private YouTubePlayer.OnInitializedListener onInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                youTubePlayerScreen = youTubePlayer;
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener());
                playYoutubeVideo(youTubePlayer, videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult initializationResult) {

            }
        };
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener() {
        return new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                playYoutubeVideo(youTubePlayerScreen,
                        videoUrl);
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(youTubePlayerScreen != null) youTubePlayerScreen.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void playYoutubeVideo(YouTubePlayer youTubePlayer,
                                  String videoID) {
        youTubePlayer.loadVideo(videoID);
    }

    @Override
    protected void onDestroy() {
        if(youTubePlayerScreen != null) {
            youTubePlayerScreen.release();
        }
        super.onDestroy();
    }
}