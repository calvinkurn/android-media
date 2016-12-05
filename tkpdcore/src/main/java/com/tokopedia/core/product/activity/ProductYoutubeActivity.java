package com.tokopedia.core.product.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.customview.YoutubeActivityThumbnail;
import com.tokopedia.core.product.listener.ProductYoutubeActivityView;
import com.tokopedia.core.product.model.goldmerchant.Video;
import com.tokopedia.core.product.model.goldmerchant.VideoData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductYoutubeActivity extends YouTubeBaseActivity
        implements ProductYoutubeActivityView {

    public static final String EXTRA_YOUTUBE_VIDEO_DATA = "EXTRA_YOUTUBE_VIDEO_DATA";
    public static final String EXTRA_YOUTUBE_VIDEO_INDEX = "EXTRA_YOUTUBE_VIDEO_INDEX";

    @BindView(R2.id.youtube_player_main)
    YouTubePlayerView youTubePlayerView;

    @BindView(R2.id.place_holder)
    LinearLayout placeHolder;

    @BindView(R2.id.place_holder_scroll_view)
    ScrollView placeHolderScrollView;

    private YouTubePlayer youTubePlayerScreen;
    private List<Video> youtubeVideoList;
    private int selectedYoutubeVideoInt;

    //TODO choose one of these OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_youtube);
        getVideoDatas();
        ButterKnife.bind(this);
        setSideBarAvailability();

    }

    private void getVideoDatas() {
        VideoData youtubeVideoData = getIntent().getParcelableExtra(EXTRA_YOUTUBE_VIDEO_DATA);
        selectedYoutubeVideoInt = getIntent().getIntExtra(EXTRA_YOUTUBE_VIDEO_INDEX, 0);
        youtubeVideoList = youtubeVideoData.getVideo();
    }

    private void setSideBarAvailability() {
        if(youtubeVideoList.size() > 1)
            showVideoList();
        else {
            youTubePlayerView.initialize(getApplicationContext().getString(R.string.GOOGLE_API_KEY),
                    onSingleVideoInitializedListener());
        }
    }

    private void showVideoList() {
        for(int i = 0; i < youtubeVideoList.size(); i++) {
            YoutubeActivityThumbnail youtubeActivityThumbnail = new YoutubeActivityThumbnail(this);
            youtubeActivityThumbnail.setListener(this);
            placeHolder.addView(youtubeActivityThumbnail);
            youtubeActivityThumbnail.setVideo(youtubeVideoList.get(i).getUrl(), i);
        }
    }

    private YouTubePlayer.OnInitializedListener onSingleVideoInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                ProductYoutubeActivity.this.youTubePlayerScreen = youTubePlayer;
                youTubePlayer.setFullscreen(true);
                youTubePlayer.setShowFullscreenButton(false);
                playYoutubeVideo(youTubePlayer,
                        youtubeVideoList.get(selectedYoutubeVideoInt).getUrl(),
                        selectedYoutubeVideoInt);
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
                playYoutubeVideo(youTubePlayer,
                        youtubeVideoList.get(selectedYoutubeVideoInt).getUrl(),
                        selectedYoutubeVideoInt);
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
                placeHolderScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                if((selectedYoutubeVideoInt + 1) < youtubeVideoList.size()) {
                    playYoutubeVideo(youTubePlayerScreen,
                            youtubeVideoList.get(selectedYoutubeVideoInt + 1).getUrl(),
                            selectedYoutubeVideoInt + 1);
                }
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
                                  String videoID,
                                  int selectedYoutubeVideoInt) {
        youTubePlayer.loadVideo(videoID);
        this.selectedYoutubeVideoInt = selectedYoutubeVideoInt;
    }

    @Override
    public void onPlayYoutubeVideo(String videoID, int videoIndex) {
        playYoutubeVideo(youTubePlayerScreen, videoID, videoIndex);
    }

    @Override
    public void onInitiazlizeVideo(int videoIndex) {
        if(videoIndex == youtubeVideoList.size() -1)
            youTubePlayerView.initialize(getApplicationContext().getString(R.string.GOOGLE_API_KEY),
                    onInitializedListener());
    }

    @Override
    protected void onDestroy() {
        if(youTubePlayerScreen != null) {
            youTubePlayerScreen.release();
        }
        super.onDestroy();
    }
}