package com.tokopedia.youtubeutils.activity;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tokopedia.youtubeutil.R;
import com.tokopedia.youtubeutils.common.YoutubeInitializer;
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant;

/**
 * @author by yfsx on 29/06/18.
 */
public abstract class YoutubePlayerActivity extends YouTubeBaseActivity implements YoutubeInitializer.OnSingleVideoInitialListener {

    public static final String EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL";

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayerScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_youtube_player);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_main);
        initVideo();

    }

    protected abstract String getVideoUrl();

    private void initVideo() {
        try {
            youTubePlayerView.initialize(YoutubePlayerConstant.GOOGLE_API_KEY,
                    YoutubeInitializer.singleVideoInitializer(getVideoUrl(), this));
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    public void onSuccessInitializePlayer(YouTubePlayer player, String videoUrl) {
        this.youTubePlayerScreen = player;
        playYoutubeVideo(player, videoUrl);
    }

    @Override
    public void onFailedInitializePlayer(String error) {

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
