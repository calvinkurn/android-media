package com.tokopedia.core.product.customview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.core.R;
import com.tokopedia.core.product.activity.ProductYoutubeActivity;
import com.tokopedia.core.product.model.goldmerchant.VideoData;

/**
 * Created by kris on 11/3/16. Tokopedia
 */

public class YoutubeThumbnailViewHolder extends RelativeLayout{

    private RelativeLayout mainView;
    private ProgressBar loadingBar;
    private VideoData videoData;
    private int selectedVideo;

    public YoutubeThumbnailViewHolder(Context context, VideoData videoData, int selectedVideo) {
        super(context);
        this.videoData = videoData;
        this.selectedVideo = selectedVideo;
        initView(context, videoData.getVideo().get(selectedVideo).getUrl());
    }

    public YoutubeThumbnailViewHolder(Context context) {
        super(context);
    }

    public YoutubeThumbnailViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YoutubeThumbnailViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, final String youtubeVideoId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.place_holder_youtube_view, this, true);

        mainView = (RelativeLayout) findViewById(R.id.video_thumbnail_main_view);
        YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtube_thumbnail_view);
        loadingBar = (ProgressBar) findViewById(R.id.youtube_thumbnail_loading_bar);
        youTubeThumbnailView.initialize(getContext().getApplicationContext()
                .getString(R.string.GOOGLE_API_KEY),
                thumbnailInitializedListener(youtubeVideoId));


        //TODO Choose one of it
/*        youTubeThumbnailView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductYoutubeActivity.class);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_ID, youtubeVideoId);
                getContext().startActivity(intent);
            }
        });*/

        //TODO Choose one of it
        youTubeThumbnailView.setOnClickListener(onYoutubeThumbnailClickedListener());
    }

    private YouTubeThumbnailView
            .OnInitializedListener thumbnailInitializedListener(final String youtubeVideoId) {
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                final YouTubeThumbnailLoader loader) {
                loader.setVideo(youtubeVideoId);
                mainView.setVisibility(VISIBLE);
                loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                        .OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView,
                                                  String s) {
                        loader.release();
                        loadingBar.setVisibility(GONE);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                 YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult result) {
                loadingBar.setVisibility(GONE);
            }
        };
    }

    private OnClickListener onYoutubeThumbnailClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductYoutubeActivity.class);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_INDEX, selectedVideo);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_DATA, videoData);
                getContext().startActivity(intent);
            }
        };
    }
}
